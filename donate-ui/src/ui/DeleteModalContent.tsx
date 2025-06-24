import { useAppDispatch } from "../store";
import { deleteMarket } from "../store/MarketStore/Market/thunks";
import Button from "./Button";

interface InfoProps {
  name: string;
  type: string;
  onCloseModal?: () => void;
  deleteDispatch?: () => void;
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
      deleteDispatch();
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
