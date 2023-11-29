import { ProductDTO } from "./dto";

export type WebExceptionResponse = {
  status: number;
  message: string;
};

export type ProductsResponse = {
  products: ProductDTO[];
  pageCount: number;
};
