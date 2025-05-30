import { useEffect, useState } from "react";
import { useAppDispatch, useAppSelector } from "../store";
import ProductFilter from "../ui/ProductFilter";
import ProductList from "../ui/ProductsList";
import {
  getAllProducts,
  searchProduct,
} from "../store/ProductStore/Products/thunks";

const Products = () => {
  const dispatch = useAppDispatch();

  // normal product ve searchin loading vs ayir
  const { productsArr, searchProducts, loading, number, size } = useAppSelector(
    (state: any) => state.Product
  );

  const [searchTerm, setSearchTerm] = useState("");

  const useDebounce = (value: string, delay: number) => {
    const [debouncedValue, setDebouncedValue] = useState(value);

    useEffect(() => {
      const handler = setTimeout(() => {
        setDebouncedValue(value);
      }, delay);

      return () => {
        clearTimeout(handler);
      };
    }, [value, delay]);

    return debouncedValue;
  };

  const debouncedSearchTerm = useDebounce(searchTerm, 500);

  useEffect(() => {
    if (debouncedSearchTerm.trim() !== "") {
      dispatch(searchProduct(debouncedSearchTerm));
    } else {
      dispatch(getAllProducts());
    }
  }, [dispatch, number, size, debouncedSearchTerm]);

  const showResults = searchTerm ? searchProducts : productsArr;

  return (
    <div className="group-data-[sidebar-size=lg]:ltr:md:ml-vertical-menu group-data-[sidebar-size=lg]:rtl:md:mr-vertical-menu group-data-[sidebar-size=md]:ltr:ml-vertical-menu-md group-data-[sidebar-size=md]:rtl:mr-vertical-menu-md group-data-[sidebar-size=sm]:ltr:ml-vertical-menu-sm group-data-[sidebar-size=sm]:rtl:mr-vertical-menu-sm pt-[calc(theme('spacing.header')_*_1)] pb-[calc(theme('spacing.header')_*_0.8)] px-4 group-data-[navbar=bordered]:pt-[calc(theme('spacing.header')_*_1.3)] group-data-[navbar=hidden]:pt-0 group-data-[layout=horizontal]:mx-auto group-data-[layout=horizontal]:max-w-screen-2xl group-data-[layout=horizontal]:px-0 group-data-[layout=horizontal]:group-data-[sidebar-size=lg]:ltr:md:ml-auto group-data-[layout=horizontal]:group-data-[sidebar-size=lg]:rtl:md:mr-auto group-data-[layout=horizontal]:md:pt-[calc(theme('spacing.header')_*_1.8)] group-data-[layout=horizontal]:px-3 group-data-[layout=horizontal]:group-data-[navbar=hidden]:pt-[calc(theme('spacing.header')_*_0.9)]">
      <div className="container-fluid group-data-[content=boxed]:max-w-boxed mx-auto">
        <div className="grid grid-cols-1 2xl:grid-cols-12 gap-x-5">
          <ProductFilter
            searchTerm={searchTerm}
            setSearchTerm={setSearchTerm}
          />
          <ProductList products={showResults} />
        </div>
      </div>
    </div>
  );
};

export default Products;
