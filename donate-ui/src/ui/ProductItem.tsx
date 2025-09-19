import { Link } from "react-router";
import Button from "./Button";
import { RiArrowDropDownLine } from "react-icons/ri";
import { useState } from "react";
import { addProductToCart } from "../store/CartStore/Cart/thunks";
import { useAppDispatch, useAppSelector } from "../store";
import QuantityInput from "./QuantityInput";
import toast from "react-hot-toast";
import { SuccesNotafication } from "../Toast-Notification/SuccesNotification";
import { BsBasketFill } from "react-icons/bs";
import { hasPermission } from "../utils/permissionUtils";

interface ProductItemProps {
  id: number;
  image: string | null;
  label: string;
  normalPrice: number;
  discountPrice: number;
}
const ProductItem: React.FC<ProductItemProps> = ({
  id,
  image,
  label,
  normalPrice,
  discountPrice,
}) => {
  const [dropdown, setDropdown] = useState(false);
  const dispatch = useAppDispatch();
  const { userId, role } = useAppSelector((state) => state.Auth);
  const onCart = useAppSelector((state: any) =>
    state.Cart.cartItems.find((item: any) => item.product.id === id)
  );

  const addToCartHandler = () => {
    if (userId !== null) {
      toast.loading("Adding Product");
      dispatch(
        addProductToCart({
          productId: id,
          productQuantity: 1,
        })
      )
        .then(() => {
          toast.dismiss();
          toast.custom((t) => (
            <SuccesNotafication
              title="Product added to cart"
              text=""
              icon={<BsBasketFill className="w-7 h-7 text-green-800" />}
              t={t}
            />
          ));
        })
        .catch((error) => toast.error(error));
    } else {
      console.log("Please first login!!");
      // burada belki bir modal gösterilebilir
    }
  };

  return (
    <div className="py-4 shadow-md shadow-slate-200 border-0 mb-5 border-transparent md:group-[.gridView]:flex relative bg-gray-50 rounded-md">
      <div className="relative group-[.gridView]:static p-6 group-[.gridView]:p-5">
        <div className="group-[.gridView]:p-3 group-[.gridView]:bg-slate-100 group-[.gridView]:inline-block rounded-md ">
          <Link to={"/home"}>
            <img
              src="/public/images/donation.jpg"
              className="group-[.gridView]:h-16"
              alt="ürün1"
            />
          </Link>
        </div>
      </div>
      <div className="p-5 !pt-0 md:group-[.gridView]:flex group-[.gridView]:!p-5 group-[.gridView]:gap-3 group-[.gridView]:grow">
        <div className="group-[.gridView]:grow">
          <h6 className="mb-1 truncate transition-all duration-200 ease-linear text-md hover:text-custom-500">
            <Link to={"/home"}>{label}</Link>
          </h6>
          <div className="flex items-center justify-between mt-4">
            <h5 className=" text-xl font-medium">
              ₺{discountPrice}{" "}
              <small className="font-medium line-through text-slate-500 dark:text-zink-200">
                {normalPrice}
              </small>
            </h5>

            {discountPrice === 0 ? (
              <span className="inline-flex items-center gap-1 rounded-md bg-emerald-600 px-2.5 py-1 text-xs font-semibold text-white shadow-sm ring-1 ring-inset ring-emerald-700/30 dark:bg-emerald-500 dark:ring-emerald-400/30">
                Free
              </span>
            ) : (
              <span className="inline-flex items-center gap-1 rounded-md bg-amber-600 px-2.5 py-1 text-xs font-semibold text-white shadow-sm ring-1 ring-inset ring-amber-700/30">
                {`${(normalPrice - discountPrice).toFixed(2)}₺`} Save
              </span>
            )}
          </div>
        </div>
        <div className="flex items-center gap-2 mt-4 group-[.gridView]:mt-0 group-[.gridView]:self-end">
          {onCart ? (
            <QuantityInput id={id} />
          ) : (
            <Button
              className="w-full h-[38.39px] rounded-xs"
              onClick={addToCartHandler}
              variation="addToCart"
            >
              <span className="align-middle">Add to Cart</span>
            </Button>
          )}

          {hasPermission(role, "edit:product") && (
            <div className="dropdown relative float-right dropdown">
              <button
                onClick={() => setDropdown(!dropdown)}
                className="flex items-center justify-center w-[38.39px] h-[38.39px] p-0 text-slate-100 bg-slate-600 hover:text-white hover:bg-slate-500 focus:text-white focus:bg-slate-600 focus:ring focus:ring-slate-100 active:text-white active:bg-slate-600 active:ring active:ring-slate-100 "
              >
                <RiArrowDropDownLine className="w-7 h-7" />
              </button>

              <div
                className={`${
                  dropdown ? "" : "hidden"
                } absolute z-50 py-2 mt-1 ltr:text-left rtl:text-right list-none bg-white rounded-md shadow-md dropdown-menu min-w-[10rem]`}
                style={{
                  position: "absolute",
                  inset: "0px 0px auto auto",
                  margin: "0px",
                  transform: "translate(0px, 54px)",
                }}
              >
                <li>
                  <button className="block px-4 py-1.5 text-base transition-all duration-200 ease-linear text-slate-600 dropdown-item hover:bg-slate-100 hover:text-slate-500 focus:bg-slate-100 focus:text-slate-500 dark:text-zink-100 dark:hover:bg-zink-500 dark:hover:text-zink-200 dark:focus:bg-zink-500 dark:focus:text-zink-200">
                    <span className="align-middle">Overview</span>
                  </button>
                  <button className="block px-4 py-1.5 text-base transition-all duration-200 ease-linear text-slate-600 dropdown-item hover:bg-slate-100 hover:text-slate-500 focus:bg-slate-100 focus:text-slate-500 dark:text-zink-100 dark:hover:bg-zink-500 dark:hover:text-zink-200 dark:focus:bg-zink-500 dark:focus:text-zink-200">
                    <span className="align-middle">Edit</span>
                  </button>
                  <button className="block px-4 py-1.5 text-base transition-all duration-200 ease-linear text-slate-600 dropdown-item hover:bg-slate-100 hover:text-slate-500 focus:bg-slate-100 focus:text-slate-500 dark:text-zink-100 dark:hover:bg-zink-500 dark:hover:text-zink-200 dark:focus:bg-zink-500 dark:focus:text-zink-200">
                    <span className="align-middle">Delete</span>
                  </button>
                </li>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default ProductItem;
