DROP TABLE IF EXISTS plate;
CREATE TABLE plate(
	pName VARCHAR(255) NOT NULL,
    
    PRIMARY KEY (pName)
);

DROP TABLE IF EXISTS composition;
CREATE TABLE composition(
    tableRow INT NOT NULL,
    tableCol INT NOT NULL,
    reading  DOUBLE NOT NULL,
    
    PRIMARY KEY (tableRow, tableCol)
);

DROP TABLE IF EXISTS element;
CREATE TABLE element(
	eName VARCHAR(2) NOT NULL,
    
    PRIMARY KEY (eName)
);

DROP TABLE IF EXISTS contains_mixture;
CREATE TABLE contains_mixture(
	pName    VARCHAR(255) NOT NULL,
    tableRow INT NOT NULL,
    tableCol INT NOT NULL,
    
    FOREIGN KEY(pName)    REFERENCES plate(pName),
    FOREIGN KEY(tableRow, tableCol) REFERENCES composition(tableRow, tableCol)
);

DROP TABLE IF EXISTS contains_element;
CREATE TABLE contains_element(
	pName    VARCHAR(255) NOT NULL,
    tableRow INT NOT NULL,
    tableCol INT NOT NULL,
    eName    VARCHAR(2) NOT NULL,
    
    FOREIGN KEY(pName)    REFERENCES plate(pName),
    FOREIGN KEY(tableRow, tableCol) REFERENCES composition(tableRow, tableCol),
    FOREIGN KEY(eName)    REFERENCES element(eName)
);

