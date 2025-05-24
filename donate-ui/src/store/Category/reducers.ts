import { createSlice } from "@reduxjs/toolkit";
import { getCategory } from "./thunks";

interface Category {
  id: number;
  name: string;
}
interface GetCategory {
  categories: Category[];
  loading: boolean;
  error: string | null;
}

const initialState: GetCategory = {
  categories: [],
  loading: false,
  error: null,
};

const CategoryReducer = createSlice({
  name: "GetCategory",
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder.addCase(getCategory.pending, (state, action) => {
      state.loading = true;
      state.categories = [];
    });

    builder.addCase(getCategory.fulfilled, (state, action) => {
      state.loading = false;
      state.categories = action.payload;
      state.error = null;
    });

    builder.addCase(getCategory.rejected, (state, action) => {
      state.error = action.error.message || "Something went wrong!";
      state.categories = [];
      state.loading = false;
    });
  },
});

export const CateogryActions = CategoryReducer.actions;

export default CategoryReducer.reducer;
