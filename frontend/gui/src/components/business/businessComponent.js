import React from 'react'
import '../../css/business.css';
import NotifyWindow from './notifyWindow.js';
import CheckinGraph from './checkinGraph.js';
import 'bootstrap/dist/css/bootstrap.min.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faEdit } from '@fortawesome/free-solid-svg-icons';

/**
 * BusinessComponent
 * - The main component shown on the business metrics page
 * - houses a CheckinGraph and Notify Component
 */
class BusinessComponent extends React.Component {
	constructor(props) {
		super(props);
        this.state = {
            business : null
        }
    }
    componentDidMount() {
        this.setState({business:this.props.business})
    }    
    render() {
        if (!this.state.business) {
            return <div />
        }        
        return (
            <div class="BusinessComponent">
                <h1>{this.state.business.businessName}</h1>
                    <h2>{this.state.business.address}&nbsp;
                    {this.state.business.suburb}&nbsp;
                    {this.state.business.postcode}&nbsp;&nbsp;
                    <a  href={'/business/' + this.state.business.id + '/edit'}><FontAwesomeIcon icon={faEdit}/></a>
                    </h2>
                <CheckinGraph businessId={this.state.business.id} capacity = {this.state.business.capacity} />
                <div class = "notifyWrapper">
                <NotifyWindow businessId = {this.state.business.id} /> 
                </div>
            </div>
        )
    }
};

export default BusinessComponent;