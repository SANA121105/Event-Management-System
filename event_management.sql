CREATE DATABASE event_management;
USE event_management;

CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE events (
    event_id INT AUTO_INCREMENT PRIMARY KEY,
    event_name VARCHAR(150) NOT NULL,
    event_date DATE NOT NULL,
    event_time TIME NOT NULL,
    venue VARCHAR(150),
    description TEXT,
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
    ON DELETE CASCADE
);

CREATE TABLE schedules (
    schedule_id INT AUTO_INCREMENT PRIMARY KEY,
    event_id INT,
    session_name VARCHAR(150),
    start_time TIME,
    end_time TIME,
    FOREIGN KEY (event_id) REFERENCES events(event_id)
    ON DELETE CASCADE
);
CREATE TABLE registrations (
    registration_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    event_id INT,
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (event_id) REFERENCES events(event_id)
);

INSERT INTO users (name, email, password)
VALUES 
('Sana', 'sana@gmail.com', '1234'),
('Arnav', 'arnav@gmail.com', 'abcd');

INSERT INTO events (event_name, event_date, event_time, venue, description, user_id)
VALUES
('Tech Fest', '2026-05-10', '10:00:00', 'Auditorium', 'Annual tech event', 1),
('Workshop', '2026-05-12', '14:00:00', 'Lab 1', 'Java workshop', 2);

INSERT INTO schedules (event_id, session_name, start_time, end_time)
VALUES
(1, 'Opening Ceremony', '10:00:00', '11:00:00'),
(1, 'Coding Contest', '11:30:00', '13:30:00');

SELECT 
    e.event_name,
    e.event_date,
    e.event_time,
    e.venue,
    s.session_name,
    s.start_time,
    s.end_time
FROM events e
LEFT JOIN schedules s ON e.event_id = s.event_id;

ALTER TABLE events 
MODIFY venue VARCHAR(150) NOT NULL;

SELECT * FROM users;
