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

        <AppShell.Main>
          <InventoryAdjustment />
        </AppShell.Main>
      </AppShell>
    </>
  );
}
