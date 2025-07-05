import { FaArrowDown } from "react-icons/fa";

import FormRow from "../ui/FormRow";
import Input from "../ui/Input";
import Form from "../ui/Form";
import Button from "../ui/Button";
import { SubmitHandler, useForm } from "react-hook-form";

import { MdDriveFileRenameOutline } from "react-icons/md";
import { TbCategoryFilled } from "react-icons/tb";
import { MdDateRange } from "react-icons/md";
import { BsBoxes } from "react-icons/bs";
import { IoPricetagsOutline } from "react-icons/io5";
import { LiaDonateSolid } from "react-icons/lia";
import { FaRegImage, FaShop } from "react-icons/fa6";
import { BsChatLeftText } from "react-icons/bs";
import { useEffect, useState } from "react";
import { useAppDispatch, useAppSelector } from "../store";
import { getCategory } from "../store/Category/thunks";
import { createProduct } from "../store/ProductStore/Products/thunks";
import { getMarketByUser } from "../store/MarketStore/Market/thunks";

interface Inputs {
  name: string;
  productionDate: string;
  lastDonatedDate: string;
  expiryDate: string; //stt
  price: number;
  discountedPrice: number;
  productStatus: string;
  quantity: number;
  description: string;
  categoryId: number;
  marketId: number;
}

interface AddProductFormProps {
  type?: string; // opsiyonel olsun istiyorsan
  marketId?: number;
  onCloseModal?: () => void;
}

