import { Button, Drawer, Flex, Grid, Tabs, Title, rem } from "@mantine/core";
import { useForm } from "@mantine/form";
import { useDisclosure } from "@mantine/hooks";
import { IconMessageCircle, IconPhoto } from "@tabler/icons-react";
import { InventoryAdjustmentCreateRequestDomainItem } from "../../types/requests";
import { ItemsSection } from "./ItemsSection";
import { ProductsTable } from "./ProductsTable";
import { TransactionLogs } from "./TransactionLogs";

export const InventoryAdjustment = () => {
  const iconStyle = { width: rem(12), height: rem(12) };
  const [opened, { open, close }] = useDisclosure(false);

  const form = useForm({
    initialValues: {
      items: [] as InventoryAdjustmentCreateRequestDomainItem[],
    },
  });

  return (
    <>
      <Title mb="lg" order={1}>
        Inventory Adjustment
      </Title>
      <Tabs defaultValue="adjustment">
        <Tabs.List>
          <Tabs.Tab
            value="adjustment"
            leftSection={<IconPhoto style={iconStyle} />}
          >
            Adjustment
          </Tabs.Tab>
          <Tabs.Tab
            value="logs"
            leftSection={<IconMessageCircle style={iconStyle} />}
          >
            Logs
          </Tabs.Tab>
        </Tabs.List>

        <Tabs.Panel value="adjustment">
          <Flex my="md" justify="space-between" align="center">
            <Button hiddenFrom="xl" onClick={open}>
              View Items ({form.values.items.length})
            </Button>
          </Flex>
          <Grid>
            <Grid.Col span={{ base: 12, xl: 8 }}>
              <ProductsTable form={form} />
              <Drawer
                hiddenFrom="xl"
                opened={opened}
                onClose={close}
                position="right"
                overlayProps={{ backgroundOpacity: 0.5, blur: 4 }}
              >
                <ItemsSection form={form} />
              </Drawer>
            </Grid.Col>
            <Grid.Col visibleFrom="xl" span={{ base: 0, xl: 4 }}>
              <ItemsSection form={form} />
            </Grid.Col>
          </Grid>
        </Tabs.Panel>

        <Tabs.Panel value="logs">
          <TransactionLogs />
        </Tabs.Panel>
      </Tabs>
    </>
  );
};
