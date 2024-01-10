import { Badge } from "@mantine/core";
import { InventoryTransactionDTO } from "../../../../types/api-alias";

interface Props {
  status: InventoryTransactionDTO["status"];
}

export const StatusBadge = ({ status }: Props) => {
  switch (status) {
    case "DELETED":
      return (
        <Badge variant="light" color="red">
          {status}
        </Badge>
      );
    case "PENDING":
      return (
        <Badge variant="light" color="yellow">
          {status}
        </Badge>
      );
    case "VALID":
      return (
        <Badge variant="light" color="green">
          {status}
        </Badge>
      );
  }
};
