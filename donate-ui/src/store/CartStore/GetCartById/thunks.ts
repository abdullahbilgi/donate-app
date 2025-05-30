import { createAsyncThunk } from "@reduxjs/toolkit";
import axios from "axios";
import axiosPrivate from "../../../api/axiosPrivate";

export const getCartById = createAsyncThunk(
  "getCartById",
  async function (userId: any, thunkAPI) {
    try {
      const res = await axiosPrivate.get(`carts/currentUserCart/${userId}`);
      console.log("girdi getbyid", res.data);

      localStorage.setItem("cartId", res.data.id);
      return res.data;
    } catch (error) {
      return thunkAPI.rejectWithValue("Error!");
    }
  }
);
