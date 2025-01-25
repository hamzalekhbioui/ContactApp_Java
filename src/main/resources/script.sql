-- Drop the table if it already exists (useful for resetting the database)
DROP TABLE IF EXISTS person;

-- Create the 'person' table
CREATE TABLE person (
    id INTEGER PRIMARY KEY AUTOINCREMENT, -- Unique ID for each person
    firstName TEXT NOT NULL,              -- First name (required)
    lastName TEXT NOT NULL,               -- Last name (required)
    nickname TEXT,                        -- Optional nickname
    phone_number TEXT,                    -- Optional phone number
    address TEXT,                         -- Optional address
    email_address TEXT,                   -- Optional email address
    birth_date DATE                       -- Optional birth date
);

-- Insert sample data
INSERT INTO person (firstName, lastName, nickname, phone_number, address, email_address, birth_date)
VALUES
('John', 'Doe', 'Johnny', '123-456-7890', '123 Elm St', 'john.doe@example.com', '1990-01-01'),
('Jane', 'Smith', NULL, '555-555-5555', '456 Oak St', 'jane.smith@example.com', NULL);
