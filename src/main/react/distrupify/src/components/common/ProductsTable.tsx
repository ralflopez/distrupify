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
import { useDebounce } from "../../hooks/debounce";
import { useProductsRequest } from "../../hooks/server/products";
import { ProductDTO } from "../../types/api-alias";
import { getItemNumber } from "../../utils/display";
import { token } from "../../utils/token";

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

interface Props {
  thead: () => React.ReactNode;
  trows: (product: ProductDTO, index: number) => React.ReactNode;
  action?: () => React.ReactNode;
}

export const ProductsTable = ({ thead, trows, action }: Props) => {
  const [page, setPage] = useState(1);
  const [pageSize] = useState(100);
  const [search, setSearch] = useState("");
  const [filterBy, setFilterBy] = useState<"NAME" | "SKU">("NAME");
  const debouncedSearch = useDebounce(search);

  const { isLoading, data } = useProductsRequest(
    token,
    page,
    pageSize,
    debouncedSearch,
    filterBy
  );

  return (
    <Box>
      <Flex my="sm" justify="space-between" align="center" gap="xs" wrap="wrap">
        <ProductsSearchBar
          setSearch={(value) => setSearch(value)}
          filterBy={filterBy}
          setFilterBy={(value) => setFilterBy(value)}
        />
        {action && action()}
      </Flex>
      <Card withBorder>
        <ScrollArea offsetScrollbars type="auto">
          <Table stickyHeader striped>
            <Table.Thead>
              <Table.Tr>
                <Table.Th c="dimmed">#</Table.Th>
                {thead()}
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

              {data?.products.map((product, index) => (
                <Table.Tr key={product.id}>
                  <Table.Td c="dimmed">
                    {getItemNumber(index, page, pageSize)}
                  </Table.Td>
                  {trows(product, index)}
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
