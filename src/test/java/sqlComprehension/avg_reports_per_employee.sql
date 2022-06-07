SELECT  AVG(reports_to) AS Average, COUNT(*)
FROM employees
WHERE reports_to >= (SELECT AVG (reports_to) FROM employees);