import { Box, Card, Flex, Grid, Tabs, Text, Title, rem } from "@mantine/core";
import { useForm } from "@mantine/form";
import { IconPlusMinus } from "@tabler/icons-react";
import { InventoryAdjustmentCreateRequestItemWithProduct } from "../../types/api-alias";
import { InventoryAdjustmentProductsTable } from "./InventoryAdjustmentProductsTable";
import { ItemsSection } from "./ItemsSection";

export const InventoryAdjustment = () => {
  const iconStyle = { width: rem(12), height: rem(12) };

  const form = useForm({
    initialValues: {
      items: [] as InventoryAdjustmentCreateRequestItemWithProduct[],
    },
  });

  return (
    <Flex direction="column" w="100%">
      <Tabs
        defaultValue="adjustment"
        h="100%"
        display="flex"
        style={{ flexDirection: "column" }}
      >
        <Box mb="lg">
          <Title order={1}>Stock Adjustment</Title>
          <Text size="sm" c="dimmed">
            Manually add or subtract items from the inventory
          </Text>
        </Box>

        <Tabs.List>
          <Tabs.Tab
            value="adjustment"
            leftSection={<IconPlusMinus style={iconStyle} />}
          >
            Adjustment
          </Tabs.Tab>
        </Tabs.List>

        <Tabs.Panel value="adjustment" pos="relative" pb={{ base: 65, md: 0 }}>
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
