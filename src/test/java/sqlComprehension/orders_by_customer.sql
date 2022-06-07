SELECT COUNT (*), o.customer_id
FROM orders o
LEFT JOIN customers c  ON o.customer_id = c.customer_id
LEFT JOIN shippers s ON s.shipper_id = o.ship_via
where c.company_name = 'Simons bistro'
AND s.company_name = 'United Package'
GROUP BY o.customer_id;