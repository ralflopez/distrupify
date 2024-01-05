import { notifications } from "@mantine/notifications";
import { WebExceptionResponse } from "../../types/response";

export class ApiNotification {
  name: string;
  constructor(name: string) {
    this.name = name;
  }

  fail(message: string) {
    notifications.show({
      title: this.name,
      message,
      color: "red",
    });
  }

  success(message: string) {
    notifications.show({
      title: this.name,
      message,
      color: "green",
    });
  }

  onError(e: Error) {
    this.fail(e.message);
  }
}

export async function handleResponse(response: Response) {
  console.log(response);
  if (!response.ok) {
    let message = "";

    try {
      const body: WebExceptionResponse = await response.json();

      message = body.message;
      if (!message) throw new Error();
    } catch (e) {
      message = `There was an error: ${response.statusText} (${response.status})`;
    }

    throw new Error(message);
  }

  try {
    const result = await response.json();
    return result;
  } catch (e) {
    return "";
  }
}
