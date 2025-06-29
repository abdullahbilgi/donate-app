import { createSlice } from "@reduxjs/toolkit";
import { acceptApply, rejectedApply } from "./AcceptRejectApplies/thunks";
import { deleteOrganization, getAllOrganizations } from "./thunks";

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
  Organizations: IUOrganization[];
  loading: boolean;
  error: null | string;
}
const initialState: IUInitialState = {
  Organizations: [],
  loading: false,
  error: null,
};
const OrganizationReducer = createSlice({
  name: "Organization",
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(getAllOrganizations.pending, (state) => {
        state.loading = true;
      })
      .addCase(getAllOrganizations.fulfilled, (state, action) => {
        console.log(action.payload);
        state.loading = false;
        state.error = null;
        state.Organizations = action.payload;
      })
      .addCase(getAllOrganizations.rejected, (state) => {
        state.loading = false;
        state.error = null;
      })
      .addCase(acceptApply.pending, (state) => {
        state.loading = true;
      })
      .addCase(acceptApply.fulfilled, (state, action) => {
        console.log(action.payload);
        state.loading = false;
        state.error = null;
        state.Organizations = [...state.Organizations, action.payload];
      })
      .addCase(acceptApply.rejected, (state) => {
        state.loading = false;
        state.error = null;
      })
      .addCase(deleteOrganization.pending, (state) => {
        state.loading = true;
      })
      .addCase(deleteOrganization.fulfilled, (state, action) => {
        console.log(action.payload);
        state.loading = false;
        state.error = null;
        state.Organizations = state.Organizations.filter(
          (apply) => apply.id !== action.payload.id
        );
      })
      .addCase(deleteOrganization.rejected, (state) => {
        state.loading = false;
        state.error = null;
      });
  },
});

export const organizationActions = OrganizationReducer.actions;
export default OrganizationReducer.reducer;
