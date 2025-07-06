import Button from "../ui/Button";
import MarketCreateModalContent from "../ui/MarketCreateModalContent";
import { useEffect } from "react";
import { useAppDispatch, useAppSelector } from "../store";
import { getMyApply } from "../store/Organization/GetMyApply/thunks";
import { rejectedApply } from "../store/Organization/AcceptRejectApplies/thunks";
import toast from "react-hot-toast";
import { SuccesNotafication } from "../Toast-Notification/SuccesNotification";
import { FaTrashAlt } from "react-icons/fa";
import Modal from "../ui/Modal";
import DeleteModalContent from "../ui/DeleteModalContent";

const ApplyOrganization = () => {
  const { Apply, loading, error } = useAppSelector((state) => state.MyApply);
  const dispatch = useAppDispatch();
  useEffect(() => {
    dispatch(getMyApply());
  }, []);

  console.log(Apply);
  return (
    <div className="flex h-full">
      <div className="bg-[url('/images/organization.jpg')] bg-cover bg-center h-full flex items-end justify-center w-2/5">
        <div className="mb-10">
          <p className="relative left-2/7 text-4xl font-semibold text-gray-200 mb-6">
            Lead the way to
          </p>
          <h1 className="text-9xl font-bold text-gray-200">goodness</h1>
        </div>
      </div>
      <div className="w-3/5 p-10">
        {Apply ? (
          <>
            <div className="bg-blue-100 p-5 mb-5">
              <p className="font-semibold text-blue-900">
                You already have a pending application. It will be reviewed
                shortly. Thank you for your understanding.
              </p>
            </div>
            <div className="p-6 bg-[#f5faff] rounded shadow-md max-w-3xl mx-auto my-6 space-y-4">
              <div className="flex items-center justify-between">
                {" "}
                <h2 className="text-lg font-semibold text-gray-800">
                  Application Details
                </h2>
                <div className="flex items-center gap-2">
                  <span className="relative flex h-3 w-3">
                    <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-orange-400 opacity-75"></span>
                    <span className="relative inline-flex rounded-full h-3 w-3 bg-orange-400"></span>
                  </span>
                  <p className="font-semibold text-yellow-600">
                    {Apply.status}
                  </p>
                </div>
              </div>

              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div className="bg-white p-4 rounded border">
                  <p className="text-sm text-gray-500">Organization Name</p>
                  <p className="font-medium text-gray-800">{Apply.name}</p>
                </div>
                <div className="bg-white p-4 rounded border">
                  <p className="text-sm text-gray-500">
                    Organization Tax Number
                  </p>
                  <p className="font-semibold ">{Apply.taxNumber}</p>
                </div>
                <div className="bg-white p-4 rounded border">
                  <p className="text-sm text-gray-500">City</p>
                  <p className="font-semibold ">
                    {Apply.address.region.city.name}
                  </p>
                </div>
                <div className="bg-white p-4 rounded border">
                  <p className="text-sm text-gray-500">Region</p>
                  <p className="font-semibold ">{Apply.address.region.name}</p>
                </div>
              </div>

              <div className="bg-white p-4 rounded border">
                <p className="text-sm text-gray-500 mb-1">Full Address</p>
                <p className="text-gray-700">{Apply.address.name}</p>
              </div>
            </div>
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 max-w-3xl mx-auto">
              <Button variation="cell" size="large" className="rounded-sm">
                Edit Application
              </Button>
              <Modal>
                <Modal.Open modalName="deleteApply">
                  <Button
                    variation="danger"
                    size="large"
                    className="rounded-sm"
                  >
                    Delete Application
                  </Button>
                </Modal.Open>
                <Modal.Window name="deleteApply">
                  <DeleteModalContent
                    name={Apply.name}
                    type="apply"
                    loading={loading}
                    error={error}
                    deleteDispatch={() => dispatch(rejectedApply(Apply?.id))}
                  />
                </Modal.Window>
              </Modal>
            </div>
          </>
        ) : (
          <>
            <div className="bg-blue-100 p-5 mb-5">
              <p className="font-semibold text-blue-900">
                By applying to become an organization, your current user role
                will be updated. After the application is approved, you will no
                longer be able to add markets or products. Instead, you will
                gain access to free products and can place orders as an
                organization.
              </p>
            </div>
            <MarketCreateModalContent type="organization" />
          </>
        )}
      </div>
    </div>
  );
};

export default ApplyOrganization;
