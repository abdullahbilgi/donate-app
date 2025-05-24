import { Link, useNavigate } from "react-router";
import Button from "./Button";
import { RiShoppingBasketLine } from "react-icons/ri";
import { useState } from "react";
import SideModalBasket from "./SideModalBasket";
import { useSelector } from "react-redux";

const Header = () => {
  const navigate = useNavigate();
  const [showBasket, setShowBasket] = useState(false);
  const { cartItems } = useSelector((state: any) => state.Cart);

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
            <h1 className="text-2xl font-semibold ">LastBite</h1>
          </Link>
        </div>
        <div className="flex gap-12 items-center font-semibold ">
          <ul className="flex gap-8">
            <li className="hover:text-zinc-500 transition-colors duration-300">
              <Link to="/bagisyap">Hakkimizda</Link>
            </li>
            <li className="hover:text-zinc-500 transition-colors duration-300">
              <Link to="/bagisyap">Donation</Link>
            </li>
            <li className="hover:text-zinc-500 transition-colors duration-300">
              <Link to="/products">Ürünler</Link>
            </li>
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
          <Button onClick={() => navigate("/login")}>Sign In</Button>
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
