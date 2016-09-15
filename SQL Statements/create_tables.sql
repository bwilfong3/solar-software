DROP TABLE IF EXISTS composes;
DROP TABLE IF EXISTS associated_with_data;
DROP TABLE IF EXISTS template;
DROP TABLE IF EXISTS data_file;

#==================================================================================

#CREATE TABLE element(
#	atomicSymbol VARCHAR(5) NOT NULL,
#    eName        VARCHAR(30) NOT NULL,
#    atomicNumber INT UNSIGNED NOT NULL,
    
#    PRIMARY KEY (atomicSymbol)
#);

#==================================================================================

CREATE TABLE template(
    t_file_name VARCHAR(255) NOT NULL,
    submitted_by VARCHAR(255) NOT NULL,
    ratio_data VARCHAR(2048) NOT NULL,
    
    PRIMARY KEY (t_file_name)
);

#==================================================================================

CREATE TABLE data_file(
	uid INT UNSIGNED NOT NULL,
	d_file_name      VARCHAR(255) NOT NULL,
	submitted_by VARCHAR(255) NOT NULL,
    data_readings VARCHAR(4096) NOT NULL,
    
    PRIMARY KEY (uid)
);

#==================================================================================

CREATE TABLE composes(
    t_file_name VARCHAR(255) NOT NULL,
    atomicSymbol VARCHAR(5) NOT NULL,
    pos INT UNSIGNED NOT NULL,
    concentration VARCHAR(10) NOT NULL,
    salt_used VARCHAR(20) NOT NULL,
    
    FOREIGN KEY(t_file_name) REFERENCES template(t_file_name),
    FOREIGN KEY(atomicSymbol) REFERENCES element(atomicSymbol)
);


#==================================================================================

CREATE TABLE associated_with_data(
	t_file_name VARCHAR(255) NOT NULL,
    uid INT UNSIGNED NOT NULL,
    
    FOREIGN KEY(t_file_name)        REFERENCES template(t_file_name),
    FOREIGN KEY(uid) REFERENCES data_file(uid)
);


