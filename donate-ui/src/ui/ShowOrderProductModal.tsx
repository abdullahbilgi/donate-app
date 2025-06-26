import { useAppDispatch, useAppSelector } from "../store";
import { createOrder } from "../store/Order/thunks";
import Button from "./Button";
import { ShowOrderModalRow } from "./ShowOrderModalRow";

type Market = {
  id: number;
  name: string;
  taxNumber: string;
  status: string | null;
};

type ProductResponse = {
  description: string;
  discount: number;
  discountedPrice: number;
  expiryDate: string;
  lastDonatedDate: string;
  market: Market;
  name: string;
  price: number;
  productStatus: string;
  productionDate: string;
  quantity: number;
};

type ProductItem = {
  productPrice: number;
  productQuantity: number;
  productResponse: ProductResponse;
};
interface Props {
  productItems: ProductItem[];
}
export const ShowOrderProductModal = ({ productItems }: Props) => {
  return (
    <div>
      <h6 className="mb-4 text-lg">Order Products</h6>
      <div className="overflow-x-auto">
        <table className="w-full border-separate table-custom border-spacing-y-1">
          <thead className="ltr:text-left rtl:text-right ">
            <tr className="relative rounded-md bg-slate-50 after:absolute after:border-l-2 after:left-0 after:top-0 after:bottom-0 after:border-transparent dark:bg-zink-600 [&.active]:after:border-custom-500">
              <th className="px-3.5 py-10 font-semibold">Product Name</th>
              <th className="px-3.5 py-10 font-semibold">Quantity</th>
              <th className="px-3.5 py-10 font-semibold">Price</th>
              <th className="px-3.5 py-10 font-semibold">Discounted Price</th>
              <th className="px-3.5 py-10 font-semibold">Market</th>
            </tr>
          </thead>
          <tbody>
            {productItems.map((product) => {
              return (
                <tr className="relative rounded-md bg-slate-50 after:absolute after:border-l-2 after:left-0 after:top-0 after:bottom-0 after:border-transparent dark:bg-zink-600 [&.active]:after:border-custom-500">
                  <ShowOrderModalRow>
                    {product.productResponse.name}
                  </ShowOrderModalRow>
                  <ShowOrderModalRow>{product.productPrice}</ShowOrderModalRow>
                  <ShowOrderModalRow>
                    {product.productResponse.price}
                  </ShowOrderModalRow>
                  <ShowOrderModalRow>
                    {product.productResponse.discountedPrice}
                  </ShowOrderModalRow>
                  <ShowOrderModalRow>
                    {product.productResponse.market.name}
                  </ShowOrderModalRow>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>
    </div>
  );
};
