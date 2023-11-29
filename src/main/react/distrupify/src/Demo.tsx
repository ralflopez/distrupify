import { Button, Pagination, Table } from "@mantine/core";
import { useState } from "react";
import { useProductsRequest } from "./hooks/server";
import { ProductDTO } from "./types/dto";

const elements: ProductDTO[] = [
  {
    id: 0,
    sku: "1234",
    brand: "Head",
    name: "Radical MP",
    description: "Red",
    unitPrice: 15000,
    quantity: 0,
  },
  {
    id: 1,
    sku: "1235",
    brand: "Head",
    name: "Gravity MP",
    description: "Red",
    unitPrice: 15000,
    quantity: 0,
  },
];

export function Demo() {
  const [page, setPage] = useState(1);
  const token =
    "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwczovL2Rpc3RydXBpZnkuY29tIiwidXBuIjoidGVzdC11c2VyQGVtYWlsLmNvbSIsImdyb3VwcyI6W10sIm9yZ2FuaXphdGlvbl9pZCI6MSwiaWF0IjoxNzAxMjcwMzc4LCJleHAiOjE3MDEyNzA2NzgsImp0aSI6ImYxZDIxMjhlLTU1ODUtNGNiNS04NzFkLWU4MmE4Yzg2NDQ4OSJ9.ZHkuE4M7MxSQtYLNPmavunjtBFMoYnebzCJVP0bTu4NpbgA5_53tmG3x0neM-9Tv00Ar7Km8i85Ky9drJgxOckwH65Wz9n41T7DM90498HHeAZu6OiZ3Q7t51UtLPDaOp0CXBos73tllwEO-B23MHeDOKJiE_AXfINV7-1nkLnRZywi-jqBKwtmeGf2eVLXIPwbXYMysF68oeprVBxx8qvUWbIsICAdFMvCrX4j2Ib03XOlxHp7RHMmdyOWeg1thbN7JHm9YW5o2L5gUGWMRRZVccJFsq8P5twpIwQ2m0hrLYcslVstf_w4u48D603i7rX7PQm98l8GjWpIFgHUCVg";
  const { isLoading, isError, error, data } = useProductsRequest(token, page);

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (isError) {
    return <div>{error.message}</div>;
  }

  const rows = data?.products.map((element) => (
    <Table.Tr key={element.id}>
      <Table.Td>{element.sku}</Table.Td>
      <Table.Td>
        {element.brand} {element.name} {element.description}
      </Table.Td>
      <Table.Td>{element.unitPrice}</Table.Td>
      <Table.Td>{element.quantity}</Table.Td>
      <Table.Td>
        <Button variant="default">More</Button>
      </Table.Td>
    </Table.Tr>
  ));

  return (
    <>
      <Table stickyHeader>
        <Table.Thead>
          <Table.Tr>
            <Table.Th>SKU</Table.Th>
            <Table.Th>Name</Table.Th>
            <Table.Th>Unit Price (PHP)</Table.Th>
            <Table.Th>Stock</Table.Th>
            <Table.Th></Table.Th>
          </Table.Tr>
        </Table.Thead>
        <Table.Tbody>{rows}</Table.Tbody>
      </Table>
      <Pagination
        value={page}
        onChange={setPage}
        total={data?.pageCount || 0}
      />
      ;
    </>
  );
}
