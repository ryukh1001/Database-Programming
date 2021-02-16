create or replace procedure insert_playlist(id IN VARCHAR2, --���̵�
                            nsongId NUMBER, --�뷡 ��ȣ
                            result OUT VARCHAR2) --��� �����
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
  /* �÷��̸���Ʈ �뷡 �߰� */
  INSERT INTO playlist VALUES(id, nsongId);
  UPDATE song SET hit=hit+1 WHERE songId=nsongId;
  COMMIT;
  result := '�÷��̸���Ʈ�� �뷡�� �߰��Ǿ����ϴ�.';
  END IF;
  
  CLOSE song_exist;
EXCEPTION
    WHEN no_data THEN
       result := '�������� �ʴ� �뷡 ��ȣ�Դϴ�.'; 
END;