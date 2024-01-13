import { components } from "./api";

export type ProductDTO = components["schemas"]["ProductDTO"];
export type ProductCreateRequest =
  components["schemas"]["ProductCreateRequest"];
export type ProductEditRequest = components["schemas"]["ProductEditRequest"];
export type ProductsResponse = components["schemas"]["ProductsResponse"];

export type InventoryLogType = components["schemas"]["InventoryLogType"];

export type InventoryAdjustmentDTO =
  components["schemas"]["InventoryAdjustmentDTO"];
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

export type SalesDTO = components["schemas"]["SalesDTO"];
export type SalesCreateRequest = components["schemas"]["SalesCreateRequest"];
export type SalesCreateRequestItem =
  components["schemas"]["SalesCreateRequestItem"];
export type SalesCreateRequestItemWithProduct = Omit<
  SalesCreateRequestItem,
  "productId"
> & { product: ProductDTO };

export type InventoryTransactionDTO =
  components["schemas"]["InventoryTransactionDTO"];
export type InventoryTransactionResponse =
  components["schemas"]["InventoryTransactionResponse"];
export type InventoryTransactionType =
  components["schemas"]["InventoryTransactionType"];
export type InventoryTransactionStatus =
  components["schemas"]["InventoryTransactionStatus"];

export type PurchaseOrderDTo = components["schemas"]["PurchaseOrderDTO"];
export type PurchaseOrderCreateRequest =
  components["schemas"]["PurchaseOrderCreateRequest"];
export type PurchaseOrderCreateRequestItem =
  components["schemas"]["PurchaseOrderCreateRequestItem"];
export type PurchaeOrderCreateRequestItemWithProduct = Omit<
  PurchaseOrderCreateRequestItem,
  "productId"
> & { product: ProductDTO };

export type SupplierDTO = components["schemas"]["SupplierDTO"];
export type SuppliersResponse = components["schemas"]["SuppliersResponse"];
export type SupplierCreateRequest =
  components["schemas"]["SupplierCreateRequest"];
export type SupplierEditRequest = components["schemas"]["SupplierEditRequest"];
