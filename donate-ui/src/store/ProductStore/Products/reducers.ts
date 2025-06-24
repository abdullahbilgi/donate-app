import { createSlice } from "@reduxjs/toolkit";
import {
  createProduct,
  deleteProduct,
  getAllProducts,
  searchProduct,
  updateProduct,
} from "./thunks";

interface Products {
  productsArr: any[];
  searchProducts: any[];
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
  searchProducts: [],
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
      console.log(action.payload);
      state.loading = false; // Yükleniyor durumu
      state.error = null; // Hata mesajı sıfırlanıyor
      state.number = action.payload.number; // Sayfa numarasını güncelle
      state.size = action.payload.size; // Sayfa başı kaç veri olduğunu güncelle
      state.totalPages = action.payload.totalPages;
      state.totalElements = action.payload.totalElements;
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

    builder.addCase(createProduct.pending, (state, action) => {
      state.loading = true;
    });

    builder.addCase(createProduct.fulfilled, (state, action) => {
      state.loading = false; // Yükleniyor durumu
      state.error = null; // Hata mesajı sıfırlanıyor

      state.productsArr = [...state.productsArr, action.payload.content]; // Yeni verilerle listeyi güncelle
    });

    builder.addCase(createProduct.rejected, (state, action) => {
      state.loading = false;
      state.error = action.error.message || "Something went wrong";
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

    builder.addCase(searchProduct.pending, (state, action) => {
      state.searchProducts = []; // Önceki verileri temizle
      state.loading = true; // Yükleniyor durumu
    });

    builder.addCase(searchProduct.fulfilled, (state, action) => {
      state.loading = false; // Yükleniyor durumu
      state.error = null; // Hata mesajı sıfırlanıyor
      state.number = action.payload.page + 1; // Sayfa numarasını güncelle (0-indexli backend için +1 ekle)
      state.size = action.payload.size; // Sayfa başı kaç veri olduğunu güncelle
      state.totalPages = action.payload.totalPages;
      state.totalElements = action.payload.totalElements;
      state.searchProducts = action.payload.content; // Yeni verilerle listeyi güncelle
    });

    builder.addCase(searchProduct.rejected, (state, action) => {
      state.loading = false;
      state.error = action.error.message || "Something went wrong";
      state.searchProducts = [];
      state.number = 0;
      state.size = 12;
      state.totalPages = 0;
      state.totalElements = 0;
    });
  },
});

export const ProductActions = ProductsReducer.actions;

export default ProductsReducer.reducer;
