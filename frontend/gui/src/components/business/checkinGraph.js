import React from 'react'
import '../../css/business.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import axios from "axios";
import { Bar } from 'react-chartjs-2';
import Dropdown from 'react-bootstrap/Dropdown';
import DropdownButton from 'react-bootstrap/DropdownButton';


  const options = {
    legend: {
        display: false
    },
    tooltips: {
        callbacks: {
            label: function(tooltipItem) {
                    return tooltipItem.yLabel;
            }
        }
    }
};

/**
 * CheckinGraph
 * - Displays the hourly checkin data for a business from the last week
 * - uses React-ChartJS-2
 * - Dynamic count of check in numbers is displayed next to capacity.
 */
class CheckinGraph extends React.Component {

	constructor(props) {
        super(props);
        this.handleClick = this.handleClick.bind(this);
        this.formData = new FormData();
        this.state = {
            businessId : 0,
            capacity:0,
            checkedIn:0,
            data : null,
            dateRange : [],
            day : null,
            today : false,
            stats:[],
            isToggle: false,            
            chartData : {
                labels: ['12am','1am', '2am', '3am', '4am','5am', '6am', 
                        '7am', '8am', '9am', '10am', '11am', '12pm', 
                        '1pm', '2pm', '3pm', '4pm,','5pm', '6pm', 
                        '7pm', '8pm', '9pm', '10pm', '11pm'],
                datasets: [
                  {
                    label: '',
                    fill: false,
                    lineTension: 0.1,
                    backgroundColor: 'rgba(75,192,192,0.6)',
                    borderColor: 'rgba(75,192,192,1)',
                    borderCapStyle: 'butt',
                    borderDash: [],
                    borderDashOffset: 0.0,
                    borderJoinStyle: 'miter',
                    pointBorderColor: 'rgba(75,192,192,1)',
                    pointBackgroundColor: '#fff',
                    pointBorderWidth: 1,
                    pointHoverRadius: 5,
                    pointHoverBackgroundColor: 'rgba(75,192,192,1)',
                    pointHoverBorderColor: 'rgba(220,220,220,1)',
                    pointHoverBorderWidth: 2,
                    pointRadius: 1,
                    pointHitRadius: 10,
                    data: [0]
                  }
                ]
              }
        }
    }
    // Update the displayed data based on the date selected.
    // Change to "Today" if the current date is selected.
    handleClick(event) {
        const target = event.target.getAttribute('value');
        var dataset = this.state.chartData
        var tempStats = []
        var tempToday = false;
        if (target === this.state.dateRange[0]) {
            tempToday = true;
        }
        Object.keys(this.state.data[target]).map((key) => {
            tempStats = [...tempStats, this.state.data[target][key]]
        })
        var count = 0;
        tempStats.forEach(item=>count+=item);
        dataset.datasets[0].data = tempStats;
        this.setState({
            checkedIn: count,
            today : tempToday,
            stats:tempStats,
            day : target,
            chartData : dataset
        })
    }

    componentDidMount() {
          const apiUrl = `http://localhost:8080/elec5619/business/${this.props.businessId}/checkins`;
          axios.get(apiUrl)
            .then((response) => this.setState({ 
                businessId : this.props.businessId,
                capacity : this.props.capacity,
                data:response.data,
                }))
            // Map data returned from server to the chart-js component format
            .then(() => {
                Object.keys(this.state.data).map((key,i) => {
                    this.setState({ dateRange: [...this.state.dateRange, key] })
                })
                this.setState({day:this.state.dateRange[0]})
            })
            .then(()=>{
                var dataset = this.state.chartData
                Object.keys(this.state.data[this.state.day]).map((key) => {
                    this.setState({ stats: [...this.state.stats, this.state.data[this.state.day][key]]})
                })
                var count = 0;
                this.state.stats.forEach(item=>count+=item);
                dataset.datasets[0].data = this.state.stats;
                this.setState({
                    today : true,
                    isToggle:true,
                    chartData : dataset,
                    checkedIn: count
                })

            })
        }
    render() {
        if (!this.state.data) {
            return <div />
        }           
        return (
            <div className='chartWrapper'>
                <div class="metrics">
                    <table>
                        <tr>
                            <th>Capacity</th>
                            <th>Checked-In</th>
                        </tr>
                        <tr>
                            <td>{this.state.capacity}</td>
                            <td>{this.state.checkedIn}</td>
                        </tr>
                    </table>
                </div>                
                <div class="dropDown">
                    <DropdownButton id="dropdown-menu" title={this.state.today? "Today" : this.state.day}>
                        {this.state.dateRange.map((date) => {
                        if(date === this.state.dateRange[0]) {
                            return (
                                <Dropdown.Item class="dropdown-item" 
                                value = {date} onClick={this.handleClick}>
                                    Today
                                </Dropdown.Item>
                            );
                        }
                        return (
                            <Dropdown.Item class="dropdown-item" 
                            value = {date} onClick={this.handleClick}>
                                {date}
                            </Dropdown.Item>
                        );
                        })}
                    </DropdownButton>
                </div>            
                <Bar
                    data={this.state.chartData}
                    width={100}
                    height={50}
                    options={options}
                /> 
            </div>
        )
    }
}
export default CheckinGraph;