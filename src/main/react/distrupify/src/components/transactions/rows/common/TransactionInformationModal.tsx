import {
  Badge,
  Divider,
  Modal,
  NumberFormatter,
  Table,
  Text,
} from "@mantine/core";
import { ReactNode } from "react";
import { InventoryTransactionDTO } from "../../../../types/api-alias";
import { getProductDisplayName } from "../../../../utils/display";
import { StatusBadge } from "./StatusBadge";

const getTransactionTitle = (transaction: InventoryTransactionDTO) => {
  switch (transaction.inventoryTransactionType) {
    case "ADJUSTMENT":
      return "Adjustment Information";
    case "PURCHASE_ORDER":
      return "Purchase Order Information";
    case "SALES":
      return "Sales Information";
  }
};

interface Props {
  opened: boolean;
  close: () => void;
  transaction: InventoryTransactionDTO;
  before?: ReactNode;
}

export const TransactionInformationModal = ({
  before,
  close,
  opened,
  transaction,
}: Props) => {
  return (
    <Modal
      opened={opened}
      onClose={close}
      size="xl"
      title={getTransactionTitle(transaction)}
      centered
    >
      {before}
      {before && <Divider mt="md" mb="md" />}
      <Text size="sm" mb="sm">
        Transaction Information
      </Text>
      <Table stickyHeader striped>
        <Table.Thead>
          <Table.Tr>
            <Table.Th c="dimmed">Property</Table.Th>
            <Table.Th c="dimmed">Value</Table.Th>
          </Table.Tr>
        </Table.Thead>
        <Table.Tbody>
          <Table.Tr>
            <Table.Td>Type</Table.Td>
            <Table.Td>
              <Badge variant="light" color="blue">
                {transaction.inventoryTransactionType}
              </Badge>
            </Table.Td>
          </Table.Tr>
          <Table.Tr>
            <Table.Td>Status</Table.Td>
            <Table.Td>
              <StatusBadge status={transaction.status} />
            </Table.Td>
          </Table.Tr>
          <Table.Tr>
            <Table.Td>Timestamp</Table.Td>
            <Table.Td>{transaction.timestamp}</Table.Td>
          </Table.Tr>
          <Table.Tr>
            <Table.Td>Value</Table.Td>
            <Table.Td>
              <NumberFormatter
                prefix="₱ "
                value={transaction.value}
                thousandSeparator
                decimalScale={2}
                fixedDecimalScale
              />
            </Table.Td>
          </Table.Tr>
        </Table.Tbody>
      </Table>
      <Divider mt="md" mb="md" />
      <Text size="sm" mb="sm">
        Items
      </Text>
      <Table stickyHeader striped>
        <Table.Thead>
          <Table.Tr>
            <Table.Th c="dimmed">SKU</Table.Th>
            <Table.Th c="dimmed">Name</Table.Th>
            <Table.Th c="dimmed">Unit Price</Table.Th>
            <Table.Th c="dimmed">Quantity</Table.Th>
          </Table.Tr>
        </Table.Thead>
        <Table.Tbody>
          {transaction.items.map((item, index) => (
            <Table.Tr key={index}>
              <Table.Td>{item.product.sku}</Table.Td>
              <Table.Td>{getProductDisplayName(item.product)}</Table.Td>
              <Table.Td>
                <NumberFormatter
                  prefix="₱ "
                  value={item.price}
                  thousandSeparator
                  decimalScale={2}
                  fixedDecimalScale
                />
              </Table.Td>
              <Table.Td>{item.quantity}</Table.Td>
            </Table.Tr>
          ))}
        </Table.Tbody>
      </Table>
    </Modal>
  );
};
