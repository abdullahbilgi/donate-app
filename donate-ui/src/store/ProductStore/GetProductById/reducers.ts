import { createSlice } from "@reduxjs/toolkit";
import { getProductById } from "./thunks";

interface Category {
  id: number;
  name: string;
}

interface ProductById {
  id: number;
  name: string;
  productionDate: string;
  expiryDate: string;
  price: number;
  discountedPrice: number;
  discount: number;
  quantity: number;
  description: string;
  productStatus: "DISCOUNT" | "NEW" | "OUT_OF_STOCK" | string;
  category: Category;
  imageUrl: string | null;
  categoryResponse: any;
}

interface GetProduct {
  productById: ProductById | null;
  loading: boolean;
  error: string | null;
}

const initialState: GetProduct = {
  productById: null,
  loading: false,
  error: null,
};

const ProductByIdReducer = createSlice({
  name: "GetProduct",
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder.addCase(getProductById.pending, (state, action) => {
      state.loading = true;
      state.productById = null;
    });

    builder.addCase(getProductById.fulfilled, (state, action) => {
      state.loading = false;
      state.productById = action.payload;
      state.error = null;
    });

    builder.addCase(getProductById.rejected, (state, action) => {
      state.error = action.error.message || "Something went wrong!";
      state.productById = null;
      state.loading = false;
    });
  },
});

export const ByIdProductActions = ProductByIdReducer.actions;

export default ProductByIdReducer.reducer;
