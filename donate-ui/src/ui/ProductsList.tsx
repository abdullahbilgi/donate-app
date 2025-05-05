import ProductItem from "./ProductItem";

interface ProductItem {
  id: number;
  name: string;
  productionDate: string;
  expiryDate: string;
  price: number;
  discountedPrice: number;
  discount: number;
  quantity: number;
  description: string;
  productStatus: "DISCOUNT" | "NORMAL" | string;
  category: {
    id: number;
    name: string;
  };
  imageUrl: string | null;
  categoryResponse: any; // Gerekirse daha sonra detaylandırılır
}

interface ProductListProps {
  products: ProductItem[];
}

const ProductList: React.FC<ProductListProps> = ({ products }) => {
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
        {products.map((item) => {
          return (
            <ProductItem
              key={item.id}
              id={item.id}
              image={item.imageUrl}
              label={item.name}
              normalPrice={item.price}
              discountPrice={item.discountedPrice}
            />
          );
        })}
      </div>
    </div>
  );
};

export default ProductList;
