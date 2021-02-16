create or replace procedure insert_playlist(id IN VARCHAR2, --아이디
                            nsongId NUMBER, --노래 번호
                            result OUT VARCHAR2) --출력 결과문
IS
  CURSOR song_exist IS SELECT songId FROM song WHERE songId=nsongId;
  song_id song.songId%TYPE;
  no_data EXCEPTION;

BEGIN
  OPEN song_exist;
  
  result := '';
  FETCH song_exist INTO song_id;

  IF song_exist%NOTFOUND THEN
    RAISE no_data;
  ELSE  
  /* 플레이리스트 노래 추가 */
  INSERT INTO playlist VALUES(id, nsongId);
  UPDATE song SET hit=hit+1 WHERE songId=nsongId;
  COMMIT;
  result := '플레이리스트에 노래가 추가되었습니다.';
  END IF;
  
  CLOSE song_exist;
EXCEPTION
    WHEN no_data THEN
       result := '존재하지 않는 노래 번호입니다.'; 
END;
