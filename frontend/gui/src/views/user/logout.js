import React from 'react';
import cookie from 'react-cookies';

/**
 * This page would log out user and redirect to the login page
 */
class LogOut extends React.Component {
	constructor(props) {
		super(props);
	}
	componentWillMount() {
		// set cookies
		cookie.save("userId", -1 );
		cookie.save("token", "-1");
		this.props.setLoggedIn(-1);
		this.props.history.push({
			pathname: '/',
		});
    }


	render() {
		return (
			<div>
				<h1>Successfully Logged Out </h1>
			</div>
		)
	}
}
export default LogOut;