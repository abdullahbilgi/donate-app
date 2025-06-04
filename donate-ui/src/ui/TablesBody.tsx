import { useEffect } from "react";
import { IUMarket } from "../store/MarketStore/Market/reducers";
import { useAppDispatch, useAppSelector } from "../store";
import { getProductsByMarket } from "../store/ProductStore/GetProductByMarket/thunks";
import { Link } from "react-router";

interface TablesBodyProps {
  children: React.ReactNode;
}
const TablesBody = ({ children }: TablesBodyProps) => {
  return (
    <tr className="group-[.stripe]:even:bg-slate-50 group-[.stripe]:dark:even:bg-zink-600 transition-all duration-150 ease-linear group-[.hover]:hover:bg-slate-50 dark:group-[.hover]:hover:bg-zink-600 [&.selected]:bg-custom-500 dark:[&.selected]:bg-custom-500 [&.selected]:text-custom-50 dark:[&.selected]:text-custom-50">
      {children}
    </tr>
  );
};

export default TablesBody;

{
  /* <td className="p-3 group-[.bordered]:border group-[.bordered]:border-slate-200 group-[.bordered]:dark:border-zink-500">
        {market.address.region.name}, {market.address.region.city.name}
      </td>
      <td className="p-3 group-[.bordered]:border group-[.bordered]:border-slate-200 group-[.bordered]:dark:border-zink-500">
        17 June 2025
      </td>
      <td className="p-3 group-[.bordered]:border group-[.bordered]:border-slate-200 group-[.bordered]:dark:border-zink-500">
        <div className="flex justify-between">
          <div>{totalElements}</div>
          <Link to="/productsByMarket">Show products</Link>
        </div>
      </td> */
}
