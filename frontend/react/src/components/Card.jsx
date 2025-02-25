import { Button, Card, VStack, Stack, Strong, Text, Tag } from "@chakra-ui/react"
import { LuCheck, LuX } from "react-icons/lu"
import { useState } from "react";
import { Avatar } from "./ui/avatar"
import AlertDialog from "./AlertDialog"
import DrawerForm from "./DrawerForm";
import { deleteCustomer } from '../services/client.js'
import {successNotification, errorNotification} from "../services/notification.js"

export default function CardWithImage({id, name, email, age, gender, fetchCustomers}) {
    var imageGender = gender === "MALE" ? "men" : "women";
    var imageNumber = id % 100;
    const titleString = `Are you sure you want to delete ${name}?`
    const [dialogOpen, setDialogOpen] = useState(false); // State to control dialog
    const buttonMinWidth = "70px";
    // Function for deleting a customer
    const handleDelete = () => {
      deleteCustomer(id)
        .then(res => {
          console.log(res);
          successNotification(
            'Customer deleted',
            `${name} was successfully deleted.`
          );
          fetchCustomers();
        })
        .catch(err => {
          console.log(err);
          errorNotification(
            err.code,
            err.response.data.message
          );
        })
        .finally(() => {
          setDialogOpen(false);
        });
    }
  return (
    <Card.Root width="320px">
      <Card.Body>
        <VStack mb="6" gap="3">
           <Avatar
            size="2x1"
            src={`https://randomuser.me/api/portraits/${imageGender}/${imageNumber}.jpg`}
           />
          <Stack gap="1" align = {'center'}>
            <Tag.Root borderRadius = {"full"}>
                <Tag.Label >{id}</Tag.Label>
            </Tag.Root>
            <Text fontWeight="semibold" textStyle="sm">
              {name}
            </Text>
            <Text color="fg.muted" textStyle="sm">
              {email}
            </Text>
            <Text color="fg.muted" textStyle="sm" mb={16}>
              Age {age} | {gender}
            </Text>
            <DrawerForm
                fetchCustomers={fetchCustomers}
                mode="update"
                buttonMinWidth={buttonMinWidth}
                customerId={id}
                customerName={name}
                customerEmail={email}
                customerAge={age}
                customerGender={gender}
            />
            <AlertDialog
              buttonName="Delete"
              title={titleString}
              message="This action cannot be undone. This will permanently remove all customer data from the system."
              buttonFunction={handleDelete}
              open={dialogOpen}
              onOpenChange={setDialogOpen}
              buttonMinWidth={buttonMinWidth}
            />
          </Stack>
        </VStack>
      </Card.Body>
    </Card.Root>
  )
}
