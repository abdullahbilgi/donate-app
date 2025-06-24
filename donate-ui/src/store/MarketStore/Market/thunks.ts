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

export const deleteMarket = createAsyncThunk(
  "deleteMarket",
  async (marketId: any, thunkAPI) => {
    console.log("thunk");
    try {
      const res = await axiosPrivate.delete(`/markets/${marketId}`);

      console.log(res.data);
      return res.data;
    } catch (error: any) {
      return thunkAPI.rejectWithValue("Ürün silinirken bir hata oluştu");
    }
  }
);

type LocationInfo = {
  cityName: string;
  displayName: string;
  latitude: number;
  longitude: number;
  name: string;
  regionName: string;
  taxNumber: string;
  userId: number;
  zipCode: string;
};

export const createMarket = createAsyncThunk(
  "createMarket",
  async (newMarket: LocationInfo, thunkAPI) => {
    try {
      const res = await axiosPrivate.post("/markets", newMarket);

      console.log(res.data);
      return res.data;
    } catch (error: any) {
      return thunkAPI.rejectWithValue("Market eklenirken bir hata oluştu");
    }
  }
);
