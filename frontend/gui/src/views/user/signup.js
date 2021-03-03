import React from 'react';
import '../../css/userForm.css';
import axios from "axios"
import JSEncrypt from 'jsencrypt';
import cookie from 'react-cookies';


/** 
 *  Sign-up page allows user to sign up a new account with thier
 *   personal information
 */
// public key
const PUB_KEY = `
MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC8rlhHdXuhIHcd84Lbw0E2V/G5
tOpVA1vpcb0HtGEkd5DKIBP/MINNE2P79k7JgIS5MmR0AdN5JQS4CANBHDFQpnRZ
AbD8OSYwe7aThL9TZS3bhagQzfEw6X6mUCcgazlBH5k2VEMXColjXM667CPaPDu6
gpfNVdj84sczK5q3DQIDAQAB`;

class Signup extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
			id: -1,
			email: 'default',
			password: 'none',
			firstName: 'default',
			lastName: 'default',
			address: 'address',
			phone: '0123456789',

			// email 
			emailOK: false,
			emailFilled: false,

			// password
			pwOk: false,
			pwFilled: false,

			// name
			lnOk: false,
			fnOk: false,

			// address
			addressOk: false,

			// phone
			filled: false, 
			phone1OK: true,
			phone2OK: true,
			phone3OK: true

			
		}

		this.handleSubmit = this.handleSubmit.bind(this);
		this.emailChange = this.emailChange.bind(this);
		this.passwordChange = this.passwordChange.bind(this);
		this.fnChange = this.fnChange.bind(this);
		this.lnChange = this.lnChange.bind(this);
		this.addressChange = this.addressChange.bind(this);
		this.phoneChange1 = this.phoneChange1.bind(this);
		this.phoneChange2 = this.phoneChange2.bind(this);
		this.phoneChange3 = this.phoneChange3.bind(this);
	}


	handleSubmit(event) {
		event.preventDefault();

		// verify email
		if (!this.state.emailFilled) {
			// no email
			alert("Please fill in the email");
			return;
		} else {
			// wrong email
			if (!this.state.emailOK) {
				alert("Email must be \"xxx@xxx.com\"");
				return;
            }
		}

		// verify password
		if (!this.state.pwFilled) {
			// no password
			alert("Please fill in the password");
			return;
		} else {
			// wrong password
			if (!this.state.pwOk) {
				alert("Password is too short");
				return;
			}
		}

		// verify name
		if (!(this.state.fnOk && this.state.lnOk)) {
			alert("Please fill in your name");
			return;
		}

		// verify address
		if (!this.state.addressOk) {
			alert("Please fill in the address");
			return;
		}

		if (this.state.filled) {
			if (!(this.state.phone1OK && this.state.phone2OK && this.state.phone3OK)) {
				alert("Phone number should have 10 digits, in the format \"xx-xxxx-xxxx\", starting with 0");
				return;
			}
		} else {
			alert("Please fill in the phone number");
			return;
        }

		/* register process */

		// encrypt
		const jsencrypt = new JSEncrypt();
		jsencrypt.setPublicKey(PUB_KEY);

		axios({
			method: 'post',
			url: 'http://localhost:8080/elec5619/signup',
			params: {
				email: this.state.email,
				password: jsencrypt.encrypt(this.state.password),
				firstName: this.state.firstName,
				lastName: this.state.lastName,
				address: this.state.address,
				phone: this.state.phone
			}
		})
			.then((response) => {
				if (response.data.id == -2) {
					alert("The email has been occupied");
				}
				else if (response.data.id != -1) {
					cookie.save("userId", response.data.id);
					cookie.save("token", response.data.token);
					console.log("Successfully Signed Up ");
					this.props.history.push({
						pathname: '/business/search'
					});
				} else {
					console.log("Failed to Sign Up");
				}
			})
			.catch(function (error) {
				console.log("Error" + error);
			});
	}

	/*
	 * verify and update the email
	 */
	emailChange(event) {
		event.preventDefault();
		var email = event.target.value;
		var emailFormat = /^[a-zA-Z0-9_-]+@([a-zA-Z0-9]+\.)+com$/;
		var flag = emailFormat.test(email);


		this.setState({
			email: event.target.value,
			emailFilled: true,
			emailOK: flag
        })
	}

	/*
	* verify and update the password
	*/
	passwordChange(event) {
		event.preventDefault();
		var flag = (event.target.value.length >= 6);
		this.setState({
			pwFilled: true,
			pwOk: flag,
			password: event.target.value
		})
	}

	/* first name input event */
	fnChange(event) {
		event.preventDefault();
		this.setState({
			firstName: event.target.value,
			fnOk: true
		})
	}

	/* last name input event */
	lnChange(event) {
		event.preventDefault();
		this.setState({
			lastName: event.target.value,
			lnOk: true
		})
	}

	addressChange(event) {
		event.preventDefault();
		this.setState({
			addressOk:true,
			address: event.target.value
		})
	}

	/**
	 * Changes the first two digtis of the phone number
	 */
	phoneChange1(event) {
		event.preventDefault();
		/* get the string */
		var num = event.target.value;
		/* validate the string */
		var flag = (num.length === 2) && (num.substring(0, 1) === "0") && (/^\d+$/.test(num));

		num = (num.length === 1) ? (num + "0" + this.state.phone.substring(2)) : (num.substring(0, 2) + this.state.phone.substring(2));
		this.setState({
			phone: num,
			phone1OK: flag,
			filled: true
		})
	}

	/**
	 * Changes the four middle digtis of the phone number
	 */
	phoneChange2(event) {
		event.preventDefault();
		/* get the string */
		var num = event.target.value;
		/* validate the string */
		var flag = (num.length === 4) && (/^\d+$/.test(num));

		num = (!flag) ? (this.state.phone) : (this.state.phone.substring(0,2) + num.substring(0, 4) + this.state.phone.substring(6,10));
		this.setState({
			phone: num,
			phone2OK: flag,
			filled: true
		})
	}

	/**
	 * Changes the last four digtis of the phone number
	 */
	phoneChange3(event) {
		event.preventDefault();
		/* get the string */
		var num = event.target.value;
		/* validate the string */
		var flag = (num.length === 4) && (/^\d+$/.test(num));

		num = (!flag) ? (this.state.phone) : (this.state.phone.substring(0, 6) + num.substring(0, 4));
		this.setState({
			phone: num,
			phone3OK: flag,
			filled: true
		})
	} 

	render() {
		{/* Email Style */ }
		const emailStyle = {
			borderColor: this.state.emailFilled ? (this.state.emailOK? "#ccc":"red") : "#ccc",
			outlineColor: this.state.emailFilled ? (this.state.emailOK ? "black" : "red") : "black",
			borderWidth: this.state.emailFilled ? (this.state.emailOK ? "1px" : "2px") : "1px"
		};

		{/* Password Style */ }
		const passwordStyle = {
			borderColor: this.state.pwFilled ? (this.state.pwOk ? "#ccc" : "red") : "#ccc",
			outlineColor: this.state.pwFilled ? (this.state.pwOk ? "black" : "red") : "black",
			borderWidth: this.state.pwFilled ? (this.state.pwOk ? "1px" : "2px") : "1px"
		};

		{/* Phone Number 1 Style */}
		const phoneStyle1 = {
			width: "18%", display: "inline", textAlign: "center",
			borderColor: this.state.phone1OK? "#ccc" : "red",
			outlineColor: this.state.phone1OK ? "black" : "red",
			borderWidth: this.state.phone1OK? "1px" : "2px"
		};

		{/* Phone Number 2 Style */ }
		const phoneStyle2 = {
			width: "36%", marginLeft: "5%", display: "inline", textAlign: "center",
			borderColor: this.state.phone2OK ? "#ccc" : "red",
			outlineColor: this.state.phone2OK ? "black" : "red",
			borderWidth: this.state.phone2OK ? "1px" : "2px"
		};

		{/* Phone Number 3 Style */ }
		const phoneStyle3 = {
			width: "36%", marginLeft: "5%", display: "inline", textAlign: "center",
			borderColor: this.state.phone3OK ? "#ccc" : "red",
			outlineColor: this.state.phone3OK ? "black" : "red",
			borderWidth: this.state.phone3OK ? "1px" : "2px"
		};


		return (
			<div className="UserForm">
				<h1> Sign Up</h1>
				<form onSubmit={this.handleSubmit}>
					<label>
						Email
						<input type="text" style={emailStyle}onChange={this.emailChange} />
					</label>
					<br></br>

					<label>
						Password
						<input type="password" style={passwordStyle} onChange={this.passwordChange} />
					</label>
					<br></br>

					<label>
						First Name
						<input type="text" onChange={this.fnChange} />
					</label>
					<br></br>

					<label>
						Last Name
						<input type="text" onChange={this.lnChange} />
					</label>
					<br></br>

					<label>
						Address
						<input type="text" onChange={this.addressChange} />
					</label>
					<br></br>

					<label>
						Contact Number<br></br>
						<input style={phoneStyle1} type="text" onChange={this.phoneChange1} placeholder="XX" />
						<input style={phoneStyle2} type="text" onChange={this.phoneChange2} placeholder="XXXX" />
						<input style={phoneStyle3} type="text" onChange={this.phoneChange3} placeholder="XXXX"/>
					</label>
					<br></br>
					
					<input type="submit" value="Register" />
				</form>
			</div>
		)
	}
}
export default Signup;
