import { components } from "./api";

export type ProductDTO = components["schemas"]["ProductDTO"];
export type ProductCreateRequest =
  components["schemas"]["ProductCreateRequest"];
export type ProductEditRequest = components["schemas"]["ProductEditRequest"];
export type ProductsResponse = components["schemas"]["ProductsResponse"];

export type InventoryLogType = components["schemas"]["InventoryLogType"];

export type InventoryAdjustmentResponse =
  components["schemas"]["InventoryAdjustmentResponse"];
export type InventoryAdjustmentCreateRequest =
  components["schemas"]["InventoryAdjustmentCreateRequest"];
export type InventoryAdjustmentCreateRequestItem =
  components["schemas"]["InventoryAdjustmentCreateRequestItem"];
export type InventoryAdjustmentCreateRequestItemWithProduct = Omit<
  InventoryAdjustmentCreateRequestItem,
  "productId"
> & { product: ProductDTO };

export type SalesCreateRequest = components["schemas"]["SalesCreateRequest"];
export type SalesCreateRequestItem =
  components["schemas"]["SalesCreateRequestItem"];
export type SalesCreateRequestItemWithProduct = Omit<
  SalesCreateRequestItem,
  "productId"
> & { product: ProductDTO };
