import { useEffect } from "react";
import { useAppDispatch, useAppSelector } from "../store";
import TablesBody from "../ui/TablesBody";
import TablesColumn from "../ui/TablesColumn";
import { getMarketByUser } from "../store/MarketStore/Market/thunks";
import { TablesCell } from "../ui/TablesCell";
import { getProductsByMarket } from "../store/ProductStore/GetProductByMarket/thunks";
import { Link } from "react-router";
import Button from "../ui/Button";
import { MdEdit } from "react-icons/md";
import { FaRegTrashAlt } from "react-icons/fa";

export const Markets = () => {
  const dispatch = useAppDispatch();
  const { marketsArr } = useAppSelector((state) => state.Market);

  const totals = useAppSelector((state) => state.ProductsByMarket);
  const userId = localStorage.getItem("userId");

  useEffect(() => {
    dispatch(getMarketByUser(userId));
  }, []);

  useEffect(() => {
    marketsArr.forEach((market) => dispatch(getProductsByMarket(market.id)));
  }, [marketsArr]);

  console.log("m", marketsArr);
  return (
    <div className="group-data-[sidebar-size=lg]:ltr:md:ml-vertical-menu group-data-[sidebar-size=lg]:rtl:md:mr-vertical-menu p-8 group-data-[sidebar-size=md]:ltr:ml-vertical-menu-md group-data-[sidebar-size=md]:rtl:mr-vertical-menu-md group-data-[sidebar-size=sm]:ltr:ml-vertical-menu-sm group-data-[sidebar-size=sm]:rtl:mr-vertical-menu-sm pt-[calc(theme('spacing.header')_*_1)] pb-[calc(theme('spacing.header')_*_0.8)] px-4 group-data-[navbar=bordered]:pt-[calc(theme('spacing.header')_*_1.3)] group-data-[navbar=hidden]:pt-0 group-data-[layout=horizontal]:mx-auto group-data-[layout=horizontal]:max-w-screen-2xl group-data-[layout=horizontal]:px-0 group-data-[layout=horizontal]:group-data-[sidebar-size=lg]:ltr:md:ml-auto group-data-[layout=horizontal]:group-data-[sidebar-size=lg]:rtl:md:mr-auto group-data-[layout=horizontal]:md:pt-[calc(theme('spacing.header')_*_1.8)] group-data-[layout=horizontal]:px-3 group-data-[layout=horizontal]:group-data-[navbar=hidden]:pt-[calc(theme('spacing.header')_*_0.9)]">
      <div className="container-fluid group-data-[content=boxed]:max-w-boxed mx-auto bg-gray-50 p-6 shadow-md shadow-slate-200">
        <h6 className="mb-4 text-2xl">Markets Table</h6>
        <div className="my-2 col-span-12 overflow-x-auto lg:col-span-12">
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

                        <Button variation="cell">
                          <Link to={`/productsByMarket/${market.id}`}>
                            {total > 0 ? "Show Products" : "Add Product"}
                          </Link>
                        </Button>
                      </div>
                    </TablesCell>

                    <TablesCell>
                      <div className="flex justify-around">
                        <Button variation="submit">
                          <MdEdit />
                        </Button>
                        <Button variation="danger">
                          <FaRegTrashAlt />
                        </Button>
                      </div>
                    </TablesCell>
                  </TablesBody>
                );
              })}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};
