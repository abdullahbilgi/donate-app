import { useNavigate } from "react-router";
import Button from "../ui/Button";
import { FaHandHoldingHeart } from "react-icons/fa6";
import { FaCartShopping } from "react-icons/fa6";
import { GrOrganization } from "react-icons/gr";
import MarketCreateModalContent from "../ui/MarketCreateModalContent";

const ApplyOrganization = () => {
  const navigate = useNavigate();

  return (
    <div className="flex h-full">
      <div className="bg-[url('/images/organization.jpg')] bg-cover bg-center h-full flex items-end justify-center w-2/5">
        <div>
          <h1 className="text-5xl font-bold text-gray-200">
            <span className="text-10xl font-bold">İyiliğe</span>
          </h1>
          <p className="relative left-2/7 text-4xl font-semibold text-gray-200 mb-12">
            yön ver
          </p>
        </div>
      </div>
      <div className="w-3/5 p-10">
        <MarketCreateModalContent type="organization" />
      </div>
    </div>
  );
};

export default ApplyOrganization;
