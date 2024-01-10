import { Menu, Text, rem } from "@mantine/core";
import { modals } from "@mantine/modals";
import { IconTrash } from "@tabler/icons-react";
import { useTransactionDeleteRequest } from "../../../../hooks/server/transactions";
import { InventoryTransactionDTO } from "../../../../types/api-alias";
import { token } from "../../../../utils/token";

interface Props {
  transaction: InventoryTransactionDTO;
}

export const TransactionDeleteItem = ({ transaction }: Props) => {
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
    <Menu.Item
      disabled={transaction.status === "DELETED"}
      color="red"
      onClick={deleteModal}
      leftSection={<IconTrash style={{ width: rem(14), height: rem(14) }} />}
    >
      Delete
    </Menu.Item>
  );
};
