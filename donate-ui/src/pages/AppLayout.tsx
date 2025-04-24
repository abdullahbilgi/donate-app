import { Outlet } from "react-router";
import Header from "../ui/Header";

const AppLayout = () => {
  return (
    <div className="bg-gray-50 grid grid-rows-[auto_1fr] h-svh font-poppins">
      <Header />
      <main>
        <Outlet />
      </main>
    </div>
  );
};
export default AppLayout;
