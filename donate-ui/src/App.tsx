import { BrowserRouter, Navigate, Route, Routes } from "react-router";
import Home from "./pages/Home";
import Login from "./pages/Login";
import AppLayout from "./pages/AppLayout";
import Signup from "./pages/Signup";
import Products from "./pages/Products";
import { useEffect, useState } from "react";
import { setToken, setupInterceptors } from "./api/interceptors";
import { Markets } from "./pages/Markets";
import ProductsByMarket from "./pages/ProductsByMarket";
import AddProductForm from "./pages/AddProductForm";
import { Orders } from "./pages/Orders";
import SoldProductByMarket from "./pages/SoldProductByMarket";
import ProtectedRoute from "./Routes/ProtectedRoute";
import { useAppDispatch, useAppSelector } from "./store";
import { getMe } from "./store/Auth/Login/thunks";
import ApplyOrganization from "./pages/ApplyOrganization";
import AppliesOrganization from "./pages/AppliesOrganization";
import { Toaster } from "react-hot-toast";

function App() {
  const [ready, setReady] = useState(false);
  const dispatch = useAppDispatch();

  useEffect(() => {
    const token = localStorage.getItem("accessToken");
    if (token) {
      setToken(token);
      setupInterceptors();
      dispatch(getMe()); // user Redux update
    }
    setReady(true); // Token kontrolü yapıldıktan sonra render’a geç
  }, [dispatch]);

  if (!ready) return <div>Yükleniyor...</div>; // İlk yükleme sırasında beklet
  return (
    <>
      <Toaster />
      <BrowserRouter>
        <Routes>
          <Route element={<AppLayout />}>
            <Route index element={<Navigate replace to="/home" />} />
            <Route
              path="/home"
              element={
                <ProtectedRoute allowedRoles={["ADMIN", "USER", "BENEFACTOR"]}>
                  <Home />
                </ProtectedRoute>
              }
            />
            <Route
              path="/addProduct"
              element={
                <ProtectedRoute allowedRoles={["ADMIN", "USER"]}>
                  <AddProductForm />
                </ProtectedRoute>
              }
            />
            <Route
              path="/products"
              element={
                <ProtectedRoute allowedRoles={["ADMIN", "USER", "BENEFACTOR"]}>
                  <Products />
                </ProtectedRoute>
              }
            />
            <Route
              path="/markets"
              element={
                <ProtectedRoute allowedRoles={["ADMIN", "USER"]}>
                  <Markets />
                </ProtectedRoute>
              }
            />
            <Route
              path="/orders"
              element={
                <ProtectedRoute allowedRoles={["ADMIN", "USER", "BENEFACTOR"]}>
                  <Orders />
                </ProtectedRoute>
              }
            />
            <Route
              path="/productsByMarket/:id"
              element={
                <ProtectedRoute allowedRoles={["ADMIN", "USER"]}>
                  <ProductsByMarket />
                </ProtectedRoute>
              }
            />
            <Route
              path="/soldProductByMarket/:id"
              element={
                <ProtectedRoute allowedRoles={["ADMIN", "USER"]}>
                  <SoldProductByMarket />
                </ProtectedRoute>
              }
            />
            <Route
              path="/applyOrganization"
              element={
                <ProtectedRoute allowedRoles={["ADMIN", "USER"]}>
                  <ApplyOrganization />
                </ProtectedRoute>
              }
            />
            <Route
              path="/appliesOrganization"
              element={
                <ProtectedRoute allowedRoles={["ADMIN"]}>
                  <AppliesOrganization />
                </ProtectedRoute>
              }
            />

            <Route
              path="/organization"
              element={
                <ProtectedRoute allowedRoles={["ADMIN"]}>
                  <AppliesOrganization />
                </ProtectedRoute>
              }
            />
          </Route>

          <Route path="/login" element={<Login />} />

          <Route path="/signup" element={<Signup />} />
        </Routes>
      </BrowserRouter>
    </>
  );
}

export default App;

// product ekleme donate - normal
// market ekleme, güncelleme, silme
// sonra delete modali ayarla gerekli yerlere
