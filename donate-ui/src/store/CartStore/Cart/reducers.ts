import { createSlice } from "@reduxjs/toolkit";
import { addProductToCart, removeItemFromCart, updateCartItem } from "./thunks";
import { getCartById } from "../GetCartById/thunks";

interface ProductResponse {
  id: number;
  name: string;
  productionDate: string;
  expiryDate: string;
  lastDonatedDate: string;
  price: number;
  discountedPrice: number;
  discount: number;
  quantity: number;
  description: string;
  productStatus: "REAL" | "DONATE";
  category: Category;
  market: any | null;
  imageUrl: string | null;
}

type Category = {
  id: number;
  name: string;
};

interface CartItem {
  product: ProductResponse;
  productQuantity: number;
}

interface CartState {
  cartItems: CartItem[];
  subTotal: number;
  totalDiscPrice: number;
  totalPrice: 0;
  loading: boolean;
  error: string | null;
}

const initialState: CartState = {
  cartItems: [],
  subTotal: 0,
  totalDiscPrice: 0,
  totalPrice: 0,
  loading: false,
  error: null,
};
const CartReducer = createSlice({
  name: "Cart",
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(getCartById.fulfilled, (state, action) => {
        // sayfa ilk yüklendiginde(applayout) cart verilerini getirir
        console.log("getirdi:", action.payload);
        state.loading = false;
        state.error = null;
        state.cartItems = action.payload.productItems.map((item: any) => ({
          product: item.productResponse,
          productQuantity: item.productQuantity,
        }));
        state.subTotal = action.payload.productItems.reduce(
          (acc: number, item: any) =>
            acc + item.productResponse.price * item.productQuantity,
          0
        );
        state.totalDiscPrice = action.payload.productItems.reduce(
          (acc: number, item: any) =>
            acc +
            (item.productResponse.price -
              item.productResponse.discountedPrice) *
              item.productQuantity,
          0
        );
        state.totalPrice = action.payload.totalPrice;
      })
      .addCase(getCartById.rejected, (state, action) => {
        // sayfa ilk yüklendiginde(applayout) cart verilerini getirir
        console.log("getiremedi");
        state.loading = false;
        state.error = null;
        state.cartItems = [];
      })
      .addCase(addProductToCart.pending, (state) => {
        state.loading = true;
      })
      .addCase(addProductToCart.fulfilled, (state, action) => {
        state.loading = false;
        state.error = null;

        const existingItem = state.cartItems.find(
          (item) => item?.product?.id === action.payload?.product?.id
        );

        if (existingItem) {
          console.log(action.payload.productQuantity);
          existingItem.productQuantity = action.payload.productQuantity;
        } else {
          state.cartItems.push(action.payload);
        }

        state.subTotal += Number(action.payload.product.price);
        state.totalDiscPrice +=
          Number(action.payload.product.price) -
          Number(action.payload.product.discountedPrice);
        state.totalPrice += Number(action.payload.product.discountedPrice);
      })
      .addCase(addProductToCart.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as
          | string
          | "Ürün eklenirken bir hata oluştu";
      })

      .addCase(updateCartItem.pending, (state) => {
        state.loading = true;
      })
      .addCase(updateCartItem.fulfilled, (state, action) => {
        state.loading = false;
        state.error = null;

        console.log("gelen:", action.payload);

        const existingItem = state.cartItems.find(
          (item) => item.product.id === action.payload.productResponse.id
        );

        if (existingItem) {
          console.log(action.payload);
          const diff =
            action.payload.productQuantity - existingItem.productQuantity;
          // existingItem.product = action.payload.product;
          existingItem.productQuantity = action.payload.productQuantity;
          state.subTotal += Number(action.payload.productResponse.price) * diff;
          state.totalDiscPrice +=
            (Number(action.payload.productResponse.price) -
              Number(action.payload.productResponse.discountedPrice)) *
            diff;
          state.totalPrice +=
            Number(action.payload.productResponse.discountedPrice) * diff;
        }
      })
      .addCase(updateCartItem.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as
          | string
          | "Ürün eklenirken bir hata oluştu";
      })
      .addCase(removeItemFromCart.pending, (state) => {
        state.loading = true;
      })
      .addCase(removeItemFromCart.fulfilled, (state, action) => {
        state.loading = false;
        state.error = null;

        console.log(action.payload);
        const existingItem = state.cartItems.find(
          (item) => item.product.id === action.payload.productResponse.id
        );

        state.cartItems = state.cartItems.filter(
          (item) => item.product.id !== action.payload.productResponse.id
        );

        console.log(state.totalDiscPrice);
        console.log(
          (Number(existingItem?.product.price) -
            Number(existingItem?.product.discountedPrice)) *
            Number(existingItem?.productQuantity)
        );
        state.totalDiscPrice =
          state.totalDiscPrice -
          (Number(existingItem?.product.price) -
            Number(existingItem?.product.discountedPrice)) *
            Number(existingItem?.productQuantity);
        state.subTotal -=
          Number(existingItem?.product.price) *
          Number(existingItem?.productQuantity);

        state.totalPrice -=
          Number(existingItem?.product.discountedPrice) *
          Number(existingItem?.productQuantity);
      })
      .addCase(removeItemFromCart.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as
          | string
          | "Ürün eklenirken bir hata oluştu";
      });
  },
});

export const cartActions = CartReducer.actions;
export default CartReducer.reducer;
