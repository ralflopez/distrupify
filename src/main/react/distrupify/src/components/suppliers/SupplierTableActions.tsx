import {
  ActionIcon,
  Button,
  Group,
  Menu,
  Modal,
  Text,
  TextInput,
  rem,
} from "@mantine/core";
import { useForm } from "@mantine/form";
import { useDisclosure } from "@mantine/hooks";
import { modals } from "@mantine/modals";
import { IconDots, IconEdit, IconTrash } from "@tabler/icons-react";
import {
  useSupplierDeleteRequest,
  useSupplierEditRequest,
} from "../../hooks/server/supplier";
import { SupplierDTO, SupplierEditRequest } from "../../types/api-alias";
import { token } from "../../utils/token";

interface Props {
  supplier: SupplierDTO;
}

export const SupplierTableActions = ({ supplier }: Props) => {
  // const iconStyle = { width: rem(12), height: rem(12) };
  const [opened, { open, close }] = useDisclosure(false);
  const supplierDeleteRequest = useSupplierDeleteRequest(token, () => {});

  const deleteModal = () =>
    modals.openConfirmModal({
      title: `Delete ${supplier.name}`,
      children: (
        <Text size="sm">Are you sure you want to delete {supplier.name}?</Text>
      ),
      centered: true,
      labels: { confirm: "Confirm", cancel: "Cancel" },
      confirmProps: { bg: "red" },
      onCancel: () => console.log("Cancel"),
      onConfirm: () => {
        supplierDeleteRequest.mutate(supplier.id);
      },
    });

  const form = useForm({
    initialValues: {
      address: supplier.address,
      contactNumber: supplier.contactNumber,
      name: supplier.name,
    } as SupplierEditRequest,
  });

  const supplierEditRequest = useSupplierEditRequest(supplier.id, token, () => {
    close();
  });

  const onSubmit = () => {
    return form.onSubmit((values) => {
      console.log(values);
      supplierEditRequest.mutate(values);
    });
  };

  return (
    <>
      <Modal opened={opened} onClose={close} title="Edit Supplier" centered>
        <form onSubmit={onSubmit()}>
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
            <Button type="submit">Edit</Button>
          </Group>
        </form>
      </Modal>

      <Menu shadow="md" width={200}>
        <Menu.Target>
          <ActionIcon variant="default">
            <IconDots style={{ width: "70%", height: "70%" }} stroke={1.5} />
          </ActionIcon>
        </Menu.Target>

        <Menu.Dropdown>
          <Menu.Label>Supplier</Menu.Label>
          <Menu.Item
            onClick={open}
            leftSection={
              <IconEdit style={{ width: rem(14), height: rem(14) }} />
            }
          >
            Edit
          </Menu.Item>
          <Menu.Divider />
          <Menu.Item
            color="red"
            onClick={deleteModal}
            leftSection={
              <IconTrash style={{ width: rem(14), height: rem(14) }} />
            }
          >
            Delete
          </Menu.Item>
        </Menu.Dropdown>
      </Menu>
    </>
  );
};
