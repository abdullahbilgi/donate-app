import { createAsyncThunk, createSlice, Dispatch } from "@reduxjs/toolkit";

interface CartItem {
  id: string;
  image: string;
  label: string;
  normalPrice: number;
  discountPrice: number;
  totalPrice: number;
  quantity: number;
}

interface CartState {
  products: CartItem[];
  totalQuantity: number;
}

const initialState: CartState = {
  products: [],
  totalQuantity: 0,
};

const cartSlice = createSlice({
  name: "cart",
  initialState,

  reducers: {},
});

export const cartActions = cartSlice.actions;
export default cartSlice;
