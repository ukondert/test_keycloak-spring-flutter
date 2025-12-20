-- Create schema for user domain
CREATE SCHEMA IF NOT EXISTS demo_users_schema;

-- Create users table
CREATE TABLE demo_users_schema.users (
    id UUID PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    keycloak_id VARCHAR(255) UNIQUE,
    CONSTRAINT users_username_check CHECK (length(username) >= 3),
    CONSTRAINT users_email_check CHECK (email ~* '^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')
);

-- Create indexes for better query performance
CREATE INDEX idx_users_username ON demo_users_schema.users(username);
CREATE INDEX idx_users_email ON demo_users_schema.users(email);
CREATE INDEX idx_users_keycloak_id ON demo_users_schema.users(keycloak_id);

-- Add comments
COMMENT ON TABLE demo_users_schema.users IS 'Stores user information and links with Keycloak';
COMMENT ON COLUMN demo_users_schema.users.id IS 'Internal user identifier (UUID)';
COMMENT ON COLUMN demo_users_schema.users.username IS 'Unique username (3-50 characters)';
COMMENT ON COLUMN demo_users_schema.users.email IS 'User email address (unique)';
COMMENT ON COLUMN demo_users_schema.users.first_name IS 'User first name';
COMMENT ON COLUMN demo_users_schema.users.last_name IS 'User last name';
COMMENT ON COLUMN demo_users_schema.users.keycloak_id IS 'Keycloak user ID (linked after registration)';
