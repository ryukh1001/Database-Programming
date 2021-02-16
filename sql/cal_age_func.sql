create or replace function cal_age(birth IN DATE)
RETURN NUMBER

IS
    age NUMBER;

BEGIN
    SELECT months_between(SYSDATE, birth)
    INTO age
    FROM DUAL;

    age := age/12;

    RETURN age;
END;
