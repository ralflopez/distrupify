import { notifications } from "@mantine/notifications";
import { useMutation } from "react-query";
import { queryClient } from "../../main";
import { _getAuthHeader, _handleResponse } from "../server";

export type ProductCreateRequest = {
  sku: string;
  brand: string;
  name: string;
  description: string;
  unitPrice: number;
};

export const useProductCreateRequest = (token: string, cleanUp: () => void) => {
  return useMutation(
    async (data: ProductCreateRequest) => {
      const response = await fetch("http://localhost:8080/api/v1/products", {
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
          title: "Product",
          message: e.message,
          color: "red",
        });
      },
      onSuccess: () => {
        notifications.show({
          title: "Product",
          message: "Successfully added product",
          color: "green",
        });
        queryClient.invalidateQueries(["products"]);
        cleanUp();
      },
    }
  );
};

export type ProductEditRequest = {
  sku: string;
  brand: string;
  name: string;
  description: string;
  unitPrice: number;
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
            Authorization: _getAuthHeader(token),
            "Content-Type": "application/json",
          },
          method: "PUT",
          body: JSON.stringify(data),
        }
      );
      return _handleResponse(response);
    },
    {
      onError: (e: Error) => {
        console.log("error");
        console.log(e.message);
        notifications.show({
          title: "Product",
          message: e.message,
          color: "red",
        });
      },
      onSuccess: () => {
        notifications.show({
          title: "Product",
          message: "Successfully edited product",
          color: "green",
        });
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
          title: "Product",
          message: e.message,
          color: "red",
        });
      },
      onSuccess: () => {
        notifications.show({
          title: "Product",
          message: "Successfully deleted product",
          color: "green",
        });
        queryClient.invalidateQueries(["products"]);
        cleanUp();
      },
    }
  );
};
