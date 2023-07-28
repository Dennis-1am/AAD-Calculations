import React, { useState, useEffect } from 'react';
import axios from 'axios';

const AAD = async (sku, vendor, shipping_method) => {

    const API_URL = 'https://h7l6hh4mj5.execute-api.us-east-1.amazonaws.com/Test/';

    var data = {
        SKU: sku,
        Vendor_ID: vendor,
        Shipping_Method: shipping_method,
    }

    data = JSON.stringify(data);
    const response = await axios.post(API_URL, data)
    
    return JSON.parse(JSON.parse(response.data).body).ActualArrivalDate;
   
}

function ActualArrivalDate(props){
    const [date, setdate] = useState('')
    useEffect(() => {
        AAD(props.sku, props.vendor, props.shipping_method).then((response) => {
            setdate(response)
        })
    }, [props.shipping_method, props.sku, props.vendor])

    console.log(date)

    if(date === ''){
        return 'Loading...'
    }

    return <p>Actual Arrival Date: {date}</p>
}

export default ActualArrivalDate;