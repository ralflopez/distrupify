import { Box, Flex, Tabs, Text, Title, rem } from "@mantine/core";
import { IconList } from "@tabler/icons-react";
import { SuppliersTable } from "./SuppliersTable";

export const Suppliers = () => {
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
          <Title order={2}>Suppliers</Title>
          <Text size="sm" c="dimmed">
            Manage suppliers information
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
          <SuppliersTable />
        </Tabs.Panel>
      </Tabs>
    </Flex>
  );
};
