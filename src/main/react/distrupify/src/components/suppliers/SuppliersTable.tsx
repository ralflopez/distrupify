import {
  Box,
  Card,
  Center,
  Flex,
  Group,
  Input,
  Loader,
  Pagination,
  ScrollArea,
  Select,
  Table,
} from "@mantine/core";
import { IconSearch } from "@tabler/icons-react";
import { useState } from "react";
import { useSupplierAllRequest } from "../../hooks/server/supplier";
import { getItemNumber } from "../../utils/display";
import { token } from "../../utils/token";
import { SupplierCreateModal } from "./SupplierCreateModal";
import { SupplierTableActions } from "./SupplierTableActions";

interface ProductsSearchBarProps {
  setSearch: (s: string) => void;
  filterBy: "NAME" | "SKU";
  setFilterBy: (s: "NAME" | "SKU") => void;
}

export const ProductsSearchBar = ({
  setSearch,
  filterBy,
  setFilterBy,
}: ProductsSearchBarProps) => {
  return (
    <Flex
      w={{
        base: "100%",
        md: 500,
      }}
      gap="sm"
    >
      <Input
        w="100%"
        onChange={(e) => setSearch(e.target.value)}
        placeholder="Search Name"
        leftSection={<IconSearch size={16} />}
      />
      <Select
        w={150}
        value={filterBy}
        onChange={(value) => setFilterBy((value as "NAME" | "SKU") || "NAME")}
        data={[
          { label: "Name", value: "NAME" },
          { label: "SKU", value: "SKU" },
        ]}
        defaultValue="NAME"
        allowDeselect={false}
      />
    </Flex>
  );
};

export const SuppliersTable = () => {
  const [page, setPage] = useState(1);
  const [pageSize] = useState(100);

  const { isLoading, data } = useSupplierAllRequest(token);

  return (
    <Box>
      <Flex my="sm" justify="flex-end" align="center" gap="xs" wrap="wrap">
        <SupplierCreateModal />
      </Flex>
      <Card withBorder>
        <ScrollArea offsetScrollbars type="auto">
          <Table stickyHeader striped>
            <Table.Thead>
              <Table.Tr>
                <Table.Th c="dimmed">#</Table.Th>
                <Table.Th c="dimmed">Name</Table.Th>
                <Table.Th c="dimmed">Address</Table.Th>
                <Table.Th c="dimmed">Contact #</Table.Th>
                <Table.Th c="dimmed"></Table.Th>
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

              {data?.suppliers.map((supplier, index) => (
                <Table.Tr key={supplier.id}>
                  <Table.Td c="dimmed">
                    {getItemNumber(index, page, pageSize)}
                  </Table.Td>
                  <Table.Td>{supplier.name}</Table.Td>
                  <Table.Td>{supplier.address}</Table.Td>
                  <Table.Td>{supplier.contactNumber}</Table.Td>
                  <Table.Td>
                    <SupplierTableActions supplier={supplier} />
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
