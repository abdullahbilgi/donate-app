import { createSlice } from "@reduxjs/toolkit";
import { deleteProduct, getAllProducts, updateProduct } from "./thunks";

interface Products {
  productsArr: any[];
  loading: boolean;
  error: string | null;
  number: number;
  size: number;
  totalPages: number;
  totalElements: number;
}

interface ProductItem {
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
  category: {
    id: number;
    name: string;
  };
  imageUrl: string | null;
  categoryResponse: any;
}

const initialState: Products = {
  productsArr: [],
  loading: false,
  error: null,
  number: 0,
  size: 12,
  totalPages: 0,
  totalElements: 0,
};

const ProductsReducer = createSlice({
  name: "Product",
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder.addCase(getAllProducts.pending, (state, action) => {
      state.productsArr = []; // Önceki verileri temizle
      state.loading = true; // Yükleniyor durumu
    });

    builder.addCase(getAllProducts.fulfilled, (state, action) => {
      state.loading = false; // Yükleniyor durumu
      state.error = null; // Hata mesajı sıfırlanıyor
      state.number = action.payload.page + 1; // Sayfa numarasını güncelle (0-indexli backend için +1 ekle)
      state.size = action.payload.limit; // Sayfa başı kaç veri olduğunu güncelle

      state.productsArr = action.payload.content; // Yeni verilerle listeyi güncelle
    });

    builder.addCase(getAllProducts.rejected, (state, action) => {
      state.loading = false;
      state.error = action.error.message || "Something went wrong";
      state.productsArr = [];
      state.number = 0;
      state.size = 12;
      state.totalPages = 0;
      state.totalElements = 0;
    });

    builder.addCase(updateProduct.fulfilled, (state, action) => {
      state.loading = false; // Yükleniyor durumu
      state.error = null; // Hata mesajı sıfırlanıyor
      state.productsArr = state.productsArr.map((product) => {
        //bunun nedeni getAll'i beklemeden hemen UIda günceli göstermek icin

        return product.id === action.payload.id
          ? { ...product, ...action.payload }
          : product;
      });
    });

    builder.addCase(updateProduct.rejected, (state, action) => {
      state.loading = false;
      state.error = action.error.message || "Something went wrong";
    });

    builder.addCase(deleteProduct.fulfilled, (state, action) => {
      state.loading = false; // Yükleniyor durumu
      state.error = null; // Hata mesajı sıfırlanıyor
      state.productsArr = state.productsArr.filter(
        (product) => product.id !== action.payload.id
      );
    });

    builder.addCase(deleteProduct.rejected, (state, action) => {
      state.loading = false;
      state.error = action.error.message || "Something went wrong";
    });
  },
});

export const ProductActions = ProductsReducer.actions;

export default ProductsReducer.reducer;
