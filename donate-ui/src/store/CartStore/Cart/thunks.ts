import { createAsyncThunk } from "@reduxjs/toolkit";
import axios from "axios";

interface AddItemRequest {
  userId: number;
  productId: number;
  productQuantity: number;
}

interface DeleteItemRequest {
  cartId: number;
  productId: number;
}

interface UpdateItemRequest {
  cartId: number;
  productId: number;
  productQuantity: number;
}

export const addProductToCart = createAsyncThunk(
  "addProductToCart",
  async function (item: AddItemRequest, thunkAPI) {
    console.log(item);
    try {
      let token =
        "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlcmF5dGVzdCIsImlhdCI6MTc0NzkxMjkzOSwiZXhwIjoxNzQ3OTk5MzM5fQ.R-_vAAsPfeq6BDza6oTSmVGqq9mV_JqC4q4ibTsfVXg";

      const res = await axios({
        url: "http://localhost:8080/api/v1/carts/addProduct",
        method: "POST",
        data: {
          userId: item.userId,
          productId: item.productId,
          productQuantity: item.productQuantity,
        },
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      return res.data;
    } catch (err: any) {
      return thunkAPI.rejectWithValue("Ürün eklenirekn bir hata oluştu");
    }
  }
);

export const removeItemFromCart = createAsyncThunk(
  "removeItemFromCart",
  async function (deleteItem: DeleteItemRequest, thunkAPI) {
    try {
      let token =
        "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlcmF5dGVzdCIsImlhdCI6MTc0NzkxMjkzOSwiZXhwIjoxNzQ3OTk5MzM5fQ.R-_vAAsPfeq6BDza6oTSmVGqq9mV_JqC4q4ibTsfVXg";

      const res = await axios({
        url: "http://localhost:8080/api/v1/carts/removeProduct",
        method: "POST",
        data: {
          cartId: deleteItem.cartId,
          productId: deleteItem.productId,
        },
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      console.log("api delete", res.data);
      return res.data;
    } catch (err: any) {
      return thunkAPI.rejectWithValue("Ürün eklenirekn bir hata oluştu");
    }
  }
);

export const updateCartItem = createAsyncThunk(
  "updateCartItem",
  async function (updateItem: UpdateItemRequest, thunkAPI) {
    try {
      const res = await axios({
        url: "http://localhost:8080/api/v1/carts/updateCartProduct",
        method: "PUT",
        data: {
          cartId: updateItem.cartId,
          productId: updateItem.productId,
          productQuantity: updateItem.productQuantity,
        },
      });

      console.log("api update", res.data);
      return res.data;
    } catch (err: any) {
      return thunkAPI.rejectWithValue("Bir hata oluştu");
    }
  }
);
