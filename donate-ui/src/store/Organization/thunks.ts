import { createAsyncThunk } from "@reduxjs/toolkit";
import axiosPrivate from "../../api/axiosPrivate";

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
