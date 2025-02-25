import { Button } from "./components/ui/button";
import CardWithImage from "./components/Card";
import DrawerForm from "./components/DrawerForm";
import { Spinner, Text } from "@chakra-ui/react"
import * as React from 'react';
import ResponsiveDrawer from './components/shared/SideBar.jsx';
import { useEffect, useState } from 'react';
import { getCustomers } from './services/client.js'
import { errorNotification} from "./services/notification.js"


const App = () =>{

    const [customers, setCustomers] = useState([]);
    const [loading, setLoading] = useState(false);
    const[err, setError] = useState("");

    const fetchCustomers = () =>{
        setLoading (true);
        getCustomers().then(res => {
            setCustomers(res.data)
            console.log(res)
        }).catch(err => {
            setError(err.response.data.message)
            errorNotification(
                err.code,
                err.response.data.message
            )
        }).finally(() =>{
            setLoading(false);
        })
    }

    useEffect(() => {
        fetchCustomers();
    },[])

    if (loading){
        return <ResponsiveDrawer>
             <Spinner
                color="blue.500"
                css={{ "--spinner-track-color": "colors.gray.200" }}
              />
        </ResponsiveDrawer>
    }

    if (err) {
        return (<ResponsiveDrawer>
                    <DrawerForm
                        fetchCustomers={fetchCustomers}
                        mode="create"
                    />
                    <Text mt={5}>Ups! Error!!!</Text>
                </ResponsiveDrawer>
        )
    }

    if (customers.length <= 0){
        return (<ResponsiveDrawer>
                    <DrawerForm
                        fetchCustomers={fetchCustomers}
                        mode="create"
                    />
                    <Text mt={5}>No customers available.</Text>
                </ResponsiveDrawer>
        )
    }

    return (<ResponsiveDrawer>
                <DrawerForm
                        fetchCustomers={fetchCustomers}
                        mode="create"
                />
                <br/>
                <br/>
                <div style={{
                           display: 'flex',               // Flex container for cards.
                           justifyContent: 'left',      //Center alignment.
                           gap: '30px',                   // Gap between cards.
                           flexWrap: 'wrap',              // Wrap cards to the next line.
                         }}>
                    {customers.map((customer, index) =>(
                        <CardWithImage
                            key={index}
                            {...customer}
                            fetchCustomers={fetchCustomers}
                         />
                    ))}
                </div>
            </ResponsiveDrawer>)
}

export default App