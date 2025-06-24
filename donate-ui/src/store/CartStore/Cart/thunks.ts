import { createAsyncThunk } from "@reduxjs/toolkit";
import axios from "axios";
import axiosPrivate from "../../../api/axiosPrivate";

interface AddItemRequest {
  productId: number;
  productQuantity: number;
}

interface DeleteItemRequest {
  cartId: number;
  productId: number;
}

interface UpdateItemRequest {
  productId: number;
  productQuantity: number;
}

export const addProductToCart = createAsyncThunk(
  "addProductToCart",

  async function (item: AddItemRequest, thunkAPI) {
    console.log(item);
    const userId = localStorage.getItem("userId");

    try {
      const res = await axiosPrivate.post("/carts/addProduct", {
        userId: userId,
        ...item,
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
      console.log(deleteItem);
      const res = await axiosPrivate.post("/carts/removeProduct", deleteItem);

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
    const cartId = Number(localStorage.getItem("cartId"));
    const userId = Number(localStorage.getItem("userId"));

    console.log("update send data", {
      ...updateItem,
      cartId,
      userId,
    });

    try {
      const res = await axiosPrivate.put("/carts/updateCartProduct", {
        ...updateItem,
        cartId,
        userId,
      });
      return res.data;
    } catch (err: any) {
      return thunkAPI.rejectWithValue("Sepet güncellenirken bir hata oluştu");
    }
  }
);
