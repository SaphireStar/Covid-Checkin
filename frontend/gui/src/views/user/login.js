import React from 'react';
import '../../css/userForm.css';
import axios from "axios"
import JSEncrypt from 'jsencrypt';
import cookie from 'react-cookies';


/**
 *  Log-in page allows user to login with email and password
 */


// public key
const PUB_KEY = `
MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC8rlhHdXuhIHcd84Lbw0E2V/G5
tOpVA1vpcb0HtGEkd5DKIBP/MINNE2P79k7JgIS5MmR0AdN5JQS4CANBHDFQpnRZ
AbD8OSYwe7aThL9TZS3bhagQzfEw6X6mUCcgazlBH5k2VEMXColjXM667CPaPDu6
gpfNVdj84sczK5q3DQIDAQAB`;

class Login extends React.Component {
	constructor(props) {
		super(props);

		this.state = {
			email: '1',          //user email
			password: '123456',  //user password
			id: -1               //user id
		}

		//bind functions
		this.updateEmail = this.updateEmail.bind(this);
		this.blockKeyEvent = this.blockKeyEvent.bind(this);
		this.handleTest = this.handleTest.bind(this);
		this.handleSubmit = this.handleSubmit.bind(this);
		this.updataPassword = this.updataPassword.bind(this);
	}

	// detect whether the user has logged in
	componentWillMount() {
		if (cookie.load("userId") == null) {
			this.props.setLoggedIn(-1);
		} else {
			if (!(cookie.load("userId") == -1)) {
				this.props.setLoggedIn(cookie.load("userId"));
				this.props.history.push({
					pathname: '/user/form',
				});
			}
        }
	}

	//log in
	handleSubmit(event) {
		event.preventDefault();

		// encrypt
		const jsencrypt = new JSEncrypt();
		jsencrypt.setPublicKey(PUB_KEY);

		// request
		axios({
			method: 'post',
			url: 'http://localhost:8080/elec5619/login',
			params: {
				username: this.state.email,
				password: jsencrypt.encrypt(this.state.password)
			}
		})
			.then((response) => {
				// no such user
				if (response.data.id == -1) {
					alert("No Such User, Please Register");
				}
				// wrong password
				else if (response.data.id == -2) {
					alert("Wrong Password");
				}
				// success
				else {
					// set cookies
					cookie.save("userId", response.data.id);
					cookie.save("token", response.data.token);
					this.props.setLoggedIn(response.data.id);
					
					this.props.history.push({
						pathname: '/business/search',
					});
					this.props.setLoggedIn(response.data.id);

				}
			})
			
	}

	//sign up
	handleTest() {
		console.log("Sign Up");
		this.props.history.push('/signup');
	}

	// block keyboard event when the keyCode is 13 -> Enter
	blockKeyEvent(event) {
		if (event.keyCode === 13) {
			event.preventDefault();
		}
	}

	// update email
	updateEmail(event) {
		this.setState({ email: event.target.value });

	}

	// update password
	updataPassword(event) {
		this.setState({ password: event.target.value });

	}

	render() {
		return (
			<div className="UserForm">
				<h1>Simple Check-in</h1>
				<p> One form to rule them all</p>
				<form onSubmit={this.handleSubmit}>
					<label>
						E-mail
						<br></br>
						<input type="text" onChange={this.updateEmail} onKeyDown={this.blockKeyEvent} />
					</label>
					<p></p>
					<label>
						Password
						<br></br>
						<input type="password" onChange={this.updataPassword} />
					</label>
					<p></p>
					<input type="submit" value="Log In" />
					<button style={{ marginLeft: "10%", width: "80%", marginRight: "10%"}}onClick={this.handleTest} > Sign Up</button>
				</form>
			</div>
		)
	}
}
export default Login;
