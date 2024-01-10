import { useQuery } from "react-query";
import { SuppliersResponse } from "../../types/api-alias";
import { ApiNotification, handleResponse } from "./common";

const apiNotification = new ApiNotification("Suppliers");

export const useSupplierAllRequest = (token: string) => {
  return useQuery<SuppliersResponse, Error>(
    ["suppliers"],
    async () => {
      const response = await fetch(`http://localhost:8080/api/v1/suppliers`, {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      return handleResponse(response);
    },
    {
      onError: apiNotification.onError,
      keepPreviousData: true,
    }
  );
};
