import {
  ActionIcon,
  Badge,
  Menu,
  NumberFormatter,
  Table,
  Text,
  rem,
} from "@mantine/core";
import { useDisclosure } from "@mantine/hooks";
import { modals } from "@mantine/modals";
import {
  IconCornerLeftDown,
  IconDots,
  IconInfoCircle,
} from "@tabler/icons-react";
import { usePurchaseOrderReceiveRequest } from "../../../hooks/server/purchase-order";
import { InventoryTransactionDTO } from "../../../types/api-alias";
import { getItemNumber } from "../../../utils/display";
import { token } from "../../../utils/token";
import { StatusBadge } from "./common/StatusBadge";
import { TransactionDeleteItem } from "./common/TransactionDeleteItem";
import { TransactionInformationModal } from "./common/TransactionInformationModal";

interface Props {
  transaction: InventoryTransactionDTO;
  index: number;
  page: number;
  pageSize: number;
}

export const PurchaseOrderRow = ({
  index,
  page,
  pageSize,
  transaction,
}: Props) => {
  const [infoModalOpened, { open: openInfoModal, close: closeInfoModal }] =
    useDisclosure(false);
  const purchseOrderReceiveRequest = usePurchaseOrderReceiveRequest(token);

  const receiveModal = () =>
    modals.openConfirmModal({
      title: `Delete Transaction`,
      children: (
        <Text size="sm">
          Are you sure you want to receive this purchase order?
        </Text>
      ),
      centered: true,
      labels: { confirm: "Confirm", cancel: "Cancel" },
      onCancel: () => console.log("Cancel"),
      onConfirm: () => {
        purchseOrderReceiveRequest.mutate(transaction.id);
      },
    });

  return (
    <>
      <TransactionInformationModal
        opened={infoModalOpened}
        close={closeInfoModal}
        transaction={transaction}
      />
      <Table.Td c={transaction.status === "DELETED" ? "dark.0" : ""}>
        {getItemNumber(index, page, pageSize)}
      </Table.Td>
      <Table.Td c={transaction.status === "DELETED" ? "dark.0" : ""}>
        {transaction.timestamp}
      </Table.Td>
      <Table.Td>
        <Badge variant="light" color="blue">
          Purchase Order
        </Badge>
      </Table.Td>
      <Table.Td>
        <StatusBadge status={transaction.status} />
      </Table.Td>
      <Table.Td ta="right">
        <Text c={transaction.status === "DELETED" ? "dark.0" : "red"}>
          <NumberFormatter
            prefix="-₱ "
            value={transaction.value}
            thousandSeparator
            decimalScale={2}
            allowNegative={false}
            fixedDecimalScale
          />
        </Text>
      </Table.Td>
      <Table.Td ta="center">
        <Menu shadow="md" width={200}>
          <Menu.Target>
            <ActionIcon variant="default">
              <IconDots style={{ width: "70%", height: "70%" }} stroke={1.5} />
            </ActionIcon>
          </Menu.Target>

          <Menu.Dropdown>
            <Menu.Label>Transaction</Menu.Label>
            <Menu.Item
              onClick={openInfoModal}
              leftSection={
                <IconInfoCircle style={{ width: rem(14), height: rem(14) }} />
              }
            >
              More Info
            </Menu.Item>
            <Menu.Divider />
            <Menu.Item
              disabled={transaction.status !== "PENDING"}
              onClick={receiveModal}
              leftSection={
                <IconCornerLeftDown
                  style={{ width: rem(14), height: rem(14) }}
                />
              }
            >
              Receive
            </Menu.Item>
            <TransactionDeleteItem transaction={transaction} />
          </Menu.Dropdown>
        </Menu>
      </Table.Td>
    </>
  );
};
