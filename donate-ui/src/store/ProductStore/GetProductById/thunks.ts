import { createAsyncThunk } from "@reduxjs/toolkit";
import axios from "axios";

export const getProductById = createAsyncThunk(
  "getProductById",
  async (id: any) => {
    //filter, page falan burda ver o reducera ona göre veri dndürüür
    const res = await axios({
      url: `http://localhost:8080/api/v1/products/${id}`,
      method: "GET",
      //headers: { "Content-Type": "application/json" },
    });

    return res.data;
  }
);
