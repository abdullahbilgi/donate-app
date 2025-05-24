import { useAppDispatch, useAppSelector } from "../store";
import {
  addProductToCart,
  removeItemFromCart,
} from "../store/CartStore/Cart/thunks";

const QuantityInput = ({ id }: { id: number }) => {
  const dispatch = useAppDispatch();

  const currItem = useAppSelector((state: any) =>
    state.Cart.cartItems.find((item: any) => item.product.id === id)
  );

  console.log(currItem);

  function handleIncrease() {
    dispatch(
      addProductToCart({
        userId: 1,
        productId: id,
        productQuantity: 1,
      })
    );
  }

  function handleDecrease() {
    dispatch(removeItemFromCart(currItem.id));
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
        {currItem.productQuantity}
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
