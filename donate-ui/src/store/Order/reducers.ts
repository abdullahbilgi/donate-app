import { createSlice } from "@reduxjs/toolkit";
import { createOrder, getAllOrderByUser } from "./thunks";
type Category = {
  id: number;
  name: string;
};

type Market = {
  id: number;
  name: string;
  taxNumber: string;
  status: string | null;
};

type ProductResponse = {
  id: number;
  description: string;
  discount: number;
  discountedPrice: number;
  expiryDate: string;
  imageUrl: string | null;
  lastDonatedDate: string;
  market: Market;
  name: string;
  price: number;
  productStatus: string;
  productionDate: string;
  quantity: number;
  category: Category;
};

type ProductItem = {
  cartId: number;
  productPrice: number;
  productQuantity: number;
  productResponse: ProductResponse;
};

type Order = {
  id: number;
  expiredDate: string | null;
  productItems: ProductItem[];
  purchaseDate: string;
  status: string;
  totalPrice: number;
};

interface Orders {
  myOrders: Order[];
  loading: boolean;
  error: string | null;
}

const initialState: Orders = {
  myOrders: [],
  loading: false,
  error: null,
};

const OrderReducer = createSlice({
  name: "OrderReducer",
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder.addCase(getAllOrderByUser.pending, (state, action) => {
      state.loading = true;
    });

    builder.addCase(getAllOrderByUser.fulfilled, (state, action) => {
      state.loading = false;
      state.myOrders = action.payload; //gelen diziyi at
      state.error = null;
    });

    builder.addCase(getAllOrderByUser.rejected, (state, action) => {
      state.error = action.error.message || "Something went wrong!";
      state.loading = false;
    });

    builder.addCase(createOrder.pending, (state, action) => {
      state.loading = true;
    });

    builder.addCase(createOrder.fulfilled, (state, action) => {
      console.log(action.payload);
      // state.loading = false;
      // state.myOrders = [...state.myOrders, action.payload];
      // state.error = null;
    });

    builder.addCase(createOrder.rejected, (state, action) => {
      state.error = action.error.message || "Something went wrong!";
      state.loading = false;
    });
  },
});

export const OrdersActions = OrderReducer.actions;

export default OrderReducer.reducer;
