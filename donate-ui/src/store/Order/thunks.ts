import { createAsyncThunk } from "@reduxjs/toolkit";
import axiosPrivate from "../../api/axiosPrivate";

export const getAllOrderByUser = createAsyncThunk(
  "getAllOrderByUser",
  async (userId: any) => {
    const res = await axiosPrivate.get(`/carts/purchasedProducts/${userId}`);

    console.log(res.data);

    return res.data;
  }
);

export const createOrder = createAsyncThunk(
  "createOrder",
  async (cartId: any) => {
    const res = await axiosPrivate.put(`/carts/approveCart/${cartId}`);

    console.log(res.data);
    return res.data;
  }
);

export const getOrderInvoice = createAsyncThunk(
  "getOrderInvoice",
  async (orderId: any) => {
    const res = await axiosPrivate.get(`/carts/${orderId}/pdf`, {
      responseType: "blob",
    });

    console.log(res.data);

    const url = window.URL.createObjectURL(res.data);
    window.open(url); // yeni sekmede açar
  }
);
