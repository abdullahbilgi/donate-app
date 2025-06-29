import { configureStore, createSlice } from "@reduxjs/toolkit";
import productsReducer from "./ProductStore/Products/reducers";
import cartReducer from "./CartStore/Cart/reducers";
import authReducer from "./Auth/Login/reducers";
import categoryReducer from "./Category/reducers";
import marketReducer from "./MarketStore/Market/reducers";
import productByMarket from "./ProductStore/GetProductByMarket/reducers";
import citiesReducer from "./City/reducers";
import ordersReducer from "./Order/reducers";
import getSoldProductByMarket from "./ProductStore/GetSoldProductByMarket/reducers";
import applyOrganizationReducer from "./Organization/ApplyOrganization/reducers";
import organizationReducer from "./Organization/reducers";
import { useDispatch, useSelector, TypedUseSelectorHook } from "react-redux";

const store = configureStore({
  reducer: {
    Cart: cartReducer,
    Category: categoryReducer,
    Product: productsReducer,
    Auth: authReducer,
    Market: marketReducer,
    ProductsByMarket: productByMarket,
    SoldProductsByMarket: getSoldProductByMarket,
    City: citiesReducer,
    Order: ordersReducer,
    ApplyOrganizations: applyOrganizationReducer,
    Organization: organizationReducer,
  },
});

// Burada tipleri tanımlıyoruz:
export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

// useDispatch ve useSelector'u tipli hale getiriyoruz:
export const useAppDispatch: () => AppDispatch = useDispatch;
export const useAppSelector: TypedUseSelectorHook<RootState> = useSelector;

export default store;
