CREATE TABLE AUTHORS (
    ID BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    FIRST_NAME VARCHAR(50) NOT NULL,
    LAST_NAME VARCHAR(50) NOT NULL
);

CREATE TABLE WORKS (
    ID BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    TITLE VARCHAR(250) NOT NULL,
    PUBLISHED DATE NOT NULL,
    RARE BOOLEAN DEFAULT FALSE
);

CREATE TABLE AUTHOR_WORK_XREF (
    ID BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    WORK_ID BIGINT,
    AUTHOR_ID INT,
    FOREIGN KEY (WORK_ID) REFERENCES WORKS(ID),
    FOREIGN KEY (AUTHOR_ID) REFERENCES AUTHORS(ID)
);

CREATE TABLE CITATIONS (
    ID BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    WORK_ID BIGINT,
    PAGE_NUMBER SMALLINT,
    CITED_WORK VARCHAR(250) NOT NULL,
    CITATION_AUTHOR VARCHAR(100) NOT NULL,
    CITED_WHEN DATE NOT NULL
);

CREATE TABLE FORMATS (
    ID BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    WORK_ID BIGINT,
    FORMAT SMALLINT,
    SHIPPING_COST DECIMAL(10, 2)
);

CREATE TABLE FORWARDS (
    ID BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    WORK_ID BIGINT,
    AUTHOR VARCHAR(100) NOT NULL,
    FORWARD LONGTEXT NOT NULL
);

CREATE TABLE VOLUMES (
    ID BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    WORK_ID BIGINT,
    VOLUME_NUMBER INT,
    SERIES_TITLE VARCHAR(100) NOT NULL
);
