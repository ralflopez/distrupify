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
import { useState } from "react";
import { usePurchaseOrderCreateRequest } from "../../hooks/server/purchase-order";
import { useSupplierAllRequest } from "../../hooks/server/supplier";
import {
  PurchaeOrderCreateRequestItemWithProduct,
  PurchaseOrderCreateRequest,
  PurchaseOrderCreateRequestItem,
} from "../../types/api-alias";
import { getProductDisplayName } from "../../utils/display";
import { token } from "../../utils/token";

interface Props {
  form: UseFormReturnType<{
    supplierId: number;
    items: PurchaeOrderCreateRequestItemWithProduct[];
  }>;
}

export const ItemsSection = ({ form }: Props) => {
  const [supplierId, setSupplierId] = useState<string | null>("");
  const iconStyle = { width: rem(15), height: rem(15) };

  const suppliersAllRequest = useSupplierAllRequest(token);
  const inventoryAdjustmentCreate = usePurchaseOrderCreateRequest(token, () => {
    form.setValues({
      supplierId: 0,
      items: [],
    });
  });

  function removeItem(item: PurchaeOrderCreateRequestItemWithProduct) {
    const index = form.values.items.findIndex(
      (i) => i.product.id === item.product.id
    );
    form.removeListItem("items", index);
  }

  function onSubmit() {
    return form.onSubmit((values) => {
      const request: PurchaseOrderCreateRequest = {
        supplierId: parseInt(supplierId || ""),
        items: values.items.map(
          (i) =>
            ({
              productId: i.product.id,
              quantity: i.quantity,
              unitPrice: i.unitPrice,
            } as PurchaseOrderCreateRequestItem)
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
          Purchase Order Form
        </Title>
        <Select
          mb="sm"
          required
          label="Supplier"
          placeholder="Pick supplier"
          data={
            suppliersAllRequest.data?.suppliers.map((s) => ({
              label: s.name,
              value: s.id.toString(),
            })) || []
          }
          value={supplierId}
          onChange={setSupplierId}
          searchable
        />
        <Box style={{ flex: 1, overflow: "auto" }}>
          <Table>
            <Table.Thead>
              <Table.Tr>
                <Table.Th fw="normal">Name</Table.Th>
                <Table.Th fw="normal">Quantity</Table.Th>
                <Table.Th fw="normal">Purchase Price</Table.Th>
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
                    <NumberInput
                      size="xs"
                      w={80}
                      min={1}
                      {...form.getInputProps(`items.${index}.quantity`)}
                    />
                  </Table.Td>
                  <Table.Td>
                    <NumberInput
                      size="xs"
                      w={80}
                      min={0}
                      defaultValue={0}
                      prefix="₱ "
                      {...form.getInputProps(`items.${index}.unitPrice`)}
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
