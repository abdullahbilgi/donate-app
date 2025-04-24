import { useNavigate } from "react-router";
import Button from "../ui/Button";
import { FaHandHoldingHeart } from "react-icons/fa6";
import { FaHandHoldingUsd } from "react-icons/fa";

const Home = () => {
  const navigate = useNavigate();
  return (
    <div className="h-full px-8 pb-6">
      <div className="bg-[url('/images/home-bg1.webp')] bg-cover bg-center h-full rounded-4xl flex items-center justify-center">
        <div className="absolute inset-0 bg-black/2"></div>
        <div className="absolute bottom-13 left-15 right-15 z-10 flex items-end justify-between">
          <div>
            <h1 className="text-5xl font-bold text-gray-200">
              <span className="text-10xl font-bold">Fazlan</span> var mi?
            </h1>
            <p className="relative left-3/7 text-2xl font-semibold text-gray-200">
              Küçük bir paylaşım, büyük bir fark yaratır.
            </p>
          </div>
          <div className="p-20 flex flex-col gap-5">
            {" "}
            <Button
              onClick={() => {
                navigate("/donateCellProduct");
              }}
              size="xxl"
              variation="donation"
            >
              <div className="flex items-center gap-1.5">
                <span>Donate Now</span> <FaHandHoldingHeart />
              </div>
            </Button>
            <Button
              onClick={() => {
                navigate("/donateCellProduct");
              }}
              size="xxl"
              variation="cell"
            >
              <div className="flex items-center gap-1.5">
                <span>Share at a Fair Price</span> <FaHandHoldingUsd />
              </div>
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Home;
