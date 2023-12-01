import { useState } from "react";
import { useInventoryAdjustments } from "../../hooks/server/inventory-adjustment";
import { token } from "../../utils/token";

export const TransactionLogs = () => {
  const [page, setPage] = useState(1);
  const { data } = useInventoryAdjustments(token, page);
  return <div>{JSON.stringify(data)}</div>;
};
