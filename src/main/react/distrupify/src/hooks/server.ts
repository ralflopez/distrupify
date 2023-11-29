import { useQuery } from "react-query";
import { ProductsResponse, WebExceptionResponse } from "../types/response";

export const useProductsRequest = (token: string, page: number) => {
  return useQuery<ProductsResponse, Error>(
    ["products", page],
    async () => {
      const response = await fetch(
        `http://localhost:8080/api/v1/products?page=${page}`,
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
      keepPreviousData: true,
    }
  );
};

function _getAuthHeader(token: string): string {
  return `Bearer ${token}`;
}

async function _handleResponse(response: Response) {
  if (!response.ok) {
    if (response.status === 401) {
      throw new Error(response.statusText);
    }

    const body: WebExceptionResponse = await response.json();
    throw new Error(body.message);
  }

  return response.json();
}
