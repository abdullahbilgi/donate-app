import { FaArrowDown } from "react-icons/fa";

import FormRow from "../ui/FormRow";
import Input from "../ui/Input";
import Form from "../ui/Form";
import Button from "../ui/Button";
import { SubmitHandler, useForm } from "react-hook-form";

import { MdDriveFileRenameOutline } from "react-icons/md";
import { MdDateRange } from "react-icons/md";
import { BsBoxes } from "react-icons/bs";
import { IoPricetagsOutline } from "react-icons/io5";
import { LiaDonateSolid } from "react-icons/lia";
import { FaRegImage } from "react-icons/fa6";
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
              Seninle Büyüyen Bir İyilik Hareketi
            </h1>
            <p className="text-xl font-medium  tracking-wider leading-relaxed mb-13">
              Tarihi yaklaşan ürünlerini veya tüketemeyecegin ürünlerini
              paylaşarak hem ihtiyacı olanlara destek ol, hem de israfı önle.
              Seninle birlikte bu zinciri büyütelim. Her yeni ürün, her
              paylaşım, her katkı bizi biraz daha büyütüyor.
              <strong>Sen de bu topluluğun bir parçası ol.</strong> Bugün bir
              paylaşım senden, yarın binlerce iyilik hepimizden.
            </p>
            <p className="text-xl font-medium  tracking-wider leading-relaxed">
              LastBite'i daha yakından tanımak ister misiniz? Biz, ihtiyaç
              sahipleriyle fazla ürünleri buluşturarak israfı önlemeyi ve
              toplumsal dayanışmayı büyütmeyi hedefliyoruz. Misyonumuz,
              değerlerimiz ve diger tüm sürecler hakkında daha fazla bilgi almak
              için lütfen Hakkımızda sayfamıza göz atın!
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
              Donate yapmak istiyorum
            </label>
          </div>
          <Form
            onSubmit={handleSubmit(onSubmit, (errors) => console.log(errors))}
            formVariation="donate"
          >
            <FormRow
              labelText={
                <>
                  <MdDriveFileRenameOutline className="w-5 h-5" /> Ürün adi *
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
                  <BsBoxes /> Ürün Adeti *
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
                  <MdDateRange /> ÜT(Üretim Tarihi) *
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
                  <MdDateRange /> STT(Son Tüketim Tarihi) *
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
                      <IoPricetagsOutline /> Ürün Normal Fiyati *
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
                      <LiaDonateSolid /> Indirimli Fiyat *
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
                  <FaRegImage /> Ürün Kategorisi
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
                <option value="">Kategori Sec</option>
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
                  <FaRegImage /> Ürün Resmi
                </>
              }
            >
              <Input type="file" id="image" inputVariation="donate" />
            </FormRow>

            {!donate && (
              <FormRow
                labelText={
                  <>
                    <MdDateRange /> Son Bagis Tarihi*
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
                    <BsChatLeftText /> Market
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
                  <option value="">Market Sec</option>
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
                  <BsChatLeftText /> Ürün Bilgisi
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
                  Ürünün tüketime uygun olduğunu onaylıyorum. *
                </label>
              </div>
              <Button variation="submit" disabled={notAccepted}>
                Ürünü Paylaş
              </Button>
            </div>
          </Form>
        </div>
      </div>
    </div>
  );
};

export default AddProductForm;
