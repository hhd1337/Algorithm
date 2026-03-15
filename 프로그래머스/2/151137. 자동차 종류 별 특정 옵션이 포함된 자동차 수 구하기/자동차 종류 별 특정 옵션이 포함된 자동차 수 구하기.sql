-- [알게 된 것]
-- 먼저 WHERE에서 통풍시트 포함, 또는 열선시트 포함, 또는 가죽시트 포함 인 자동차만 남기고
-- 그 남은 행을 GROUP BY CAR_TYPE으로 묶은 뒤 COUNT(*)로 개수를 세야 한다.
-- COUNT()는 보통 행 개수를 세거나, 특정 컬럼의 NULL이 아닌 개수를 셀 때 쓴다.

SELECT CAR_TYPE, COUNT(*) AS 'CARS'
FROM CAR_RENTAL_COMPANY_CAR 
WHERE OPTIONS LIKE '%통풍시트%'
   OR OPTIONS LIKE '%열선시트%'
   OR OPTIONS LIKE '%가죽시트%'
GROUP BY CAR_TYPE
ORDER BY CAR_TYPE;

