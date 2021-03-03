import React, {useState} from 'react';
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Redirect
} from "react-router-dom";
import cookie from "react-cookies";
import Login from './views/user/login';
import LogOut from './views/user/logout';
import Signup from './views/user/signup';
import Form from './views/user/form';
import VisitHistory from './views/user/visithistory';
import BusinessRegister from './views/businessManage/businessRegister';
import BusinessPage from './views/businessManage/businessPage';
import BusinessManage from './views/businessManage/businessManage';
import BusinessEdit from './views/businessManage/businessEdit';
import Navbar from './components/navbar';
import Business from './views/businessSearch/business';
import BusinessSearch from './views/businessSearch/BusinessSearch';
import CovidStats from './views/covidStats.view'
import './App.css';
import './style.css';

function App() {
  // used to trigger re-rendering of navbar for logged in / out users.
  const [loggedIn, setLoggedIn] = useState(cookie.load('userId'));  
  return (
      <Router>
          <Navbar {...{loggedIn}}/>
          <Switch>
            <Route exact path="/covid19-info" component={CovidStats}/>
            <Route exact path="/business/register" component={BusinessRegister} />
            <Route exact path="/business/:id/edit" component={BusinessEdit} />
            <Route path="/business/search" exact render={
              (routeProps) => <BusinessSearch {...{setLoggedIn, ...routeProps}} />
            } />                    
            <Route exact path="/business/:id" component={BusinessPage} />
            <Route path="/biz/:id" component={Business} />
            <Route exact path="/business/manage/:id" component={BusinessManage} />
		    	  <Route exact path="/logout" exact render={
              (routeProps) => <LogOut {...{setLoggedIn, ...routeProps}} />
            } />
            <Route path="/" exact render={
              (routeProps) => <Login {...{setLoggedIn, ...routeProps}} />
            } />
            <Route path="/signup" component={Signup} />
            <Route path="/user/form" component={Form} /> 
            <Route path="/user/history" component={VisitHistory} />
            <Redirect to="/" />
          </Switch>
      </Router>

  );
}
export default App;
