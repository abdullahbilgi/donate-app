import axiosPrivate from "./axiosPrivate";
import store, { useAppDispatch, useAppSelector } from "../store";
import { refreshToken } from "../store/Auth/Login/thunks";

let isRefreshing = false;
let token: string | null = null;

let requestInterceptorId: number | null = null;
let responseInterceptorId: number | null = null;

export const setToken = (newToken: string | null) => {
  token = newToken;
  if (newToken) {
    localStorage.setItem("accessToken", newToken);
  } else {
    localStorage.removeItem("accessToken");
  }
};

export const setupInterceptors = () => {
  //burada useSelector gibi React Hook'larını kullanamazsın. Hook'lar sadece component'lerin içinde veya custom hook'ların içinde çalışır.

  // Eski interceptor varsa kaldır
  if (requestInterceptorId !== null) {
    axiosPrivate.interceptors.request.eject(requestInterceptorId);
  }
  if (responseInterceptorId !== null) {
    axiosPrivate.interceptors.response.eject(responseInterceptorId);
  }

  requestInterceptorId = axiosPrivate.interceptors.request.use(
    (config) => {
      //const token = store.getState().Auth.token;

      if (!config.headers["Authorization"] && token) {
        config.headers["Authorization"] = `Bearer ${token}`;
      }

      return config;
    },
    (error) => Promise.reject(error)
  );

  responseInterceptorId = axiosPrivate.interceptors.response.use(
    (response) => response,
    async (error) => {
      //eger hata varsa burasi calisir
      const originalRequest = error?.config;
      if (!originalRequest) return Promise.reject(error);

      // Eğer token expired olduysa ve daha önce retry edilmediyse
      if (
        error?.response?.status === 403 &&
        !originalRequest._retry &&
        !isRefreshing
      ) {
        originalRequest._retry = true;
        isRefreshing = true;

        try {
          const res = await store.dispatch(refreshToken());
          if (res.meta.requestStatus === "rejected") {
            console.error("Refresh token başarısız:", res.payload);
            isRefreshing = false;
            return Promise.reject("Refresh başarısız");
          }

          const newToken = res.payload?.access_token;

          if (newToken) {
            setToken(newToken);
            // Yeni token ile isteği tekrar gönder
            originalRequest.headers["Authorization"] = `Bearer ${newToken}`;
            isRefreshing = false;
            return axiosPrivate(originalRequest);
          } else {
            isRefreshing = false;
            return Promise.reject("Yeni token alınamadı.");
          }
        } catch (err) {
          isRefreshing = false;
          return Promise.reject(err);
        }
      }

      return Promise.reject(error);
    }
  );
};
