import { createAsyncThunk } from "@reduxjs/toolkit";
import axiosPrivate from "../../../api/axiosPrivate";

export const getSoldProductByMarket = createAsyncThunk(
  "getSoldProductByMarket",
  async (marketId: any, thunkAPI) => {
    console.log("thunk");
    try {
      const res = await axiosPrivate.get(`/carts/soldProducts/${marketId}`);

      console.log(res.data);
      return res.data;
    } catch (error: any) {
      return thunkAPI.rejectWithValue("Ürünler getirilirken bir hata oluştu");
    }
  }
);
