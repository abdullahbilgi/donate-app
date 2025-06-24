import { createSlice } from "@reduxjs/toolkit";
import { getAllCity } from "./thunks";

interface Cities {
  id: number;
  name: string;
}
interface GetCities {
  cities: Cities[];
  loading: boolean;
  error: string | null;
}

const initialState: GetCities = {
  cities: [],
  loading: false,
  error: null,
};

const CityReducer = createSlice({
  name: "GetCategory",
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder.addCase(getAllCity.pending, (state, action) => {
      state.loading = true;
      state.cities = [];
    });

    builder.addCase(getAllCity.fulfilled, (state, action) => {
      state.loading = false;
      state.cities = action.payload;
      state.error = null;
    });

    builder.addCase(getAllCity.rejected, (state, action) => {
      state.error = action.error.message || "Something went wrong!";
      state.cities = [];
      state.loading = false;
    });
  },
});

export const CitiesActions = CityReducer.actions;

export default CityReducer.reducer;
