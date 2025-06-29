import { createAsyncThunk } from "@reduxjs/toolkit";
import axiosPrivate from "../../../api/axiosPrivate";

export const getApplies = createAsyncThunk(
  "applyOrganization",
  async (_, thunkAPI) => {
    console.log("thunk");
    try {
      const res = await axiosPrivate.get(`/organizations/applies`);

      console.log(res.data);
      return res.data;
    } catch (error: any) {
      return thunkAPI.rejectWithValue(
        "Basvurular getirilirken bir hata oluştu"
      );
    }
  }
);

export const rejectedApply = createAsyncThunk(
  "rejectedApply",
  async (applyId: any, thunkAPI) => {
    console.log("thunk");
    try {
      const res = await axiosPrivate.post(`/organizations/reject/${applyId}`);

      console.log(res.data);
      return res.data;
    } catch (error: any) {
      return thunkAPI.rejectWithValue("HATA");
    }
  }
);

export const acceptApply = createAsyncThunk(
  "acceptApply",
  async (applyId: any, thunkAPI) => {
    console.log("thunk");
    try {
      const res = await axiosPrivate.post(`/organizations/confirm/${applyId}`);

      console.log(res.data);
      return res.data;
    } catch (error: any) {
      return thunkAPI.rejectWithValue("HATA");
    }
  }
);
