import { notifications } from "@mantine/notifications";
import { useMutation, useQuery } from "react-query";
import { queryClient } from "../main";
import { InventoryAdjustmentCreateRequest } from "../types/requests";
import { ProductsResponse, WebExceptionResponse } from "../types/response";

export const useProductsRequest = (
  token: string,
  page: number,
  pageSize: number
) => {
  return useQuery<ProductsResponse, Error>(
    ["products", page],
    async () => {
      const response = await fetch(
        `http://localhost:8080/api/v1/products?page=${page}&page_size=${pageSize}`,
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
          title: "Products",
          message: e.message,
          color: "red",
        });
      },

      keepPreviousData: true,
    }
  );
};

export const useInventoryAdjustmentRequest = (
  token: string,
  cleanUp: () => void
) => {
  return useMutation(
    async (data: InventoryAdjustmentCreateRequest) => {
      const response = await fetch(
        "http://localhost:8080/api/v1/inventory/adjustments",
        {
          headers: {
            Authorization: _getAuthHeader(token),
            "Content-Type": "application/json",
          },
          method: "POST",
          body: JSON.stringify(data),
        }
      );
      return _handleResponse(response);
    },
    {
      onError: (e: Error) => {
        notifications.show({
          title: "Inventory Adjustment",
          message: e.message,
          color: "red",
        });
      },
      onSuccess: () => {
        notifications.show({
          title: "Inventory Adjustment",
          message: "Successfully adjusted inventory items",
          color: "green",
        });
        queryClient.invalidateQueries(["products"]);
        queryClient.invalidateQueries(["inventoryAdjustments"]);
        cleanUp();
      },
    }
  );
};

export function _getAuthHeader(token: string): string {
  return `Bearer ${token}`;
}

export async function _handleResponse(response: Response) {
  console.log(response);
  if (!response.ok) {
    let message = "";

    try {
      const body: WebExceptionResponse = await response.json();
      message = body.message;
    } catch (e) {
      message = `There was an error: ${response.statusText} (${response.status})`;
    }

    throw new Error(message);
  }

  try {
    const result = await response.json();
    return result;
  } catch (e) {
    return "";
  }
}
