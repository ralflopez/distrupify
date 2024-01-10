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
import { IconDots, IconInfoCircle } from "@tabler/icons-react";
import { InventoryTransactionDTO } from "../../../types/api-alias";
import { getItemNumber } from "../../../utils/display";
import { StatusBadge } from "./common/StatusBadge";
import { TransactionDeleteItem } from "./common/TransactionDeleteItem";
import { TransactionInformationModal } from "./common/TransactionInformationModal";

interface Props {
  transaction: InventoryTransactionDTO;
  index: number;
  page: number;
  pageSize: number;
}

export const InventoryAdjustmentRow = ({
  index,
  page,
  pageSize,
  transaction,
}: Props) => {
  const [infoModalOpened, { open: openInfoModal, close: closeInfoModal }] =
    useDisclosure(false);

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
          Adjustment
        </Badge>
      </Table.Td>
      <Table.Td>
        <StatusBadge status={transaction.status} />
      </Table.Td>
      <Table.Td ta="right">
        <Text
          c={
            transaction.status === "DELETED"
              ? "dark.0"
              : transaction.value === 0
              ? ""
              : transaction.value > 0
              ? "green"
              : "red"
          }
        >
          <NumberFormatter
            prefix="₱ "
            value={transaction.value}
            thousandSeparator
            decimalScale={2}
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
            <TransactionDeleteItem transaction={transaction} />
          </Menu.Dropdown>
        </Menu>
      </Table.Td>
    </>
  );
};
