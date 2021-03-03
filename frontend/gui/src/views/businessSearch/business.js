/*
  This is an individual business page, where end-user can view all related information of business,
  also perform Checkin action.
  User can enter this business by select business from BusinessSearch page
*/ 

import React from 'react';
import axios from 'axios';
import { Container, Row, Col, Card, Button } from 'react-bootstrap';
import cookie from 'react-cookies';

class Business extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      business: null,
    };
  }
  componentDidMount() {
    const { id } = this.props.match.params
    this.setState({ isLoading: true });

    var url = new URL(`http://localhost:8080/elec5619/business/${id}`),
    params ={
     uid: cookie.load('userId'),
     token: cookie.load('token')
   }
   Object.keys(params).forEach(key => url.searchParams.append(key, params[key]))
    axios.get(url).then(response => response.data)
      .then((data) => {
        this.setState({ business: data })
      })
  }

  //user checks in 
  handleSubmit = () => {
    const { id } = this.props.match.params

    //get current user
    cookie.load('token');
    const userId = cookie.load('userId')

    var url = new URL(`http://localhost:8080/elec5619/business/${id}/${userId}`),
    params ={
     uid: cookie.load('userId'),
     token: cookie.load('token')
   }
    //post request
     axios.post(url)
      .then((response) => {
        //redirect
        this.props.history.push('/business/search')
      })
      .catch(function (error) {
          console.log("Error" + error);
      });
  }

  render() {

    const { business } = this.state;
    if (business === null) {
      return <p>Loading ...</p>;
    }

    return (
      <Container className="bizContainer">
        <Row>
          <Col>
            <Card className="mx-auto p-3 mt-2">
              <Card.Body >
                <Card.Title className="bizName ">{business.businessName}</Card.Title>
                <Card.Subtitle className="mb-2 text-muted">{business.address}</Card.Subtitle>
                <div className="contentWrapper">
                  <div className="detailWrapper">
                    <dl className="row p-1">
                      <dt className="col-6">Postcode</dt>
                      <dd className="col-6">{business.postcode}</dd>
                      <dt className="col-6">Contact Email</dt>
                      <dd className="col-6">{business.businessEmail}</dd>
                      <dt className="col-6">Contact Number</dt>
                      <dd className="col-6">{business.phoneNumber}</dd>
                      <dt className="col-6">Capcity</dt>
                      <dd className="col-6">{business.capacity}</dd>
                    </dl>
                  </div>
                  <div className="imgWrapper">
                    <img src={business.photo} />
                  </div>
                </div>
                <Row>
                  <Col>
                    <div className="checkInButton">
                      <Button variant="primary" onClick={this.handleSubmit}> Check In </Button>
                    </div>
                  </Col>
                </Row>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Container>
    );
  }
}
export default Business;