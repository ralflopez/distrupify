import { useMutation } from "react-query";
import { queryClient } from "../../main";
import { PurchaseOrderCreateRequest } from "../../types/api-alias";
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
