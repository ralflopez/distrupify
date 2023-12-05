import { ProductDTO } from "../types/dto";

export const getProductDisplayName = (product: ProductDTO): string =>
  `${product.brand} ${product.name} ${product.description}`;

export const getItemNumber = (
  index: number,
  page: number,
  perPage: number
): number => {
  return index + 1 + perPage * (page - 1);
};
