import { Outlet } from "react-router";
import Header from "../ui/Header";

const AppLayout = () => {
  return (
    <div
      className="grid grid-rows-[auto_1fr] min-h-svh font-poppins"
      style={{ backgroundColor: "#f1f5f9" }}
    >
      <Header />
      <main className="flex-1">
        <Outlet />
      </main>
    </div>
  );
};

export default AppLayout;
