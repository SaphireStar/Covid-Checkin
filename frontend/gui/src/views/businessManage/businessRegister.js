import React, { useEffect, useState } from 'react';
import axios from "axios";
import { Redirect } from 'react-router';
import Autocomplete from 'react-google-autocomplete';
import {Alert, OverlayTrigger, Tooltip} from 'react-bootstrap'
import cookie from "react-cookies";
import '../../css/business.css';
import AddressAlert from '../../components/addressAlert';
import placeholder from '../../css/img.jpg';
const qs = require('querystring')

/**
 * BusinessRegister
 * - Page used for registering a new business.
 */
class BusinessRegister extends React.Component {
	constructor(props) {
        super(props);
        this.formData = new FormData();
        this.formData.append('photo',placeholder);
		this.state = {
            business : {
                id: '',
                businessName: '',
                phoneNumber: '',
                businessEmail: '',        
                address: '',
                suburb: '',
                postcode: '',
                capacity: '',
                photo: '',
            },
            photo: placeholder,
            redirect : false,
            repId: '',

            addressEntered: false,
            validAddressEntered: false 
		}

		// //bind functions
		this.handleInputChange = this.handleInputChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this)
        this.handlePhoto = this.handlePhoto.bind(this);
	}

    // Updates the state with values from form
	handleInputChange(event) {
		const target = event.target;
		const value = target.type === 'checkbox' ? target.checked : target.value;
		const name = target.name;
        var updatedBusiness = this.state.business; 
        updatedBusiness[name] = value;
		this.setState({
            business: updatedBusiness
        });
      }

      // If a photo is selected, generate a temp url for it
      handlePhoto(e) {
        if(e.target.files[0]!==null){
            this.formData.append('photo', e.target.files[0]);
            this.setState({
                photo: URL.createObjectURL(e.target.files[0]),
            })
        }
    };

    handleSubmit(event) {
        event.preventDefault();
        console.log("Request for business register");
        // Post the form data to create business
		axios.post(
			`http://localhost:8080/elec5619/business/add`, 
			qs.stringify(this.state.business), 
			{
				headers: {
				'Content-Type': 'application/x-www-form-urlencoded'
              },
              params :{
                uid: cookie.load('userId'),
                token: cookie.load('token')
              }
            })
            //After creating business, upload a photo for said business
            .then((response) => {
                this.setState({ repId: response.data.businessRepId });
                if(this.state.photo !== this.state.business.photo) {
                    console.log("Request for business photo update");
                    axios.post(
                        `http://localhost:8080/elec5619/business/${response.data.id}/photo`, 
                        this.formData, 
                        {
                            headers: {
                            'Content-Type': 'multipart/form-data'
                          }
                        })
                }
            })
            // after signing up, set the state to true. This will trigger a re-render  
			.then((response) => {
				this.setState({ redirect: true }); 
			})
			.catch(function (error){
				console.log("Error"+error);
			});
    }

    // handle address validation
    onPlaceSelected = (place) => {
        let address = place.address_components;

        //used to reset the alerts
        this.setState({
            addressEntered: false,
        })

        try {
            var updatedBusiness = this.state.business;
            updatedBusiness['address'] = `${address[0].long_name} ${address[1].long_name}`;
            updatedBusiness['suburb'] = address[2].long_name;
            updatedBusiness['postcode'] = address[6].short_name
            this.setState({
                business: updatedBusiness
    
            })
            this.state.addressEntered = true;
            this.state.validAddressEntered = true;
        } catch(err){
            this.setState({
                addressEntered: true,
                validAddressEntered: false
            })
            console.log("invalid address");
        }


    }

    invalidAddressAlert(){

        if (this.state.addressEntered && !this.state.validAddressEntered) {
            console.log("returning");
            return (
                <AddressAlert/>
            )
        }
    }

	render() {
        
		if (this.state.redirect) {
			return <Redirect to={`/business/manage/${this.state.repId}`}/>;
		}
		return (
			<div className="BusinessForm">
				<h1>Register Your Business</h1>
				<p>Save your customers, and yourself time.</p>
				<form method="post" onSubmit={this.handleSubmit}>
                    <table>
                        <tbody>

                        
                                <tr>
                                    <td>
                                        <label>
                                            Business Name 
                                            <br></br>
                                            <input required type="text"  name="businessName" className="span" 
                                            value={this.state.business.businessName}
                                            placeholder={this.state.business.businessName}  
                                            onChange={this.handleInputChange} 
                                            onKeyDown={this.blockKeyEvent} />
                                        </label>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <p>Enter Address</p>
                                        <Autocomplete
                                            className="span"
                                            onPlaceSelected={this.onPlaceSelected}
                                            types={['geocode']}
                                            componentRestrictions={{country:'au'}}
                                        />
                                        
                                    </td>
                                </tr>
                                {this.invalidAddressAlert()}
                                <tr>
                                    <td>
                                    <OverlayTrigger overlay={<Tooltip id="tooltip-disabled">Please use the provided Autocomplete Box</Tooltip>}>
                                        <label>
                                            Address 
                                            <br></br>
                                            <input disabled required type="text" name="address" className="span" 
                                            value={this.state.business.address} 
                                            placeholder={this.state.business.address}  
                                            onChange={this.handleInputChange} 
                                            onKeyDown={this.blockKeyEvent} />
                                        </label>
                                    </OverlayTrigger>

      
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                    <OverlayTrigger overlay={<Tooltip id="tooltip-disabled">Please use the provided Autocomplete Box</Tooltip>}>
                                        <label>
                                            Suburb 
                                            <br></br>
                                            <input disabled required type="text" name="suburb" className="half" 
                                            value={this.state.business.suburb}
                                            placeholder={this.state.business.suburb} 
                                            onChange={this.handleInputChange}  
                                            onKeyDown={this.blockKeyEvent} />
                                        </label>
                                        </OverlayTrigger>
                                    </td>
                                    
                                    <td>
                                        <OverlayTrigger overlay={<Tooltip id="tooltip-disabled">Please use the provided Autocomplete Box</Tooltip>}>
                                        <label>
                                            Postcode 
                                            <br></br>
                                            <input disabled required type="text" name="postcode" className="half" 
                                            value={this.state.business.postcode}
                                            placeholder={this.state.business.postcode} 
                                            onChange={this.handleInputChange} 
                                            onKeyDown={this.blockKeyEvent} />
                                        </label>
                                        </OverlayTrigger>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <label>
                                            Business Email 
                                            <br></br>
                                            <input required type="text" name="businessEmail" className="half" 
                                            value={this.state.business.businessEmail}
                                            placeholder={this.state.business.businessEmail} 
                                            onChange={this.handleInputChange} 
                                            onKeyDown={this.blockKeyEvent} />
                                        </label>
                                    </td>
                                    <td>
                                        <label>
                                            Business Phone 
                                            <br></br>
                                            <input required type="tel" name="phoneNumber" className="half" 
                                            value={this.state.business.phoneNumber} 
                                            placeholder={this.state.business.phoneNumber} 
                                            onChange={this.handleInputChange} 
                                            onKeyDown={this.blockKeyEvent} />
                                        </label>
                                    </td>
                                </tr>						
                                <tr>
                                    <td>
                                        <label>
                                            Max Occupancy 
                                            <br></br>
                                            <input required type="number" name="capacity" className="half" 
                                            value={this.state.business.capacity}
                                            placeholder={this.state.business.capacity} 
                                            onChange={this.handleInputChange} 
                                            onKeyDown={this.blockKeyEvent} />
                                        </label>
                                    </td>
                                    <td>
                                    <label>
                                        Photo 
                                        <br></br>
                                        <img key={Date.now()} src={this.state.photo} />
                                        <input type="file" accept="image/jpeg" className="half"
                                        onChange={this.handlePhoto} 
                                        onKeyDown={this.blockKeyEvent} />
                                    </label>
                                </td>
                                </tr>   
                            </tbody>                                                                                               
                        </table>                    
					<p></p>
					<p></p>
                    <div className='formUpdateWrapper'><input type="submit" value="Register" /></div>
				</form>
			</div>
		)
	}
}
export default BusinessRegister;