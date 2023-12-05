import { AppShell, Burger } from "@mantine/core";
import { useDisclosure } from "@mantine/hooks";
import { InventoryAdjustment } from "./components/inventory-adjustment/InventoryAdjustment";

export function Demo() {
  const [opened, { toggle }] = useDisclosure();

  return (
    <>
      <AppShell
        header={{ height: 60 }}
        navbar={{
          width: 300,
          breakpoint: "md",
          collapsed: { mobile: !opened },
        }}
        padding="md"
      >
        <AppShell.Header>
          <Burger opened={opened} onClick={toggle} hiddenFrom="md" size="sm" />
          <div>Logo</div>
        </AppShell.Header>

        <AppShell.Navbar p="md">Navbar</AppShell.Navbar>

        <AppShell.Main bg="gray.0">
          <InventoryAdjustment />
        </AppShell.Main>
      </AppShell>
    </>
  );
}
// display="flex"
//           bg="gray.0"
//           h="100vh"
//           mih={800}
//           pr="sm"
//           pb="sm"
//           pt="calc(var(--mantine-spacing-sm) + 60px)"
//           pl={{
//             base: "sm",
//             md: "calc(var(--mantine-spacing-sm) + 300px)",
//           }}
//           style={{ overflow: "auto" }}
