import { createSlice } from "@reduxjs/toolkit";
import { getMe, login, logout, refreshToken } from "./thunks";
import { Role } from "../../../permissions/permissions";

interface LoginState {
  userId: number | null;
  role: Role | null;
  token: string | null;
  refreshToken: string | null;
  error: string;
  loading: boolean;
  isLogged: boolean;
}

const initialState: LoginState = {
  userId: null,
  role: null,
  token: null,
  refreshToken: null,
  error: "",
  loading: false,
  isLogged: false,
};

const authReducer = createSlice({
  name: "Auth",
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

      //Update redux
      .addCase(getMe.pending, (state) => {
        state.loading = true;
      });

    builder.addCase(getMe.fulfilled, (state: any, action: any) => {
      console.log(action.payload);
      state.loading = false;
      state.userId = action.payload.userId;
      state.role = action.payload.role;
      state.token = localStorage.getItem("accessToken"); // tekrar yükle
      state.refreshToken = localStorage.getItem("refreshToken"); // tekrar yükle
      state.isLogged = true;
    });

    builder
      .addCase(getMe.rejected, (state, action) => {
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
        localStorage.removeItem("cartId");
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

export const AuthActions = authReducer.actions;

export default authReducer.reducer;
