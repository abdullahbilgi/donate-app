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

function App() {
  const [ready, setReady] = useState(false);

  useEffect(() => {
    const token = localStorage.getItem("accessToken");
    if (token) {
      setToken(token);
      setupInterceptors();
    }
    setReady(true); // Token kontrolü yapıldıktan sonra render’a geç
  }, []);

  if (!ready) return <div>Yükleniyor...</div>; // İlk yükleme sırasında beklet
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<AppLayout />}>
          <Route index element={<Navigate replace to="/home" />} />
          <Route path="/home" element={<Home />} />
          <Route path="/addProduct" element={<AddProductForm />} />
          <Route path="/products" element={<Products />} />
          <Route path="/markets" element={<Markets />} />
          <Route path="/productsByMarket/:id" element={<ProductsByMarket />} />
        </Route>

        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;

// product ekleme donate - normal
// market ekleme, güncelleme, silme
// sonra delete modali ayarla gerekli yerlere
