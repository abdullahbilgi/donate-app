import { MdDriveFileRenameOutline } from "react-icons/md";
import Button from "./Button";
import Form from "./Form";
import FormRow from "./FormRow";
import Input from "./Input";
import { SubmitHandler, useForm } from "react-hook-form";
import { useEffect, useState } from "react";
import { useAppDispatch, useAppSelector } from "../store";
import { createMarket } from "../store/MarketStore/Market/thunks";
import MarketMap from "./MarketMap";
import { applyOrganization } from "../store/Organization/ApplyOrganization/thunks";
import toast from "react-hot-toast";
import { ToastCard } from "../Toast-Notification/ToastCard";
import { BsBuildingFillCheck, BsShop } from "react-icons/bs";
import { getMyApply } from "../store/Organization/GetMyApply/thunks";

export interface Address {
  country: string;
  province?: string;
  postcode: string;
  region?: string;
  state: string;
  town?: string;
  village?: string;
}

export interface LocationData {
  address: Address;
  display_name: string;
  lat: string;
  lon: string;
}
interface Inputs {
  name: string;
  taxNumber: string;
  cityName: string;
  zipCode: string;
  displayName: string;
}

interface CreateMarketModalContentProps {
  onCloseModal?: () => void;
  type?: string;
}

const MarketCreateModalContent = ({
  onCloseModal,
  type,
}: CreateMarketModalContentProps) => {
  const {
    handleSubmit,
    formState: { errors },
    register,
    setValue,
    reset,
  } = useForm<Inputs>();

  const dispatch = useAppDispatch();

  const { loading, error } = useAppSelector((state) => state.Market);
  const { applyLoading, applyError } = useAppSelector(
    (state) => state.ApplyOrganizations
  );

  const [address, setAddress] = useState<LocationData | null>(null);

  const userId = localStorage.getItem("userId");

  const { cities } = useAppSelector((state) => state.City);

  useEffect(() => {
    if (address) {
      setValue("zipCode", address.address.postcode);
      setValue("displayName", address.display_name);
      setValue("cityName", address.address.province || address.address.state);
    }
  }, [address, setValue]);

  const onSubmit: SubmitHandler<Inputs> = (data) => {
    //console.log("WWWWWWWWWWWWWWWWWWWWWWWWW", data);
    const sendObject = {
      ...data,
      latitude: parseFloat(address?.lat ?? "0"),
      longitude: parseFloat(address?.lon ?? "0"),
      regionName:
        (
          address?.address?.town || address?.address?.village
        )?.toLocaleUpperCase("tr") ?? "",
      userId: Number(userId),
      cityName: data.cityName.toLocaleUpperCase("tr"),
    };

    console.log(sendObject);

    if (type && type === "organization") {
      toast.loading("Submitting application...");
      dispatch(applyOrganization({ ...sendObject, userId }))
        .then(() => {
          toast.dismiss();
          toast.custom((t) => {
            return (
              <ToastCard
                title="Application created"
                description="It is no awaiting admin approval"
                t={t}
                icon={
                  <BsBuildingFillCheck className="w-6 h-6 text-green-600" />
                }
              />
            );
          });
        })
        .catch((error) => toast.error(error));

      dispatch(getMyApply());
    } else {
      toast.loading("Creating market...");
      dispatch(createMarket(sendObject)).then(() => {
        toast.dismiss();
        toast.custom((t) => {
          return (
            <ToastCard
              title="Market created"
              description="You can check it on the Markets page."
              t={t}
              icon={<BsShop className="w-6 h-6 text-green-600" />}
            />
          );
        });
      });
    }

    if (!loading && !error && onCloseModal && type !== "organization") {
      reset();
      onCloseModal();
    }
    if (type === "organization" && !applyLoading && !applyError) {
      reset();
    }
  };

  return (
    <Form
      onSubmit={handleSubmit(onSubmit, (errors) => console.log(errors))}
      formVariation="donate"
    >
      <FormRow
        labelText={
          <>
            <MdDriveFileRenameOutline className="w-5 h-5" />
            {type === "organization" ? "Organization Name" : "Market Name"}*
          </>
        }
        errors={errors?.name?.message}
      >
        <Input
          type="text"
          id="name"
          inputVariation="donate"
          {...register("name", {
            required: "This field is required!",
            maxLength: {
              value: 20,
              message: "The value should be max 20 character",
            },
            minLength: {
              value: 2,
              message: "The value should be min 2 character",
            },
          })}
        />
      </FormRow>

      <FormRow
        labelText={
          <>
            <MdDriveFileRenameOutline className="w-5 h-5" /> Tax Number *
          </>
        }
        errors={errors?.taxNumber?.message}
      >
        <Input
          type="text"
          id="taxNumber"
          inputVariation="donate"
          {...register("taxNumber", {
            required: "This field is required!",
            maxLength: {
              value: 20,
              message: "The value should be max 20 character",
            },
            minLength: {
              value: 2,
              message: "The value should be min 2 character",
            },
          })}
        />
      </FormRow>

      <FormRow
        labelText={
          <>
            <MdDriveFileRenameOutline className="w-5 h-5" /> Address
          </>
        }
        className="col-span-2 mb-10"
      >
        <MarketMap setAddress={setAddress} />
      </FormRow>

      <FormRow
        labelText={
          <>
            <MdDriveFileRenameOutline className="w-5 h-5" /> City *
          </>
        }
        errors={errors?.cityName?.message}
      >
        <select
          className="bg-gray-100 p-3 rounded-lg w-90 border border-gray-300"
          {...register("cityName", {
            required: "This field is required!",
          })}
        >
          <option
            value={
              address ? address.address.province || address.address.state : ""
            }
          >
            {address
              ? address.address.province || address.address.state
              : "Sehir Sec"}
          </option>
          {cities.map((city) => (
            <option key={city.id} value={city.name}>
              {city.name}
            </option>
          ))}
        </select>
      </FormRow>

      <FormRow
        labelText={
          <>
            <MdDriveFileRenameOutline className="w-5 h-5" /> Zip Code *
          </>
        }
        errors={errors?.zipCode?.message}
      >
        <Input
          type="text"
          id="zipCode"
          inputVariation="donate"
          defaultValue=""
          {...register("zipCode", {
            required: "This field is required!",
            maxLength: {
              value: 20,
              message: "The value should be max 20 character",
            },
            minLength: {
              value: 2,
              message: "The value should be min 2 character",
            },
          })}
        />
      </FormRow>

      <FormRow
        labelText={
          <>
            <MdDriveFileRenameOutline className="w-5 h-5" /> Tam Address
          </>
        }
        className="col-span-2 "
        errors={errors?.displayName?.message}
      >
        <Input
          type="text"
          id="displayName"
          inputVariation="donate"
          defaultValue=""
          {...register("displayName", {
            required: "This field is required!",
            maxLength: {
              value: 90,
              message: "The value should be max 90 character",
            },
            minLength: {
              value: 2,
              message: "The value should be min 2 character",
            },
          })}
        />
      </FormRow>
      <div className="flex justify-end col-span-2">
        <Button disabled={loading}>
          {loading
            ? "Spinner"
            : type === "organization"
            ? "Apply"
            : "Market Ekle"}
        </Button>
      </div>
    </Form>
  );
};

export default MarketCreateModalContent;
