import React from 'react'
import {Card, Container,  Accordion, Tab, Tabs, Button}from 'react-bootstrap';

/* 
    Creates a list of COVID Cases inside a drop-down 'accordion component, which is divided according 
    to the alert category of the reported case: 'monitor' or 'self-isolate'
*/

/* A drop-down list that demonstrates the details of recently reported COVID cases */
const LocationListAccordion = props => (
    <Accordion defaultActiveKey="0">
        <Card>
            <Card.Header>
            <Accordion.Toggle as={Button} variant="link" eventKey="1">
                See full list of locations affected by COVID-19
            </Accordion.Toggle>
            </Card.Header>
            <Accordion.Collapse eventKey="1">
                <Container>
                    <Tabs defaultActiveKey="isolate" transition={false}>
                        <Tab eventKey="isolate" title="Isolate">
                            <Card.Body className="my-2" style={{maxHeight:"500px", overflowY:"scroll"}}>{props.isolate}</Card.Body>
                        </Tab>
                        <Tab eventKey="monitor" title="Monitor">
                            <Card.Body className="my-2"  style={{maxHeight:"500px", overflowY:"scroll"}}>{props.monitor}</Card.Body>
                        </Tab>
                    </Tabs>
                </Container>
            
            </Accordion.Collapse>
        </Card>
    </Accordion>   

)

/* An element that contains a description of each COVID case */
const LocDescription = props => (
    <div>
        <br/>
        <h6>{props.case.Venue + ", " + props.case.Suburb }</h6>
        <h7>{props.case.Date + ": " + props.case.Time}</h7>
        <br/>
        <h8>{props.case.Address}</h8>  
        <br/>
    </div>

)

/* Creates a list of locations inside a drop-down 'accordion' component defined in LocationListAccordion*/
export default function LocationList(cases){

    /* Retrieve the cases that require monitoring symptoms */
    let monitor = cases.cases['monitor'];
    var numMonitor = 0;
    if (typeof monitor !== "undefined"){
        var numMonitor = monitor.length;
    } 

    /* Retrieve the cases that require self-isolation */
    let isolate = cases.cases['isolate'];
    var numIsolate = 0;
    if (typeof isolate !== "undefined") {
        var numIsolate = isolate.length;
    }

    /* Define a JSON object containing the two lists of case types for easier referencing */
    let caseList = {};
    caseList['isolate'] = [];
    caseList['monitor'] = [];

    /* Create a list of descriptions to be read within the Accordion drop-down component */
    if (numMonitor + numIsolate > 0){
        
        var i;
        if (numMonitor == 0){
            caseList['monitor'].push(
                <div>
                    <p>No cases to report</p>
                </div>
            )
        } else {
            for (i=0;i<numMonitor;i++){
                caseList['monitor'].push(<LocDescription case={monitor[i]}/>)    
            }
        }

        if (numIsolate == 0){
            caseList['isolate'].push(
                <div>
                    <p>No cases to report</p>
                </div>
            )
        } else {
            for (i=0;i<numIsolate;i++){
                caseList['isolate'].push(<LocDescription case={isolate[i]}/>)    
            }
        }
        
        /* Return the accordion drop-down containing the description of all the cases */
        return (
            <div className="container-fluid py-2">
                <LocationListAccordion monitor={caseList['monitor']} isolate={caseList['isolate']}/>
            </div>

        );

    /* If there are no cases to report, return an empty list */
    } else {
        return (
            <Accordion defaultActiveKey="0">
                <Card>
                    <Card.Header>
                    <Accordion.Toggle as={Button} variant="link" eventKey="1">
                        See full list of locations affected by COVID-19
                    </Accordion.Toggle>
                    </Card.Header>
                    <Accordion.Collapse eventKey="1">
                    <Card.Body>No cases currently reported.</Card.Body>
                    </Accordion.Collapse>
                </Card>
            </Accordion>    

        );
    }

}