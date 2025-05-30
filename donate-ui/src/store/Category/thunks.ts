import { createAsyncThunk } from "@reduxjs/toolkit";
import axios from "axios";
import axiosPrivate from "../../api/axiosPrivate";

export const getCategory = createAsyncThunk("getProductById", async () => {
  const res = await axiosPrivate.get("/categories");

  return res.data;
});
