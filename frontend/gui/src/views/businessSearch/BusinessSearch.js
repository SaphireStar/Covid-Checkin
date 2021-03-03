import React from 'react';
import axios from 'axios';
import { Container, Row, Col, Card, Form, FormControl, InputGroup, DropdownButton, Dropdown } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import '../../css/business.css'
import cookie from 'react-cookies';

//search and checkin to buisness 
/*
  users can search the businesses in the Businsess table through a serch bar. 
  Buisness matching the serach will  reamain on the page. once clicked the user 
  will be redirected to the checkin page for the selected buisness.
  
  the check in page displays the buinesses details.On the checkin page if the 
  user clicks the check in button their details and the buisness bage that they 
  are on will be posted onto the Visit table, along with the date and time of 
  the post.
*/ 

function BizCard({ biz }) {
  return (
    <div className="bizCard">
      <Card>
        <Link to={'/biz/' + biz.id} >
          <Card.Body>
            <img className="card-img" src={biz.photo}></img>
            <Card.Title>{biz.businessName}</Card.Title>
            <Card.Subtitle className="mb-2 text-muted">{biz.postcode}</Card.Subtitle>
            <Card.Text>{biz.address}</Card.Text>
          </Card.Body>
        </Link>
      </Card>
    </div>
  )
}

var BizList = (list) => (
  list.data.map(biz => {
    return (
      <BizCard key={biz.id} biz={biz} />
    )
  })
)

class BusinessSearch extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      bizList: null,
      filted: null,
      search: 'Business Name'
    };
    this.handleChange = this.handleChange.bind(this);
    this.props.setLoggedIn(cookie.load('userId'));      
  }

  componentDidMount() {
    this.setState({ isLoading: true });

    var url = new URL(`http://localhost:8080/elec5619/business/`),
    params ={
     uid: cookie.load('userId'),
     token: cookie.load('token')
   }
   Object.keys(params).forEach(key => url.searchParams.append(key, params[key]))       
    axios.get(url).then(response => response.data)
      .then((data) => {
        console.log(data)
        this.setState({ bizList: data })
        this.setState({ filted: data })
      })
  }

  handleChange(e) {
    e.preventDefault()
    console.log(e.target.value)
    let currentList = [];
    let newList = [];
    if (e.target.value !== "") {
      console.log(this.state.search)
      currentList = this.state.bizList;
      if (this.state.search == "Postcode") {
        newList = currentList.filter(item => {
          const lc = item.postcode;
          const filter = e.target.value
          return lc == filter;
        });
      } else {
        newList = currentList.filter(item => {
          const lc = item.businessName.toLowerCase();
          const filter = e.target.value.toLowerCase();
          return lc.includes(filter);
        });
      }
    } else {
      // If the search bar is empty, set newList to original task list
      newList = this.state.bizList;
    }
    // Set the filtered state based on what our rules added to newList
    this.setState({
      filted: newList
    });
  }

  render() {
    const { filted } = this.state;
    if (filted === null) {
      return <p>Loading ...</p>;
    }

    return (
      <Container className="bizContainer">
        <Row className="bizList-title">
          <Col><h2>Find Business in Sydney</h2></Col>
        </Row>
        <div >
          <Form onSubmit={this.onSubmit} >
            <InputGroup className="mb-3">
              <DropdownButton
                variant="outline-primary"
                title={this.state.search}
                id="input-group-dropdown-1"
              >
                <Dropdown.Item value="businessName" onClick={(e) => this.setState({ search: e.target.textContent })}>Business Name</Dropdown.Item>
                <Dropdown.Item value="postcode" onClick={(e) => this.setState({ search: e.target.textContent })}>Postcode</Dropdown.Item>
              </DropdownButton>
              <FormControl
                placeholder="Search for business"
                className="mr-sm-2"
                onChange={e => this.handleChange(e)} />
            </InputGroup>
          </Form>
        </div>
        <div className="bizList" >
          <BizList data={this.state.filted} />
        </div>
      </Container>
    );
  }
}

export default BusinessSearch;