SELECT DISTINCT customers.contact_name,quantity
FROM customers,orders, order_details
WHERE customers.customer_id = orders.customer_id
AND orders.order_id = order_details.order_id
AND quantity = (SELECT MAX(quantity)
FROM order_details);