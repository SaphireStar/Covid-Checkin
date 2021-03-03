<p>
  <img src="frontend/gui/public/logo192.png" alt="logo" width="10%">
</p>
<h1>
  Covid Check In 
</h1>

**Covid Check** In is a web platform that allows businesses to keep track of the customers that visit their business premises. 

## Motivation
As part of the effort to help control the spread of COVID-19, most businesses in NSW require to collect customer details for contact tracing purposes. If a visitor tests positive, NSW Health will then be able to alter other visitors who many have been in contact with that person. The old fashion way of physical records has drawbacks such as hygenie, human error, management, and requies staff monitor whether your customers are filling out the form etc. However an electronic solution can resolves all above issues, to make the check-in and tracking process much more efficient.

## Features
* Provide a standardised check-in process for a variety of businesses.
* Streamline the check-in process to save users time.
* Provide a reliable way for businesses to track metrics.
* Provide an effective way to notify customers of potential contact risks.
* Managing customer check-in across multiple venus.
* Up-to-date Covid-19 Statistics.

## Technologies 
* MySQL 8.0.21
* STS 3.9.13
* React 16.13.1
* JDK 8
* Apache 8.5.59

## How to run
1. Clone this repo use `git clone <repo url>`.
2. Setup MySQL server locally with username `root` with pasword `root`.
4. To ensure we all have a same DB for development, plase use `backend/databaseSetup.sql` to set up the schema before you start dev. *(more detail instruction can be found inside the file)*
3. Open backend project in STS IDE.
4. Run backend on the Tomcat servet, default run on `localhost:8080`. *(You need to manually rebuild the project and run on the server if you manke changes to the code)*
5. Open frontend project in terminal by navigating to frontend/gui 
6. Run the commands `npm install` to install dependencies.
7. Run `npm start` to run the app in development mode, default run on `localhost:3000`. *(The page will automatically reload if you make changes to the code)*

