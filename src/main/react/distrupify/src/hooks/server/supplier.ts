import { useMutation, useQuery } from "react-query";
import { queryClient } from "../../main";
import {
  SupplierCreateRequest,
  SupplierEditRequest,
  SuppliersResponse,
} from "../../types/api-alias";
import { ApiNotification, handleResponse } from "./common";

const apiNotification = new ApiNotification("Suppliers");

export const useSuppliersRequest = (
  token: string,
  page?: number,
  pageSize?: number
) => {
  return useQuery<SuppliersResponse, Error>(
    ["suppliers"],
    async () => {
      const response = await fetch(
        `http://localhost:8080/api/v1/suppliers${page ? `&page=${page}` : ""}${
          pageSize ? `&page_size=${pageSize}` : ""
        }`,
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

export const useSupplierCreateRequest = (
  token: string,
  cleanUp: () => void
) => {
  return useMutation(
    async (data: SupplierCreateRequest) => {
      const response = await fetch("http://localhost:8080/api/v1/suppliers", {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        method: "POST",
        body: JSON.stringify(data),
      });
      return handleResponse(response);
    },
    {
      onError: apiNotification.onError,
      onSuccess: () => {
        apiNotification.success("Successfully created supplier");
        queryClient.invalidateQueries(["suppliers"]);
        cleanUp();
      },
    }
  );
};

export const useSupplierEditRequest = (
  productId: number,
  token: string,
  cleanUp: () => void
) => {
  return useMutation(
    async (data: SupplierEditRequest) => {
      const response = await fetch(
        `http://localhost:8080/api/v1/suppliers/${productId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
          method: "PUT",
          body: JSON.stringify(data),
        }
      );
      return handleResponse(response);
    },
    {
      onError: apiNotification.onError,
      onSuccess: () => {
        apiNotification.success("Successfully edited supplier");
        queryClient.invalidateQueries(["suppliers"]);
        cleanUp();
      },
    }
  );
};

export const useSupplierDeleteRequest = (
  token: string,
  cleanUp: () => void = () => {}
) => {
  return useMutation(
    async (id: number) => {
      const response = await fetch(
        `http://localhost:8080/api/v1/suppliers/${id}`,
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
        apiNotification.success("Successfully deleted supplier");
        queryClient.invalidateQueries(["suppliers"]);
        cleanUp();
      },
    }
  );
};
