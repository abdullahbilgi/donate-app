import { useEffect } from "react";
import { useAppDispatch, useAppSelector } from "../store";
import TablesBody from "../ui/TablesBody";
import TablesColumn from "../ui/TablesColumn";
import { getMarketByUser } from "../store/MarketStore/Market/thunks";
import { TablesCell } from "../ui/TablesCell";
import { getProductsByMarket } from "../store/ProductStore/GetProductByMarket/thunks";
import { Link } from "react-router";

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
    <div>
      <h6 className="mb-4">Markets Table</h6>
      <div className="my-2 col-span-12 overflow-x-auto lg:col-span-12">
        <table className="bordered group dataTable w-full text-sm align-middle whitespace-nowrap no-footer">
          <thead className="border-b border-slate-200 dark:border-zink-500">
            <tr className="group-[.stri1pe]:even:bg-slate-50 group-[.stripe]:dark:even:bg-zink-600 transition-all duration-150 ease-linear group-[.hover]:hover:bg-slate-50 dark:group-[.hover]:hover:bg-zink-600 [&.selected]:bg-custom-500 dark:[&.selected]:bg-custom-500 [&.selected]:text-custom-50 dark:[&.selected]:text-custom-50">
              <TablesColumn title="Name" />
              <TablesColumn title="Address" />
              <TablesColumn title="Created at" />
              <TablesColumn title="Product" />
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
                  <TablesCell>17 June 2024</TablesCell>
                  <TablesCell>
                    <div className="flex justify-between">
                      <p>{total}</p>

                      <Link to={`/productsByMarket/${market.id}`}>
                        Show Products
                      </Link>
                    </div>
                  </TablesCell>
                </TablesBody>
              );
            })}
          </tbody>
        </table>
      </div>
    </div>
  );
};
