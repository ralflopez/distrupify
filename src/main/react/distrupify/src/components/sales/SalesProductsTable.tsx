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
import {
  ProductDTO,
  SalesCreateRequestItemWithProduct,
} from "../../types/api-alias";
import { getProductDisplayName } from "../../utils/display";
import { ProductsTable } from "../common/ProductsTable";
import { ItemsSection } from "./ItemsSection";

interface Props {
  form: UseFormReturnType<{
    items: SalesCreateRequestItemWithProduct[];
  }>;
}

export const SalesProductsTable = ({ form }: Props) => {
  const [
    itemsDrawerOpened,
    { open: openItemsDrawer, close: closeItemsDrawer },
  ] = useDisclosure(false);

  function getProductStock(product: ProductDTO) {
    const existing = form.values.items.find((i) => i.product.id === product.id);
    const qty = product.quantity || 0;
    const added = existing?.quantity || 0;
    return qty - added;
  }

  function addItem(product: ProductDTO) {
    const items = [...form.values.items];

    const existing = items.find((i) => i.product.id === product.id);
    if (!existing) {
      form.insertListItem("items", {
        inventoryLogType: "INCOMING",
        product,
        quantity: 1,
        unitPrice: product.unitPrice,
      } as SalesCreateRequestItemWithProduct);
      return;
    }

    if (existing.quantity >= (product.quantity || 0)) {
      return;
    }

    existing.quantity += 1;
    form.setFieldValue("items", items);
    return;
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
            w="100%"
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
              color="red"
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
          <Table.Th miw={120} align="right">
            Unit Price (PHP)
          </Table.Th>
          <Table.Th align="right">Stock</Table.Th>
          <Table.Th></Table.Th>
        </>
      )}
      trows={(product) => (
        <>
          <Table.Td>{product.sku}</Table.Td>
          <Table.Td>{getProductDisplayName(product)}</Table.Td>
          <Table.Td ta="right">
            <NumberFormatter
              prefix="₱ "
              value={product.unitPrice}
              thousandSeparator
              decimalScale={2}
              fixedDecimalScale
            />
          </Table.Td>
          <Table.Td ta="right">{getProductStock(product)}</Table.Td>
          <Table.Td>
            <Button
              disabled={getProductStock(product) < 1}
              fullWidth
              variant="default"
              onClick={() => addItem(product)}
            >
              Add
            </Button>
          </Table.Td>
        </>
      )}
    />
  );
};
