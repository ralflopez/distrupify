import { useMutation } from "react-query";
import { queryClient } from "../../main";
import { SalesCreateRequest } from "../../types/api-alias";
import { ApiNotification, handleResponse } from "./common";

const apiNotification = new ApiNotification("Sales");

export const useSalesCreateRequest = (token: string, cleanUp: () => void) => {
  return useMutation(
    async (data: SalesCreateRequest) => {
      const response = await fetch("http://localhost:8080/api/v1/sales", {
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
        apiNotification.success("Successfully recorded sales");
        queryClient.invalidateQueries(["products"]);
        queryClient.invalidateQueries(["sales"]);
        cleanUp();
      },
    }
  );
};
