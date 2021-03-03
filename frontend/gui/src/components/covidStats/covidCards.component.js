import React from 'react'
import {Card }from 'react-bootstrap';

/* 
    This component is a list of horizontally scrollable 'cards' that display details about recently 
    reported COVID cases
*/


/* Returns a 'card' that details each reported COVID case */
const CovidCard = props => (
    <div>
        <Card style={{maxWidth:"400px", minWidth:"200px",  height:"250px"}}><Card.Body>
            <Card.Title>{props.case.Venue + ", " + props.case.Suburb }</Card.Title>
            <Card.Subtitle>{props.case.Date + ": " + props.case.Time}</Card.Subtitle>
            <Card.Text>{props.case.Address}</Card.Text>    
        </Card.Body></Card>
    </div>

)

/* Create a list of scrollable, descriptive 'cards' that detail recently reported COVID cases */
export default function CovidCaseCards({cases, statusKey}){
    if (statusKey != 'monitor' && statusKey != 'isolate'){
        return (
            <div></div>
        );
    }

    /* Covid alerts can belong to two categories: monitor or self-isolate. This determines the heading 
    of the component that either displays the 'monitor' category of alerts or the 'isolate' category  */
    let titleString = statusKey == 'monitor' ? "Monitor your symptoms "  : "Self-isolate immediately "

    var numCases = cases[statusKey].length;
    let caseList = [];

    /* Create a list of information COVID alert cards that detail recent covid cases and can be scrolled horizontally */
    if (numCases > 0){
        
        var i;
        for (i=0;i<numCases;i++){
            caseList.push(<CovidCard case={cases[statusKey][i]}/>)
        }

        return (
            <div className="container-fluid py-2">
                <h2 className ="font-weight-light">{titleString + "if you have visited these locations"}</h2>
                <div className="d-flex flex-row flex-nowrap" style={{flexWrap:"nowrap", display:"flex", overflowY: "hidden" }}>
                
                    {caseList}
                
                </div>
            </div>
        );
    }

    /* If there are no cases to report, return a single card that says there are no cases to report. */
    return(
        <div>  
            <div className="container-fluid py-2">
                <h2 className ="font-weight-light">{titleString + "if you have visited these locations"}</h2>
                <div className="d-flex flex-row flex-nowrap" style={{overflowX: "auto", whiteSpace:"nowrap", flexWrap:"nowrap", display:"flex", overflowY: "hidden" }}>
                <Card><Card.Body>
                    <Card.Text>{"No cases to report."}</Card.Text>    
                </Card.Body></Card>
                </div>
            </div>
        </div>
    )
}

