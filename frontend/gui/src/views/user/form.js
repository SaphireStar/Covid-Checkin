import React from 'react';
import { Link } from 'react-router-dom';
import '../../css/business.css';
import axios from "axios"
import cookie from 'react-cookies';

/**
 *  Form pages allows user to view and update their information
 */
class Form extends React.Component {
    constructor(props) {
        super(props);
        if (cookie.load("userId") == null) {
            alert("Please log in");
            this.props.history.push({
                pathname: '/',
            });
        }
        
        this.state = {
            id: cookie.load('userId'),
            firstName: 'default',
            lastName: 'default',
            address: 'address',
            phone: '2'
        }
        this.handleSubmit = this.handleSubmit.bind(this);
        this.fnChange = this.fnChange.bind(this);
        this.lnChange = this.lnChange.bind(this);
        this.addressChange = this.addressChange.bind(this);
        this.phoneChange = this.phoneChange.bind(this);

        //login
        if (this.state.id == -1) {
            this.props.history.push('/');
        }
    }

    // load userid and token from cookie
   componentDidMount() {
        axios({
            method: 'GET',
            url: 'http://localhost:8080/elec5619/form',
            params: {
                id: this.state.id,
                token: cookie.load('token')
            }
        })
            .then((response) => {
                this.setState({
                    firstName: response.data.firstName,
                    lastName: response.data.lastName,
                    address: response.data.address,
                    phone: response.data.phone
                });
            })
            .catch(function (error) {
                console.log("Error" + error);
            });
    }

    // upload changes
    handleSubmit(event) {
        event.preventDefault();
        console.log("update");

        axios({
            method: "POST",
            url: 'http://localhost:8080/elec5619/form/update/',
            params: {
                id: this.state.id,
                token: cookie.load('token'),
                firstName: this.state.firstName,
                lastName: this.state.lastName,
                address: this.state.address,
                phone: this.state.phone
            }
        })
            .then((response) => {
                console.log("successfully received message");
            })
            .catch(function (error) {
                console.log("Error" + error);
            });
    }

    // first name change
    fnChange(event) {
        event.preventDefault();
        this.setState({
            firstName: event.target.value
        })
    }

    // last name change
    lnChange(event) {
        event.preventDefault();
        this.setState({
            lastName: event.target.value
        })
    }

    // address change
    addressChange(event) {
        event.preventDefault();
        this.setState({
            address: event.target.value
        })
    }

    // phone change
    phoneChange(event) {
        event.preventDefault();
        this.setState({
            phone: event.target.value
        })
    }

	render() {
        return (
                <div className ="bodyContent">
                <div className="UserForm">
                        <h1> Form </h1>
                        <form onSubmit={this.handleSubmit}>
                            <label>First Name <input type="text" value={this.state.firstName} onChange={this.fnChange} /></label>
                            <p></p>

                            <label>Last Name<input type="text" value={this.state.lastName} onChange={this.lnChange} ></input></label>
                            <p></p>

                            <label>Address <input type="text" value={this.state.address} onChange={this.addressChange} /></label>
                            <p></p>

                            <label>Contact Number<input type="text" value={this.state.phone} onChange={this.phoneChange} ></input></label>
                            <p></p>

                            <input type="submit" value="Update" />
                        </form>
                    </div>
                </div>
		)
	}
}
export default Form;


