import { ActionIcon, Button, Group, Modal, TextInput } from "@mantine/core";
import { useForm } from "@mantine/form";
import { useDisclosure } from "@mantine/hooks";
import { IconPlus } from "@tabler/icons-react";
import { useSupplierCreateRequest } from "../../hooks/server/supplier";
import { SupplierCreateRequest } from "../../types/api-alias";
import { token } from "../../utils/token";

export const SupplierCreateModal = () => {
  const [opened, { open, close }] = useDisclosure(false);
  // const iconStyle = { width: rem(12), height: rem(12) };

  const form = useForm({
    initialValues: {
      address: "",
      contactNumber: "",
      name: "",
    } as SupplierCreateRequest,
  });

  const supplierCreateRequest = useSupplierCreateRequest(token, () => {
    close();
    form.reset();
  });

  const onSumbit = () => {
    return form.onSubmit((values) => {
      supplierCreateRequest.mutate(values);
    });
  };

  return (
    <>
      <Button onClick={open} rightSection={<IconPlus size={16} />}>
        New
      </Button>
      <ActionIcon
        onClick={open}
        pos="fixed"
        bottom="var(--app-shell-padding)"
        right="var(--app-shell-padding)"
        variant="filled"
        size="xl"
        radius="md"
        style={{ zIndex: 200 }}
        display={{
          md: "none",
        }}
      >
        <IconPlus style={{ width: "50%", height: "50%" }} stroke={1.5} />
      </ActionIcon>
      <Modal opened={opened} onClose={close} title="New Supplier" centered>
        <form onSubmit={onSumbit()}>
          <TextInput
            mb="md"
            required
            label="Name"
            placeholder=""
            {...form.getInputProps("name")}
          />
          <TextInput
            mb="md"
            required
            label="Address"
            placeholder=""
            {...form.getInputProps("address")}
          />
          <TextInput
            mb="md"
            required
            label="Contact Number"
            placeholder=""
            {...form.getInputProps("contactNumber")}
          />
          <Group mt="md" gap="md" justify="flex-end">
            <Button type="button" onClick={close} variant="default">
              Cancel
            </Button>
            <Button type="submit">Add</Button>
          </Group>
        </form>
      </Modal>
    </>
  );
};
