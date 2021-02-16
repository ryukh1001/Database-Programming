create or replace procedure insert_member(id IN VARCHAR2, --���̵�
                            pwd IN VARCHAR2, --��й�ȣ
                            birth IN VARCHAR2, --�������
                            result OUT VARCHAR2) --��� �����
IS
  age NUMBER;
  nLength			NUMBER;
  nBlank			NUMBER;
  underflow_length  EXCEPTION;
  invalid_value     EXCEPTION; 
  wrong_birth_form  EXCEPTION;
  too_young EXCEPTION;


  
BEGIN
  result := '';
  nLength := length(pwd);
  nBlank := instr(pwd, ' ');

  IF (nLength<4) THEN
     RAISE underflow_length;
  ELSIF (nBlank > 0) THEN
     RAISE invalid_value;
  END IF;
  
  IF (birth NOT LIKE '____/__/__') THEN 
    RAISE wrong_birth_form;
  ELSE
    age := cal_age(to_date(birth, 'yyyy/mm/dd'));
  END IF;  
  IF(age<14) THEN
    RAISE too_young;
  END IF;

  /* ����� ��� */
  INSERT INTO member 
  VALUES(id, pwd, birth);

  COMMIT;
  result := 'ȸ�������� �Ϸ�Ǿ����ϴ�.';
  
EXCEPTION
    WHEN underflow_length THEN
       result := '��ȣ�� 4�ڸ� �̻��̾�� �մϴ�';
    WHEN invalid_value THEN
       result := '��ȣ�� ������ �Էµ��� �ʽ��ϴ�.';
    WHEN wrong_birth_form THEN
        result := '��������� "yyyy/mm/dd" �������� �ۼ��ϼ���.';
    WHEN too_young THEN
        result := '�� 14�� �̻� ������ �� �ֽ��ϴ�.';

END;