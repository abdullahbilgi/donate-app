import { useState } from "react";
import { useSelector } from "react-redux";
import { useDispatch } from "react-redux";
import { cartActions } from "../store/cart-slice";

const QuantityInput = ({ id }: { id: string }) => {
  const dispatch = useDispatch();

  const currItem = useSelector((item: any) =>
    item.cart.items.find((item: any) => item.id === id)
  );

  function handleIncrease() {
    dispatch(cartActions.addItemToCart(currItem));
  }

  function handleDecrease() {
    dispatch(cartActions.removeItemFromCart(currItem.id));
  }

  return (
    <div className="flex items-center border border-gray-300 rounded-sm overflow-hidden w-fit h-8">
      <button
        onClick={handleDecrease}
        className="w-10 h-8 flex items-center justify-center text-xl text-gray-500 hover:bg-gray-100"
      >
        –
      </button>
      <div className="w-10 h-8 flex items-center justify-center border-l border-r border-r-gray-200 border-l-gray-200 text-md">
        {currItem.quantity}
      </div>
      <button
        onClick={handleIncrease}
        className="w-10 h-8 flex items-center justify-center text-xl text-gray-500 hover:bg-gray-100"
      >
        +
      </button>
    </div>
  );
};

export default QuantityInput;
