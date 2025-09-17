import { useId } from "react";

type Props = {
  searchTerm: string;
  setSearchTerm: (v: string) => void;
  donateProducts: boolean;
  setDonateProducts: (v: boolean) => void;
  className?: string;
};

export default function ProductFilter({
  searchTerm,
  setSearchTerm,
  donateProducts,
  setDonateProducts,
  className,
}: Props) {
  const searchId = useId();

  const clearAll = () => {
    setSearchTerm("");
    setDonateProducts(false);
  };

  return (
    <aside className={className ?? "sticky top-24"}>
      <div className="rounded-2xl border bg-white p-4 md:p-5 shadow-sm">
        <div className="mb-4 flex items-center justify-between">
          <h3 className="text-base font-semibold text-gray-800">Filters</h3>
          <button
            type="button"
            onClick={clearAll}
            className="text-xs font-medium text-gray-500 hover:text-gray-700"
          >
            Clear all
          </button>
        </div>

        <label
          htmlFor={searchId}
          className="block text-sm font-medium text-gray-700 mb-1"
        >
          Search
        </label>
        <div className="relative mb-5">
          <span className="pointer-events-none absolute inset-y-0 left-3 flex items-center text-gray-400">
            🔎
          </span>
          <input
            id={searchId}
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            placeholder="Search product"
            className="w-full rounded-xl border border-gray-400 bg-gray-50 pl-9 pr-9 py-2.5 text-sm outline-none
                       focus:border-gray-800 focus:bg-white transition"
          />
          {searchTerm && (
            <button
              type="button"
              onClick={() => setSearchTerm("")}
              className="absolute inset-y-0 right-2 my-auto h-7 w-7 rounded-md text-gray-400 hover:text-gray-600"
              aria-label="Clear search"
            >
              ×
            </button>
          )}
        </div>

        <div className="mb-1">
          <label className="flex items-center gap-3 cursor-pointer select-none">
            <span className="relative inline-flex h-5 w-9 items-center">
              <input
                type="checkbox"
                checked={donateProducts}
                onChange={(e) => setDonateProducts(e.target.checked)}
                className="peer sr-only"
              />
              <span className="h-5 w-9 rounded-full bg-gray-200 peer-checked:bg-emerald-500 transition" />
              <span
                className="absolute left-0.5 top-0.5 h-4 w-4 rounded-full bg-white shadow
                               transition peer-checked:translate-x-4"
              />
            </span>
            <span className="text-sm font-medium text-gray-800">
              Donate products
            </span>
          </label>
          <p className="mt-1 text-xs text-gray-500">
            Only show products available for donation.
          </p>
        </div>
      </div>
    </aside>
  );
}
