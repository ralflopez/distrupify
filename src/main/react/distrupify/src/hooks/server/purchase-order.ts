import { useMutation, useQuery } from "react-query";
import { queryClient } from "../../main";
import {
  PurchaseOrderCreateRequest,
  PurchaseOrderDTo,
} from "../../types/api-alias";
import { ApiNotification, handleResponse } from "./common";

const apiNotification = new ApiNotification("Purchase Order");

export const usePurchaseOrderCreateRequest = (
  token: string,
  cleanUp: () => void
) => {
  return useMutation(
    async (data: PurchaseOrderCreateRequest) => {
      const response = await fetch(
        "http://localhost:8080/api/v1/purchase-orders",
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
      onError: (e: Error) => {
        apiNotification.fail(e.message);
      },
      onSuccess: () => {
        apiNotification.success("Successfully recorded purchase order");
        queryClient.invalidateQueries(["products"]);
        cleanUp();
      },
    }
  );
};

export const usePurchaseOrderReceiveRequest = (
  token: string,
  cleanUp: () => void = () => {}
) => {
  return useMutation(
    async (transactionId: number) => {
      const response = await fetch(
        `http://localhost:8080/api/v1/purchase-orders/${transactionId}/receive`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
          method: "POST",
        }
      );
      return handleResponse(response);
    },
    {
      onError: (e: Error) => {
        apiNotification.fail(e.message);
      },
      onSuccess: () => {
        apiNotification.success("Successfully received purchase order");
        queryClient.invalidateQueries(["products"]);
        queryClient.invalidateQueries(["transactions"]);
        queryClient.invalidateQueries(["purchase-orders"]);
        cleanUp();
      },
    }
  );
};

export const usePurchaseOrderByTransactionIdRequest = (
  token: string,
  transactionId: number
) => {
  return useQuery<PurchaseOrderDTo, Error>(
    ["purchase-orders", "transactions", transactionId],
    async () => {
      const response = await fetch(
        `http://localhost:8080/api/v1/purchase-orders/transactions/${transactionId}`,
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
