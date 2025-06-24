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
}

const MarketCreateModalContent = ({
  onCloseModal,
}: CreateMarketModalContentProps) => {
  const {
    handleSubmit,
    formState: { errors },
    register,
    setValue,
  } = useForm<Inputs>();

  const dispatch = useAppDispatch();

  const { loading, error } = useAppSelector((state) => state.Market);

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

    dispatch(createMarket(sendObject));

    if (!loading && !error && onCloseModal) {
      console.log("girdi");
      onCloseModal();
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
            <MdDriveFileRenameOutline className="w-5 h-5" /> Market adi *
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
          {loading ? "Spinner" : "Market Ekle"}
        </Button>
      </div>
    </Form>
  );
};

export default MarketCreateModalContent;
