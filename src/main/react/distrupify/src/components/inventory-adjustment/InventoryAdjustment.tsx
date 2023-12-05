import { Box, Flex, Tabs, Text, Title, rem } from "@mantine/core";
import { useForm } from "@mantine/form";
import { IconClock, IconPlusMinus } from "@tabler/icons-react";
import { InventoryAdjustmentCreateRequestDomainItem } from "../../types/requests";
import { ProductsTable } from "./ProductsTable";
import { TransactionLogs } from "./TransactionLogs";

export const InventoryAdjustment = () => {
  const iconStyle = { width: rem(12), height: rem(12) };

  const form = useForm({
    initialValues: {
      items: [] as InventoryAdjustmentCreateRequestDomainItem[],
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
          <Tabs.Tab value="logs" leftSection={<IconClock style={iconStyle} />}>
            History
          </Tabs.Tab>
        </Tabs.List>

        <Tabs.Panel value="adjustment" h="100%" style={{ overflow: "auto" }}>
          <ProductsTable form={form} />
          {/* <Grid
            h="100%"
            align="stretch"
            styles={{
              inner: { height: "100%" },
            }}
          >
            <Grid.Col span={{ base: 12, xl: 8 }}>
              <ProductsTable form={form} />
            </Grid.Col>
            <Grid.Col visibleFrom="xl" span={{ base: 0, xl: 4 }} py="md">
              <ItemsSection form={form} />
            </Grid.Col>
          </Grid> */}
          {/* <div
            style={{
              marginTop: "10px",
              height: "2000px",
              width: "50px",
              backgroundColor: "coral",
            }}
          ></div> */}
        </Tabs.Panel>

        <Tabs.Panel value="logs">
          <TransactionLogs />
        </Tabs.Panel>
      </Tabs>
    </Flex>
  );
};
