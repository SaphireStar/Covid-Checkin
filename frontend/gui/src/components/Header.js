import React from 'react';
import { Navbar, Nav } from 'react-bootstrap';

class Header extends React.Component {
    render() {
        return (
            <Navbar bg="primary" variant="dark">
                <Navbar.Brand href="/">Home</Navbar.Brand>
                <Nav className="mr-auto">
                    <Nav.Link href="/business/search">Business</Nav.Link>
                    <Nav.Link href="/Covid19-info">COVID19 Tracker</Nav.Link>
                </Nav>
            </Navbar>
        )
    }
}

export default Header;