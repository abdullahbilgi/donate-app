import { createAsyncThunk } from "@reduxjs/toolkit";
import axios from "axios";
import axiosPrivate from "../../../api/axiosPrivate";

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

type GetAllProductsArgs = {
  page?: number;
  sort?: string;
};

export const getAllProducts = createAsyncThunk<any, GetAllProductsArgs>(
  "getAllProducts",
  async function ({ page = 0, sort }) {
    console.log(`/products?page=${page}&sort=${sort}`);
    const res = await axiosPrivate.get(`/products?page=${page}&sort=${sort}`);

    return res.data;
  }
);

export const createProduct = createAsyncThunk(
  "createProduct",
  async function (newProduct: CreateProductRequest) {
    console.log(newProduct);
    const res = await axiosPrivate.post("/products", newProduct);
    return res.data;
  }
);

export const updateProduct = createAsyncThunk(
  "updateProduct",
  async function (productId: any) {
    let token =
      "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlcmF5dGVzdCIsImlhdCI6MTc0NzkxMjkzOSwiZXhwIjoxNzQ3OTk5MzM5fQ.R-_vAAsPfeq6BDza6oTSmVGqq9mV_JqC4q4ibTsfVXg";

    const res = await axios({
      url: `http://localhost:8080/api/v1/products/${productId}`,
      method: "PATCH",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return res.data;
  }
);

export const deleteProduct = createAsyncThunk(
  "deleteProduct",
  async function (productId: any) {
    let token =
      "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlcmF5dGVzdCIsImlhdCI6MTc0NzkxMjkzOSwiZXhwIjoxNzQ3OTk5MzM5fQ.R-_vAAsPfeq6BDza6oTSmVGqq9mV_JqC4q4ibTsfVXg";

    const res = await axios({
      url: `http://localhost:8080/api/v1/products/${productId}`,
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return res.data;
  }
);

export const searchProduct = createAsyncThunk(
  "searchProduct",
  async function (key: string) {
    const res = await axiosPrivate.get(
      `/products/search?keyword=${key}&page=0&size=10`
    );

    return res.data;
  }
);

type GetAllDonatedProductsArgs = {
  page?: number;
};

export const getAllDonatedProducts = createAsyncThunk<
  any,
  GetAllDonatedProductsArgs
>("getAllDonatedProducts", async function ({ page = 0 }) {
  const res = await axiosPrivate.get(`/products/donated?page=${page}`);

  return res.data;
});
