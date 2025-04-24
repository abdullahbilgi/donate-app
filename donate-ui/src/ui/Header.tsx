import { Link, useNavigate } from "react-router";
import Button from "./Button";

const Header = () => {
  const navigate = useNavigate();
  return (
    <header className="bg-gray-50 h-20 flex items-center justify-between px-10 relative z-10 text-zinc-700">
      <div className="flex items-center gap-4">
        <img
          src="../../public/images/helping-hand.png"
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
            <Link to="/bagisyap">About Us</Link>
          </li>
          <li className="hover:text-zinc-500 transition-colors duration-300">
            <Link to="/bagisyap">Donation</Link>
          </li>
          <li className="hover:text-zinc-500 transition-colors duration-300">
            <Link to="/bagisyap">Bagis Yap</Link>
          </li>
        </ul>
        <Button onClick={() => navigate("/login")}>Sign In</Button>
      </div>
    </header>
  );
};

export default Header;
