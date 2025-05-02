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
import { useState } from "react";

interface Inputs {
  productName: string;
  stt: string;
  productNumber: number;
  normalPrice: number;
  cellPrice: number;
  image: string;
  note?: string;
}
const DonateCellProduct = () => {
  const {
    register,
    handleSubmit,
    formState: { errors },
    getValues,
  } = useForm<Inputs>();

  const onSubmit: SubmitHandler<Inputs> = (data) => console.log(data);

  const [notAccepted, setNotAccepted] = useState(true);
  return (
    <div className="flex flex-col">
      <div className="relative bg-[url('/images/donation.jpg')] bg-cover bg-center h-[70vh]">
        <div className="absolute inset-0 bg-black/40"></div>
        <div className="absolute p-20 text-center text-gray-200">
          <h1 className="text-4xl font-bold mb-10">
            Seninle Büyüyen Bir İyilik Hareketi
          </h1>
          <p className="text-xl font-medium  tracking-wider leading-relaxed mb-13">
            Tarihi yaklaşan ürünlerini veya tüketemeyecegin ürünlerini
            paylaşarak hem ihtiyacı olanlara destek ol, hem de israfı önle.
            Seninle birlikte bu zinciri büyütelim. Her yeni ürün, her paylaşım,
            her katkı bizi biraz daha büyütüyor.
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
          Donation Form <FaArrowDown />
        </div>
      </div>

      <div className="py-15 flex items-center justify-center">
        <div className="p-20 border border-gray-300 shadow-lg">
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
              errors={errors?.productName?.message}
            >
              <Input
                type="text"
                id="productName"
                inputVariation="donate"
                {...register("productName", {
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
                  <MdDateRange /> STT(Son Tüketim Tarihi) *
                </>
              }
            >
              <Input
                type="date"
                id="stt"
                inputVariation="donate"
                {...register("stt")}
              />
            </FormRow>
            <FormRow
              labelText={
                <>
                  <BsBoxes /> Ürün Adeti *
                </>
              }
              errors={errors?.productNumber?.message}
            >
              <Input
                type="number"
                id="productNumber"
                inputVariation="donate"
                {...register("productNumber", {
                  required: "This field is required!",
                  min: {
                    value: 1,
                    message: "Minimum number should be 1",
                  },
                  max: {
                    value: 1000,
                    message: "Maximum price can be 1000",
                  },
                })}
              />
            </FormRow>
            <FormRow
              labelText={
                <>
                  <IoPricetagsOutline /> Ürün Normal Fiyati *
                </>
              }
              errors={errors?.normalPrice?.message}
            >
              <Input
                type="text"
                id="normalPrice"
                inputVariation="donate"
                {...register("normalPrice", {
                  required: "This field is required!",
                  min: { value: 1, message: "Minimum price should be 1" },
                })}
              />
            </FormRow>
            <FormRow
              labelText={
                <>
                  <LiaDonateSolid /> Ürünü Satacaginiz Fiyat *
                </>
              }
              errors={errors?.cellPrice?.message}
            >
              <Input
                type="text"
                id="cellPrice"
                inputVariation="donate"
                {...register("cellPrice", {
                  required: "This field is required!",
                  min: { value: 1, message: "Minimum price should be 1" },
                  validate: (price) => {
                    return (
                      price < Number(getValues("normalPrice")) ||
                      "Discount should be less than regular price"
                    );
                  },
                })}
              />
            </FormRow>

            <FormRow
              labelText={
                <>
                  <FaRegImage /> Ürün Resmi *
                </>
              }
              errors={errors?.image?.message}
            >
              <Input
                type="file"
                id="image"
                inputVariation="donate"
                {...register("image", {
                  required: "This field is required!",
                })}
              />
            </FormRow>
            <FormRow
              labelText={
                <>
                  <BsChatLeftText /> Not(Istege Bagli)
                </>
              }
              className="col-span-2"
            >
              <Input type="textarea" id="note" inputVariation="donate" />
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

export default DonateCellProduct;
