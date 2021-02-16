create or replace procedure delete_playlist(sid IN VARCHAR2, --아이디
                            nsongId NUMBER, --노래 번호
                            result OUT VARCHAR2) --출력 결과문
IS
  CURSOR song_to_delete IS SELECT songId FROM playlist WHERE songId=nsongId AND id=sid;
  song_id song.songId%TYPE;
  no_data_to_delete EXCEPTION;
  
BEGIN
  OPEN song_to_delete;
  result := '';
  FETCH song_to_delete INTO song_id;

  IF song_to_delete%NOTFOUND THEN
    RAISE no_data_to_delete;  
  ELSE
    /* 플레이리스트 노래 삭제 */
    DELETE FROM playlist WHERE id=sid AND songId=nsongId;
    UPDATE song SET hit=hit-1 WHERE songId=nsongId;
    COMMIT;
    result := '플레이리스트에서 노래가 삭제되었습니다.';
  END IF;
  CLOSE song_to_delete;
EXCEPTION
    WHEN no_data_to_delete THEN
       result := '플레이리스트에 없는 노래 번호입니다.'; 
END;