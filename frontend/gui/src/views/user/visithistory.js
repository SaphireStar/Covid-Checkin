import React from 'react';
import '../../css/visithistory.css';
import axios from "axios"
import cookie from 'react-cookies';


/**
 *  This page allows user to view their recent visit history
 */


const rows = [];  // table rows to be render
var history = new Array(); // data array
var num = -1;   // num of rows
var flag = false;

// red text style 
const redStyle = {
    color: "red"
};

// green text style 
const greenStyle = {
    color: "green"
};

class VisitHistory extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
            id: cookie.load('userId'),
            msg: false
        }
    }

    // fetch visit history
    componentWillMount() {
        axios({
            method: "GET",
            url: 'http://localhost:8080/elec5619/history',
            params: {
                id: this.state.id,
                token: cookie.load('token')
            }
        })
            .then((response) => {
                num = response.data.num;
                console.log(response);
                for (let y = 0; y < num; y += 1) {
                    var arr = new Array();
                    arr[0] = response.data.records[y].date;
                    arr[1] = response.data.records[y].location;
                    arr[2] = response.data.records[y].risk;
                    history[y] = arr;
                }
                this.setState({
                    msg: true
                });
            })
            .catch(function (error) {
                console.log("Error" + error);
            });
    }


    render() {
        if (this.state.msg) {
            if (!flag) {
                for (let y = 0; y < num; y += 1) {
                    var date = history[y][0];
                    var location = history[y][1];
                    var risk = history[y][2];
                    if (risk == "false") {
                        rows.push(
                            <tr>
                                <th className='Date' style={greenStyle}>{date}</th>
                                <th className='location' style={greenStyle}>{location}</th>
                            </tr>
                        );
                    } else {
                        rows.push(
                            <tr>
                                <th className='Date' style={redStyle}>{date}</th>
                                <th className='location' style={redStyle}>{location}</th>
                            </tr>
                        );
                    }
                    
                }
                flag = true;
            }
            return (
                <div>
                    <div className='bodyContent'>
                        <div id="history">
                            <h1> Visit History</h1>
                            <div></div>
                            <table>
                                <tr>
                                    <th className='Date' >Date</th>
                                    <th className='location'>Place</th>
                                </tr>
                                {rows}
                            </table>
                        </div>
                    </div>
                </div>
            )
        } else {
            return (<div></div>);
        }
    }

}
export default VisitHistory;
