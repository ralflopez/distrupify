import {
  Card,
  Center,
  Group,
  List,
  Loader,
  Pagination,
  ScrollArea,
  Table,
  Text,
} from "@mantine/core";
import React, { useState } from "react";
import { useInventoryAdjustments } from "../../hooks/server/inventory-adjustment";
import { getItemNumber, getProductDisplayName } from "../../utils/display";
import { token } from "../../utils/token";

export const TransactionLogs = () => {
  const [page, setPage] = useState(1);
  const [pageSize] = useState(30);
  const { data, isLoading } = useInventoryAdjustments(token, page, pageSize);

  return (
    <>
      <Card withBorder mt="sm">
        <ScrollArea offsetScrollbars>
          <Table striped>
            <Table.Thead>
              <Table.Tr>
                <Table.Th c="dimmed">#</Table.Th>
                <Table.Th miw={500}>Datetime</Table.Th>
                <Table.Th>Items</Table.Th>
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
              {data?.inventoryAdjustments.map((data, i) => (
                <Table.Tr key={data.id}>
                  <Table.Td c="dimmed" style={{ verticalAlign: "top" }}>
                    {getItemNumber(i, page, pageSize)}
                  </Table.Td>
                  <Table.Td style={{ verticalAlign: "top" }}>
                    {data.createdAt}
                  </Table.Td>
                  <Table.Td style={{ verticalAlign: "top" }}>
                    <List>
                      {data.inventoryTransaction.inventoryTransactionLogs?.map(
                        (l) => (
                          <React.Fragment key={l.id}>
                            <Text>
                              {l.inventoryLogType === "INCOMING" ? (
                                <Text span c="green" mr="sm">
                                  +{l.quantity}
                                </Text>
                              ) : (
                                <Text span c="red" mr="sm">
                                  -{l.quantity}
                                </Text>
                              )}
                              {l.product
                                ? getProductDisplayName(l.product)
                                : null}
                            </Text>
                          </React.Fragment>
                        )
                      )}
                    </List>
                  </Table.Td>
                </Table.Tr>
              ))}
            </Table.Tbody>
          </Table>
        </ScrollArea>
      </Card>
      <Group mt="sm">
        <Pagination
          disabled={(data?.pageCount || 0) < 2}
          variant="light"
          value={page}
          onChange={setPage}
          total={data?.pageCount || 0}
        />
      </Group>
    </>
  );
};
