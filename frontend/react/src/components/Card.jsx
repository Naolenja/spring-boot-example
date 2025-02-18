import { Button, Card, VStack, Stack, Strong, Text, Tag } from "@chakra-ui/react"
import { Avatar } from "./ui/avatar"
import { LuCheck, LuX } from "react-icons/lu"

export default function CardWithImage({id, name, email, age, gender}) {
  var imageGender = gender === "MALE" ? "men" : "women";
  var imageNumber = id % 100;
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
            <Text color="fg.muted" textStyle="sm">
              Age {age} | {gender}
            </Text>
          </Stack>
        </VStack>
      </Card.Body>
    </Card.Root>
  )
}
