import { useMutation, useQuery } from "react-query";
import { queryClient } from "../../main";
import { InventoryTransactionResponse } from "../../types/api-alias";
import { ApiNotification, handleResponse } from "./common";

const apiNotification = new ApiNotification("Transaction");

export const useInventoryTransactionsRequest = (
  token: string,
  page: number,
  pageSize: number
) => {
  return useQuery<InventoryTransactionResponse, Error>(
    ["transactions", page, pageSize],
    async () => {
      const response = await fetch(
        `http://localhost:8080/api/v1/inventory/transactions?page=${page}&page_size=${pageSize}`,
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

export const useTransactionDeleteRequest = (
  token: string,
  cleanUp: () => void
) => {
  return useMutation(
    async (id: number) => {
      const response = await fetch(
        `http://localhost:8080/api/v1/inventory/transactions/${id}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
          method: "DELETE",
        }
      );
      return handleResponse(response);
    },
    {
      onError: apiNotification.onError,
      onSuccess: () => {
        apiNotification.success("Successfully deleted transaction");
        queryClient.invalidateQueries(["products"]);
        queryClient.invalidateQueries(["transactions"]);
        cleanUp();
      },
    }
  );
};
