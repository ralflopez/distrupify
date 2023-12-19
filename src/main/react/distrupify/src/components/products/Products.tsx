import {
  Box,
  Flex,
  NumberFormatter,
  Table,
  Tabs,
  Text,
  Title,
  rem,
} from "@mantine/core";
import { IconList } from "@tabler/icons-react";
import { getProductDisplayName } from "../../utils/display";
import { ProductsTable } from "../common/ProductsTable";
import { ProductCreateButton } from "./ProductCreateButton";
import { ProductTableActions } from "./ProductTableActions";

export const Products = () => {
  const iconStyle = { width: rem(12), height: rem(12) };

  return (
    <Flex direction="column" w="100%">
      <Tabs
        defaultValue="list"
        h="100%"
        display="flex"
        style={{ flexDirection: "column" }}
      >
        <Box mb="lg">
          <Title order={1}>Products</Title>
          <Text size="sm" c="dimmed">
            Manage product information
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
          <ProductsTable
            action={() => <ProductCreateButton />}
            thead={() => (
              <>
                <Table.Th miw={150}>SKU</Table.Th>
                <Table.Th miw={250}>Name</Table.Th>
                <Table.Th miw={120}>Unit Price (PHP)</Table.Th>
                <Table.Th></Table.Th>
              </>
            )}
            trows={(product) => (
              <>
                <Table.Td>{product.sku}</Table.Td>
                <Table.Td>{getProductDisplayName(product)}</Table.Td>
                <Table.Td>
                  <NumberFormatter
                    prefix="₱ "
                    value={product.unitPrice}
                    thousandSeparator
                    decimalScale={2}
                    fixedDecimalScale
                  />
                </Table.Td>
                <Table.Td>
                  <ProductTableActions product={product} />
                </Table.Td>
              </>
            )}
          />
        </Tabs.Panel>
      </Tabs>
    </Flex>
  );
};
