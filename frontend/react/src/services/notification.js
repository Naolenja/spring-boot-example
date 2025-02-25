import { Toaster, toaster } from '../components/ui/toaster';

const notification = (title, description, type) =>{
    toaster.create({
        title,
        description,
        type,
        isClosable: true,
        duration: 4000
    })
}

export const successNotification = (title, description)=>{
    notification(
        title,
        description,
        "success"
    )
}

export const errorNotification = (title, description)=>{
    notification(
        title,
        description,
        "error"
    )
}