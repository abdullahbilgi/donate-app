import { createSlice } from "@reduxjs/toolkit";
import { createProducts } from "./thunks";

interface Category {
  id: number;
  name: string;
}

interface Product {
  id: number;
  name: string;
  productionDate: string;
  expiryDate: string;
  price: number;
  discountedPrice: number;
  discount: number;
  quantity: number;
  description: string;
  productStatus: "DISCOUNT" | "NORMAL" | string;
  category: Category;
  imageUrl: string | null;
  categoryResponse: any; // net değilse any
}

interface CreateProductState {
  product: Product | null;
  loading: boolean;
  error: string | null;
}

const initialState: CreateProductState = {
  product: null,
  loading: false,
  error: null,
};

const CreateProductsReducer = createSlice({
  name: "CreateProduct",
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(createProducts.pending, (state) => {
        state.loading = true;
      })
      .addCase(createProducts.fulfilled, (state, action) => {
        state.loading = false;
        state.product = action.payload;
        state.error = null;
      })
      .addCase(createProducts.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as
          | string
          | "Ürün oluşturulurken bir hata oluştu";
      });
  },
});

export const CreateProductActions = CreateProductsReducer.actions;
export default CreateProductsReducer.reducer;
