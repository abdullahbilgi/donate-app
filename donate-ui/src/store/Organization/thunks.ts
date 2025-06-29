import { createAsyncThunk } from "@reduxjs/toolkit";
import axiosPrivate from "../../api/axiosPrivate";

export const getAllOrganizations = createAsyncThunk(
  "getAllOrganizations",
  async (_, thunkAPI) => {
    console.log("thunk");
    try {
      const res = await axiosPrivate.get(`/organizations`);

      console.log(res.data);
      return res.data;
    } catch (error: any) {
      return thunkAPI.rejectWithValue(
        "Organizasyonlar getirilirken bir hata oluştu"
      );
    }
  }
);

export const deleteOrganization = createAsyncThunk(
  "deleteOrganization",
  async (organizationId: any, thunkAPI) => {
    console.log("thunk");
    try {
      const res = await axiosPrivate.delete(`/organizations/${organizationId}`);

      console.log(res.data);
      return res.data;
    } catch (error: any) {
      return thunkAPI.rejectWithValue(
        "Organizasyonlar silinirken bir hata oluştu"
      );
    }
  }
);
