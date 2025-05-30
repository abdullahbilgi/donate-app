import { IoClose } from "react-icons/io5";
import QuantityInput from "./QuantityInput";

import { deleteProduct } from "../store/ProductStore/Products/thunks";
import { useAppDispatch } from "../store";
import { removeItemFromCart } from "../store/CartStore/Cart/thunks";

interface BasketItemProps {
  itemId: any;
  image: string;
  quantity: string;
  name: string;
  normalPrice: string;
  discountedPrice: string;
  productStatus: string;
}
const BasketItem: React.FC<BasketItemProps> = ({
  itemId,
  image,
  quantity,
  name,
  normalPrice,
  discountedPrice,
  productStatus,
}) => {
  const dispatch = useAppDispatch();

  const cartId = Number(localStorage.getItem("cartId"));
  function handleDelete() {
    dispatch(removeItemFromCart({ productId: itemId, cartId }));
  }

  return (
    <div className="flex justify-between min-h-20 p-3 mb-1.5 border-b border-b-gray-100">
      <div className="flex items-start gap-3">
        <div className="w-16 h-16 flex items-start">
          <img src="/public/images/donation.jpg" className="object-contain" />
        </div>
        <div className="flex flex-col gap-2">
          <div className="">
            <h2 className="font-semibold text-sm">{name}</h2>
            <p className="font-medium text-md">
              ${discountedPrice}{" "}
              <span className="text-sm line-through">{normalPrice}</span>
            </p>
          </div>
          <div className="flex items-center justify-between">
            <QuantityInput id={itemId} />
            <span className="font-medium text-md">
              {productStatus === "DONATE"
                ? "Free"
                : `$${Number(quantity) * Number(discountedPrice)}`}{" "}
            </span>
          </div>
        </div>
      </div>
      <IoClose
        className="w-5 h-5 cursor-pointer hover:text-gray-500 duration-200"
        onClick={() => handleDelete()}
      />
    </div>
  );
};

export default BasketItem;
