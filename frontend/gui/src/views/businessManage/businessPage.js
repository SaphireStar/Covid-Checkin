import React from 'react';
import '../../css/business.css';
import BusinessComponent from '../../components/business/businessComponent';
import cookie from "react-cookies";

const qs = require('querystring')

/**
 * BusinessPage
 * - The main page for displaying a business
 * - Parent to the BusinessComponent
 */
class BusinessPage extends React.Component {

	constructor(props) {
		super(props);
        this.state = {
            business : null
        }
    }

    componentDidMount() {
      const { id } = this.props.match.params
        // get details of specific business
        var url = new URL(`http://localhost:8080/elec5619/business/${id}`),
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
        // pass details to business component
        return ( 
          <div id="BusinessPageWrapper">
            <div className ="businessComponentWrapper">
            <BusinessComponent business={this.state.business} />
            </div> 
          </div>
        )
      }
    }
export default BusinessPage;