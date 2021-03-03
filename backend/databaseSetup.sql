-- --------------------------------------------------
-- Generates the Database tables required for the covid check-in web interface.
-- Requires an existing database 'SpringApp' in mysql.
-- To use the file, via the command line run:
---- "mysql -u root -p springapp < ~/...path to repo.../elec5619-checkin/backend/databaseSetup.sql"
-- --------------------------------------------------

USE springapp;
GO

-- --------------------------------------------------
-- Drop Existing Tables (foreign key checks disabled)
-- --------------------------------------------------
SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS VisitRecord;
DROP TABLE IF EXISTS Business;
DROP TABLE IF EXISTS Notifications;
DROP TABLE IF EXISTS CovidCases;
DROP TABLE IF EXISTS Suburb;
SET FOREIGN_KEY_CHECKS=1;
GO

-- --------------------------------------------------
-- Create Tables
-- --------------------------------------------------
CREATE TABLE Users (
    user_id INT NOT NULL AUTO_INCREMENT,
    userEmail VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,     
    firstname VARCHAR(20) NOT NULL, 
    lastname VARCHAR(20) NOT NULL, 
    address VARCHAR(50) NULL,  
    mobileNumber VARCHAR(20) NOT NULL, 
    isBusinessRep BOOLEAN,
    PRIMARY KEY(user_id)
);
GO

CREATE TABLE Suburb (
    postcode INT NOT NULL,
    suburb VARCHAR(20) NOT NULL,
    PRIMARY KEY(postcode)
);
GO

CREATE TABLE Business (
    business_id INT NOT NULL AUTO_INCREMENT,
    businessName VARCHAR(20) NOT NULL,
    phoneNumber VARCHAR(20) NOT NULL, 
    businessEmail VARCHAR(20) NOT NULL,
    address VARCHAR(50) NOT NULL,  
    postcode INT NOT NULL,  
    capacity INT NULL,
    businessRepId INT NOT NULL,
    photo VARCHAR(900) NULL,
    PRIMARY KEY(business_id),
    CONSTRAINT FK_BusinessUsers FOREIGN KEY (businessRepId) REFERENCES Users(user_id) ON DELETE CASCADE,
    CONSTRAINT FK_BusinessSuburb FOREIGN KEY (postcode) REFERENCES Suburb(postcode) ON DELETE NO ACTION
);
GO


CREATE TABLE VisitRecord (
    visit_id INT NOT NULL AUTO_INCREMENT,
    checkInTime DATETIME NOT NULL,
    customer_id INT NOT NULL,
    business_id INT NOT NULL,
    PRIMARY KEY(visit_id),
    CONSTRAINT FK_VisitUser FOREIGN KEY (customer_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    CONSTRAINT FK_VisitBusiness FOREIGN KEY (business_id) REFERENCES Business(business_id) ON DELETE CASCADE
);
GO

CREATE TABLE CovidCases (
    case_id INT NOT NULL AUTO_INCREMENT,
    createTime DATE,
	publishDate VARCHAR(50),
	venue VARCHAR(50),
	address VARCHAR(50),
	suburb VARCHAR(50),
	foundDate VARCHAR(50),
	time VARCHAR(50),
    postcode INT,
    PRIMARY KEY(case_id)
);
GO

CREATE TABLE Notification (
    Id INT NOT NULL AUTO_INCREMENT,
	userId INT,
    bussinessId INT,
	case_Id INT,
	record_Id INT,
	content VARCHAR(50),
	status INT,
	type INT,
	createTime DATE,
    PRIMARY KEY(Id),
);
GO
