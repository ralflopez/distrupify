import {
  Box,
  Button,
  Card,
  Center,
  Drawer,
  Flex,
  Group,
  Indicator,
  Input,
  Loader,
  NumberFormatter,
  Pagination,
  ScrollArea,
  Table,
} from "@mantine/core";
import { UseFormReturnType } from "@mantine/form";
import { useDisclosure } from "@mantine/hooks";
import { IconArrowNarrowRight, IconSearch } from "@tabler/icons-react";
import React, { useState } from "react";
import { useProductsRequest } from "../../hooks/server";
import { ProductDTO } from "../../types/dto";
import { InventoryAdjustmentCreateRequestDomainItem } from "../../types/requests";
import { getItemNumber, getProductDisplayName } from "../../utils/display";
import { token } from "../../utils/token";
import { ItemsSection } from "./ItemsSection";

interface Props {
  form: UseFormReturnType<{
    items: InventoryAdjustmentCreateRequestDomainItem[];
  }>;
}

export const ProductsTable = ({ form }: Props) => {
  const [page, setPage] = useState(1);
  const [pageSize] = useState(5);

  const [
    itemsDrawerOpened,
    { open: openItemsDrawer, close: closeItemsDrawer },
  ] = useDisclosure(false);
  const { isLoading, data } = useProductsRequest(token, page, pageSize);

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
          value={1000000}
          thousandSeparator
          decimalScale={2}
          fixedDecimalScale
        />
      </Table.Td>
      <Table.Td>{element.quantity}</Table.Td>
    </React.Fragment>
  );

  return (
    <Box>
      <Flex my="sm" justify="space-between" align="center" gap="xs">
        <Input
          w={350}
          placeholder="Search Name"
          leftSection={<IconSearch size={16} />}
        />
        <Drawer
          opened={itemsDrawerOpened}
          onClose={closeItemsDrawer}
          position="right"
          overlayProps={{ backgroundOpacity: 0.5, blur: 4 }}
          styles={{
            body: {
              height: "calc(100% - 60px)",
            },
          }}
        >
          <ItemsSection form={form} />
        </Drawer>
        <Group>
          <Indicator
            position="top-start"
            color="red"
            disabled={!form.values.items.length}
          >
            <Button
              onClick={openItemsDrawer}
              rightSection={<IconArrowNarrowRight size={16} />}
            >
              Next ({form.values.items.length})
            </Button>
          </Indicator>
        </Group>
      </Flex>
      <Card withBorder>
        <ScrollArea offsetScrollbars type="auto">
          <Table stickyHeader striped>
            <Table.Thead>
              <Table.Tr>
                <Table.Th c="dimmed">#</Table.Th>
                <Table.Th miw={150}>SKU</Table.Th>
                <Table.Th miw={250}>Name</Table.Th>
                <Table.Th miw={120}>Unit Price (PHP)</Table.Th>
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
              {data?.products.map((p, i) => (
                <Table.Tr key={p.id}>
                  <Table.Td c="dimmed">
                    {getItemNumber(i, page, pageSize)}
                  </Table.Td>
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
  );
};
