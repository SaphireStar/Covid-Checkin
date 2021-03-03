import React, { useState } from 'react';
import {Alert} from 'react-bootstrap'
export default function AddressAlert() {
    const [show, setShow] = useState(true);
  
    if (show) {

        return (
            <Alert style={{width:"auto"}} variant="danger" onClose={() => setShow(false)} dismissible>
            <h7>The address you entered was invalid</h7>
            </Alert>
        );
    }
    return null;
}
