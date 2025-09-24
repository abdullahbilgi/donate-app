import { useEffect, useRef } from "react";
import { IoIosCloseCircleOutline } from "react-icons/io";
import BasketItem from "./BasketItem";
import BasketPriceInfoRow from "./BasketPriceInfoRow";
import Button from "./Button";
import { useAppSelector } from "../store";
import Modal from "./Modal";
import { CreateOrderModalContent } from "./CreateOrderModalContent";

import { IoBagCheck } from "react-icons/io5";

interface SideModalBasketProps {
  isOpen: boolean;
  onClose: () => void;
}

const SideModalBasket: React.FC<SideModalBasketProps> = ({
  isOpen,
  onClose,
}) => {
  const ref = useRef<HTMLDivElement>(null);

  const { cartItems, subTotal, totalDiscPrice, totalPrice } = useAppSelector(
    (state: any) => state.Cart
  );

  useEffect(() => {
    function handleClickOutside(e: any) {
      if (ref.current && !ref.current.contains(e.target)) {
        onClose();
      }
    }
    // toast.custom(

    // );
    document.addEventListener("mousedown", handleClickOutside);

    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  });
  return (
    isOpen && (
      <>
        <div className="fixed inset-0 bg-black opacity-35 z-40"></div>

        <div
          className="bg-white fixed top-0 right-0 w-100 h-svh z-50 py-6 px-2"
          ref={ref}
        >
          <div className="flex justify-between items-center pb-8 border-b border-b-gray-300">
            <h2 className="inline-flex items-center gap-2">
              <span className="font-semibold h-6">Sepet Icerigi</span>
              <span className="text-sm bg-blue-400 font-semibold text-white rounded-full w-6 h-6 aspect-square flex items-center justify-center">
                {cartItems.length}
              </span>
            </h2>
            <button onClick={onClose} className="cursor-pointer">
              <IoIosCloseCircleOutline className="w-7 h-7 hover:text-gray-500 duration-200" />
            </button>
          </div>

          <div className="border-b border-b-gray-300 h-[400px] overflow-y-scroll">
            {cartItems.map((item: any) => {
              return (
                <BasketItem
                  key={item.product.id}
                  itemId={item.product.id}
                  image={item.product.imageUrl}
                  name={item.product.name}
                  quantity={item.productQuantity}
                  normalPrice={item.product.price}
                  discountedPrice={item.product.discountedPrice}
                  productStatus={item.product.productStatus}
                />
              );
            })}
          </div>

          <div className="flex flex-col justify-between min-h-55 pt-6">
            <div>
              <BasketPriceInfoRow
                rowTitle="Sub Total"
                rowValue={subTotal.toFixed(2)}
              />
              <BasketPriceInfoRow
                rowTitle="Discount"
                rowValue={totalDiscPrice.toFixed(2)}
              />
              <BasketPriceInfoRow
                rowTitle="Total Price"
                rowValue={totalPrice.toFixed(2)}
              />
            </div>
            <div className="flex items-center justify-between px-4">
              <Button
                variation="basketButton"
                className="rounded-xs text-white bg-gray-500 hover:bg-gray-600"
                onClick={() => onClose()}
              >
                Continue Shopping
              </Button>
              <Modal>
                <Modal.Open modalName="createOrder">
                  <Button
                    variation="basketButton"
                    className="rounded-xs bg-red-400 text-white hover:bg-red-500"
                  >
                    Checkout
                  </Button>
                </Modal.Open>
                <Modal.Window name="createOrder">
                  <CreateOrderModalContent onClose={onClose} />
                </Modal.Window>
              </Modal>
            </div>
          </div>
        </div>
      </>
    )
  );
};

export default SideModalBasket;
