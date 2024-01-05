import {
  ActionIcon,
  Box,
  Button,
  Card,
  Group,
  NumberFormatter,
  NumberInput,
  Table,
  Text,
  Title,
  rem,
} from "@mantine/core";
import { UseFormReturnType } from "@mantine/form";
import { IconX } from "@tabler/icons-react";
import { useSalesCreateRequest } from "../../hooks/server/sales";
import {
  SalesCreateRequest,
  SalesCreateRequestItem,
  SalesCreateRequestItemWithProduct,
} from "../../types/api-alias";
import { getProductDisplayName } from "../../utils/display";
import { token } from "../../utils/token";

interface Props {
  form: UseFormReturnType<{
    items: SalesCreateRequestItemWithProduct[];
  }>;
}

export const ItemsSection = ({ form }: Props) => {
  const iconStyle = { width: rem(15), height: rem(15) };

  const salesCreateRequest = useSalesCreateRequest(token, () => {
    form.setValues({
      items: [],
    });
  });

  function getTotal() {
    return form.values.items
      .map((item) => (item.quantity || 0) * (item.product.unitPrice || 0))
      .reduce((sum, item) => sum + item, 0);
  }

  function removeItem(item: SalesCreateRequestItemWithProduct) {
    const index = form.values.items.findIndex(
      (i) => i.product.id === item.product.id
    );
    form.removeListItem("items", index);
  }

  function onSubmit() {
    return form.onSubmit((values) => {
      const request: SalesCreateRequest = {
        ...values,
        items: values.items.map(
          (i) =>
            ({
              productId: i.product.id,
              quantity: i.quantity,
              unitPrice: i.unitPrice,
            } as SalesCreateRequestItem)
        ),
      };

      salesCreateRequest.mutate(request);
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
                <Table.Th fw="normal">Unit Price</Table.Th>
                <Table.Th fw="normal">Quantity</Table.Th>
                <Table.Th fw="normal" ta="right">
                  Amount
                </Table.Th>
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
                    <Text size="xs">
                      <NumberFormatter
                        prefix="₱ "
                        value={item.product.unitPrice}
                        thousandSeparator
                        decimalScale={2}
                        fixedDecimalScale
                      />
                    </Text>
                  </Table.Td>
                  <Table.Td>
                    <NumberInput
                      w={70}
                      min={1}
                      max={item.product.quantity || 0}
                      size="xs"
                      {...form.getInputProps(`items.${index}.quantity`)}
                    />
                  </Table.Td>
                  <Table.Td ta="right">
                    <Text size="xs">
                      <NumberFormatter
                        prefix="₱ "
                        value={item.product.unitPrice * item.quantity}
                        thousandSeparator
                        decimalScale={2}
                        fixedDecimalScale
                      />
                    </Text>
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
          <Text ta="right" size="xs" w="100%">
            Total:{" "}
            <NumberFormatter
              prefix="₱ "
              value={getTotal()}
              thousandSeparator
              decimalScale={2}
              fixedDecimalScale
            />
          </Text>
          <Button
            loading={salesCreateRequest.isLoading}
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
