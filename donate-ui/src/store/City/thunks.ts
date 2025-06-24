import { createAsyncThunk } from "@reduxjs/toolkit";
import axiosPrivate from "../../api/axiosPrivate";

export const getAllCity = createAsyncThunk("getAllCity", async () => {
  const res = await axiosPrivate.get("/cities");

  return res.data;
});
