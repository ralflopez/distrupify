import {
  ActionIcon,
  Menu,
  Modal,
  NumberFormatter,
  Table,
  Text,
  rem,
} from "@mantine/core";
import { useDisclosure } from "@mantine/hooks";
import { modals } from "@mantine/modals";
import { IconDots, IconInfoCircle, IconTrash } from "@tabler/icons-react";
import { useTransactionDeleteRequest } from "../../hooks/server/transactions";
import { InventoryTransactionDTO } from "../../types/api-alias";
import { getProductDisplayName } from "../../utils/display";
import { token } from "../../utils/token";

interface Props {
  transaction: InventoryTransactionDTO;
}

export const TransactionTableActions = ({ transaction }: Props) => {
  const [opened, { open, close }] = useDisclosure(false);
  const transactionDeleteRequest = useTransactionDeleteRequest(token, () => {});

  const deleteModal = () =>
    modals.openConfirmModal({
      title: `Delete Transaction`,
      children: (
        <Text size="sm">Are you sure you want to delete this transaction?</Text>
      ),
      centered: true,
      labels: { confirm: "Confirm", cancel: "Cancel" },
      confirmProps: { bg: "red" },
      onCancel: () => console.log("Cancel"),
      onConfirm: () => {
        transactionDeleteRequest.mutate(transaction.id);
      },
    });

  return (
    <>
      <Modal
        opened={opened}
        onClose={close}
        title="Transaction Information"
        centered
      >
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
            {transaction.items.map((item) => (
              <Table.Tr>
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
            <Table.Tr>
              <Table.Td>{}</Table.Td>
            </Table.Tr>
          </Table.Tbody>
        </Table>
      </Modal>
      <Menu shadow="md" width={200}>
        <Menu.Target>
          <ActionIcon variant="default">
            <IconDots style={{ width: "70%", height: "70%" }} stroke={1.5} />
          </ActionIcon>
        </Menu.Target>

        <Menu.Dropdown>
          <Menu.Label>Transaction</Menu.Label>
          <Menu.Item
            onClick={open}
            leftSection={
              <IconInfoCircle style={{ width: rem(14), height: rem(14) }} />
            }
          >
            More Info
          </Menu.Item>
          <Menu.Divider />
          <Menu.Item
            color="red"
            onClick={deleteModal}
            leftSection={
              <IconTrash style={{ width: rem(14), height: rem(14) }} />
            }
          >
            Delete
          </Menu.Item>
        </Menu.Dropdown>
      </Menu>
    </>
  );
};
