import { createSlice } from "@reduxjs/toolkit";
import { getProductsByMarket } from "./thunks";

export interface Category {
  id: number;
  name: string;
}

export interface Market {
  id: number;
  name: string;
  taxNumber: string;
  status: string | null;
  user: any | null;
  address: any | null;
}

export interface Product {
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
  market: Market;
  imageUrl: string | null;
}

export interface Pagination {
  pageNumber: number;
  pageSize: number;
  offset: number;
  paged: boolean;
  unpaged: boolean;
}

export interface ProductApiResponse {
  content: Product[];
  pageable: Pagination;
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
  sort: any;
  first: boolean;
  last: boolean;
  numberOfElements: number;
  empty: boolean;
}

interface ProductState {
  products: Product[];
  loading: boolean;
  error: string | null;
  totalPages: number;
  currentPage: number;
  totalElements: number;
}

type ProductsByMarketState = Record<number, ProductState>;

const initialState: ProductsByMarketState = {};

const productByMarket = createSlice({
  name: "productByMarket",
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(getProductsByMarket.pending, (state, action) => {
        const marketId = action.meta.arg;
        state[marketId] = {
          ...state[marketId],
          loading: true,
        };
      })
      .addCase(getProductsByMarket.fulfilled, (state, action) => {
        const marketId = action.meta.arg;

        state[marketId] = {
          loading: false,
          error: null,
          products: action.payload.content,
          totalPages: action.payload.totalPages,
          currentPage: action.payload.number,
          totalElements: action.payload.totalElements,
        };
      })
      .addCase(getProductsByMarket.rejected, (state, action) => {
        const marketId = action.meta.arg;
        state[marketId] = {
          loading: false,
          error: String(action.error.message),
          products: [],
          totalPages: 0,
          currentPage: 0,
          totalElements: 0,
        };
      });
  },
});

export const cartActions = productByMarket.actions;
export default productByMarket.reducer;
