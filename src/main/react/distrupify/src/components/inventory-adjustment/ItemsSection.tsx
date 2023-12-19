import {
  ActionIcon,
  Box,
  Button,
  Card,
  Group,
  NumberInput,
  Select,
  Table,
  Text,
  Title,
} from "@mantine/core";
import { UseFormReturnType } from "@mantine/form";
import { IconTrashX } from "@tabler/icons-react";
import { useInventoryAdjustmentRequest } from "../../hooks/server";
import {
  InventoryAdjustmentCreateRequest,
  InventoryAdjustmentCreateRequestDomainItem,
  InventoryAdjustmentCreateRequestItem,
} from "../../types/requests";
import { getProductDisplayName } from "../../utils/display";
import { token } from "../../utils/token";

interface Props {
  form: UseFormReturnType<{
    items: InventoryAdjustmentCreateRequestDomainItem[];
  }>;
}

export const ItemsSection = ({ form }: Props) => {
  const inventoryAdjustmentCreate = useInventoryAdjustmentRequest(token, () => {
    form.setValues({
      items: [],
    });
  });

  function removeItem(item: InventoryAdjustmentCreateRequestDomainItem) {
    const index = form.values.items.findIndex(
      (i) => i.product.id === item.product.id
    );
    form.removeListItem("items", index);
  }

  function onSubmit() {
    return form.onSubmit((values) => {
      const request: InventoryAdjustmentCreateRequest = {
        ...values,
        items: values.items.map(
          (i) =>
            ({
              inventoryLogType: i.inventoryLogType,
              productId: i.product.id,
              quantity: i.quantity,
            } as InventoryAdjustmentCreateRequestItem)
        ),
      };

      inventoryAdjustmentCreate.mutate(request);
    });
  }

  return (
    <Card h="100%">
      <form
        style={{ display: "flex", flexDirection: "column", height: "100%" }}
        onSubmit={onSubmit()}
      >
        <Title order={3}>Items</Title>
        <Box style={{ flex: 1, overflow: "auto" }}>
          <Table>
            <Table.Thead>
              <Table.Tr>
                <Table.Th>Name</Table.Th>
                <Table.Th>Operation</Table.Th>
                <Table.Th>Quantity</Table.Th>
                <Table.Th></Table.Th>
              </Table.Tr>
            </Table.Thead>
            <Table.Tbody>
              {form.values.items.map((item, index) => (
                <Table.Tr key={item.product.id}>
                  <Table.Td>
                    <Text>{getProductDisplayName(item.product)}</Text>
                  </Table.Td>
                  <Table.Td>
                    <Select
                      w={110}
                      allowDeselect={false}
                      data={[
                        {
                          label: "Add",
                          value: "INCOMING",
                        },
                        {
                          label: "Subtract",
                          value: "OUTGOING",
                        },
                      ]}
                      {...form.getInputProps(`items.${index}.inventoryLogType`)}
                    />
                  </Table.Td>
                  <Table.Td>
                    <NumberInput
                      w={80}
                      min={1}
                      {...form.getInputProps(`items.${index}.quantity`)}
                    />
                  </Table.Td>
                  <Table.Td>
                    <ActionIcon
                      variant="light"
                      aria-label="Remove"
                      color="red"
                      onClick={() => removeItem(item)}
                    >
                      <IconTrashX />
                    </ActionIcon>
                  </Table.Td>
                </Table.Tr>
              ))}
            </Table.Tbody>
          </Table>
        </Box>
        <Group mt="sm">
          <Button
            loading={inventoryAdjustmentCreate.isLoading}
            type="submit"
            disabled={form.values.items.length < 1}
            fullWidth
          >
            Submit
          </Button>
        </Group>
      </form>
    </Card>
  );
};
