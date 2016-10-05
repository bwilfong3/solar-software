DROP TABLE IF EXISTS result;
DROP TABLE IF EXISTS element_data;
DROP TABLE IF EXISTS ratio_data;
DROP TABLE IF EXISTS plate_data;
#DROP TABLE IF EXISTS element; # will stay consistent throughtout life of program

create table plate_data (
    plate_no INT NOT NULL,
    plate_runner VARCHAR(50),

    PRIMARY KEY (plate_no)
    );
    
create table ratio_data (
    plate_no INT NOT NULL,
    row_index INT NOT NULL,
    col_index INT NOT NULL,
    ratio VARCHAR(25) NOT NULL,
    
    PRIMARY KEY (plate_no,row_index,col_index)
	);
    
create table result (
    plate_no INT NOT NULL,
    plate_type VARCHAR(10),
    row_index INT NOT NULL,
    col_index INT NOT NULL,
    reading DOUBLE,
    ratio_to_standard1 DOUBLE,
    ratio_to_standard2 DOUBLE,
    data_id LONG,
    
    FOREIGN KEY (plate_no) REFERENCES plate_data(plate_no),
    FOREIGN KEY (plate_no,row_index,col_index) REFERENCES ratio_data(plate_no,row_index,col_index)
    );

#CREATE TABLE element(
#   atomic_symbol VARCHAR(5) NOT NULL,
#   eName        VARCHAR(30) NOT NULL,
#   atomicNumber INT UNSIGNED NOT NULL,

#    PRIMARY KEY (atomic_symbol)
#    );

create table element_data(
    plate_no INT NOT NULL,
    atomic_symbol VARCHAR(5) NOT NULL,
    salt_used VARCHAR(20) NOT NULL,
    pos INT NOT NULL,
    concentration VARCHAR(10) NOT NULL,
    
    FOREIGN KEY (plate_no) REFERENCES plate_data(plate_no),
    FOREIGN KEY (atomic_symbol) REFERENCES element(atomic_symbol)
    );
