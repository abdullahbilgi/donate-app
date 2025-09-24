import { Outlet } from "react-router";
import Header from "../ui/Header";
import { useEffect } from "react";
import { useAppDispatch } from "../store";
import { getCartById } from "../store/CartStore/GetCartById/thunks";

const AppLayout = () => {
  const dispatch = useAppDispatch();
  const userId = localStorage.getItem("userId");

  useEffect(() => {
    dispatch(getCartById(userId));
  });

  return (
    <div
      className="grid grid-rows-[auto_1fr] min-h-svh font-poppins "
      style={{ backgroundColor: "#f1f5f9" }}
    >
      <Header />

      <main className="flex-1 h-[calc(100vh_-_theme('spacing.header'))] overflow-y-auto [scrollbar-gutter:stable]">
        <Outlet />
      </main>
    </div>
  );
};

export default AppLayout;
