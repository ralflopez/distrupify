export type ProductDTO = {
  id: number;
  sku: string;
  brand: string;
  name: string;
  description: string;
  unitPrice: number;
  quantity: number | null;
};
