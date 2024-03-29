/**
 * This file was auto-generated by openapi-typescript.
 * Do not make direct changes to the file.
 */


export interface paths {
  "/api/v1/auth/login": {
    post: {
      requestBody?: {
        content: {
          "application/json": components["schemas"]["LoginRequest"];
        };
      };
      responses: {
        /** @description OK */
        200: {
          content: never;
        };
      };
    };
  };
  "/api/v1/auth/signup": {
    post: {
      requestBody?: {
        content: {
          "application/json": components["schemas"]["SignupRequest"];
        };
      };
      responses: {
        /** @description OK */
        200: {
          content: never;
        };
      };
    };
  };
  "/api/v1/auth/user": {
    get: {
      responses: {
        /** @description OK */
        200: {
          content: never;
        };
        /** @description Not Authorized */
        401: {
          content: never;
        };
        /** @description Not Allowed */
        403: {
          content: never;
        };
      };
    };
  };
  "/api/v1/inventory/adjustments": {
    get: {
      parameters: {
        query?: {
          page?: number;
          page_size?: number;
        };
      };
      responses: {
        /** @description Successful operation */
        200: {
          content: {
            "application/json": components["schemas"]["InventoryAdjustmentResponse"];
          };
        };
        /** @description Not Authorized */
        401: {
          content: never;
        };
        /** @description Not Allowed */
        403: {
          content: never;
        };
      };
    };
    post: {
      requestBody?: {
        content: {
          "application/json": components["schemas"]["InventoryAdjustmentCreateRequest"];
        };
      };
      responses: {
        /** @description OK */
        200: {
          content: never;
        };
        /** @description Not Authorized */
        401: {
          content: never;
        };
        /** @description Not Allowed */
        403: {
          content: never;
        };
      };
    };
  };
  "/api/v1/inventory/adjustments/transactions/{transactionId}": {
    get: {
      responses: {
        /** @description OK */
        200: {
          content: never;
        };
        /** @description Not Authorized */
        401: {
          content: never;
        };
        /** @description Not Allowed */
        403: {
          content: never;
        };
      };
    };
  };
  "/api/v1/inventory/transactions": {
    get: {
      parameters: {
        query?: {
          asc?: boolean;
          end?: string;
          page?: number;
          page_size?: number;
          start?: string;
          status?: components["schemas"]["InventoryTransactionStatus"][];
          type?: components["schemas"]["InventoryTransactionType"][];
        };
      };
      responses: {
        /** @description Successful operation */
        200: {
          content: {
            "application/json": components["schemas"]["InventoryTransactionResponse"];
          };
        };
        /** @description Not Authorized */
        401: {
          content: never;
        };
        /** @description Not Allowed */
        403: {
          content: never;
        };
      };
    };
  };
  "/api/v1/inventory/transactions/all": {
    get: {
      responses: {
        /** @description Successful operation */
        200: {
          content: {
            "application/json": components["schemas"]["InventoryTransactionResponse"];
          };
        };
        /** @description Not Authorized */
        401: {
          content: never;
        };
        /** @description Not Allowed */
        403: {
          content: never;
        };
      };
    };
  };
  "/api/v1/inventory/transactions/{id}": {
    delete: {
      parameters: {
        path: {
          id: number;
        };
      };
      responses: {
        /** @description OK */
        200: {
          content: never;
        };
        /** @description Not Authorized */
        401: {
          content: never;
        };
        /** @description Not Allowed */
        403: {
          content: never;
        };
      };
    };
  };
  "/api/v1/products": {
    get: {
      parameters: {
        query?: {
          filter_by?: components["schemas"]["ProductSearchFilterBy"];
          page?: number;
          page_size?: number;
          search?: string;
        };
      };
      responses: {
        /** @description Successful operation */
        200: {
          content: {
            "application/json": components["schemas"]["ProductsResponse"];
          };
        };
        /** @description Not Authorized */
        401: {
          content: never;
        };
        /** @description Not Allowed */
        403: {
          content: never;
        };
      };
    };
    post: {
      requestBody?: {
        content: {
          "application/json": components["schemas"]["ProductCreateRequest"];
        };
      };
      responses: {
        /** @description OK */
        200: {
          content: never;
        };
        /** @description Not Authorized */
        401: {
          content: never;
        };
        /** @description Not Allowed */
        403: {
          content: never;
        };
      };
    };
  };
  "/api/v1/products/{id}": {
    put: {
      parameters: {
        path: {
          id: number;
        };
      };
      requestBody?: {
        content: {
          "application/json": components["schemas"]["ProductEditRequest"];
        };
      };
      responses: {
        /** @description OK */
        200: {
          content: never;
        };
        /** @description Not Authorized */
        401: {
          content: never;
        };
        /** @description Not Allowed */
        403: {
          content: never;
        };
      };
    };
    delete: {
      parameters: {
        path: {
          id: number;
        };
      };
      responses: {
        /** @description OK */
        200: {
          content: never;
        };
        /** @description Not Authorized */
        401: {
          content: never;
        };
        /** @description Not Allowed */
        403: {
          content: never;
        };
      };
    };
  };
  "/api/v1/purchase-orders": {
    post: {
      requestBody?: {
        content: {
          "application/json": components["schemas"]["PurchaseOrderCreateRequest"];
        };
      };
      responses: {
        /** @description OK */
        200: {
          content: never;
        };
        /** @description Not Authorized */
        401: {
          content: never;
        };
        /** @description Not Allowed */
        403: {
          content: never;
        };
      };
    };
  };
  "/api/v1/purchase-orders/transactions/{transactionId}": {
    get: {
      parameters: {
        path: {
          transactionId: number;
        };
      };
      responses: {
        /** @description Successful operation */
        200: {
          content: {
            "application/json": components["schemas"]["PurchaseOrderDTO"];
          };
        };
        /** @description Not Authorized */
        401: {
          content: never;
        };
        /** @description Not Allowed */
        403: {
          content: never;
        };
      };
    };
  };
  "/api/v1/purchase-orders/{id}/receive": {
    post: {
      parameters: {
        path: {
          id: number;
        };
      };
      responses: {
        /** @description OK */
        200: {
          content: never;
        };
        /** @description Not Authorized */
        401: {
          content: never;
        };
        /** @description Not Allowed */
        403: {
          content: never;
        };
      };
    };
  };
  "/api/v1/sales": {
    get: {
      parameters: {
        query?: {
          page?: number;
          page_size?: number;
        };
      };
      responses: {
        /** @description Successful operation */
        200: {
          content: {
            "application/json": components["schemas"]["SalesResponse"];
          };
        };
        /** @description Not Authorized */
        401: {
          content: never;
        };
        /** @description Not Allowed */
        403: {
          content: never;
        };
      };
    };
    post: {
      requestBody?: {
        content: {
          "application/json": components["schemas"]["SalesCreateRequest"];
        };
      };
      responses: {
        /** @description OK */
        200: {
          content: never;
        };
        /** @description Not Authorized */
        401: {
          content: never;
        };
        /** @description Not Allowed */
        403: {
          content: never;
        };
      };
    };
  };
  "/api/v1/sales/transactions/{transactionId}": {
    get: {
      parameters: {
        path: {
          transactionId: number;
        };
      };
      responses: {
        /** @description Successful operation */
        200: {
          content: {
            "application/json": components["schemas"]["SalesDTO"];
          };
        };
        /** @description Not Authorized */
        401: {
          content: never;
        };
        /** @description Not Allowed */
        403: {
          content: never;
        };
      };
    };
  };
  "/api/v1/sales/{id}": {
    delete: {
      parameters: {
        path: {
          id: number;
        };
      };
      responses: {
        /** @description OK */
        200: {
          content: never;
        };
        /** @description Not Authorized */
        401: {
          content: never;
        };
        /** @description Not Allowed */
        403: {
          content: never;
        };
      };
    };
  };
  "/api/v1/suppliers": {
    get: {
      parameters: {
        query?: {
          page?: number;
          page_size?: number;
        };
      };
      responses: {
        /** @description Successful operation */
        200: {
          content: {
            "application/json": components["schemas"]["SuppliersResponse"];
          };
        };
        /** @description Not Authorized */
        401: {
          content: never;
        };
        /** @description Not Allowed */
        403: {
          content: never;
        };
      };
    };
    post: {
      requestBody?: {
        content: {
          "application/json": components["schemas"]["SupplierCreateRequest"];
        };
      };
      responses: {
        /** @description OK */
        200: {
          content: never;
        };
        /** @description Not Authorized */
        401: {
          content: never;
        };
        /** @description Not Allowed */
        403: {
          content: never;
        };
      };
    };
  };
  "/api/v1/suppliers/{id}": {
    put: {
      parameters: {
        path: {
          id: number;
        };
      };
      requestBody?: {
        content: {
          "application/json": components["schemas"]["SupplierEditRequest"];
        };
      };
      responses: {
        /** @description OK */
        200: {
          content: never;
        };
        /** @description Not Authorized */
        401: {
          content: never;
        };
        /** @description Not Allowed */
        403: {
          content: never;
        };
      };
    };
    delete: {
      parameters: {
        path: {
          id: number;
        };
      };
      responses: {
        /** @description OK */
        200: {
          content: never;
        };
        /** @description Not Authorized */
        401: {
          content: never;
        };
        /** @description Not Allowed */
        403: {
          content: never;
        };
      };
    };
  };
}

