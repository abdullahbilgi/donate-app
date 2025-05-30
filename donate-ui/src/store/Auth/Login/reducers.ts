import { createSlice } from "@reduxjs/toolkit";
import { login, logout, refreshToken } from "./thunks";
import { setToken, setupInterceptors } from "../../../api/interceptors";

interface LoginState {
  userId: number | null;
  role: string | null;
  token: string | null;
  refreshToken: string | null;
  error: string;
  loading: boolean;
  isLogged: boolean;
}

const initialState: LoginState = {
  userId: Number(localStorage.getItem("userId")),
  role: localStorage.getItem("role"),
  token: localStorage.getItem("accessToken"),
  refreshToken: localStorage.getItem("refreshToken"),
  error: "",
  loading: false,
  isLogged: false,
};

const authReducer = createSlice({
  name: "auth",
  initialState,
  reducers: {
    setError: (state, action) => {
      state.error = action.payload;
    },
  },
  extraReducers(builder) {
    builder

      //LOGIN
      .addCase(login.pending, (state) => {
        state.loading = true;
      });

    builder.addCase(login.fulfilled, (state: any, action: any) => {
      console.log("login: ", action.payload?.access_token);
      console.log("login: ", action.payload?.refresh_token);
      console.log(action.payload);
      state.loading = false;
      state.token = action.payload?.access_token;
      state.refreshToken = action.payload?.refresh_token;
      state.userId = action.payload.userId;
      state.role = action.payload.role;

      localStorage.setItem("accessToken", action.payload.access_token);
      localStorage.setItem("refreshToken", action.payload.refresh_token);
      localStorage.setItem("userId", action.payload.userId);
      localStorage.setItem("role", action.payload.role);
      localStorage.setItem("isLogged", "true");
      state.isLogged = true;
    });

    builder
      .addCase(login.rejected, (state, action) => {
        state.loading = false;
        state.error = (action.payload as string) || "Something Went Wrong";
      })
      //LOGOUT
      .addCase(logout.pending, (state) => {
        state.loading = true;
      })

      .addCase(logout.fulfilled, (state: any, action: any) => {
        console.log("logout: ", action.payload);
        state.loading = false;
        state.token = null;
        state.refreshToken = null;
        state.userId = null;
        state.role = null;

        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");
        localStorage.removeItem("userId");
        localStorage.removeItem("role");
        localStorage.setItem("isLogged", "false");
        state.isLogged = false;
      })
      .addCase(logout.rejected, (state, action) => {
        state.loading = false;
        state.error = (action.payload as string) || "Something Went Wrong";
      })

      //REFRESH_TOKEN
      .addCase(refreshToken.pending, (state) => {
        state.loading = true;
      })
      .addCase(refreshToken.fulfilled, (state: any, action: any) => {
        console.log("refresh: ", action.payload?.access_token);
        console.log("refresh: ", action.payload?.refresh_token);
        state.loading = false;
        state.error = "";

        state.token = action.payload?.access_token;
        state.refreshToken = action.payload?.refresh_token;
      })
      .addCase(refreshToken.rejected, (state, action) => {
        state.loading = false;
        state.isLogged = false;
        state.error = (action.payload as string) || "Something Went Wrong";
      });
  },
});

export const { setError } = authReducer.actions;

export default authReducer.reducer;

// builder.addCase(companyLogin.pending, (state) => {
//   state.loading = true;
// });
// builder.addCase(companyLogin.fulfilled, (state: any, action: any) => {
//   state.loading = false;
//   state.success = true;
//   state.token = action.payload?.access_token;
//   localStorage.setItem("token", action.payload?.access_token);
//   localStorage.setItem("token_refresh", action.payload?.refresh_token);
//   state.isLoggedIn = true;
//   state.user = action.payload.user;
//   state.type = "COMPANY";
// });
// builder.addCase(companyLogin.rejected, (state, action) => {
//   state.loading = false;
//   state.success = false;
//   state.error = action.error.message || "Something Went Wrong";
//   state.user = null;
// });
