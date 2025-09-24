import toast from "react-hot-toast";
import { useAppDispatch, useAppSelector } from "../store";
import { createOrder } from "../store/Order/thunks";
import Button from "./Button";
import { ToastCard } from "../Toast-Notification/ToastCard";
import { IoBagRemove } from "react-icons/io5";
import { FaCheckCircle } from "react-icons/fa";

export const AcceptRejectedApplyModal = ({
  onCloseModal,
  dispFunc,
  loading,
  title,
  context,
  buttonText,
}: {
  onCloseModal?: () => void;
  dispFunc: () => Promise<any>;
  loading: boolean;
  title: string;
  context: string;
  buttonText: string;
}) => {
  const dispatch = useAppDispatch();

  const approveOrder = () => {
    console.log("rrrr");
    toast.loading("Please wait...");
    dispFunc()
      .then(() => {
        toast.dismiss();
        toast.custom((t) => {
          return (
            <ToastCard
              title="Action completed successfully"
              t={t}
              icon={<FaCheckCircle className="w-6 h-6 text-gray-600" />}
            />
          );
        });
      })
      .catch((error) => toast.error(error));

    if (!loading && onCloseModal) {
      onCloseModal();
    }
  };

  return (
    <div>
      <h3 className="text-xl font-bold mb-5 ">{title}</h3>
      <p className="mb-2 text-lg font-semibold text-gray-800">{context}</p>
      <div className="flex justify-end gap-5">
        <Button variation="submit" onClick={onCloseModal}>
          Cancel
        </Button>
        <Button variation="danger" disabled={loading} onClick={approveOrder}>
          {buttonText}
        </Button>
      </div>
    </div>
  );
};
