import { useEffect, useState } from "react";
import { useAppDispatch, useAppSelector } from "../store";
import ProductFilter from "../ui/ProductFilter";
import ProductList from "../ui/ProductsList";
import {
  getAllProducts,
  searchProduct,
} from "../store/ProductStore/Products/thunks";
import TablesColumn from "../ui/TablesColumn";
import TablesBody from "../ui/TablesBody";
import { useParams } from "react-router";
import { TablesCell } from "../ui/TablesCell";

const ProductsByMarket = () => {
  const dispatch = useAppDispatch();
  const { id } = useParams();
  const products = useAppSelector(
    (state) => state.ProductsByMarket[Number(id)]?.products
  );

  console.log(products);

  return (
    <div className="group-data-[sidebar-size=lg]:ltr:md:ml-vertical-menu group-data-[sidebar-size=lg]:rtl:md:mr-vertical-menu group-data-[sidebar-size=md]:ltr:ml-vertical-menu-md group-data-[sidebar-size=md]:rtl:mr-vertical-menu-md group-data-[sidebar-size=sm]:ltr:ml-vertical-menu-sm group-data-[sidebar-size=sm]:rtl:mr-vertical-menu-sm pt-[calc(theme('spacing.header')_*_1)] pb-[calc(theme('spacing.header')_*_0.8)] px-4 group-data-[navbar=bordered]:pt-[calc(theme('spacing.header')_*_1.3)] group-data-[navbar=hidden]:pt-0 group-data-[layout=horizontal]:mx-auto group-data-[layout=horizontal]:max-w-screen-2xl group-data-[layout=horizontal]:px-0 group-data-[layout=horizontal]:group-data-[sidebar-size=lg]:ltr:md:ml-auto group-data-[layout=horizontal]:group-data-[sidebar-size=lg]:rtl:md:mr-auto group-data-[layout=horizontal]:md:pt-[calc(theme('spacing.header')_*_1.8)] group-data-[layout=horizontal]:px-3 group-data-[layout=horizontal]:group-data-[navbar=hidden]:pt-[calc(theme('spacing.header')_*_0.9)]">
      <div className="container-fluid group-data-[content=boxed]:max-w-boxed mx-auto">
        <div className="my-2 col-span-12 overflow-x-auto lg:col-span-12">
          <table className="bordered group dataTable w-full text-sm align-middle whitespace-nowrap no-footer">
            <thead className="border-b border-slate-200 dark:border-zink-500">
              <tr className="group-[.stripe]:even:bg-slate-50 group-[.stripe]:dark:even:bg-zink-600 transition-all duration-150 ease-linear group-[.hover]:hover:bg-slate-50 dark:group-[.hover]:hover:bg-zink-600 [&.selected]:bg-custom-500 dark:[&.selected]:bg-custom-500 [&.selected]:text-custom-50 dark:[&.selected]:text-custom-50">
                <TablesColumn title="Name" />
                <TablesColumn title="Category" />
                <TablesColumn title="Price" />
                <TablesColumn title="Discount" />
                <TablesColumn title="Quantity" />
                <TablesColumn title="Description" />
                <TablesColumn title="Status" />
              </tr>
            </thead>
            <tbody>
              {products.map((pro) => {
                return (
                  <TablesBody key={pro.id}>
                    <TablesCell>{pro.name}</TablesCell>
                    <TablesCell>{pro.category.name}</TablesCell>
                    <TablesCell>{pro.price}</TablesCell>
                    <TablesCell>{pro.discount}</TablesCell>
                    <TablesCell>{pro.quantity}</TablesCell>
                    <TablesCell>{pro.description}</TablesCell>
                    <TablesCell>{pro.productStatus}</TablesCell>
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

export default ProductsByMarket;
