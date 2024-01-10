import { AppShell, Burger } from "@mantine/core";
import { useDisclosure } from "@mantine/hooks";
import { ReactNode } from "react";
import { Link, RouterProvider, createBrowserRouter } from "react-router-dom";
import { InventoryAdjustment } from "./components/inventory-adjustment/InventoryAdjustment";
import { Products } from "./components/products/Products";
import { PurchaseOrder } from "./components/purchase-order/PurchaseOrder";
import { Sales } from "./components/sales/Sales";
import { Transactions } from "./components/transactions/Transactions";

const router = createBrowserRouter([
  {
    path: "/",
    element: (
      <div>
        <h1>Hello World</h1>
        <Link to="about">About Us</Link>
      </div>
    ),
  },
  {
    path: "/inventories/adjustments",
    element: (
      <Layout>
        <InventoryAdjustment />
      </Layout>
    ),
  },
  {
    path: "/transactions",
    element: (
      <Layout>
        <Transactions />
      </Layout>
    ),
  },
  {
    path: "/products",
    element: (
      <Layout>
        <Products />
      </Layout>
    ),
  },
  {
    path: "/purchase-orders",
    element: (
      <Layout>
        <PurchaseOrder />
      </Layout>
    ),
  },
  {
    path: "/sales",
    element: (
      <Layout>
        <Sales />
      </Layout>
    ),
  },
]);

function Layout({ children }: { children: ReactNode }) {
  const [opened, { toggle }] = useDisclosure();
  return (
    <AppShell
      header={{ height: 60 }}
      navbar={{
        width: 300,
        breakpoint: "md",
        collapsed: { mobile: !opened },
      }}
      padding="md"
    >
      <AppShell.Header display="flex" style={{ alignItems: "center" }}>
        <Burger opened={opened} onClick={toggle} hiddenFrom="md" size="sm" />
      </AppShell.Header>

      <AppShell.Navbar p="md">
        <Link to="/inventories/adjustments">Stock Adjustment</Link>
        <Link to="/products">Products</Link>
        <Link to="/purchase-orders">Purchase Order</Link>
        <Link to="/sales">Sales</Link>
        <Link to="/transactions">Transactions</Link>
      </AppShell.Navbar>

      <AppShell.Main bg="gray.0" pos="relative">
        {children}
      </AppShell.Main>
    </AppShell>
  );
}

function App() {
  return (
    <>
      <RouterProvider router={router} />
    </>
  );
}

export default App;
