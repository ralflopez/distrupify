import { useMutation, useQuery } from "react-query";
import { queryClient } from "../../main";
import {
  ProductCreateRequest,
  ProductEditRequest,
  ProductsResponse,
} from "../../types/api-alias";
import { ApiNotification, handleResponse } from "./common";

const apiNotification = new ApiNotification("Products");

export const useProductsRequest = (
  token: string,
  page: number,
  pageSize: number,
  searchString?: string,
  filterBy?: "NAME" | "SKU"
) => {
  return useQuery<ProductsResponse, Error>(
    ["products", searchString, filterBy, page, pageSize],
    async () => {
      const searchStringQuery = searchString ? `&search=${searchString}` : "";
      const filterByQuery =
        searchString && filterBy ? `&filter_by=${filterBy}` : "";

      const response = await fetch(
        `http://localhost:8080/api/v1/products?page=${page}&page_size=${pageSize}${searchStringQuery}${filterByQuery}`,
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

export const useProductCreateRequest = (token: string, cleanUp: () => void) => {
  return useMutation(
    async (data: ProductCreateRequest) => {
      const response = await fetch("http://localhost:8080/api/v1/products", {
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
        apiNotification.success("Successfully created product");
        queryClient.invalidateQueries(["products"]);
        cleanUp();
      },
    }
  );
};

export const useProductEditRequest = (
  productId: number,
  token: string,
  cleanUp: () => void
) => {
  return useMutation(
    async (data: ProductEditRequest) => {
      const response = await fetch(
        `http://localhost:8080/api/v1/products/${productId}`,
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
        apiNotification.success("Successfully edited product");
        queryClient.invalidateQueries(["products"]);
        cleanUp();
      },
    }
  );
};

export const useProductDeleteRequest = (token: string, cleanUp: () => void) => {
  return useMutation(
    async (id: number) => {
      const response = await fetch(
        `http://localhost:8080/api/v1/products/${id}`,
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
        apiNotification.success("Successfully deleted product");
        queryClient.invalidateQueries(["products"]);
        cleanUp();
      },
    }
  );
};
