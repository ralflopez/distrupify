import { notifications } from "@mantine/notifications";
import { useQuery } from "react-query";
import { ProductDTO } from "../../types/dto";
import { InventoryLogType } from "../../types/requests";
import { _getAuthHeader, _handleResponse } from "../server";

export type InventoryLogsDTO = {
  id: number;
  quantity: number;
  price: number;
  inventoryLogType: InventoryLogType;
  timestamp: string;
  product: ProductDTO | null;
};

export type InventoryTransactionDTO = {
  id: number;
  inventoryTransactionType: string;
  timestamp: string;
  inventoryTransactionLogs: InventoryLogsDTO[] | null;
};

export type InventoryAdjustmentDTO = {
  id: number;
  createdAt: string;
  inventoryTransaction: InventoryTransactionDTO;
};

export type InventoryAdjustmentResponse = {
  inventoryAdjustments: InventoryAdjustmentDTO[];
  pageCount: number;
};

export const useInventoryAdjustments = (
  token: string,
  page: number,
  pageSize: number
) => {
  return useQuery<InventoryAdjustmentResponse, Error>(
    ["inventoryAdjustments", page],
    async () => {
      const response = await fetch(
        `http://localhost:8080/api/v1/inventory/adjustments?page=${page}&pageSize=${pageSize}`,
        {
          method: "GET",
          headers: {
            Authorization: _getAuthHeader(token),
          },
        }
      );

      return _handleResponse(response);
    },
    {
      onError: (e: Error) => {
        notifications.show({
          title: "Inventory Adjustments",
          message: e.message,
          color: "red",
        });
      },

      keepPreviousData: true,
    }
  );
};
