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
import { Box, X } from "lucide-react";
import { useSmoothLoader } from "../hooks/useSmoothLoader";

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

  const showLoader = useSmoothLoader(loading, {
    showAfter: 200,
    minVisible: 400,
  });

  const canViewProducts = useMemo(
    () => hasPermission(role, "view:products"),
    [role]
  );
  const canViewDonated = useMemo(
    () => hasPermission(role, "view:donateProducts"),
    [role]
  );

  type SortKeyArg = "asc" | "desc";
  type SortState = null | { field: "discountedPrice"; sortKey: SortKeyArg };

  const [searchTerm, setSearchTerm] = useState("");
  const [pageNumber, setPageNumber] = useState(0);
  const [sort, setSort] = useState<SortState>(null);
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

  const handleSortByPrice = useCallback((sort: SortState) => {
    setSort(sort);
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
        className: "bg-emerald-600",
        onRemove: () => handleToggleDonate(false),
      });
    }

    if (debouncedSearchTerm.trim()) {
      tags.push({
        key: "q",
        label: `Search "${debouncedSearchTerm}"`,
        className: "bg-sky-600",
        onRemove: () => handleSearchTerm(""),
      });
    }

    if (sort?.sortKey) {
      const asc = sort.sortKey === "asc";
      tags.push({
        key: "sort",
        label: `${asc ? "Low → High" : "High → Low"}`,
        className: `${
          asc
            ? "bg-gradient-to-r from-lime-400 to-blue-600"
            : "bg-gradient-to-r from-purple-600 to-yellow-400"
        }`,
        onRemove: () => handleSortByPrice(null),
      });
    }

    return tags;
  }, [
    donateProducts,
    handleToggleDonate,
    debouncedSearchTerm,
    handleSearchTerm,
    sort,
    handleSortByPrice,
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
            dispatch(
              getAllProducts({
                page: pageNumber,
                sort: sort ? `${sort?.field},${sort?.sortKey}` : undefined,
              })
            );
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
    sort,
  ]);

  console.log(debouncedSearchTerm);
  const showResults = useMemo(
    () => (debouncedSearchTerm ? searchProducts : productsArr),
    [debouncedSearchTerm, searchProducts, productsArr]
  );

  console.log(showResults);
  return (
    <div className="group-data-[sidebar-size=lg]:ltr:md:ml-vertical-menu group-data-[sidebar-size=lg]:rtl:md:mr-vertical-menu group-data-[sidebar-size=md]:ltr:ml-vertical-menu-md group-data-[sidebar-size=md]:rtl:mr-vertical-menu-md group-data-[sidebar-size=sm]:ltr:ml-vertical-menu-sm group-data-[sidebar-size=sm]:rtl:mr-vertical-menu-sm pt-[calc(theme('spacing.header')_*_1)] pb-[calc(theme('spacing.header')_*_0.8)] px-4 group-data-[navbar=bordered]:pt-[calc(theme('spacing.header')_*_1.3)] group-data-[navbar=hidden]:pt-0 group-data-[layout=horizontal]:mx-auto group-data-[layout=horizontal]:max-w-screen-2xl group-data-[layout=horizontal]:group-data-[sidebar-size=lg]:ltr:md:ml-auto group-data-[layout=horizontal]:group-data-[sidebar-size=lg]:rtl:md:mr-auto group-data-[layout=horizontal]:md:pt-[calc(theme('spacing.header')_*_1.8)] group-data-[layout=horizontal]:px-3 group-data-[layout=horizontal]:group-data-[navbar=hidden]:pt-[calc(theme('spacing.header')_*_0.9)] ">
      <div className="container-fluid group-data-[content=boxed]:max-w-boxed mx-auto my-16 flex flex-col gap-5">
        <div className="grid grid-cols-1 xl:grid-cols-12 gap-x-5">
          <div className="xl:col-span-3">
            <ProductFilter
              searchTerm={searchTerm}
              setSearchTerm={handleSearchTerm}
              setDonateProducts={handleToggleDonate}
              donateProducts={donateProducts}
              sort={sort}
              setSort={handleSortByPrice}
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
                      <div
                        key={t.key}
                        className={`${t.className} inline-flex items-center gap-2 border transition px-3 py-1 rounded-lg font-medium text-white mr-2`}
                      >
                        <p>{t.label}</p>

                        <button
                          onClick={t.onRemove}
                          className="hover:opacity-90 font-semibold cursor-pointer"
                        >
                          <X className="w-4 h-4" aria-hidden />
                        </button>
                      </div>
                    ))}
              </div>
            </div>
            {showLoader ? (
              <div className="absolute inset-0 grid place-items-center bg-white/60 backdrop-blur-sm">
                <div className="flex flex-col items-center gap-3">
                  <div className="rounded-2xl p-4 shadow-lg bg-white">
                    {/* 360° dönen kutu ikonu */}
                    <Box
                      className="animate-[spin_1.1s_linear_infinite]"
                      style={{ width: size, height: size }}
                    />
                  </div>
                  <p className="text-sm text-gray-600">Loading...</p>
                </div>
              </div>
            ) : (
              <>
                <ProductList products={showResults} />
              </>
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
