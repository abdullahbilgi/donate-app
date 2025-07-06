import { useEffect } from "react";
import { useAppDispatch, useAppSelector } from "../store";
import TablesBody from "../ui/TablesBody";
import TablesColumn from "../ui/TablesColumn";
import {
  deleteMarket,
  getMarketByUser,
} from "../store/MarketStore/Market/thunks";
import { TablesCell } from "../ui/TablesCell";
import { Link, useNavigate } from "react-router";
import Button from "../ui/Button";
import { MdEdit } from "react-icons/md";
import { FaRegTrashAlt } from "react-icons/fa";
import Modal from "../ui/Modal";
import { getAllCity } from "../store/City/thunks";
import MarketCreateModalContent from "../ui/MarketCreateModalContent";
import DeleteModalContent from "../ui/DeleteModalContent";
import AddProductForm from "./AddProductForm";
import { getProductsByMarket } from "../store/ProductStore/GetProductByMarket/thunks";
import { getAllOrderByUser, getOrderInvoice } from "../store/Order/thunks";
import { format } from "date-fns";
import { ShowOrderProductModal } from "../ui/ShowOrderProductModal";

export const Orders = () => {
  const dispatch = useAppDispatch();
  const { myOrders, loading, error } = useAppSelector((state) => state.Order);

  const totals = useAppSelector((state) => state.ProductsByMarket);
  const userId = localStorage.getItem("userId");
  console.log(myOrders);
  useEffect(() => {
    dispatch(getAllOrderByUser(userId));
  }, []);

  const getInvoice = (orderId: any) => {
    dispatch(getOrderInvoice(orderId));
  };
  // useEffect(() => {
  //   marketsArr.forEach((market) => dispatch(getProductsByMarket(market.id)));
  // }, [marketsArr]);

  return (
    <div className="group-data-[sidebar-size=lg]:ltr:md:ml-vertical-menu group-data-[sidebar-size=lg]:rtl:md:mr-vertical-menu p-8 group-data-[sidebar-size=md]:ltr:ml-vertical-menu-md group-data-[sidebar-size=md]:rtl:mr-vertical-menu-md group-data-[sidebar-size=sm]:ltr:ml-vertical-menu-sm group-data-[sidebar-size=sm]:rtl:mr-vertical-menu-sm pt-[calc(theme('spacing.header')_*_1)] pb-[calc(theme('spacing.header')_*_0.8)] px-4 group-data-[navbar=bordered]:pt-[calc(theme('spacing.header')_*_1.3)] group-data-[navbar=hidden]:pt-0 group-data-[layout=horizontal]:mx-auto group-data-[layout=horizontal]:max-w-screen-2xl group-data-[layout=horizontal]:px-0 group-data-[layout=horizontal]:group-data-[sidebar-size=lg]:ltr:md:ml-auto group-data-[layout=horizontal]:group-data-[sidebar-size=lg]:rtl:md:mr-auto group-data-[layout=horizontal]:md:pt-[calc(theme('spacing.header')_*_1.8)] group-data-[layout=horizontal]:px-3 group-data-[layout=horizontal]:group-data-[navbar=hidden]:pt-[calc(theme('spacing.header')_*_0.9)]">
      <div className="container-fluid group-data-[content=boxed]:max-w-boxed mx-auto bg-gray-50 p-6 shadow-md shadow-slate-200">
        <h6 className="mb-4 text-2xl">My All Orders</h6>
        <div className="my-2 col-span-12 overflow-x-auto lg:col-span-12">
          {!loading && myOrders.length === 0 && (
            <div className="flex justify-between items-center p-2 mt-5 bg-yellow-100 mb-4">
              <h3 className="font-medium text-md">
                Your order history is empty. Ready to place your first one?
              </h3>
              <div className="flex justify-end">
                <Button
                  variation="pagination"
                  className="hover:bg-gray-400 hover:text-green-800 shadow-none"
                >
                  <Link to="/products">Start ordering now!</Link>
                </Button>
              </div>
            </div>
          )}

          {loading ? (
            <div className="animate-spin rounded-full h-10 w-10 border-3 border-green-900 mx-auto" />
          ) : (
            <table className="bordered group dataTable w-full text-sm align-middle whitespace-nowrap no-footer">
              <thead className="border-b border-slate-200 dark:border-zink-500">
                <tr className="group-[.stri1pe]:even:bg-slate-50 group-[.stripe]:dark:even:bg-zink-600 transition-all duration-150 ease-linear group-[.hover]:hover:bg-slate-50 dark:group-[.hover]:hover:bg-zink-600 [&.selected]:bg-custom-500 dark:[&.selected]:bg-custom-500 [&.selected]:text-custom-50 dark:[&.selected]:text-custom-50">
                  <TablesColumn title="Products" />
                  <TablesColumn title="Price" />
                  <TablesColumn title="Saved Amount" />
                  <TablesColumn title="Discounted Price" />
                  <TablesColumn title="Order Date" />
                  <TablesColumn title="Status" />
                  <TablesColumn title="" />
                </tr>
              </thead>
              <tbody>
                {myOrders.map((order) => {
                  return (
                    <TablesBody key={order.id}>
                      <TablesCell>
                        <div className="flex justify-between">
                          <div>{order.productItems.length} Items</div>
                          <Modal>
                            <Modal.Open modalName="showProducts">
                              <button className="cursor-pointer">
                                Show Products
                              </button>
                            </Modal.Open>
                            <Modal.Window name="showProducts">
                              <ShowOrderProductModal
                                productItems={order.productItems}
                              />
                            </Modal.Window>
                          </Modal>
                        </div>
                      </TablesCell>
                      <TablesCell>
                        {" "}
                        <p>
                          {order.productItems.reduce(
                            (acc, item) => acc + item.productResponse.price,
                            0
                          )}
                          ₺
                        </p>
                      </TablesCell>
                      <TablesCell>
                        <p className="font-semibold text-green-600">
                          {order.productItems.reduce(
                            (acc, item) => acc + item.productResponse.price,
                            0
                          ) - order.totalPrice}
                          ₺
                        </p>
                      </TablesCell>
                      <TablesCell>
                        <p>
                          {order.productItems.reduce(
                            (acc, item) => acc + item.productPrice,
                            0
                          )}
                          ₺
                        </p>
                      </TablesCell>
                      <TablesCell>
                        <div className="flex justify-between">
                          <p>
                            {format(new Date(order.purchaseDate), "MM-dd-yyyy")}
                          </p>
                        </div>
                      </TablesCell>

                      <TablesCell>
                        <p
                          className={`font-bold ${
                            order.status === "APPROVED"
                              ? "text-green-600"
                              : "text-red-600"
                          }`}
                        >
                          {order.status}
                        </p>
                      </TablesCell>

                      <TablesCell>
                        <div className="flex items-center justify-end gap-2">
                          {/* <Modal>
                            <Modal.Open modalName="addProductModal">
                              <Button variation="cell">Show Market</Button>
                            </Modal.Open>

                            <Modal.Window name="addProductModal">
                              <AddProductForm
                                type="modal"
                                marketId={market.id}
                              />
                            </Modal.Window>
                          </Modal> */}

                          <Button
                            variation="cell"
                            onClick={() => getInvoice(order.id)}
                          >
                            Get Invoice
                          </Button>
                        </div>
                      </TablesCell>
                    </TablesBody>
                  );
                })}
              </tbody>
            </table>
          )}
        </div>
      </div>
    </div>
  );
};
