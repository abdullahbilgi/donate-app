import { createAsyncThunk } from "@reduxjs/toolkit";
import axios from "axios";

interface CreateProductRequest {
  name: string;
  productionDate: string;
  lastDonatedDate?: string; //sell icin
  expiryDate: string;
  productStatus: string;
  price?: number;
  discountedPrice?: number;
  quantity: number;
  description: string;
  categoryId: number;
  marketId?: number; //market icin
}

export const createProducts = createAsyncThunk(
  "createProduct",
  async function (newProduct: CreateProductRequest, thunkAPI) {
    console.log("Thunk'a gelen veri:", newProduct);
    try {
      let token =
        "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlcmF5dGVzdCIsImlhdCI6MTc0NzkxMjkzOSwiZXhwIjoxNzQ3OTk5MzM5fQ.R-_vAAsPfeq6BDza6oTSmVGqq9mV_JqC4q4ibTsfVXg";

      const res = await axios({
        url: "http://localhost:8080/api/v1/products",
        method: "POST",
        data: newProduct,
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      return res.data;
    } catch (err: any) {
      return thunkAPI.rejectWithValue("Ürün oluşturulurken bir hata oluştu");
    }
  }
);
