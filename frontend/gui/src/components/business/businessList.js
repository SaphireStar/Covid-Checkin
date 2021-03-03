import React from 'react'
import '../../css/business.css';
import ListGroup from 'react-bootstrap/ListGroup';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import 'bootstrap/dist/css/bootstrap.min.css';

/**
 * Business List
 * - Generates a list of all business provided from parent
 * - Clicking elements will redirect to the statistics page
 */
class BusinessList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
          businesses: [],
          refresh: false
        }
      }

    componentDidMount() {
        this.setState({
            businesses:this.props.businesses,
        })
      }
    render(){
    return (
            <div className='BusinessList'>
                <h1>Your Businesses</h1>
                <Row>
                    <Col>
                        <ListGroup  className="card">
                        {this.state.businesses.map((business, i) => (
                            <ListGroup.Item className="card-body" action href={'/business/' + business.id}>
                                <img key={Date.now()} className="card-img" src={business.photo}></img>
                                <p className="card-title">{business.businessName}</p>
                                <p className="card-address">{business.address} {business.postcode}</p>
                                <p className="card-contact">{business.businessEmail} {business.phoneNumber}</p>
                                <p className="card-details">Capacty: {business.capacity} </p>
                            </ListGroup.Item>
                        ))}
                        <ListGroup.Item className="card-body-register" action href={'/business/register'}>
                            <p>Register New Business</p>
                        </ListGroup.Item>
                        </ListGroup>
                    </Col>
                </Row>            
            </div>
        )
    }
};

export default BusinessList;