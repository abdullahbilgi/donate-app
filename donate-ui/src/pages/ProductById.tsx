import { useEffect } from "react";
import { useAppDispatch, useAppSelector } from "../store";
import { useParams } from "react-router";
import { getProductById } from "../store/ProductStore/GetProductById/thunks";

const ProductById = () => {
  const dispatch = useAppDispatch();

  const { productById } = useAppSelector((state: any) => state.GetProduct);

  const params = useParams<{ id: string }>();
  console.log(params.id);
  useEffect(() => {
    dispatch(getProductById(params.id));
  }, [dispatch, params.id]);

  console.log(productById);
  return <div></div>;
};

export default ProductById;
