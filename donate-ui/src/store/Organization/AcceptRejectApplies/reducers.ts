import { createSlice } from "@reduxjs/toolkit";
import { acceptApply, getApplies, rejectedApply } from "./thunks";

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
  AllApplies: IUOrganization[];
  appliesLoading: boolean;
  appliesError: null | string;
}
const initialState: IUInitialState = {
  AllApplies: [],
  appliesLoading: false,
  appliesError: null,
};
const AcceptReceptOrganizationReducer = createSlice({
  name: "AcceptRejectOrganization",
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(getApplies.pending, (state) => {
        state.appliesLoading = true;
      })
      .addCase(getApplies.fulfilled, (state, action) => {
        console.log(action.payload);
        state.appliesLoading = false;
        state.appliesError = null;
        state.AllApplies = action.payload;
      })
      .addCase(getApplies.rejected, (state) => {
        state.appliesLoading = false;
        state.appliesError = null;
      })
      .addCase(acceptApply.pending, (state) => {
        state.appliesLoading = true;
      })
      .addCase(acceptApply.fulfilled, (state, action) => {
        console.log(action.payload);
        state.appliesLoading = false;
        state.appliesError = null;
        state.AllApplies = state.AllApplies.filter(
          (apply) => apply.id !== action.payload.id
        );
      })
      .addCase(acceptApply.rejected, (state) => {
        state.appliesLoading = false;
        state.appliesError = null;
      })
      .addCase(rejectedApply.pending, (state) => {
        state.appliesLoading = true;
      })
      .addCase(rejectedApply.fulfilled, (state, action) => {
        state.appliesLoading = false;
        state.appliesError = null;
        state.AllApplies = state.AllApplies.filter(
          (apply) => apply.id !== action.payload.id
        );
      })
      .addCase(rejectedApply.rejected, (state) => {
        state.appliesLoading = false;
        state.appliesError = null;
      });
  },
});

export const acceptReceptOrganizationActions =
  AcceptReceptOrganizationReducer.actions;
export default AcceptReceptOrganizationReducer.reducer;
