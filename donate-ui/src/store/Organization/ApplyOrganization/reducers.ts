import { createSlice } from "@reduxjs/toolkit";
import { applyOrganization } from "./thunks";

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

export interface IUOrganization {
  id: number;
  name: string;
  taxNumber: string;
  status: string;
  address: IUAddress;
}

interface IUInitialState {
  myApplies: IUOrganization[];
  applyLoading: boolean;
  applyError: null | string;
}
const initialState: IUInitialState = {
  myApplies: [],
  applyLoading: false,
  applyError: null,
};
const ApplyOrganizationReducer = createSlice({
  name: "ApplyOrganization",
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(applyOrganization.pending, (state, action) => {
        state.applyLoading = true;
      })
      .addCase(applyOrganization.fulfilled, (state, action) => {
        state.applyLoading = false;
        state.applyError = null;
        state.myApplies = [...state.myApplies, action.payload];
      })
      .addCase(applyOrganization.rejected, (state, action) => {
        state.applyLoading = false;
        state.applyError = null;
      });
  },
});

export const applyOrganizationActions = ApplyOrganizationReducer.actions;
export default ApplyOrganizationReducer.reducer;
