import { createSlice } from "@reduxjs/toolkit";

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
  items: CartItem[];
  totalQuantity: number;
}

const initialState: CartState = {
  items: [],
  totalQuantity: 0,
};

const cartSlice = createSlice({
  name: "cart",
  initialState,

  reducers: {
    addItemToCart(state, action) {
      const newItem = action.payload;

      const existingItem = state.items.find((item) => item.id === newItem.id);

      state.totalQuantity++;
      if (!existingItem) {
        state.items.push(newItem);
      } else {
        existingItem.quantity++;
        existingItem.totalPrice =
          Number(existingItem.totalPrice) + Number(newItem.discountPrice);
      }
    },
    removeItemFromCart(state, action) {
      const deleteItemId = action.payload;

      const existingItem = state.items.find((item) => item.id === deleteItemId);

      state.totalQuantity--;

      if (!existingItem) return;

      if (existingItem?.quantity === 1) {
        state.items = state.items.filter((item) => item.id !== existingItem.id);
      } else {
        existingItem.quantity--;
        existingItem.totalPrice =
          existingItem.totalPrice - existingItem.discountPrice;
      }
    },

    deleteItemFromCart(state, action) {
      const deleteItemId = action.payload;

      const existingItem = state.items.find((item) => item.id === deleteItemId);

      state.totalQuantity--;

      if (!existingItem) return;

      state.items = state.items.filter((item) => item.id !== existingItem.id);
    },
  },
});

export const cartActions = cartSlice.actions;
export default cartSlice;
