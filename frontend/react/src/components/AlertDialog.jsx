import { Button } from "@chakra-ui/react"
import {
  DialogActionTrigger,
  DialogBody,
  DialogCloseTrigger,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogRoot,
  DialogTitle,
  DialogTrigger,
} from "./ui/dialog"

export default function AlertDialog ({buttonName, title, message, buttonFunction, open, onOpenChange, buttonMinWidth}) {
  return (
    <DialogRoot role="alertdialog" open={open} onOpenChange={onOpenChange}>
      <DialogTrigger asChild>
        <Button
            variant="outline"
            size="sm"
            minW={buttonMinWidth}
            bg={'red.400'}
            color={'white'}
            rounded={'full'}
            _hover={{
                transform: 'translateY(-2px)',
                boxShadow: 'lg'
            }}
            _focus={{
                bg: 'grey.500'
            }}
        >
          {buttonName}
        </Button>
      </DialogTrigger>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>{title}</DialogTitle>
        </DialogHeader>
        <DialogBody>
          <p>
            {message}
          </p>
        </DialogBody>
        <DialogFooter>
          <DialogActionTrigger asChild>
            <Button variant="outline">Cancel</Button>
          </DialogActionTrigger>
          <Button colorPalette="red" onClick={buttonFunction}>{buttonName}</Button>
        </DialogFooter>
        <DialogCloseTrigger />
      </DialogContent>
    </DialogRoot>
  )
}
