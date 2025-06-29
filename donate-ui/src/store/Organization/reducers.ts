import { createSlice } from "@reduxjs/toolkit";
import { getApplies } from "./thunks";

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
const OrganizationReducer = createSlice({
  name: "Organization",
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(getApplies.pending, (state, action) => {
        state.appliesLoading = true;
      })
      .addCase(getApplies.fulfilled, (state, action) => {
        console.log(action.payload);
        state.appliesLoading = false;
        state.appliesError = null;
        state.AllApplies = action.payload;
      })
      .addCase(getApplies.rejected, (state, action) => {
        state.appliesLoading = false;
        state.appliesError = null;
      });
  },
});

export const organizationActions = OrganizationReducer.actions;
export default OrganizationReducer.reducer;
