create or replace procedure insert_member(id IN VARCHAR2, --아이디
                            pwd IN VARCHAR2, --비밀번호
                            birth IN VARCHAR2, --생년월일
                            result OUT VARCHAR2) --출력 결과문
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

  /* 사용자 등록 */
  INSERT INTO member 
  VALUES(id, pwd, birth);

  COMMIT;
  result := '회원가입이 완료되었습니다.';
  
EXCEPTION
    WHEN underflow_length THEN
       result := '암호는 4자리 이상이어야 합니다';
    WHEN invalid_value THEN
       result := '암호에 공란은 입력되지 않습니다.';
    WHEN wrong_birth_form THEN
        result := '생년월일을 "yyyy/mm/dd" 형식으로 작성하세요.';
    WHEN too_young THEN
        result := '만 14세 이상만 가입할 수 있습니다.';

END;