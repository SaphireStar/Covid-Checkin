import React from 'react';
import { withGoogleMap, GoogleMap, withScriptjs, InfoWindow, Marker } from "react-google-maps";
import Geocode from "react-geocode";
import Autocomplete from 'react-google-autocomplete';
const { MarkerWithLabel } = require("react-google-maps/lib/components/addons/MarkerWithLabel");
Geocode.setApiKey("AIzaSyAP3E8YCjc-W9rB24fdrCc5BoDN3MU8GBw");
Geocode.enableDebug();

/* Component that displays the locations of recently reported COVID cases as Red markers */
class CovidMap extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            zoom: 12,
            height: 400,
            mapPosition: {
                lat: 0,
                lng: 0,
            },
            covidLocations: props.covidLocations,
            info: false,
            selectedLoc: null,
            activeMarker: {}
        }
    };


    /* Set the default position of the mat using the Geocode API */
    componentDidMount() {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(position => {
                this.setState({
                    mapPosition: {
                        lat: position.coords.latitude,
                        lng: position.coords.longitude,
                    },
                },
                    () => {
                        Geocode.fromLatLng(position.coords.latitude, position.coords.longitude).then(
                            response => {
                                console.log(response)
                            },
                            error => {
                                console.error(error);
                            }
                        );

                    })
            });
        } else {
            console.error("Geolocation is not supported by this browser!");
        }
    };

    /* update state when any variable changes and automatically update rendered map */
    onChange = (event) => {
        this.setState({ [event.target.name]: event.target.value });
    };

    /* When a location marker is selected, update the map center position */
    onPlaceSelected = (place) => {
        console.log('plc', place);
        
        if (typeof place.geometry !== "undefined"){
            let lat = place.geometry.location.lat();
            let lng = place.geometry.location.lng();

            this.setState({
                mapPosition: {
                    lat: lat,
                    lng: lng
                },
            })
        }
    };

    /* Create NSWCovidMap element and render it */
    render() {

        const NSWCovidMap = withScriptjs(
            withGoogleMap(
                props => (
                    <GoogleMap
                        defaultZoom={this.state.zoom}
                        defaultCenter={{ lat: this.state.mapPosition.lat, lng: this.state.mapPosition.lng }}
                    >
                        {/* Create a marker for each covid location */}
                        {
                            this.state.covidLocations.map(loc => (
                                <Marker
                                    key={loc.Venue}
                                    position = {{
                                        lat: loc.Lat,
                                        lng: loc.Lon
                                    }}

                                    onClick={() => {
                                        this.setState({
                                            info: true,
                                            selectedLoc: loc,
                                            mapPosition: {
                                                lat: loc.Lat,
                                                lng: loc.Lon
                                            },
                                        })
                                    }}
                                />
                                
                            ))
                        }

                         {/* When a marker is clicked, display info about the covid case at that location */}
                        {this.state.info && this.state.selectedLoc.Venue && (
                            <InfoWindow
                                onCloseClick={()=>{
                                    this.state.selectedLoc = null;
                                    this.state.info = false;
                                }}
                                position={{
                                    lat: this.state.selectedLoc.Lat,
                                    lng: this.state.selectedLoc.Lon
                                }}
                            >
                                
                                <div>
                                    <h1>{this.state.info}</h1>
                                    <h3>{this.state.selectedLoc.Venue}</h3>
                                    <h4>{"Alert: " + this.state.selectedLoc.Alert}</h4>
                                    <h5>{"Suburb: " + this.state.selectedLoc.Suburb}</h5>
                                    <h5>{this.state.selectedLoc.Date}</h5>
                                    <h5>{this.state.selectedLoc.Time}</h5>
                                </div>
                                                    
                            </InfoWindow>
                        )}
        
                        {/* Address Autocomplete search bar to query the map and center on a new location */}
                        <Autocomplete
                            style={{
                                width: '100%',
                                height: '40px',
                                paddingLeft: '16px',
                                marginTop: '2px',
                                marginBottom: '2rem'
                            }}
                            onPlaceSelected={this.onPlaceSelected}
                            componentRestrictions={{country:'au'}}
                            types={['(regions)']}
                            
                        />
                    </GoogleMap>
                )
            )
        );
        
        /* Render Map */
        return (
            <div>
                <h2>Nearby Cases</h2>
                <NSWCovidMap
                    googleMapURL="https://maps.googleapis.com/maps/api/js?key=AIzaSyAP3E8YCjc-W9rB24fdrCc5BoDN3MU8GBw&libraries=places"
                    loadingElement={
                        <div style={{ height: `100%` }} />
                    }
                    containerElement={
                        <div style={{ height: this.state.height }} />
                    }
                    mapElement={
                        <div style={{ height: `100%` }} />
                    }
                />
            </div>
        )
    }

}

export default CovidMap;

