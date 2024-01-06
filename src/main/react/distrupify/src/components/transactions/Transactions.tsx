import {
  Badge,
  Box,
  Card,
  Center,
  Flex,
  Group,
  Loader,
  NumberFormatter,
  Pagination,
  ScrollArea,
  Table,
  Tabs,
  Text,
  Title,
  rem,
} from "@mantine/core";
import { IconList } from "@tabler/icons-react";
import { useState } from "react";
import { useInventoryTransactionsResponse } from "../../hooks/server/transactions";
import { InventoryTransactionDTO } from "../../types/api-alias";
import { getItemNumber } from "../../utils/display";
import { token } from "../../utils/token";
import { TransactionTableActions } from "./TransactionTableActions";
import {
  inventoryTransactionStatusBadges,
  inventoryTransactionTypeBadges,
} from "./badgesConfig";

const trow = (
  transaction: InventoryTransactionDTO,
  index: number,
  page: number,
  pageSize: number
) => {
  const typeBadge =
    inventoryTransactionTypeBadges[transaction.inventoryTransactionType];
  const statusBadge = inventoryTransactionStatusBadges[transaction.status];

  return (
    <>
      <Table.Td>{getItemNumber(index, page, pageSize)}</Table.Td>
      <Table.Td>{transaction.timestamp}</Table.Td>
      <Table.Td>
        <Badge variant="light" color={typeBadge.color}>
          {typeBadge.name}
        </Badge>
      </Table.Td>
      <Table.Td>
        <Badge variant="light" color={statusBadge.color}>
          {statusBadge.name}
        </Badge>
      </Table.Td>
      <Table.Td ta="right">
        <Text c={transaction.value < 0 ? "red" : "green"}>
          <NumberFormatter
            prefix="₱ "
            value={transaction.value}
            thousandSeparator
            decimalScale={2}
            fixedDecimalScale
          />
        </Text>
      </Table.Td>
      <Table.Td ta="center">
        <TransactionTableActions transaction={transaction} />
      </Table.Td>
    </>
  );
};

export const Transactions = () => {
  const iconStyle = { width: rem(12), height: rem(12) };
  const [page, setPage] = useState(1);
  const [pageSize] = useState(100);
  const { data, isLoading } = useInventoryTransactionsResponse(
    token,
    page,
    pageSize
  );

  return (
    <Flex direction="column" w="100%">
      <Tabs
        defaultValue="list"
        h="100%"
        display="flex"
        style={{ flexDirection: "column" }}
      >
        <Box mb="lg">
          <Title order={1}>Transactions</Title>
          <Text size="sm" c="dimmed">
            View all actions for adding and removing items from the inventory
          </Text>
        </Box>

        <Tabs.List>
          <Tabs.Tab value="list" leftSection={<IconList style={iconStyle} />}>
            List
          </Tabs.Tab>
        </Tabs.List>

        <Tabs.Panel
          value="list"
          h="100%"
          style={{ overflow: "auto" }}
          pb={{ base: 50, md: 0 }}
        >
          <Box>
            <Flex
              my="sm"
              justify="space-between"
              align="center"
              gap="xs"
              wrap="wrap"
            ></Flex>
            <Card withBorder>
              <ScrollArea offsetScrollbars type="auto">
                <Table stickyHeader striped>
                  <Table.Thead>
                    <Table.Tr>
                      <Table.Th c="dimmed">#</Table.Th>
                      <Table.Th c="dimmed">Timestamp</Table.Th>
                      <Table.Th c="dimmed">Type</Table.Th>
                      <Table.Th c="dimmed">Status</Table.Th>
                      <Table.Th c="dimmed" ta="right">
                        Value
                      </Table.Th>
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
                    {data?.transactions.map((transaction, index) => (
                      <Table.Tr>
                        {trow(transaction, index, page, pageSize)}
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
          </Box>
        </Tabs.Panel>
      </Tabs>
    </Flex>
  );
};
