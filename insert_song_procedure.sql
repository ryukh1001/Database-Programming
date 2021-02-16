create or replace procedure insert_song(title IN VARCHAR2, --노래 제목
                            artist IN VARCHAR2, --가수 이름
                            result OUT VARCHAR2) --출력 결과문
                            
IS
  empty_title  EXCEPTION;
  empty_artist     EXCEPTION;

BEGIN
  result := '';
  
  IF (title IS NULL) THEN
    RAISE empty_title;
  END IF;
  IF (artist IS NULL) THEN
    RAISE empty_artist;
  END IF;
    
  /* 노래 등록 */
  INSERT INTO song(songId, title, artist)
  VALUES(seq_songId.NEXTVAL, title, artist);

  COMMIT;
  result := '노래 등록이 완료되었습니다.';

EXCEPTION
    WHEN empty_title THEN
       result := '제목이 입력되지 않았습니다.';
    WHEN empty_artist THEN
        result := '아티스트가 입력되지 않았습니다.';
END;