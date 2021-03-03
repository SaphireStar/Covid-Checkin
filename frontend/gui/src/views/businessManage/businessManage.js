import React from 'react';
import '../../css/business.css';
import BusinessComponent from '../../components/business/businessComponent';
import BusinessList from '../../components/business/businessList';
import cookie from "react-cookies";

/**
 * BusinessManage
 * - Main page for the list of businesses owned by a user.
 * - also provides option for registering new businesses.
 */
class BusinessManage extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      business: null,
      refresh:false,
    }
  
  }

  componentDidMount() {
    const { id } = this.props.match.params
      // fetch all businesses owned by this user
      var url = new URL(`http://localhost:8080/elec5619/business/${id}/all`),
       params ={
        uid: cookie.load('userId'),
        token: cookie.load('token')
      }
      Object.keys(params).forEach(key => url.searchParams.append(key, params[key]))      
      fetch(url)
        .then((response) => response.json())
        .then((data) => this.setState({ business: data }))
  }

  render() {
    if (!this.state.business) {
      return <div />
    }
    // Generate a list of all the businesses for this user
    return ( 
      <div>
        <BusinessList businesses = {this.state.business} />
      </div>
    )
  }
}
export default BusinessManage;