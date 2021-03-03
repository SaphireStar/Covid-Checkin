import React, { useState } from 'react'
import '../../css/business.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import "react-datepicker/dist/react-datepicker.css";
import { Button } from 'react-bootstrap'

/**
 * Notify Window Section
 * - The main Section for Jumping to Notify Page
 * - Parent is BusinessComponent
 */
const NotifyWindow = ({ businessId }) => {

    // Onclick Event
    const handleClick = (businessId)=>{
        window.location.href="http://localhost:8080/elec5619/notification/notify/" + businessId;
    }

    return (
        <div class='notifyWindow'>
            <h3>Want to send Messages to your Customer?</h3>
            <div class='timeFrameWrapper'>
                <div  class="notifyButton">
                    <div></div>
                    <Button variant="primary" onClick={()=>handleClick(businessId)}>Go to Notify</Button>{' '}
                </div>
            </div>
        </div>
    )
};

export default NotifyWindow;
