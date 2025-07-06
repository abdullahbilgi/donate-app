import { createAsyncThunk } from "@reduxjs/toolkit";
import axiosPrivate from "../../../api/axiosPrivate";

export const getMyApply = createAsyncThunk(
  "getAllOrganizations",
  async (_, thunkAPI) => {
    try {
      const res = await axiosPrivate.get(`/organizations/applies`);

      console.log(res.data);
      return res.data;
    } catch (error: any) {
      return thunkAPI.rejectWithValue(
        "Organizasyon basvurusu getirilirken bir hata oluştu"
      );
    }
  }
);
