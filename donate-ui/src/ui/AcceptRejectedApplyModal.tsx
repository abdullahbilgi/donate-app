import { useAppDispatch, useAppSelector } from "../store";
import { createOrder } from "../store/Order/thunks";
import Button from "./Button";

export const AcceptRejectedApplyModal = ({
  onCloseModal,
  dispFunc,
  loading,
  title,
  context,
  buttonText,
}: {
  onCloseModal?: () => void;
  dispFunc: () => void;
  loading: boolean;
  title: string;
  context: string;
  buttonText: string;
}) => {
  const dispatch = useAppDispatch();

  const approveOrder = () => {
    console.log("rrrr");
    dispFunc();

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
