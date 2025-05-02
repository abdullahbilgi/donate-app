import ProductItem from "./ProductItem";

const DUMMY_ITEM = [
  {
    id: 1,
    image: "/public/images/donation.jpg",
    label: "Ülker Yarim Yagli Süt",
    normalPrice: "780",
    discountPrice: "650",
  },
  {
    id: 2,
    image: "/public/images/donation.jpg",
    label: "Ürün2",
    normalPrice: "452",
    discountPrice: "380",
  },
  {
    id: 3,
    image: "/public/images/donation.jpg",
    label: "Ürün3",
    normalPrice: "754",
    discountPrice: "557",
  },
  {
    id: 4,
    image: "/public/images/donation.jpg",
    label: "Ürün4",
    normalPrice: "584",
    discountPrice: "400",
  },
];

const ProductList = () => {
  return (
    <div className="2xl:col-span-9">
      <div className="flex flex-wrap items-center gap-2">
        <p className="grow">
          Showing All <b>545</b> items results
        </p>
      </div>
      <div
        className="grid grid-cols-1 mt-5 md:grid-cols-2 [&.gridView]:grid-cols-1 xl:grid-cols-4 group [&.gridView]:xl:grid-cols-1 gap-x-5"
        id="cardGridView"
      >
        {DUMMY_ITEM.map((item) => {
          return (
            <ProductItem
              key={item.id}
              id={item.id}
              image={item.image}
              label={item.label}
              normalPrice={item.normalPrice}
              discountPrice={item.discountPrice}
            />
          );
        })}
      </div>
    </div>
  );
};

export default ProductList;
