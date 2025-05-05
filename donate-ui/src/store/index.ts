import { configureStore, createSlice } from "@reduxjs/toolkit";
import counterSlice from "./counter";
import authSlice from "./auth";
import cartSlice from "./cart-slice";
import productsReducer from "./products/reducers";
import { useDispatch, useSelector, TypedUseSelectorHook } from "react-redux";

const store = configureStore({
  reducer: {
    counter: counterSlice,
    authentication: authSlice,
    cart: cartSlice.reducer,
    Product: productsReducer,
  },
});

// 🔸 Burada tipleri tanımlıyoruz:
export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

// 🔸 useDispatch ve useSelector'u tipli hale getiriyoruz:
export const useAppDispatch: () => AppDispatch = useDispatch;
export const useAppSelector: TypedUseSelectorHook<RootState> = useSelector;

export default store;
