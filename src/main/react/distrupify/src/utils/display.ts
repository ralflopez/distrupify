import { ProductDTO } from "../types/dto";

export const getProductDisplayName = (product: ProductDTO) =>
  `${product.brand} ${product.name} ${product.description}`;
