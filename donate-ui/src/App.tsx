import { BrowserRouter, Navigate, Route, Routes } from "react-router";
import Home from "./pages/Home";
import Login from "./pages/Login";
import AppLayout from "./pages/AppLayout";
import Signup from "./pages/Signup";
import DonateCellProduct from "./pages/donateSellProduct";
import Products from "./pages/Products";
import { useEffect, useState } from "react";
import { setToken, setupInterceptors } from "./api/interceptors";

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
          <Route path="/donateCellProduct" element={<DonateCellProduct />} />
          <Route path="/products" element={<Products />} />
        </Route>

        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
