import { createSlice } from "@reduxjs/toolkit";
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
  loading: boolean;
  error: string | null;
}

const initialState: CartState = {
  cartItems: [],
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
        console.log("getirdi:", action.payload.productItems);
        state.loading = false;
        state.error = null;
        state.cartItems = action.payload.productItems.map((item: any) => ({
          product: item.productResponse,
          productQuantity: item.productQuantity,
        }));
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
          console.log("yok");
          state.cartItems.push(action.payload);
        }
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

        const existingItem = state.cartItems.find(
          (item) => item.product.id === action.payload.product.id
        );

        if (existingItem) {
          existingItem.product = action.payload.product;
          existingItem.productQuantity = action.payload.productQuantity;
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

        const existingItem = state.cartItems.filter(
          (item) => item.product.id !== action.payload.product.id
        );

        if (!existingItem) return;
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
