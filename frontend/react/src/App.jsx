import { Button } from "./components/ui/button";
import CardWithImage from "./components/Card";
import { Spinner, Text } from "@chakra-ui/react"
import * as React from 'react';
import ResponsiveDrawer from './components/shared/SideBar.jsx';
import { useEffect, useState } from 'react';
import { getCustomers } from './services/client.js'

const App = () =>{

    const [customers, setCustomers] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        setLoading (true);
        getCustomers().then(res => {
            setCustomers(res.data)
            console.log(res)
        }).catch(err => {
            console.log(err)
        }).finally(() =>{
            setLoading(false);
        })
    },[])

    if (loading){
        return <ResponsiveDrawer>
             <Spinner
                color="blue.500"
                css={{ "--spinner-track-color": "colors.gray.200" }}
              />
        </ResponsiveDrawer>
    }

    if (customers.length <= 0){
        return (<ResponsiveDrawer>
                    <Text>No customers available.</Text>
                </ResponsiveDrawer>)
    }

    return (<ResponsiveDrawer>
                <div style={{
                           display: 'flex',               // Flex container for cards.
                           justifyContent: 'center',      //Center alignment.
                           gap: '30px',                   // Gap between cards.
                           flexWrap: 'wrap',              // Wrap cards to the next line.
                         }}>
                    {customers.map((customer, index) =>(
                        <CardWithImage
                            key={index}
                            {...customer}
                         />
                    ))}
                </div>
            </ResponsiveDrawer>)
}

export default App