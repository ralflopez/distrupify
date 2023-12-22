import {
  Box,
  Button,
  Card,
  Drawer,
  Indicator,
  NumberFormatter,
  Table,
} from "@mantine/core";
import { UseFormReturnType } from "@mantine/form";
import { useDisclosure } from "@mantine/hooks";
import { IconArrowNarrowRight } from "@tabler/icons-react";
import { ProductDTO } from "../../types/dto";
import { InventoryAdjustmentCreateRequestDomainItem } from "../../types/requests";
import { getProductDisplayName } from "../../utils/display";
import { ProductsTable } from "../common/ProductsTable";
import { ItemsSection } from "./ItemsSection";

interface Props {
  form: UseFormReturnType<{
    items: InventoryAdjustmentCreateRequestDomainItem[];
  }>;
}

export const InventoryAdjustmentProductsTable = ({ form }: Props) => {
  const [
    itemsDrawerOpened,
    { open: openItemsDrawer, close: closeItemsDrawer },
  ] = useDisclosure(false);

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

  return (
    <ProductsTable
      action={() => (
        <Box display={{ base: "block", xl: "none" }}>
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
          <Card
            withBorder
            pos={{
              base: "fixed",
              md: "static",
            }}
            bottom={0}
            left={0}
            right={0}
            p={{ base: "var(--app-shell-padding)", md: 0 }}
            bg="white"
            style={{
              zIndex: 50,
              borderRight: "0",
              borderLeft: 0,
              borderRadius: 0,
              borderBottom: 0,
            }}
          >
            <Indicator
              w="100%"
              position="top-start"
              disabled={!form.values.items.length}
            >
              <Button
                fullWidth
                onClick={openItemsDrawer}
                rightSection={<IconArrowNarrowRight size={16} />}
              >
                Next ({form.values.items.length})
              </Button>
            </Indicator>
          </Card>
        </Box>
      )}
      thead={() => (
        <>
          <Table.Th miw={150}>SKU</Table.Th>
          <Table.Th miw={250}>Name</Table.Th>
          <Table.Th miw={120}>Unit Price (PHP)</Table.Th>
          <Table.Th>Stock</Table.Th>
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
          <Table.Td>{product.quantity}</Table.Td>
          <Table.Td>
            <Button
              disabled={isProductAdded(product)}
              variant="default"
              onClick={() => addItem(product)}
            >
              {isProductAdded(product) ? "Added" : "Add"}
            </Button>
          </Table.Td>
        </>
      )}
    />
  );
};
