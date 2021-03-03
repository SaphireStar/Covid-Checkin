import React, {Component} from 'react'
import axios from 'axios';
import { Container, Col, Row }from 'react-bootstrap';
import CovidMap from '../components/covidStats/statsmap.component';
import UsefulLinks from '../components/covidStats/usefulLinks.component';
import LocationList from '../components/covidStats/locationList.component';
import CovidCaseCards from '../components/covidStats/covidCards.component';

/* 
    Page that displays Covid Statistics including the locations of nearby reported cases and the details 
    about each case. It consists of:
        1) a map (implementing Google Maps API) displaying nearby cases
        2) A (Goole API) autocomplete address search function that allows users to move the map to a desired location
        3) details about each case (using the NSW.data COVID cases API) including advice to monitor symptoms or self-isolate
        4) links to useful resources about COVID
*/
export default class CovidStats extends Component {
    constructor(props){
        super(props);
        this.state = {
            cases: null,
            loading: true
        }
    };

    async componentDidMount(){

        /* Retrieve Data from Data.NSW COVID Cases API */
        await axios.get('https://data.nsw.gov.au/data/dataset/0a52e6c1-bc0b-48af-8b45-d791a6d8e289/resource/f3a28eed-8c2a-437b-8ac1-2dab3cf760f9/download/venue-data.json')
            .then(response => {
                this.setState({loading:false })
                this.setState({cases: response.data})
                
            })
            .catch((error) => {
                console.log(error)
            })
    }

    
    render(){
        
        /* While the data is being fetched, displays an empty page */
        if (this.state.loading || this.state.cases == null ) {return (<div></div>)}

        /* After the data has been loaded, render the page view and pass in the case data into the constructor */
        return(
            <div>
                <CovidStatsView cases={this.state.cases.data}/>
            </div>
        );
    }
}





/* Returns scrollable list of 'cards' displaying the details of recent COVID cases */
function CovidStatsView(cases){
    let caseData = cases.cases;
    return(
        <Container>
            <Row>
                <Col><h1 className="mt-5" style={{color:"#3399ff",textAlign:"center", margin: "10px auto"}}>Recent COVID19 Cases in NSW</h1></Col>
            </Row>
            <div className="container-fluid">
                <Container>
                    <CovidMap covidLocations={caseData.isolate.concat(cases.cases.monitor)}/>  
                </Container>
                <br/> 
                <br/> 
                <br/>

                <CovidCaseCards cases={caseData} statusKey={'isolate'}/>
                <CovidCaseCards cases={caseData} statusKey={'monitor'}/>
                <LocationList cases={caseData}/>
                <br/>

                <UsefulLinks/>
                <br/>
            </div>
        </Container>
        
    )
}
