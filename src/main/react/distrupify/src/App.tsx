import {
  AppShell,
  Burger,
  Button,
  Divider,
  ScrollArea,
  Text,
  rem,
} from "@mantine/core";
import { useDisclosure } from "@mantine/hooks";
import {
  IconActivity,
  IconAdjustments,
  IconApps,
  IconBox,
  IconDashboard,
  IconPackageImport,
  IconReceipt,
  IconReceipt2,
  IconUserShare,
} from "@tabler/icons-react";
import React, { ReactNode } from "react";
import {
  RouterProvider,
  createBrowserRouter,
  useLocation,
  useNavigate,
} from "react-router-dom";
import { InventoryAdjustment } from "./components/inventory-adjustment/InventoryAdjustment";
import { Products } from "./components/products/Products";
import { PurchaseOrder } from "./components/purchase-order/PurchaseOrder";
import { Sales } from "./components/sales/Sales";
import { Suppliers } from "./components/suppliers/Suppliers";
import { Transactions } from "./components/transactions/Transactions";

const links_data = [
  {
    label: "Actions",
    icon: IconActivity,
    initiallyOpened: true,
    links: [
      {
        label: "Stock Adjustment",
        icon: IconAdjustments,
        path: "/inventories/adjustments",
        element: (
          <Layout>
            <InventoryAdjustment />
          </Layout>
        ),
      },
      {
        label: "Sales",
        icon: IconReceipt2,
        path: "/sales",
        element: (
          <Layout>
            <Sales />
          </Layout>
        ),
      },
      {
        label: "Purchase Orders",
        icon: IconPackageImport,
        path: "/purchase-orders",
        element: (
          <Layout>
            <PurchaseOrder />
          </Layout>
        ),
      },
    ],
  },
  {
    label: "Data",
    icon: IconDashboard,
    initiallyOpened: true,
    links: [
      {
        label: "Transactions",
        icon: IconReceipt,
        path: "/transactions",
        element: (
          <Layout>
            <Transactions />
          </Layout>
        ),
      },
    ],
  },
  {
    label: "Entities",
    icon: IconApps,
    initiallyOpened: true,
    links: [
      {
        label: "Products",
        icon: IconBox,
        path: "/products",
        element: (
          <Layout>
            <Products />
          </Layout>
        ),
      },
      {
        label: "Suppliers",
        icon: IconUserShare,
        path: "/suppliers",
        element: (
          <Layout>
            <Suppliers />
          </Layout>
        ),
      },
    ],
  },
];

const router = createBrowserRouter(
  links_data
    .flatMap((l) => l.links)
    .map(({ path, label, element }) => ({ path, label, element }))
);

function Layout({ children }: { children: ReactNode }) {
  const iconStyle = { width: rem(16), height: rem(16) };
  const [opened, { toggle }] = useDisclosure();
  const navigate = useNavigate();
  const location = useLocation();

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
      <AppShell.Header display="flex" style={{ alignItems: "center" }} p="sm">
        <Burger
          mr="sm"
          opened={opened}
          onClick={toggle}
          hiddenFrom="md"
          size="sm"
        />
      </AppShell.Header>

      <AppShell.Navbar p="md">
        <ScrollArea scrollbarSize={2}>
          {links_data.map((link, index) => (
            <React.Fragment key={link.label}>
              <Text mb="xs">{link.label}</Text>
              {link.links.map((subLink) => (
                <Button
                  key={subLink.path}
                  c={`${
                    location.pathname.includes(subLink.path) ? "" : "dark"
                  }`}
                  onClick={() => navigate(subLink.path)}
                  style={{ display: "flex" }}
                  mb="xs"
                  fullWidth
                  variant={`${
                    location.pathname.includes(subLink.path)
                      ? "light"
                      : "transparent"
                  }`}
                  leftSection={<subLink.icon style={iconStyle} />}
                >
                  {subLink.label}
                </Button>
              ))}
              {index < links_data.length - 1 && <Divider mb="xs" />}
            </React.Fragment>
          ))}
        </ScrollArea>
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
