---Create Table.
SELECT * FROM user_database.users;CREATE TABLE `users` (
  `id` bigint NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `created_by` varchar(255) NOT NULL,
  `email_address` varchar(255) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `updated_by` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--- Insert Table
INSERT INTO user_database.users
(
    id,
    created_at,
    created_by,
    email_address,
    first_name,
    last_name,
    updated_at,
    updated_by
)
WITH RECURSIVE seq AS (
    SELECT 1 AS n
    UNION ALL
    SELECT n + 1
    FROM seq
    WHERE n < 1000
)
SELECT
    n AS id,
    TIMESTAMP(DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY)) AS created_at,
    CONCAT('user', n) AS created_by,
    CONCAT('user', n, '@example.com') AS email_address,
    ELT(
        1 + FLOOR(RAND() * 20),
        'Amit','Rahul','Saurabh','Vikas','Ankit',
        'Rohit','Arjun','Vivek','Raj','Karan',
        'Neha','Priya','Anjali','Pooja','Sneha',
        'Riya','Kavita','Nisha','Simran','Aisha'
    ) AS first_name,
    ELT(
        1 + FLOOR(RAND() * 20),
        'Sharma','Verma','Gupta','Singh','Kumar',
        'Patel','Mehta','Agarwal','Jain','Joshi',
        'Malhotra','Kapoor','Mishra','Tiwari','Yadav',
        'Chauhan','Saxena','Bansal','Rathi','Shah'
    ) AS last_name,
    NOW() AS updated_at,
    'system' AS updated_by
FROM seq;