DROP TABLE IF EXISTS contains_mixture;
DROP TABLE IF EXISTS contains_element;
DROP TABLE IF EXISTS plate;
DROP TABLE IF EXISTS composition;
DROP TABLE IF EXISTS element;


#==================================================================================

CREATE TABLE plate(
	pName VARCHAR(255) NOT NULL,
    
    PRIMARY KEY (pName)
);

#==================================================================================

CREATE TABLE composition(
    tableRow INT UNSIGNED NOT NULL,
    tableCol INT UNSIGNED NOT NULL,
    reading  DOUBLE NOT NULL,
    
    PRIMARY KEY (tableRow, tableCol)
);

#==================================================================================

CREATE TABLE element(
	atomicSymbol VARCHAR(2) NOT NULL,
    eName        VARCHAR(30) NOT NULL,
    atomicNumber INT UNSIGNED NOT NULL,
    
    PRIMARY KEY (atomicSymbol)
);

#==================================================================================

CREATE TABLE contains_mixture(
	pName    VARCHAR(255) NOT NULL,
    tableRow INT UNSIGNED NOT NULL,
    tableCol INT UNSIGNED NOT NULL,
    
    FOREIGN KEY(pName)              REFERENCES plate(pName),
    FOREIGN KEY(tableRow, tableCol) REFERENCES composition(tableRow, tableCol)
);

#==================================================================================

CREATE TABLE contains_element(
	pName        VARCHAR(255) NOT NULL,
    tableRow     INT UNSIGNED NOT NULL,
    tableCol     INT UNSIGNED NOT NULL,
    atomicSymbol VARCHAR(2) NOT NULL,
    
    FOREIGN KEY(pName)              REFERENCES plate(pName),
    FOREIGN KEY(tableRow, tableCol) REFERENCES composition(tableRow, tableCol),
    FOREIGN KEY(atomicSymbol)       REFERENCES element(atomicSymbol)
);

