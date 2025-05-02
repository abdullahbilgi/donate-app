import { createStore } from "redux";
import { configureStore, createSlice } from "@reduxjs/toolkit";
import counterSlice from "./counter";
import authSlice from "./auth";
import cartSlice from "./cart-slice";

const store = configureStore({
  reducer: {
    counter: counterSlice,
    authentication: authSlice,
    cart: cartSlice.reducer,
  },
}); //bank olustur
//sadece 1 banka olusturulabilir. Birden fazla slice varsa CombineReducers veya configureStore
//configureStore tüm reducerlari ana reducerda birlestirir

export default store;
