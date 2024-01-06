import {
  InventoryTransactionStatus,
  InventoryTransactionType,
} from "../../types/api-alias";

interface BadgesConfigInfo {
  name: string;
  color: string;
}

export const inventoryTransactionTypeBadges: Record<
  InventoryTransactionType,
  BadgesConfigInfo
> = {
  ADJUSTMENT: { color: "grape", name: "Adjustment" },
  PURCHASE_ORDER: { color: "teal", name: "Purchase Order" },
  SALES: { color: "orange", name: "Sales" },
};

export const inventoryTransactionStatusBadges: Record<
  InventoryTransactionStatus,
  BadgesConfigInfo
> = {
  DELETED: { color: "red", name: "Deleted" },
  PENDING: { color: "yellow", name: "Pending" },
  VALID: { color: "green", name: "Valid" },
};
