import React, {useState} from 'react'
import {Navbar, NavDropdown, Nav, Form, Button, FormControl} from 'react-bootstrap';
import cookie from 'react-cookies';
import { render } from 'react-dom';

/**
 * NavBar
 * - Dynamic Navbar, that will change what is displayed based on user login.
 */
class navBar extends React.Component {
    constructor(props) {
        super(props);

      this.notify = this.notify.bind(this);
    }
    // Onclick event to jump to Message page
    notify() {
        if (cookie.load("userId") == null) {
            alert("Please log in");
        }else{
            cookie.load('token');
            window.location.href="http://localhost:8080/elec5619/notification/user/" + cookie.load('userId');
        }
    }
    render() { 
    if(this.props.loggedIn < 0){
        return (
            <Navbar bg="primary" variant="dark" expand="lg">
            <Navbar.Brand href="/">Covid Check In</Navbar.Brand>
            <Navbar.Toggle aria-controls="basic-navbar-nav" />
            </Navbar>
        )
    } else {
        return (
            <Navbar bg="primary" variant="dark" expand="lg">
            <Navbar.Brand href="/">Covid Check In</Navbar.Brand>
            <Navbar.Toggle aria-controls="basic-navbar-nav" />
            <Navbar.Collapse id="basic-navbar-nav">
                <Nav className="mr-auto">
                <Nav.Link href="/user/form">Form</Nav.Link>
                <Nav.Link href={'/user/history'}>Visit History</Nav.Link>
                <Nav.Link href="/business/search">Search</Nav.Link>
                <Nav.Link href={'/business/manage/'+this.props.loggedIn}>Manage Business</Nav.Link>
                <Nav.Link href="/Covid19-info">COVID19 Tracker</Nav.Link>
                <Nav.Link onClick={this.notify}>Messages</Nav.Link >
                <Nav.Link href="/logout">Log Out</Nav.Link>
                </Nav>
            </Navbar.Collapse>
            </Navbar>
        )        
    }
  }
}

export default navBar;
