import toast from "react-hot-toast";
import { useAppDispatch } from "../store";
import { deleteMarket } from "../store/MarketStore/Market/thunks";
import Button from "./Button";
import { ToastCard } from "../Toast-Notification/ToastCard";
import { FaTrashAlt } from "react-icons/fa";

interface InfoProps {
  name: string;
  type: string;
  onCloseModal?: () => void;
  deleteDispatch?: () => Promise<any>;
  loading: boolean;
  error: null | string;
}

const DeleteModalContent: React.FC<InfoProps> = ({
  name,
  type,
  onCloseModal,
  deleteDispatch,
  loading,
  error,
}) => {
  const onDeleteMarket = () => {
    if (deleteDispatch) {
      deleteDispatch()
        .then(() => {
          toast.dismiss();
          toast.custom((t) => {
            return (
              <ToastCard
                title="Successfully deleted"
                t={t}
                icon={<FaTrashAlt className="w-6 h-6 text-red-600" />}
              />
            );
          });
        })
        .catch((error) => toast.error(error));
    }

    if (!loading && onCloseModal && error) {
      onCloseModal();
    }
  };

  return (
    <>
      <h3 className="text-xl font-bold mb-5 ">
        Delete {name} {type}
      </h3>
      <p className="mb-2 text-lg font-semibold text-gray-800">
        Are you sure you want to delete this {type} permanently?
      </p>
      <div className="flex justify-end gap-5">
        <Button variation="submit" onClick={onCloseModal}>
          Cancel
        </Button>
        <Button variation="danger" disabled={loading} onClick={onDeleteMarket}>
          Delete
        </Button>
      </div>
    </>
  );
};

export default DeleteModalContent;
