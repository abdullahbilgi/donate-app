import { createAsyncThunk } from "@reduxjs/toolkit";
import axios from "axios";

export const getAllProducts = createAsyncThunk(
  "getAllProducts",
  async function () {
    let token =
      "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkZGRkZGQiLCJpYXQiOjE3NDYzOTE4NTMsImV4cCI6MTc0NjM5MjQ1M30.OOhQ8PGiTZIxwSd1d76XghP54O9yAzk1TBW_wwK1Hyc";

    //filter, page falan burda ver o reducera ona göre veri dndürüür
    const res = await axios({
      url: "http://localhost:8080/api/v1/products",
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return res.data;
  }
);
