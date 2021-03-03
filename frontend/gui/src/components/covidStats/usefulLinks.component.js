import React from 'react'
import {Card, CardGroup }from 'react-bootstrap';

/* Return a container with multiple 'cards' providing links to useful informational resources about COVID */
export default function UsefulLinks(){
    return(
        
        <div className="container-fluid py-2">
            <h2>Useful Information about COVID-19 (Coronavirus)</h2>
            <br/>
            <CardGroup>
                <Card>
                    <Card.Body>
                    <Card.Title><Card.Link href="https://www.health.nsw.gov.au/Infectious/covid-19/Pages/stats-nsw.aspx">NSW Covid Case Statistics</Card.Link></Card.Title>
                    <Card.Text>
                    Sources of COVID-19 diagnosed in NSW in last 24 hours, last 7 days and to date.
                    </Card.Text>
                    </Card.Body>
   
                </Card>
                <Card>
                    <Card.Body>
                    <Card.Title><Card.Link href="https://www.health.gov.au/news/health-alerts/novel-coronavirus-2019-ncov-health-alert/how-to-protect-yourself-and-others-from-coronavirus-covid-19">How to protect yourself and others</Card.Link></Card.Title>
                    <Card.Text>
                        Recommendations by the Australian Government about the important steps you should take to help reduce the spread of coronavirus and protect yourself and those who are most at risk. 
                    </Card.Text>
                    </Card.Body>

                </Card>
                <Card>
                    <Card.Body>
                    <Card.Title><Card.Link href="https://www.healthdirect.gov.au/coronavirus-covid-19-seeing-a-doctor-getting-tested-faqs">How to get tested</Card.Link></Card.Title>
                    <Card.Text>
                        A set of information sheets to help you understand whether you need to be tested, and how you can seek testing.
                    </Card.Text>
                    </Card.Body>

                </Card>
                <Card>
                    <Card.Body>
                    <Card.Title><Card.Link href="https://www.health.gov.au/resources/collections/novel-coronavirus-2019-ncov-resources">COVID19 Resources</Card.Link></Card.Title>
                    <Card.Text>
                    A collection of information sheets, links to apps and other resources for the general public and industry to help you stay informed and share important messages.
                    </Card.Text>
                    </Card.Body>

                </Card>
                </CardGroup>
            </div>
            
    )
}