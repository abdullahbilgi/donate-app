import { Link, useNavigate } from "react-router";
import Button from "./Button";
import { RiShoppingBasketLine } from "react-icons/ri";
import { useState } from "react";
import SideModalBasket from "./SideModalBasket";
import { useAppDispatch, useAppSelector } from "../store";
import { logout } from "../store/Auth/Login/thunks";
import { MdLogout } from "react-icons/md";
import { hasPermission } from "../utils/permissionUtils";
import { ROUTES } from "../Routes/HeaderLinks";
import toast from "react-hot-toast";
import { LoginSignupNotification } from "../Toast-Notification/LoginSignupNotification";
import { FaUserMinus } from "react-icons/fa6";

const Header = () => {
  const navigate = useNavigate();
  const [showBasket, setShowBasket] = useState(false);
  const { cartItems } = useAppSelector((state: any) => state.Cart);
  const isLogged = localStorage.getItem("isLogged");

  const { role } = useAppSelector((state) => state.Auth);
  const dispatch = useAppDispatch();
  const logoutHandler = () => {
    toast.loading("Loading");
    dispatch(logout())
      .then(() => {
        toast.dismiss();
        toast.custom((t) => (
          <LoginSignupNotification
            title="Logout"
            text="See you soon!"
            icon={<FaUserMinus className="w-5 h-5 text-red-800" />}
            t={t}
          />
        ));
        navigate("/login");
      })
      .catch((error) => toast.error(error));
  };

  return (
    <>
      <header
        className="h-20 flex items-center justify-between px-10 relative z-10 text-zinc-700"
        style={{ backgroundColor: "#f1f5f9" }}
      >
        <div className="flex items-center gap-4">
          <img
            src="../images/helping-hand.png"
            alt="logo"
            className="w-15 h-15"
          />
          <Link to="/">
            <h1 className="text-2xl font-semibold">LastBite</h1>
          </Link>
        </div>
        <div className="flex gap-12 items-center font-semibold ">
          <ul className="flex gap-8">
            {ROUTES.map(({ label, path, permission }) => {
              if (permission && !hasPermission(role, permission)) return null;
              return (
                <li className="hover:text-zinc-500 transition-colors duration-300">
                  <Link to={`${path}`}>{label}</Link>
                </li>
              );
            })}

            <li className="hover:text-zinc-500 transition-colors duration-300 relative">
              <button
                onClick={() => {
                  setShowBasket(true);
                }}
                className="cursor-pointer"
              >
                <RiShoppingBasketLine className="w-6 h-6" />
                <span className="absolute -top-1 -right-1 text-xs bg-red-400 font-semibold text-white rounded-full w-4 aspect-square flex items-center justify-center">
                  {cartItems.length}
                </span>
              </button>
            </li>
          </ul>

          {isLogged === "false" ? (
            <Button onClick={() => navigate("/login")}>Sign In</Button>
          ) : (
            <Button variation="danger" onClick={logoutHandler}>
              <MdLogout className="w-5 h-5" />
            </Button>
          )}
        </div>
      </header>

      <SideModalBasket
        isOpen={showBasket}
        onClose={() => setShowBasket(false)}
      />
    </>
  );
};

export default Header;
