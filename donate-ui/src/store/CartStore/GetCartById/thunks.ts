import { createAsyncThunk } from "@reduxjs/toolkit";
import axios from "axios";
import axiosPrivate from "../../../api/axiosPrivate";

export const getCartById = createAsyncThunk(
  "getCartById",
  async function (userId: any) {
    // let token =
    //   "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlcmF5dGVzdCIsImlhdCI6MTc0NzMyMDA2OCwiZXhwIjoxNzQ3NDA2NDY4fQ.g6qV4UcwIyzymqLIMPDpkxKO36axX7oziiM-QC4x8_A";

    // const res = await axios({
    //   url: `http://localhost:8080/api/v1/carts/${userId}`,
    //   method: "GET",
    //   // headers: {
    //   //   Authorization: `Bearer ${token}`,
    //   // },
    // });

    const res = await axiosPrivate.get(`carts/${userId}`);

    // console.log(res.data);

    return res.data;
  }
);
