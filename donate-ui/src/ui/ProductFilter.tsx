import FilterElements from "./FilterElements";
import FilterElementsContent from "./FilterElementsContent";

const ProductFilter = () => {
  return (
    <div className="hidden bg-gray-50 2xl:col-span-3 2xl:block p-15">
      <div className="card">
        <div className="card-body">
          <div className="flex items-center gap-3">
            <h6 className="text-15 grow">Filter</h6>
            <div className="shrink-0">
              <button className="underline transition-all duration-200 ease-linear hover:text-custom-500">
                Clear All
              </button>
            </div>
          </div>
          <FilterElements title="Price">
            <FilterElementsContent labelText="All" inputId="priceAll" />
            <FilterElementsContent labelText="0.00 - 50.00" inputId="price1" />
            <FilterElementsContent
              labelText="50.00 - 100.00"
              inputId="price2"
            />
            <FilterElementsContent labelText="100.00+" inputId="price3" />
          </FilterElements>
          <FilterElements title="Category">
            <FilterElementsContent labelText="All" inputId="priceAll" />
            <FilterElementsContent labelText="Icecek" inputId="icecek" />
            <FilterElementsContent
              labelText="Süt Ürünleri"
              inputId="sütürünleri"
            />
            <FilterElementsContent labelText="Et - Tavuk" inputId="et" />
          </FilterElements>
        </div>
      </div>
    </div>
  );
};

export default ProductFilter;
