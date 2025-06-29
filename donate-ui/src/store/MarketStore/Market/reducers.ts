import { createSlice } from "@reduxjs/toolkit";
import { createMarket, deleteMarket, getMarketByUser } from "./thunks";

export interface IUCity {
  id: number;
  name: string;
}
export interface IURegion {
  id: number;
  name: string;
  city: IUCity;
}
export interface IUAddress {
  id: number;
  name: string;
  region: IURegion;
  latitude: number;
  longitude: number;
  zipCode: string;
}

export interface IUMarket {
  id: number;
  name: string;
  taxNumber: string;
  status: string;
  address: IUAddress;
}

interface IUInitialState {
  marketsArr: IUMarket[];
  loading: boolean;
  error: null | string;
}
const initialState: IUInitialState = {
  marketsArr: [],
  loading: false,
  error: null,
};
const marketReducer = createSlice({
  name: "Market",
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(getMarketByUser.pending, (state, action) => {
        state.loading = true;
      })
      .addCase(getMarketByUser.fulfilled, (state, action) => {
        state.loading = false;
        state.error = null;
        state.marketsArr = action.payload;
      })
      .addCase(getMarketByUser.rejected, (state, action) => {
        state.loading = false;
        state.error = null;
        state.marketsArr = [];
      })
      .addCase(createMarket.pending, (state, action) => {
        state.loading = true;
      })
      .addCase(createMarket.fulfilled, (state, action) => {
        state.loading = false;
        state.error = null;
        state.marketsArr = [...state.marketsArr, action.payload];
      })
      .addCase(createMarket.rejected, (state, action) => {
        state.loading = false;
        state.error = null;
      })
      .addCase(deleteMarket.pending, (state, action) => {
        state.loading = true;
      })
      .addCase(deleteMarket.fulfilled, (state, action) => {
        console.log(action.payload);
        state.loading = false;
        state.error = null;
        state.marketsArr = state.marketsArr.filter(
          (market) => market.id !== action.payload
        );
      })
      .addCase(deleteMarket.rejected, (state, action) => {
        state.loading = false;
        state.error = null;
      });
  },
});

export const cartActions = marketReducer.actions;
export default marketReducer.reducer;
