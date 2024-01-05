import {
  ActionIcon,
  Button,
  Group,
  Modal,
  NumberInput,
  TextInput,
  rem,
} from "@mantine/core";
import { useForm } from "@mantine/form";
import { useDisclosure } from "@mantine/hooks";
import { IconCurrencyPeso, IconPlus } from "@tabler/icons-react";
import { useProductCreateRequest } from "../../hooks/server/products";
import { ProductCreateRequest } from "../../types/api-alias";
import { token } from "../../utils/token";

export const ProductCreateModal = () => {
  const [opened, { open, close }] = useDisclosure(false);
  const iconStyle = { width: rem(12), height: rem(12) };

  const productCreateRequest = useProductCreateRequest(token, () => {
    close();
  });

  const form = useForm({
    initialValues: {
      sku: "",
      brand: "",
      description: "",
      name: "",
      unitPrice: 0,
    } as ProductCreateRequest,
  });

  const onSumbit = () => {
    return form.onSubmit((values) => {
      productCreateRequest.mutate(values);
    });
  };

  return (
    <>
      <Button
        display={{ base: "none", md: "flex" }}
        onClick={open}
        rightSection={<IconPlus size={16} />}
      >
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
      <Modal opened={opened} onClose={close} title="New Product" centered>
        <form onSubmit={onSumbit()}>
          <TextInput
            mb="md"
            required
            label="Stock Keeping Unit (SKU)"
            placeholder=""
            {...form.getInputProps("sku")}
          />
          <TextInput
            mb="md"
            required
            label="Brand"
            placeholder=""
            {...form.getInputProps("brand")}
          />
          <TextInput
            mb="md"
            required
            label="Name"
            placeholder=""
            {...form.getInputProps("name")}
          />
          <TextInput
            mb="md"
            label="Description"
            placeholder=""
            {...form.getInputProps("description")}
          />
          <NumberInput
            leftSection={<IconCurrencyPeso style={iconStyle} />}
            mb="md"
            required
            label="Unit Price (PHP)"
            min={0}
            {...form.getInputProps("unitPrice")}
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
