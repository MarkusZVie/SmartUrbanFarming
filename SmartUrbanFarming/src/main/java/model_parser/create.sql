Create Table location(
loc_name		varchar(20) NOT NULL PRIMARY KEY,
longitude	varchar(30),
latitude		varchar(30),
direction	varchar(20)
);

Create Table farming_module(
farm_name		varchar(20) NOT NULL PRIMARY KEY,
length	DOUBLE,
width	DOUBLE,
height	DOUBLE,
loc_name VARCHAR(20),
CONSTRAINT fk1 FOREIGN KEY (loc_name) REFERENCES location(loc_name)
);

Create Table crop(
crop_name	varchar(20) NOT NULL PRIMARY KEY,
species	varchar(30), 
water 	ENUM('low','medium','high'),
light	ENUM('low','medium','high'),
temperature	ENUM('low','medium','high'),
farm_name VARCHAR(20),
CONSTRAINT fk2 FOREIGN KEY (farm_name) REFERENCES farming_module(farm_name)
);

Create Table sensordata(
farm_name		varchar(20),
time_	timestamp, 
humidity varchar(30)  ,
temp 	varchar(30),
light	varchar(30),
hygro varchar(30),
CONSTRAINT pk PRIMARY KEY (farm_name, time_)
);


