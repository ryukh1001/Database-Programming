CREATE TABLE playlist( id VARCHAR2(20),
		       songId NUMBER,
		       CONSTRAINT PK_member PRIMARY KEY(id, songId),
		       CONSTRAINT FK_id FOREIGN KEY(id) REFERENCES member(id),
		       CONSTRAINT FK_songId FOREIGN KEY(songId) REFERENCES song(songId));