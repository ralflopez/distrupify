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
  rem,
} from "@mantine/core";
import { UseFormReturnType } from "@mantine/form";
import { IconX } from "@tabler/icons-react";
import { useInventoryAdjustmentCreateRequest } from "../../hooks/server/inventory-adjustment";
import {
  InventoryAdjustmentCreateRequest,
  InventoryAdjustmentCreateRequestItem,
  InventoryAdjustmentCreateRequestItemWithProduct,
} from "../../types/api-alias";
import { getProductDisplayName } from "../../utils/display";
import { token } from "../../utils/token";

interface Props {
  form: UseFormReturnType<{
    items: InventoryAdjustmentCreateRequestItemWithProduct[];
  }>;
}

export const ItemsSection = ({ form }: Props) => {
  const iconStyle = { width: rem(15), height: rem(15) };

  const inventoryAdjustmentCreate = useInventoryAdjustmentCreateRequest(
    token,
    () => {
      form.setValues({
        items: [],
      });
    }
  );

  function removeItem(item: InventoryAdjustmentCreateRequestItemWithProduct) {
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
        <Title order={4} mb="sm">
          Items
        </Title>
        <Box style={{ flex: 1, overflow: "auto" }}>
          <Table>
            <Table.Thead>
              <Table.Tr>
                <Table.Th fw="normal">Name</Table.Th>
                <Table.Th fw="normal">Operation</Table.Th>
                <Table.Th fw="normal">Quantity</Table.Th>
                <Table.Th fw="normal"></Table.Th>
              </Table.Tr>
            </Table.Thead>
            <Table.Tbody>
              {form.values.items.map((item, index) => (
                <Table.Tr key={item.product.id}>
                  <Table.Td>
                    <Text size="xs">{getProductDisplayName(item.product)}</Text>
                  </Table.Td>
                  <Table.Td>
                    <Select
                      size="xs"
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
                      size="xs"
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
                      <IconX style={iconStyle} />
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
