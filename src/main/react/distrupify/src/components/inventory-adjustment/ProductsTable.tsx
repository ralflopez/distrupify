import {
  Button,
  Center,
  Loader,
  NumberFormatter,
  Pagination,
  Space,
  Table,
} from "@mantine/core";
import { UseFormReturnType } from "@mantine/form";
import React, { useState } from "react";
import { useProductsRequest } from "../../hooks/server";
import { ProductDTO } from "../../types/dto";
import { InventoryAdjustmentCreateRequestDomainItem } from "../../types/requests";
import { getProductDisplayName } from "../../utils/display";
import { token } from "../../utils/token";

interface Props {
  form: UseFormReturnType<{
    items: InventoryAdjustmentCreateRequestDomainItem[];
  }>;
}

export const ProductsTable = ({ form }: Props) => {
  const [page, setPage] = useState(1);
  const { isLoading, data } = useProductsRequest(token, page);

  function isProductAdded(product: ProductDTO) {
    return !!form.values.items.find((i) => i.product.id === product.id);
  }

  function addItem(product: ProductDTO) {
    if (isProductAdded(product)) {
      return;
    }

    form.insertListItem("items", {
      inventoryLogType: "INCOMING",
      product,
      quantity: 1,
    } as InventoryAdjustmentCreateRequestDomainItem);
  }

  const row = (element: ProductDTO) => (
    <React.Fragment>
      <Table.Td>{element.sku}</Table.Td>
      <Table.Td>{getProductDisplayName(element)}</Table.Td>
      <Table.Td>
        <NumberFormatter
          prefix="₱ "
          value={element.unitPrice}
          thousandSeparator
          decimalScale={2}
          fixedDecimalScale
        />
      </Table.Td>
      <Table.Td>{element.quantity}</Table.Td>
    </React.Fragment>
  );

  return (
    <>
      <Table>
        <Table.Thead>
          <Table.Tr>
            <Table.Th>SKU</Table.Th>
            <Table.Th>Name</Table.Th>
            <Table.Th>Unit Price (PHP)</Table.Th>
            <Table.Th>Stock</Table.Th>
            <Table.Th></Table.Th>
          </Table.Tr>
        </Table.Thead>
        <Table.Tbody>
          {isLoading ? (
            <Table.Tr>
              <Table.Td colSpan={5}>
                <Center w="100%">
                  <Loader m="md" />
                </Center>
              </Table.Td>
            </Table.Tr>
          ) : null}

          {data?.products.map((p) => (
            <Table.Tr key={p.id}>
              {row(p)}
              <Table.Td>
                <Button
                  disabled={isProductAdded(p)}
                  variant="default"
                  onClick={() => addItem(p)}
                >
                  {isProductAdded(p) ? "Added" : "Add"}
                </Button>
              </Table.Td>
            </Table.Tr>
          ))}
        </Table.Tbody>
      </Table>
      <Space h="lg" />
      <Pagination
        disabled={(data?.pageCount || 0) < 2}
        variant="light"
        value={page}
        onChange={setPage}
        total={data?.pageCount || 0}
      />
    </>
  );
};
