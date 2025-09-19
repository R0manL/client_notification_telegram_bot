-- trigger that set update_date value = now() every time when we update any row in the table.
DELIMITER $$

CREATE TRIGGER grow.update_date_trigger_clients
    BEFORE UPDATE ON grow.clients FOR EACH ROW
BEGIN
    SET NEW.update_date = CONVERT_TZ(NOW(), 'SYSTEM', '+03:00');
END;

$$
DELIMITER ;