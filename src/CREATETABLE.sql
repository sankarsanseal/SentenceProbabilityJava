CREATE TABLE monogram
(
word NVARCHAR(100),
count INT,
probability DOUBLE
);

CREATE TABLE bigram
(
wordp NVARCHAR(100),
wordn NVARCHAR(100),
count INT,
probability DOUBLE
);

ALTER TABLE monogram ADD CONSTRAINT monogram_pk PRIMARY KEY (word);

ALTER TABLE bigram ADD CONSTRAINT bigram_fk1 FOREIGN KEY(wordp) REFERENCES monogram(word);
ALTER TABLE bigram ADD CONSTRAINT bigram_fk1 FOREIGN KEY(wordn) REFERENCES monogram(word);
ALTER TABLE bigram ADD CONSTRAINT bigram_pk PRIMARY KEY(wordp,wordn);