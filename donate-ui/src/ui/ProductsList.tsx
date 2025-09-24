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
  category: {
    id: number;
    name: string;
  };
  imageUrl: string | null;
  categoryResponse: any;
}

interface ProductListProps {
  products: ProductItem[];
}

const ProductList: React.FC<ProductListProps> = ({ products }) => {
  return (
    <div className="2xl:col-span-9">
      <div
        className="grid grid-cols-1 md:grid-cols-2 [&.gridView]:grid-cols-1 xl:grid-cols-4 group [&.gridView]:xl:grid-cols-1 gap-x-5"
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
              expiryDate={item.expiryDate}
            />
          );
        })}
      </div>
    </div>
  );
};

export default ProductList;
