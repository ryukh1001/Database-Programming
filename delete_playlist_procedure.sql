create or replace procedure delete_playlist(sid IN VARCHAR2, --���̵�
                            nsongId NUMBER, --�뷡 ��ȣ
                            result OUT VARCHAR2) --��� �����
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
    /* �÷��̸���Ʈ �뷡 ���� */
    DELETE FROM playlist WHERE id=sid AND songId=nsongId;
    UPDATE song SET hit=hit-1 WHERE songId=nsongId;
    COMMIT;
    result := '�÷��̸���Ʈ���� �뷡�� �����Ǿ����ϴ�.';
  END IF;
  CLOSE song_to_delete;
EXCEPTION
    WHEN no_data_to_delete THEN
       result := '�÷��̸���Ʈ�� ���� �뷡 ��ȣ�Դϴ�.'; 
END;