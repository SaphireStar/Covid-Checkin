import React from 'react';
import axios from "axios";
import { Redirect } from 'react-router';
import Autocomplete from 'react-google-autocomplete';
import {OverlayTrigger, Tooltip} from 'react-bootstrap'
import cookie from "react-cookies";
import DeleteModal from '../../components/business/deleteModal.js';
import '../../css/business.css';
import AddressAlert from '../../components/addressAlert';

const qs = require('querystring');


/**
 * BusinessEdit
 * - Page used for updating / deleting an existing business.
 * - redirects back to business, or the main list if deleted.
 * - fetches existing information / photo
 */
class BusinessEdit extends React.Component {
	constructor(props) {
		super(props);
        this.formData = new FormData();
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
                businessRepId: '',
            },
            photo: null,
            redirect : false,
            deleteConfirm: false,
            deleted : false,
            repId: '',

            addressEntered: false,
            validAddressEntered: false 
		}

		// //bind functions
		this.handleInputChange = this.handleInputChange.bind(this);
        this.handlePhoto = this.handlePhoto.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.deleteHandler = this.deleteHandler.bind(this);
        this.handleDelete = this.handleDelete.bind(this);
    }
    componentDidMount() {
        this.state.business.id = this.props.match.params.id
        // Get current business information
        var url = new URL(`http://localhost:8080/elec5619/business/${this.state.business.id}`),
        params ={
         uid: cookie.load('userId'),
         token: cookie.load('token')
       }
       Object.keys(params).forEach(key => url.searchParams.append(key, params[key]))              
          axios.get(url)
            .then((response) => this.setState({ 
                business:response.data
            }))
            .then(()=>this.setState({ 
                photo:this.state.business.photo,
                repId:this.state.business.businessRepId
            }))
        }

    // update state with changed form values
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
    
    // If a photo is selected, generate a temp url
    handlePhoto(e) {
        if(e.target.files[0]!==null){
            this.formData.append('photo', e.target.files[0]);
            this.setState({
                photo: URL.createObjectURL(e.target.files[0]),
            })
        }
    };

    deleteHandler() {
        this.setState({
            deleteConfirm: true
        });
    }

	handleSubmit(event) {
        event.preventDefault();
        console.log("Request for business register / update");
		axios.post(
			`http://localhost:8080/elec5619/business/edit/${this.state.business.id}`, 
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
            .then(() => {
                if(this.state.photo !== this.state.business.photo) {
                    console.log("Request for business photo update");
                    axios.post(
                        `http://localhost:8080/elec5619/business/${this.state.business.id}/photo`, 
                        this.formData, 
                        {
                            headers: {
                            'Content-Type': 'multipart/form-data'
                          }
                        })
                }
            })
			.then((response) => {
				this.setState({ redirect: true }); // after signing up, set the state to true. This will trigger a re-render  
			})
			.catch(function (error){
				console.log("Error"+error);
			});
    }
    handleDelete() {
        this.setState({deleteConfirm:false})
        console.log("Request for business deletion");
        console.log(qs.stringify(this.state.business));
		axios.get(
            `http://localhost:8080/elec5619/business/delete/${this.state.business.id}`,
            {
            params :{
                uid: cookie.load('userId'),
                token: cookie.load('token')
              }
            }
            )
			.then((response) => {
                this.setState({ deleted: true });
			})
			.catch(function (error){
				console.log("Error"+error);
			});
    }

    onPlaceSelected = (place) => {
        // let place = this.addressAutoComplete.getPlace();
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
        }


    }

    invalidAddressAlert(){

        if (this.state.addressEntered && !this.state.validAddressEntered) {
            return (
                <AddressAlert/>
            )
        }
    }


	render() {
		if (this.state.redirect) {
			return <Redirect to={`/business/${this.state.business.id}`}/>;
        }
        if (this.state.deleteConfirm) {
            this.handleDelete()
        }
        // wait until fully deleted before redirecting
        if(this.state.deleted) {
            return <Redirect push to={`/business/manage/${this.state.repId}`}/>;
        }
		return (
			<div className="BusinessForm">
				<h1>Update Your Business</h1>
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
					<div className='formUpdateWrapper'><input type="submit" value="Update" /></div>
				</form>
                <DeleteModal business = {this.state.business.businessName} deleteHandler ={this.deleteHandler} />
			</div>
		)
	}
}
export default BusinessEdit;