import { createAsyncThunk } from "@reduxjs/toolkit";
import axios from "axios";

export const getCategory = createAsyncThunk("getProductById", async () => {
  let token =
    "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlcmF5dGVzdCIsImlhdCI6MTc0NzkxMjkzOSwiZXhwIjoxNzQ3OTk5MzM5fQ.R-_vAAsPfeq6BDza6oTSmVGqq9mV_JqC4q4ibTsfVXg";

  //filter, page falan burda ver o reducera ona göre veri dndürüür
  const res = await axios({
    url: "http://localhost:8080/api/v1/categories",
    method: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  return res.data;
});
