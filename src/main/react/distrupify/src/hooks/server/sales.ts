import { notifications } from "@mantine/notifications";
import { useMutation, useQuery } from "react-query";
import { queryClient } from "../../main";
import { _getAuthHeader, _handleResponse } from "../server";

export type SalesCreateRequestItem = {
  productId: number;
  quantity: number;
  unitPrice: number;
};

export type SalesCreateRequest = {
  items: SalesCreateRequestItem[];
  customerId?: number;
};

export const useSalesCreateRequest = (token: string, cleanUp: () => void) => {
  return useMutation(
    async (data: SalesCreateRequest) => {
      const response = await fetch("http://localhost:8080/api/v1/sales", {
        headers: {
          Authorization: _getAuthHeader(token),
          "Content-Type": "application/json",
        },
        method: "POST",
        body: JSON.stringify(data),
      });
      return _handleResponse(response);
    },
    {
      onError: (e: Error) => {
        notifications.show({
          title: "Sales",
          message: e.message,
          color: "red",
        });
      },
      onSuccess: () => {
        notifications.show({
          title: "Sales",
          message: "Successfully added sales",
          color: "green",
        });
        queryClient.invalidateQueries(["products"]);
        cleanUp();
      },
    }
  );
};

export type SalesDTO = {
  id: number;
  createdAt: string;
  organizationId: string;
  inventoryTransactionId: number;
  customerId?: number;
};

export type SalesResponse = {
  sales: [];
  pageCount: number;
};

export const useSalesRequest = (
  token: string,
  page: number,
  pageSize: number
) => {
  return useQuery<SalesResponse, Error>(
    ["sales", page, pageSize],
    async () => {
      const response = await fetch(
        `http://localhost:8080/api/v1/sales?page=${page}&page_size=${pageSize}`,
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
          title: "Sales",
          message: e.message,
          color: "red",
        });
      },

      keepPreviousData: true,
    }
  );
};

export const useSalesDeleteRequest = (token: string, cleanUp: () => void) => {
  return useMutation(
    async (salesId: number) => {
      const response = await fetch(
        `http://localhost:8080/api/v1/sales/${salesId}`,
        {
          headers: {
            Authorization: _getAuthHeader(token),
          },
          method: "DELETE",
        }
      );
      return _handleResponse(response);
    },
    {
      onError: (e: Error) => {
        notifications.show({
          title: "Sales",
          message: e.message,
          color: "red",
        });
      },
      onSuccess: () => {
        notifications.show({
          title: "Sales",
          message: "Successfully deleted sales",
          color: "green",
        });
        queryClient.invalidateQueries(["products"]);
        cleanUp();
      },
    }
  );
};
