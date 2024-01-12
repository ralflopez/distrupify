import { Box, Card, Flex, Grid, Tabs, Text, Title, rem } from "@mantine/core";
import { useForm } from "@mantine/form";
import { IconPlusMinus } from "@tabler/icons-react";
import { PurchaeOrderCreateRequestItemWithProduct } from "../../types/api-alias";
import { ItemsSection } from "./ItemsSection";
import { InventoryAdjustmentProductsTable } from "./PurchaseOrderProductsTable";

export const PurchaseOrder = () => {
  const iconStyle = { width: rem(12), height: rem(12) };

  const form = useForm({
    initialValues: {
      supplierId: 0,
      items: [] as PurchaeOrderCreateRequestItemWithProduct[],
    },
  });

  return (
    <Flex direction="column" w="100%">
      <Tabs
        defaultValue="order"
        h="100%"
        display="flex"
        style={{ flexDirection: "column" }}
      >
        <Box mb="lg">
          <Title order={2}>Purchase Order</Title>
          <Text size="sm" c="dimmed">
            Create orders from suppliers
          </Text>
        </Box>

        <Tabs.List>
          <Tabs.Tab
            value="order"
            leftSection={<IconPlusMinus style={iconStyle} />}
          >
            Order
          </Tabs.Tab>
        </Tabs.List>

        <Tabs.Panel value="order" pos="relative" pb={{ base: 65, md: 0 }}>
          <Grid style={{ overflow: "visible" }}>
            <Grid.Col
              span={{
                base: 12,
                xl: 6,
              }}
            >
              <InventoryAdjustmentProductsTable form={form} />
            </Grid.Col>
            <Grid.Col span={{ lg: 6 }} display={{ base: "none", xl: "block" }}>
              <Card
                pos="sticky"
                mt="sm"
                p={0}
                top="calc(var(--app-shell-header-height) + var(--mantine-spacing-md))"
                h="calc(calc(100vh - calc(var(--mantine-spacing-md) * 2)) - var(--app-shell-header-height))"
                w="100%"
                withBorder
              >
                <ItemsSection form={form} />
              </Card>
            </Grid.Col>
          </Grid>
        </Tabs.Panel>
      </Tabs>
    </Flex>
  );
};
