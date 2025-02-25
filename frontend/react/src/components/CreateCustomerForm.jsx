import { Formik, Form, useField } from 'formik';
import * as Yup from 'yup';
import {Alert, Box, Button, Input, Stack } from "@chakra-ui/react"
import FormControl from '@mui/material/FormControl';
import MenuItem from '@mui/material/MenuItem';
import Select from '@mui/material/Select';
import {saveCustomer, updateCustomer} from "../services/client.js"
import {successNotification, errorNotification} from "../services/notification.js"

const MyTextInput = ({ label, ...props }) => {
   // useField() returns [formik.getFieldProps(), formik.getFieldMeta()]
   // which we can spread on <input>. We can use field meta to show an error
   // message if the field is invalid and it has been touched (i.e. visited)
   const [field, meta] = useField(props);
   return (
     <>
       <label htmlFor={props.id || props.name}>{label}</label>
       <Input className="text-input" {...field} {...props} />
       {meta.touched && meta.error ? (
            <Alert.Root status="info" title="This is the alert title">
                <Alert.Indicator />
                <Alert.Title className="error" status={"error"} mt={2}>{meta.error}</Alert.Title>
            </Alert.Root>
       ) : null}
     </>
   );
 };

 const MySelect = ({ label, ...props }) => {
   const [field, meta] = useField(props);
   return (
     <Box>
        <FormControl fullWidth size="small">
            <label htmlFor={props.id || props.name}>{label}</label>
            <Select {...field} {...props} MenuProps={{ disablePortal: true }} displayEmpty sx={{ fontSize: '14px' }}/>
            {meta.touched && meta.error ? (
                <Alert.Root status="info" title="This is the alert title">
                    <Alert.Indicator />
                    <Alert.Title className="error" status={"error"} mt={2}>{meta.error}</Alert.Title>
                </Alert.Root>
            ) : null}
        </FormControl>
     </Box>
   );
 };

// And now we can use these
const CreateCustomerForm = ({fetchCustomers, customerId, customerName, customerEmail, customerAge, customerGender}) => {
  return (
    <>
      <Formik
        initialValues={{
          name: customerName || "",
          email: customerEmail || "",
          age: customerAge || "",
          gender: customerGender || "",
        }}
        validationSchema={Yup.object({
          name: Yup.string()
            .max(30, 'Must be 30 characters or less')
            .required('Required'),
          email: Yup.string()
            .email('Invalid email address')
            .required('Required'),
          age: Yup.number()
            .min(16, 'Must be at least 16 years old')
            .max(120, 'Must be at most 120 years old')
            .required('Required'),
          gender: Yup.string()
            .oneOf(
              ['MALE', 'FEMALE'],
              'Invalid Gender'
            )
            .required('Required'),
        })}
        onSubmit={(customer, { setSubmitting }) => {
          setSubmitting(true);
          (customerId ? updateCustomer(customerId, customer): saveCustomer(customer))
            .then(res=>{
                console.log(res);
                successNotification(
                    "Customer saved",
                    `${customer.name} was successfully saved`
                );
                fetchCustomers();
            }).catch(err=>{
                console.log(err);
                errorNotification(
                    err.code,
                    err.response.data.message
                )
            }).finally(()=>{
                setSubmitting(false);
            })
        }}
      >
        {({isValid, isSubmitting})=>(
            <Form>
              <Stack spacing={"24px"}>
                <MyTextInput
                  label="Name"
                  name="name"
                  type="text"
                  placeholder="Jane"
                />

                <MyTextInput
                  label="Email Address"
                  name="email"
                  type="email"
                  placeholder="jane@formik.com"
                />

                <MyTextInput
                  label="Age"
                  name="age"
                  type="number"
                  placeholder="44"
                />
                <MySelect label="Gender" name="gender">
                  <MenuItem value="">Select gender</MenuItem>
                  <MenuItem value="MALE">Male</MenuItem>
                  <MenuItem value="FEMALE">Female</MenuItem>
                </MySelect>
                <Button disabled={!isValid || isSubmitting} type="submit">Submit</Button>
              </Stack>
            </Form>
        )}

      </Formik>
    </>
  );
};

export default CreateCustomerForm;