create or replace procedure insert_song(title IN VARCHAR2, --�뷡 ����
                            artist IN VARCHAR2, --���� �̸�
                            result OUT VARCHAR2) --��� �����
                            
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
    
  /* �뷡 ��� */
  INSERT INTO song(songId, title, artist)
  VALUES(seq_songId.NEXTVAL, title, artist);

  COMMIT;
  result := '�뷡 ����� �Ϸ�Ǿ����ϴ�.';

EXCEPTION
    WHEN empty_title THEN
       result := '������ �Էµ��� �ʾҽ��ϴ�.';
    WHEN empty_artist THEN
        result := '��Ƽ��Ʈ�� �Էµ��� �ʾҽ��ϴ�.';
END;