const AddProductForm = ({
  type = "",
  marketId,
  onCloseModal,
}: AddProductFormProps) => {
  console.log(type);
  const {
    register,
    handleSubmit,
    formState: { errors },
    getValues,
  } = useForm<Inputs>();

  const userId = localStorage.getItem("userId");
  const dispatch = useAppDispatch();

  const [donate, setDonate] = useState(false);

  const { categories } = useAppSelector((state) => state.Category);
  const { marketsArr } = useAppSelector((state) => state.Market);
  const { loading } = useAppSelector((state) => state.Product);

  useEffect(() => {
    dispatch(getCategory());
  }, []);
  useEffect(() => {
    if (type !== "modal") {
      dispatch(getMarketByUser(userId));
    }
  }, []);

  const onSubmit: SubmitHandler<Inputs> = (data) => {
    const productData = {
      ...data,
      productionDate: `${!donate ? data.productionDate + "T10:00:00" : ""}`,
      expiryDate: `${!donate ? data.expiryDate + "T10:00:00" : ""}`,
      lastDonatedDate: `${!donate ? data.lastDonatedDate + "T10:00:00" : ""}`,
      price: !donate ? Number(data.price.toFixed(1)) : Number(0.0),
      productStatus: `${!donate ? "REAL" : "DONATE"}`,
      marketId: marketId ? marketId : data.marketId,
    };

    //productStatus, lastDonatedDate
    if (!donate) {
      console.log(productData);
      dispatch(createProduct(productData));
    } else {
      console.log(productData);
      dispatch(createProduct(productData));
    }

    if (!loading && onCloseModal) {
      dispatch(getMarketByUser(userId));
      onCloseModal();
    }
  };

  const [notAccepted, setNotAccepted] = useState(true);

  return (
    <div className="flex flex-col">
      {type !== "modal" && (
        <div className="relative bg-[url('/images/donation.jpg')] bg-cover bg-center h-[70vh]">
          <div className="absolute inset-0 bg-black/40"></div>
          <div className="absolute p-20 text-center text-gray-200">
            <h1 className="text-4xl font-bold mb-10">
              A Growing Movement of Kindness with You
            </h1>
            <p className="text-xl font-medium  tracking-wider leading-relaxed mb-13">
              Support those in need and help prevent waste by sharing products
              that are nearing their expiration date or that you can’t consume.
              Let’s grow this chain together. Every new item, every act of
              sharing, every contribution helps us grow a little more. Be a part
              of this community. A single contribution from you today can lead
              to thousands of acts of kindness tomorrow.
            </p>
            <p className="text-xl font-medium  tracking-wider leading-relaxed">
              Would you like to learn more about LastBite? We aim to prevent
              waste and strengthen social solidarity by connecting surplus
              products with people in need. To learn more about our mission,
              values, and everything we do, please visit our About Us page!
            </p>
          </div>

          <div className="flex items-center gap-2 absolute -bottom-8 left-1/2 -translate-x-1/2 bg-lime-300 text-teal-800 font-semibold text-xl py-3 px-9 rounded-md ">
            Product Form <FaArrowDown />
          </div>
        </div>
      )}

      <div
        className={`${
          type !== "modal" ? "py-15" : ""
        }  flex items-center justify-center`}
      >
        <div
          className={`${
            type !== "modal" ? "p-20" : "p-10"
          } border border-gray-300 shadow-lg`}
        >
          <div className="flex items-center gap-2 mb-10">
            <input
              type="checkbox"
              id="donate"
              required
              onClick={() => {
                setDonate(!donate);
              }}
              className="w-4 h-4 focus:ring-teal-800 focus:ring-2"
            />
            <label htmlFor="donate" className="font-semibold text-lg">
              I’d like to help by donating
            </label>
          </div>
          <Form
            onSubmit={handleSubmit(onSubmit, (errors) => console.log(errors))}
            formVariation="donate"
          >
            <FormRow
              labelText={
                <>
                  <MdDriveFileRenameOutline className="w-5 h-5" /> Product Name
                  *
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
                  <BsBoxes /> Product Quantity *
                </>
              }
              errors={errors?.quantity?.message}
            >
              <Input
                type="number"
                id="quantity"
                inputVariation="donate"
                {...register("quantity", {
                  required: "This field is required!",
                  min: {
                    value: 1,
                    message: "Minimum number should be 1",
                  },
                  max: {
                    value: 1000,
                    message: "Maximum price can be 1000",
                  },
                  setValueAs: (v) => (v === "" ? undefined : Number(v)),
                })}
              />
            </FormRow>

            <FormRow
              labelText={
                <>
                  <MdDateRange /> Production Date *
                </>
              }
            >
              <Input
                type="date"
                id="productionDate"
                inputVariation="donate"
                {...register("productionDate", {
                  required: "This field is required!",
                })}
              />
            </FormRow>
            <FormRow
              labelText={
                <>
                  <MdDateRange /> Expiration Date *
                </>
              }
            >
              <Input
                type="date"
                id="expiryDate"
                inputVariation="donate"
                {...register("expiryDate", {
                  required: "This field is required!",
                })}
              />
            </FormRow>

            {!donate && (
              <>
                <FormRow
                  labelText={
                    <>
                      <IoPricetagsOutline /> Regular Price *
                    </>
                  }
                  errors={errors?.price?.message}
                >
                  <Input
                    type="text"
                    id="price"
                    inputVariation="donate"
                    {...register("price", {
                      required: "This field is required!",
                      min: { value: 1, message: "Minimum price should be 1" },
                      setValueAs: (v) => (v === "" ? undefined : Number(v)),
                    })}
                  />
                </FormRow>
                <FormRow
                  labelText={
                    <>
                      <LiaDonateSolid /> Discounted Price *
                    </>
                  }
                  errors={errors?.discountedPrice?.message}
                >
                  <Input
                    type="text"
                    id="discountedPrice"
                    inputVariation="donate"
                    {...register("discountedPrice", {
                      required: "This field is required!",
                      min: { value: 1, message: "Minimum price should be 1" },
                      setValueAs: (v) => (v === "" ? undefined : Number(v)),

                      validate: (price) => {
                        return (
                          price < Number(getValues("price")) ||
                          "Discount should be less than regular price"
                        );
                      },
                    })}
                  />
                </FormRow>
              </>
            )}

            <FormRow
              labelText={
                <>
                  <TbCategoryFilled /> Category *
                </>
              }
            >
              <select
                className="bg-gray-100 p-3 rounded-lg w-90 border border-gray-300"
                {...register("categoryId", {
                  required: "This field is required!",
                  setValueAs: (v) => (v === "" ? undefined : Number(v)),
                })}
              >
                <option value="">Choose a Category</option>
                {categories.map((category) => (
                  <option key={category.id} value={category.id}>
                    {category.name}
                  </option>
                ))}
              </select>
            </FormRow>

            <FormRow
              labelText={
                <>
                  <FaRegImage /> Product Image
                </>
              }
            >
              <Input type="file" id="image" inputVariation="donate" />
            </FormRow>

            {!donate && (
              <FormRow
                labelText={
                  <>
                    <MdDateRange /> Last Donation Date *
                  </>
                }
              >
                <Input
                  type="date"
                  id="lastDonatedDate"
                  inputVariation="donate"
                  {...register("lastDonatedDate", {
                    required: "This field is required!",
                  })}
                />
              </FormRow>
            )}

            {type !== "modal" && (
              <FormRow
                labelText={
                  <>
                    <FaShop /> Market
                  </>
                }
              >
                <select
                  className="bg-gray-100 p-3 rounded-lg w-90 border border-gray-300"
                  {...register("marketId", {
                    required: "This field is required!",
                    setValueAs: (v) => (v === "" ? "" : Number(v)),
                  })}
                >
                  <option value="">Choose a Market</option>
                  {marketsArr.map((market) => (
                    <option key={market.id} value={market.id}>
                      {market.name}
                    </option>
                  ))}
                </select>
              </FormRow>
            )}

            <FormRow
              labelText={
                <>
                  <BsChatLeftText /> Product Details *
                </>
              }
              className="col-span-2"
            >
              <Input
                type="textarea"
                id="description"
                inputVariation="donate"
                {...register("description", {
                  required: "This field is required!",
                })}
              />
            </FormRow>

            <div className="col-span-2 flex justify-between items-center mt-5">
              <div className="flex items-center gap-2">
                <input
                  type="checkbox"
                  id="accept"
                  required
                  onClick={() => {
                    setNotAccepted(!notAccepted);
                  }}
                  className="w-4 h-4 focus:ring-teal-800 focus:ring-2"
                />
                <label htmlFor="accept">
                  I confirm that the product is suitable for consumption *
                </label>
              </div>
              <Button variation="submit" disabled={notAccepted}>
                Post Product
              </Button>
            </div>
          </Form>
        </div>
      </div>
    </div>
  );
};

export default AddProductForm;
