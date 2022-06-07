SELECT EXTRACT(YEAR FROM order_date) AS Each_Year, COUNT(quantity)
FROM orders, order_details
WHERE orders.order_id = order_details.order_id
AND order_date >= '1996-01-01'
GROUP BY EXTRACT(year from order_date)
ORDER BY EXTRACT(year from order_date);