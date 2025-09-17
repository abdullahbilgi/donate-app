import { useCallback, useEffect, useMemo, useState } from "react";
import { useAppDispatch, useAppSelector } from "../store";
import ProductFilter from "../ui/ProductFilter";
import ProductList from "../ui/ProductsList";
import {
  getAllDonatedProducts,
  getAllProducts,
  searchProduct,
} from "../store/ProductStore/Products/thunks";
import Button from "../ui/Button";
import { hasPermission } from "../utils/permissionUtils";

const Products = () => {
  const dispatch = useAppDispatch();

  const {
    productsArr,
    searchProducts,
    loading,
    number,
    size,
    totalPages,
    totalElements,
  } = useAppSelector((state: any) => state.Product);
  const { role, token, isLogged } = useAppSelector((state) => state.Auth);

  const canViewProducts = useMemo(
    () => hasPermission(role, "view:products"),
    [role]
  );
  const canViewDonated = useMemo(
    () => hasPermission(role, "view:donateProducts"),
    [role]
  );

  const [searchTerm, setSearchTerm] = useState("");
  const [pageNumber, setPageNumber] = useState(0);
  const [donateProducts, setDonateProducts] = useState(false);

  const handlePageClick = useCallback((index: number) => {
    setPageNumber(index);
  }, []);

  const handleToggleDonate = useCallback((donateProducts: any) => {
    setDonateProducts(donateProducts);
    setPageNumber(0);
  }, []);

  const handleSearchTerm = useCallback((searchTerm: string) => {
    setSearchTerm(searchTerm);
    setPageNumber(0);
  }, []);

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

  const activeTags = useMemo(() => {
    const tags: {
      key: string;
      label: string;
      className: string;
      onRemove: () => void;
    }[] = [];

    if (donateProducts) {
      tags.push({
        key: "donate",
        label: "Donate products",
        className:
          "bg-emerald-600 text-white hover:bg-emerald-700 dark:bg-emerald-500/90 dark:hover:bg-emerald-500",
        onRemove: () => handleToggleDonate(false),
      });
    }

    if (debouncedSearchTerm.trim()) {
      tags.push({
        key: "q",
        label: `Search "${debouncedSearchTerm}"`,
        className:
          "bg-sky-600 text-white hover:bg-sky-700 dark:bg-sky-500/90 dark:hover:bg-sky-500",
        onRemove: () => handleSearchTerm(""),
      });
    }

    return tags;
  }, [
    donateProducts,
    handleToggleDonate,
    debouncedSearchTerm,
    handleSearchTerm,
  ]);

  console.log(donateProducts);
  useEffect(() => {
    console.log("a");
    if (debouncedSearchTerm.trim() !== "" && canViewProducts) {
      dispatch(searchProduct(debouncedSearchTerm));
    } else {
      {
        if (canViewDonated && !canViewProducts) {
          // Bu sadece BENEFACTOR için geçerli: sadece bağış ürünlerini görebilir
          dispatch(getAllDonatedProducts({ page: pageNumber }));
        } else if (canViewProducts) {
          // USER burada olacak: donateProducts kontrolüne göre yönlendir
          if (donateProducts) {
            dispatch(getAllDonatedProducts({ page: pageNumber }));
          } else {
            dispatch(getAllProducts({ page: pageNumber }));
          }
        }
      }
    }
  }, [
    pageNumber,
    debouncedSearchTerm,
    donateProducts,
    canViewProducts,
    canViewDonated,
  ]);

  console.log(debouncedSearchTerm);
  const showResults = useMemo(
    () => (debouncedSearchTerm ? searchProducts : productsArr),
    [debouncedSearchTerm, searchProducts, productsArr]
  );

  console.log(donateProducts);
  return (
    <div className="group-data-[sidebar-size=lg]:ltr:md:ml-vertical-menu group-data-[sidebar-size=lg]:rtl:md:mr-vertical-menu group-data-[sidebar-size=md]:ltr:ml-vertical-menu-md group-data-[sidebar-size=md]:rtl:mr-vertical-menu-md group-data-[sidebar-size=sm]:ltr:ml-vertical-menu-sm group-data-[sidebar-size=sm]:rtl:mr-vertical-menu-sm pt-[calc(theme('spacing.header')_*_1)] pb-[calc(theme('spacing.header')_*_0.8)] px-4 group-data-[navbar=bordered]:pt-[calc(theme('spacing.header')_*_1.3)] group-data-[navbar=hidden]:pt-0 group-data-[layout=horizontal]:mx-auto group-data-[layout=horizontal]:max-w-screen-2xl group-data-[layout=horizontal]:px-0 group-data-[layout=horizontal]:group-data-[sidebar-size=lg]:ltr:md:ml-auto group-data-[layout=horizontal]:group-data-[sidebar-size=lg]:rtl:md:mr-auto group-data-[layout=horizontal]:md:pt-[calc(theme('spacing.header')_*_1.8)] group-data-[layout=horizontal]:px-3 group-data-[layout=horizontal]:group-data-[navbar=hidden]:pt-[calc(theme('spacing.header')_*_0.9)]">
      <div className="container-fluid group-data-[content=boxed]:max-w-boxed mx-auto my-16 flex flex-col gap-5">
        <div className="grid grid-cols-1 xl:grid-cols-12 gap-x-5">
          <div className="xl:col-span-3">
            <ProductFilter
              searchTerm={searchTerm}
              setSearchTerm={handleSearchTerm}
              setDonateProducts={handleToggleDonate}
              donateProducts={donateProducts}
            />
          </div>

          {/* Since the same totalElements and totalPages values ​​come from the API, for example, even if there are 5 products, 4 pages are displayed when the search is made. */}
          <div className="xl:col-span-9 flex flex-col gap-4">
            <div className="flex flex-wrap items-center gap-2 px-4">
              <p className="grow text-md font-semibold text-gray-600">
                Showing{" "}
                <b className="text-lg text-gray-700">
                  {size * pageNumber} -{" "}
                  {size * pageNumber + size >= totalElements
                    ? totalElements
                    : size * pageNumber + size}
                </b>{" "}
                products out of {totalElements}
              </p>
              <div>
                {activeTags.length === 0
                  ? ""
                  : activeTags.map((t) => (
                      <button
                        key={t.key}
                        onClick={t.onRemove}
                        className={`${t.className} inline-flex items-center gap-1 rounded-full border px-3 mr-2 py-1 text-sm transition`}
                      >
                        {t.label}
                        {/* İkon istersen: */}
                        {/* <X className="w-3.5 h-3.5" aria-hidden /> */}
                        <span aria-hidden>×</span>
                      </button>
                    ))}
              </div>
            </div>
            {loading ? (
              <div className="animate-spin rounded-full h-12 w-12 border-3 border-green-900 mx-auto" />
            ) : (
              <ProductList products={showResults} />
            )}
          </div>
        </div>
        <div className="flex gap-4 justify-center">
          {Array.from({ length: totalPages }, (_, index) => {
            return (
              <Button
                variation="pagination"
                onClick={() => handlePageClick(index)}
                disabled={index === pageNumber}
              >
                {index + 1}
              </Button>
            );
          })}
        </div>
      </div>
    </div>
  );
};

export default Products;
