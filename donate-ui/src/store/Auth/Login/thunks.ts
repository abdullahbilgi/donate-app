import { createAsyncThunk } from "@reduxjs/toolkit";
import axios from "axios";
import { RootState } from "../..";
import axiosPrivate from "../../../api/axiosPrivate";

interface userRequest {
  userName: string;
  password: string;
}

export const login = createAsyncThunk(
  "login",
  async function (user: userRequest, thunkAPI) {
    try {
      // const res = await axiosPrivate.post("/auth/login", user);
      const res = await axios({
        url: "http://localhost:8080/api/v1/auth/login",
        method: "POST",
        data: user,
      });

      console.log(res.data);
      return res.data;
    } catch (error) {
      return thunkAPI.rejectWithValue("Login Error!");
    }
  }
);

export const getMe = createAsyncThunk("getMe", async function () {
  const res = await axiosPrivate.get("/users/me");
  console.log("GETMEE", res.data);
  return res.data;
});

export const logout = createAsyncThunk("logout", async function (_, thunkAPI) {
  try {
    const res = await axios({
      url: "http://localhost:8080/api/v1/auth/logout",
      method: "POST",
    });

    console.log(res.data);
    return res.data;
  } catch (error) {
    return thunkAPI.rejectWithValue("Logout Error!");
  }
});

export const refreshToken = createAsyncThunk(
  "refreshToken",
  async function (_, thunkAPI) {
    try {
      const refreshTok = localStorage.getItem("refreshToken");
      console.log(refreshTok);

      const res = await axios({
        url: "http://localhost:8080/api/v1/auth/refresh-token",
        method: "POST",
        headers: {
          Authorization: `Bearer ${refreshTok}`,
        },
      });

      console.log(res.data);
      return res.data;
    } catch (error) {
      return thunkAPI.rejectWithValue("Refresh Token Error!");
    }
  }
);
