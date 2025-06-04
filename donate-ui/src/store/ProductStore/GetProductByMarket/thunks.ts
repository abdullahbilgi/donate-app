import { createAsyncThunk } from "@reduxjs/toolkit";
import axiosPrivate from "../../../api/axiosPrivate";

export const getProductsByMarket = createAsyncThunk(
  "getProductsByMarket",
  async (marketId: any, thunkAPI) => {
    console.log("thunk");
    try {
      const res = await axiosPrivate.get(`/products/market/${marketId}`);

      return res.data;
    } catch (error: any) {
      return thunkAPI.rejectWithValue("Ürünler getirilirken bir hata oluştu");
    }
  }
);
