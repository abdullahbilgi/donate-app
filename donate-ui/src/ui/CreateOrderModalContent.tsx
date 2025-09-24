import { useAppDispatch, useAppSelector } from "../store";
import { createOrder } from "../store/Order/thunks";
import Button from "./Button";
import { ToastCard } from "../Toast-Notification/ToastCard";
import toast from "react-hot-toast";
import { IoBagCheck } from "react-icons/io5";

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
    if (!loading && onCloseModal && onClose) {
      onClose();
      dispatch(createOrder(userId))
        .then(() => {
          toast.dismiss();
          toast.custom((t) => (
            <ToastCard
              title="Order Confirmed"
              description="Your order was placed successfully. Thank You"
              icon={<IoBagCheck className="w-5 h-5 text-green-800" />}
              t={t}
            />
          ));
        })
        .catch((error) => toast.error(error));

      onCloseModal();
    }
    toast.loading("Loading");
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
