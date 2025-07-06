import { createSlice } from "@reduxjs/toolkit";
import { getMyApply } from "./thunks";
import { rejectedApply } from "../AcceptRejectApplies/thunks";
import { applyOrganization } from "../ApplyOrganization/thunks";

export interface IUCity {
  id: number;
  name: string;
}

export interface IURegion {
  id: number;
  name: string;
  city: IUCity;
}
export interface IUUser {
  email: string;
  name: string;
  surname: string;
  phone: string;
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
  user: IUUser;
}

interface IUInitialState {
  Apply: IUOrganization | null;
  loading: boolean;
  error: null | string;
}
const initialState: IUInitialState = {
  Apply: null,
  loading: false,
  error: null,
};
const GetMyApplyReducer = createSlice({
  name: "Apply",
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(getMyApply.pending, (state) => {
        state.loading = true;
      })
      .addCase(getMyApply.fulfilled, (state, action) => {
        console.log(action.payload);
        state.loading = false;
        state.error = null;
        state.Apply = action.payload[0];
      })
      .addCase(getMyApply.rejected, (state) => {
        state.loading = false;
        state.error = null;
      })
      .addCase(applyOrganization.fulfilled, (state, action) => {
        state.loading = false;
        state.error = null;
        state.Apply = action.payload;
      })
      .addCase(rejectedApply.fulfilled, (state, action) => {
        state.loading = false;
        state.error = null;
        state.Apply = null;
      });
  },
});

export const getMyApplyActions = GetMyApplyReducer.actions;
export default GetMyApplyReducer.reducer;