export type webhooks = Record<string, never>;

export interface components {
  schemas: {
    /**
     * Format: date
     * @example "2022-03-10T00:00:00.000Z"
     */
    Date: string;
    InventoryAdjustmentCreateRequest: {
      items: components["schemas"]["InventoryAdjustmentCreateRequestItem"][];
    };
    InventoryAdjustmentCreateRequestItem: {
      /** Format: int64 */
      productId: number;
      /** Format: int32 */
      quantity: number;
      inventoryLogType: components["schemas"]["InventoryLogType"];
    };
    InventoryAdjustmentDTO: {
      /** Format: int64 */
      id: number;
      createdAt: string;
      inventoryTransaction: components["schemas"]["InventoryTransactionDTO"];
    };
    InventoryAdjustmentResponse: {
      inventoryAdjustments: components["schemas"]["InventoryAdjustmentDTO"][];
      /** Format: int32 */
      pageCount: number;
    };
    /** @enum {string} */
    InventoryLogType: "INCOMING" | "OUTGOING";
    InventoryTransactionDTO: {
      /** Format: int64 */
      id: number;
      inventoryTransactionType: components["schemas"]["InventoryTransactionType"];
      timestamp: string;
      items: components["schemas"]["TransactionItemDTO"][];
      status: components["schemas"]["InventoryTransactionStatus"];
      /** Format: double */
      value: number;
    };
    InventoryTransactionResponse: {
      transactions: components["schemas"]["InventoryTransactionDTO"][];
      /** Format: int32 */
      pageCount: number;
    };
    /** @enum {string} */
    InventoryTransactionStatus: "PENDING" | "DELETED" | "VALID";
    /** @enum {string} */
    InventoryTransactionType: "PURCHASE_ORDER" | "SALES" | "ADJUSTMENT";
    LoginRequest: {
      email: string;
      password: string;
    };
    ProductCreateRequest: {
      sku: string;
      brand: string;
      name: string;
      description: string;
      /** Format: float */
      unitPrice: number;
    };
    ProductDTO: {
      /** Format: int64 */
      id: number;
      sku: string;
      brand: string;
      name: string;
      description: string;
      /** Format: double */
      unitPrice: number;
      /** Format: int32 */
      quantity?: number;
    };
    ProductEditRequest: {
      sku: string;
      brand: string;
      name: string;
      description: string;
      /** Format: float */
      unitPrice: number;
    };
    /** @enum {string} */
    ProductSearchFilterBy: "NAME" | "SKU";
    ProductsResponse: {
      products: components["schemas"]["ProductDTO"][];
      /** Format: int32 */
      pageCount: number;
    };
    PurchaseOrderCreateRequest: {
      items: components["schemas"]["PurchaseOrderCreateRequestItem"][];
      /** Format: int64 */
      supplierId: number;
    };
    PurchaseOrderCreateRequestItem: {
      /** Format: int64 */
      productId: number;
      /** Format: int32 */
      quantity: number;
      /** Format: double */
      unitPrice: number;
    };
    PurchaseOrderDTO: {
      /** Format: int64 */
      id: number;
      createdAt: components["schemas"]["Date"];
      receivedAt?: components["schemas"]["Date"];
      supplier: components["schemas"]["SupplierDTO"];
    };
    SalesCreateRequest: {
      items: components["schemas"]["SalesCreateRequestItem"][];
      /** Format: int64 */
      customerId?: number;
    };
    SalesCreateRequestItem: {
      /** Format: int64 */
      productId: number;
      /** Format: int32 */
      quantity: number;
      /** Format: double */
      unitPrice: number;
    };
    SalesDTO: {
      /** Format: int64 */
      id: number;
      createdAt: components["schemas"]["Date"];
      /** Format: int64 */
      inventoryTransactionId: number;
      /** Format: int64 */
      customerId: number | null;
      /** Format: double */
      totalPrice: number;
    };
    SalesResponse: {
      sales: components["schemas"]["SalesDTO"][];
      /** Format: int32 */
      pageCount: number;
    };
    SignupRequest: {
      name: string;
      email: string;
      password: string;
      /** Format: int64 */
      organizationId: number;
    };
    SupplierCreateRequest: {
      name: string;
      address: string;
      contactNumber: string;
    };
    SupplierDTO: {
      /** Format: int64 */
      id: number;
      name: string;
      address: string;
      contactNumber: string;
    };
    SupplierEditRequest: {
      name: string;
      address: string;
      contactNumber: string;
    };
    SuppliersResponse: {
      suppliers: components["schemas"]["SupplierDTO"][];
      /** Format: int32 */
      pageCount: number;
    };
    TransactionItemDTO: {
      /** Format: int32 */
      quantity: number;
      /** Format: double */
      price: number;
      inventoryLogType: components["schemas"]["InventoryLogType"];
      product: components["schemas"]["ProductDTO"];
    };
  };
  responses: never;
  parameters: never;
  requestBodies: never;
  headers: never;
  pathItems: never;
}

export type $defs = Record<string, never>;

export type external = Record<string, never>;

export type operations = Record<string, never>;
