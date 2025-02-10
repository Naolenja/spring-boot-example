import { Button, Card, VStack, Stack, Strong, Text, Tag } from "@chakra-ui/react"
import { Avatar } from "./ui/avatar"
import { LuCheck, LuX } from "react-icons/lu"

export default function CardWithImage({id, name, email, age}) {
  return (
    <Card.Root width="320px">
      <Card.Body>
        <VStack mb="6" gap="3">
          <Avatar
            src="https://images.unsplash.com/photo-1511806754518-53bada35f930"
            name="Nate Foss"
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
              Age {age}
            </Text>
          </Stack>
        </VStack>
      </Card.Body>
    </Card.Root>
  )
}
