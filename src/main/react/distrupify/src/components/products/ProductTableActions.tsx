import {
  ActionIcon,
  Button,
  Group,
  Menu,
  Modal,
  NumberInput,
  Text,
  TextInput,
  rem,
} from "@mantine/core";
import { useForm } from "@mantine/form";
import { useDisclosure } from "@mantine/hooks";
import { modals } from "@mantine/modals";
import {
  IconCurrencyPeso,
  IconDots,
  IconEdit,
  IconTrash,
} from "@tabler/icons-react";
import {
  ProductEditRequest,
  useProductDeleteRequest,
  useProductEditRequest,
} from "../../hooks/server/products";
import { ProductDTO } from "../../types/dto";
import { getProductDisplayName } from "../../utils/display";
import { token } from "../../utils/token";

interface Props {
  product: ProductDTO;
}

export const ProductTableActions = ({ product }: Props) => {
  const iconStyle = { width: rem(12), height: rem(12) };
  const [opened, { open, close }] = useDisclosure(false);
  const productDeleteRequest = useProductDeleteRequest(token, () => {});

  const deleteModal = () =>
    modals.openConfirmModal({
      title: `Delete ${product.name}`,
      children: (
        <Text size="sm">
          Are you sure you want to delete {getProductDisplayName(product)}?
        </Text>
      ),
      centered: true,
      labels: { confirm: "Confirm", cancel: "Cancel" },
      confirmProps: { bg: "red" },
      onCancel: () => console.log("Cancel"),
      onConfirm: () => {
        console.log(product);
        productDeleteRequest.mutate(product.id);
      },
    });

  const form = useForm({
    initialValues: {
      sku: product.sku,
      brand: product.brand,
      description: product.description,
      name: product.name,
      unitPrice: product.unitPrice,
    } as ProductEditRequest,
  });

  const productEditRequest = useProductEditRequest(product.id, token, () => {
    close();
  });

  const onSubmit = () => {
    return form.onSubmit((values) => {
      productEditRequest.mutate(values);
    });
  };

  return (
    <>
      <Modal opened={opened} onClose={close} title="Edit Product" centered>
        <form onSubmit={onSubmit()}>
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
          <Menu.Label>Product</Menu.Label>
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
