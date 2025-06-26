import { useAppDispatch, useAppSelector } from "../store";
import { createOrder } from "../store/Order/thunks";
import Button from "./Button";

export const CreateOrderModalContent = ({
  onClose,
  onCloseModal,
}: {
  onClose?: () => void;
  onCloseModal?: () => void;
}) => {
  const dispatch = useAppDispatch();
  const userId = localStorage.getItem("userId");
  const { loading, error } = useAppSelector((state) => state.Order);

  const approveOrder = () => {
    dispatch(createOrder(userId));

    if (!loading && onCloseModal && onClose) {
      onCloseModal();
      onClose();
    }
  };

  return (
    <div>
      <h3 className="text-xl font-bold mb-5 ">Create Order</h3>
      <p className="mb-2 text-lg font-semibold text-gray-800">
        Are you sure you want to place the order?
      </p>
      <div className="flex justify-end gap-5">
        <Button variation="submit" onClick={onCloseModal}>
          Cancel
        </Button>
        <Button variation="danger" disabled={loading} onClick={approveOrder}>
          Place Order
        </Button>
      </div>
    </div>
  );
};
