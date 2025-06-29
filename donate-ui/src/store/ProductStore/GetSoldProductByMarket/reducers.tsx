import { createSlice } from "@reduxjs/toolkit";
import { getSoldProductByMarket } from "./thunks";

interface Category {
  id: number;
  name: string;
}

interface Market {
  id: number;
  name: string;
  taxNumber: string;
}
interface UserResponse {
  id: number;
  name: string;
  surname: string;
  username: string;
  phone: string;
}
interface PurchaseResponse {
  cartProductResponse: {
    cartId: number;
    productPrice: number;
    productQuantity: number;
    productResponse: {
      name: string;
      description: string;
      discount: number;
      discountedPrice: number;
      expiryDate: string;
      lastDonatedDate: string;
      id: number;
      imageUrl: string | null;
      productStatus: string;
      productionDate: string;
      quantity: number;
      category: Category;
      market: Market;
    };
  };
  purchaseDate: string;
  userResponse: UserResponse;
}

interface Props {
  soldProducts: PurchaseResponse[];
  soldProductLoading: boolean;
  error: string | null;
}

type SoldProductsByMarket = {
  [marketId: number]: Props;
};
const initialState: SoldProductsByMarket = {};

const getSoldProducts = createSlice({
  name: "Market",
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(getSoldProductByMarket.pending, (state, action) => {
        const marketId = action.meta.arg; // thunk'a gönderilen marketId

        state[marketId] = {
          ...state[marketId],
          soldProductLoading: true,
        };
      })
      .addCase(getSoldProductByMarket.fulfilled, (state, action) => {
        console.log(action.payload);
        const marketId = action.meta.arg; // thunk'a gönderilen marketId

        state[marketId] = {
          soldProductLoading: false,
          error: null,
          soldProducts: action.payload,
        };
      })
      .addCase(getSoldProductByMarket.rejected, (state, action) => {
        const marketId = action.meta.arg; // thunk'a gönderilen marketId

        state[marketId] = {
          ...state[marketId],
          error: "Something went wrong",
        };
      });
  },
});

export const soldProductActions = getSoldProducts.actions;
export default getSoldProducts.reducer;
