import { createAsyncThunk } from "@reduxjs/toolkit";
import axiosPrivate from "../../../api/axiosPrivate";

export const getMarketByUser = createAsyncThunk(
  "getMarketByUser",
  async (userId: any, thunkAPI) => {
    console.log("thunk");
    try {
      const res = await axiosPrivate.get(`/markets/user/${userId}`);

      console.log(res.data);
      return res.data;
    } catch (error: any) {
      return thunkAPI.rejectWithValue("Ürün eklenirekn bir hata oluştu");
    }
  }
);
