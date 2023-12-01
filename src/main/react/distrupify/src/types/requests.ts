import { ProductDTO } from "./dto";

export type InventoryLogType = "INCOMING" | "OUTGOING";

export type InventoryAdjustmentCreateRequestItem = {
  productId: number;
  quantity: number;
  inventoryLogType: InventoryLogType;
};

export type InventoryAdjustmentCreateRequestDomainItem = Omit<
  InventoryAdjustmentCreateRequestItem,
  "productId"
> & { product: ProductDTO };

export type InventoryAdjustmentCreateRequest = {
  items: InventoryAdjustmentCreateRequestItem[];
};
