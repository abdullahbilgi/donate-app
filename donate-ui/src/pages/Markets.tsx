import { useEffect } from "react";
import { useAppDispatch, useAppSelector } from "../store";
import TablesBody from "../ui/TablesBody";
import TablesColumn from "../ui/TablesColumn";
import {
  deleteMarket,
  getMarketByUser,
} from "../store/MarketStore/Market/thunks";
import { TablesCell } from "../ui/TablesCell";
import { Link } from "react-router";
import Button from "../ui/Button";
import { MdEdit } from "react-icons/md";
import { FaRegTrashAlt } from "react-icons/fa";
import Modal from "../ui/Modal";
import { getAllCity } from "../store/City/thunks";
import MarketCreateModalContent from "../ui/MarketCreateModalContent";
import DeleteModalContent from "../ui/DeleteModalContent";
import AddProductForm from "./AddProductForm";
import { getProductsByMarket } from "../store/ProductStore/GetProductByMarket/thunks";

export const Markets = () => {
  const dispatch = useAppDispatch();
  const { marketsArr, loading, error } = useAppSelector(
    (state) => state.Market
  );

  const totals = useAppSelector((state) => state.ProductsByMarket);
  const userId = localStorage.getItem("userId");

  useEffect(() => {
    dispatch(getMarketByUser(userId));
    dispatch(getAllCity());
  }, []);

  const onDeleteMarket = (marketId: any) => {
    dispatch(deleteMarket(marketId));
  };

  useEffect(() => {
    marketsArr.forEach((market) => dispatch(getProductsByMarket(market.id)));
  }, [marketsArr]);

  return (
    <div className="group-data-[sidebar-size=lg]:ltr:md:ml-vertical-menu group-data-[sidebar-size=lg]:rtl:md:mr-vertical-menu p-8 group-data-[sidebar-size=md]:ltr:ml-vertical-menu-md group-data-[sidebar-size=md]:rtl:mr-vertical-menu-md group-data-[sidebar-size=sm]:ltr:ml-vertical-menu-sm group-data-[sidebar-size=sm]:rtl:mr-vertical-menu-sm pt-[calc(theme('spacing.header')_*_1)] pb-[calc(theme('spacing.header')_*_0.8)] px-4 group-data-[navbar=bordered]:pt-[calc(theme('spacing.header')_*_1.3)] group-data-[navbar=hidden]:pt-0 group-data-[layout=horizontal]:mx-auto group-data-[layout=horizontal]:max-w-screen-2xl group-data-[layout=horizontal]:px-0 group-data-[layout=horizontal]:group-data-[sidebar-size=lg]:ltr:md:ml-auto group-data-[layout=horizontal]:group-data-[sidebar-size=lg]:rtl:md:mr-auto group-data-[layout=horizontal]:md:pt-[calc(theme('spacing.header')_*_1.8)] group-data-[layout=horizontal]:px-3 group-data-[layout=horizontal]:group-data-[navbar=hidden]:pt-[calc(theme('spacing.header')_*_0.9)]">
      <div className="container-fluid group-data-[content=boxed]:max-w-boxed mx-auto bg-gray-50 p-6 shadow-md shadow-slate-200">
        <h6 className="mb-4 text-2xl">Markets Table</h6>
        <div className="my-2 col-span-12 overflow-x-auto lg:col-span-12">
          <div className="flex justify-between items-center p-2 mt-5 bg-yellow-100 mb-4">
            <h3 className="font-medium text-md">
              {marketsArr.length === 0
                ? "Herhangi bir marketiniz bulunmuyor!"
                : "Yeni bir market ekle"}
            </h3>
            <div className="flex justify-end">
              <Modal>
                <Modal.Open modalName="createMarket">
                  <Button
                    variation="pagination"
                    className="hover:bg-gray-400 hover:text-green-800 shadow-none"
                  >
                    Market Ekle
                  </Button>
                </Modal.Open>
                <Modal.Window name="createMarket">
                  <MarketCreateModalContent />
                </Modal.Window>
              </Modal>
            </div>
          </div>

          {loading ? (
            "loading"
          ) : (
            <table className="bordered group dataTable w-full text-sm align-middle whitespace-nowrap no-footer">
              <thead className="border-b border-slate-200 dark:border-zink-500">
                <tr className="group-[.stri1pe]:even:bg-slate-50 group-[.stripe]:dark:even:bg-zink-600 transition-all duration-150 ease-linear group-[.hover]:hover:bg-slate-50 dark:group-[.hover]:hover:bg-zink-600 [&.selected]:bg-custom-500 dark:[&.selected]:bg-custom-500 [&.selected]:text-custom-50 dark:[&.selected]:text-custom-50">
                  <TablesColumn title="Name" />
                  <TablesColumn title="Address" />
                  <TablesColumn title="Tax Number" />
                  <TablesColumn title="Product" />
                  <TablesColumn title="" />
                </tr>
              </thead>
              <tbody>
                {marketsArr.map((market) => {
                  const total = totals[market.id]?.totalElements ?? 0;

                  return (
                    <TablesBody key={market.id}>
                      <TablesCell>{market.name}</TablesCell>
                      <TablesCell>
                        {market.address.region.name},{" "}
                        {market.address.region.city.name}
                      </TablesCell>
                      <TablesCell>{market.taxNumber}</TablesCell>
                      <TablesCell>
                        <div className="flex justify-between">
                          <p>{total}</p>

                          <div className="flex items-center gap-2">
                            {total > 0 && (
                              <Button variation="cell">
                                <Link to={`/productsByMarket/${market.id}`}>
                                  Show Products
                                </Link>
                              </Button>
                            )}
                            <Modal>
                              <Modal.Open modalName="addProductModal">
                                <Button variation="cell">Add Product</Button>
                              </Modal.Open>

                              <Modal.Window name="addProductModal">
                                <AddProductForm
                                  type="modal"
                                  marketId={market.id}
                                />
                              </Modal.Window>
                            </Modal>
                          </div>
                        </div>
                      </TablesCell>

                      <TablesCell>
                        <div className="flex justify-around">
                          {/*Her bir market icin ayri modal, bu sebeple isimler birbirine karismaz ,birbirinden bagimsizlar*/}

                          <Button variation="submit">
                            <MdEdit />
                          </Button>

                          <Modal>
                            <Modal.Open modalName="deleteMarket">
                              <Button variation="danger">
                                <FaRegTrashAlt />
                              </Button>
                            </Modal.Open>
                            <Modal.Window name="deleteMarket">
                              <DeleteModalContent
                                name={market.name}
                                type="market"
                                loading={loading}
                                error={error}
                                deleteDispatch={() =>
                                  dispatch(deleteMarket(market.id))
                                }
                              />
                            </Modal.Window>
                          </Modal>
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
