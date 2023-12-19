import { Box, Flex, Tabs, Text, Title, rem } from "@mantine/core";
import { useForm } from "@mantine/form";
import { IconClock, IconPlusMinus } from "@tabler/icons-react";
import { InventoryAdjustmentCreateRequestDomainItem } from "../../types/requests";
import { InventoryAdjustmentProductsTable } from "./InventoryAdjustmentProductsTable";
import { TransactionLogsTable } from "./TransactionLogsTable";

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

        <Tabs.Panel
          value="adjustment"
          h="100%"
          style={{ overflow: "auto" }}
          pb={65}
        >
          <InventoryAdjustmentProductsTable form={form} />
        </Tabs.Panel>

        <Tabs.Panel value="logs">
          <TransactionLogsTable />
        </Tabs.Panel>
      </Tabs>
    </Flex>
  );
};
