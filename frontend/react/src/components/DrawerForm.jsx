import {Button, HStack, Stack } from "@chakra-ui/react"
import {
  DrawerActionTrigger,
  DrawerBackdrop,
  DrawerBody,
  DrawerCloseTrigger,
  DrawerContent,
  DrawerFooter,
  DrawerHeader,
  DrawerRoot,
  DrawerTitle,
  DrawerTrigger,
} from "./ui/drawer"
import CreateCustomerForm from "./CreateCustomerForm.jsx";

const AddIcon = () => "+";
const CloseIcon = () => "x";

const DrawerForm = ({fetchCustomers, mode, buttonMinWidth, customerId, customerName, customerEmail, customerAge, customerGender}) =>{
    let triggeringButtonName = "Create customer";
    let title = "Create new customer";
    if (mode=="update"){
        triggeringButtonName = "Edit";
        title = `Edit existing customer with id ${customerId}`
    };
    return (
        <DrawerRoot size={"xl"}>
              <DrawerBackdrop />
              <DrawerTrigger asChild>
                <Button size="sm"
                        minW={buttonMinWidth}
                        rounded={'full'}
                        colorPalette = {"teal"}
                        _hover={{
                            transform: 'translateY(-2px)',
                            boxShadow: 'lg'
                        }}
                        _focus={{
                            bg: 'grey.500'
                        }}
                >{mode == "create" && <AddIcon />} {triggeringButtonName}</Button>
              </DrawerTrigger>
              <DrawerContent>
                <DrawerHeader>
                  <DrawerTitle>{title}</DrawerTitle>
                </DrawerHeader>
                <DrawerBody>
                  <Stack mt="5">
                    <CreateCustomerForm
                        fetchCustomers={fetchCustomers}
                        customerId={customerId}
                        customerName={customerName}
                        customerEmail={customerEmail}
                        customerAge={customerAge}
                        customerGender={customerGender}
                    />
                  </Stack>
                </DrawerBody>
                <DrawerFooter>
                  <DrawerActionTrigger asChild>
                    <Button size="sm" colorPalette = {"teal"}><CloseIcon /> Cancel</Button>
                  </DrawerActionTrigger>
                </DrawerFooter>
                <DrawerCloseTrigger />
              </DrawerContent>
            </DrawerRoot>
    )
}

export default DrawerForm;