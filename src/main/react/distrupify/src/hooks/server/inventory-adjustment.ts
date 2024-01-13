import { useMutation, useQuery } from "react-query";
import { queryClient } from "../../main";
import {
  InventoryAdjustmentCreateRequest,
  InventoryAdjustmentResponse,
} from "../../types/api-alias";
import { ApiNotification, handleResponse } from "./common";

const apiNotification = new ApiNotification("Stock Adjustment");

export const useInventoryAdjustmentCreateRequest = (
  token: string,
  cleanUp: () => void
) => {
  return useMutation(
    async (data: InventoryAdjustmentCreateRequest) => {
      const response = await fetch(
        "http://localhost:8080/api/v1/inventory/adjustments",
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
          method: "POST",
          body: JSON.stringify(data),
        }
      );
      return handleResponse(response);
    },
    {
      onError: apiNotification.onError,
      onSuccess: () => {
        apiNotification.success("Successfully adjusted inventory items");
        queryClient.invalidateQueries(["products"]);
        queryClient.invalidateQueries(["inventoryAdjustments"]);
        cleanUp();
      },
    }
  );
};

export const useInventoryAdjustments = (
  token: string,
  page: number,
  pageSize: number
) => {
  return useQuery<InventoryAdjustmentResponse, Error>(
    ["inventoryAdjustments", page, pageSize],
    async () => {
      const response = await fetch(
        `http://localhost:8080/api/v1/inventory/adjustments?page=${page}&pageSize=${pageSize}`,
        {
          method: "GET",
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      return handleResponse(response);
    },
    {
      onError: apiNotification.onError,
      keepPreviousData: true,
    }
  );
};
