-- Create the clients table
CREATE TABLE grow.clients (
                              client_id INT AUTO_INCREMENT PRIMARY KEY,
                              chat_id BIGINT UNIQUE,
                              phone VARCHAR(20) UNIQUE,
                              is_active BOOLEAN,
                              update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- Create the therapists table
CREATE TABLE grow.therapists (
                                 therapist_id INT AUTO_INCREMENT PRIMARY KEY,
                                 first_name VARCHAR(50),
                                 last_name VARCHAR(50),
                                 phone VARCHAR(20) UNIQUE,
                                 iban VARCHAR(34),
                                 update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create the appointments table
CREATE TABLE grow.appointments (
                                   appointment_id INT AUTO_INCREMENT PRIMARY KEY,
                                   room VARCHAR(255),
                                   event_start TIMESTAMP,
                                   chat_id BIGINT,
                                   status BOOLEAN,
                                   image_id VARCHAR(255),
                                   therapist_id INT,
                                   update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   FOREIGN KEY (therapist_id) REFERENCES therapists(therapist_id)
);
