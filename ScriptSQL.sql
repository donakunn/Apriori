DROP DATABASE IF EXISTS AprioriDB;

CREATE DATABASE AprioriDB;

DROP USER IF EXISTS 'AprioriUser'@'localhost';

CREATE USER 'AprioriUser'@'localhost' IDENTIFIED BY 'apriori';




GRANT CREATE, SELECT, INSERT, DELETE ON AprioriDB.* TO AprioriUser@localhost IDENTIFIED BY 'apriori';



CREATE TABLE AprioriDB.playtennis(
outlook varchar(10),
temperature float(5,2),
umidity varchar(10),
wind varchar(10),
play varchar(10)
);


insert into AprioriDB.playtennis values('sunny',30.3,'high','weak','no');

insert into AprioriDB.playtennis values('sunny',30.3,'high','strong','no');

insert into AprioriDB.playtennis values('overcast',30.0,'high','weak','yes');

insert into AprioriDB.playtennis values('rain',13.0,'high','weak','yes');

insert into AprioriDB.playtennis values('rain',0.0,'normal','weak','yes');

insert into AprioriDB.playtennis values('rain',0.0,'normal','strong','no');

insert into AprioriDB.playtennis values('overcast',0.1,'normal','strong','yes');

insert into AprioriDB.playtennis values('sunny',13.0,'high','weak','no');

insert into AprioriDB.playtennis values('sunny',0.1,'normal','weak','yes');

insert into AprioriDB.playtennis values('rain',12.0,'normal','weak','yes');

insert into AprioriDB.playtennis values('sunny',12.5,'normal','strong','yes');

insert into AprioriDB.playtennis values('overcast',12.5,'high','strong','yes');

insert into AprioriDB.playtennis values('overcast',29.21,'normal','weak','yes');

insert into AprioriDB.playtennis values('rain',12.5,'high','strong','no');
