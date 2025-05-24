import { createAsyncThunk } from "@reduxjs/toolkit";
import axios from "axios";
import axiosPrivate from "../../../api/axiosPrivate";

export const getAllProducts = createAsyncThunk(
  "getAllProducts",

  async function () {
    let token =
      "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlcmF5dGVzdCIsImlhdCI6MTc0NzkxMjkzOSwiZXhwIjoxNzQ3OTk5MzM5fQ.R-_vAAsPfeq6BDza6oTSmVGqq9mV_JqC4q4ibTsfVXg";

    // const res = await axios({
    //   url: `http://localhost:8080/api/v1/products`,
    //   method: "GET",
    //   headers: {
    //     Authorization: `Bearer ${token}`,
    //   },
    // });

    const res = await axiosPrivate.get("/products");
    return res.data;
  }
  // async function () {
  //   //filter, page falan burda ver o reducera ona göre veri dndürüür
  //   const res = await axiosPrivate.get("products");
  //   return res.data;
  // }
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
