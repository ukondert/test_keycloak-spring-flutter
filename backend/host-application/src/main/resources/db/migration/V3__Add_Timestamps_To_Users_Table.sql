-- Add created_at and updated_at columns to users table
ALTER TABLE demo_users_schema.users
ADD COLUMN created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN updated_at TIMESTAMP;

-- Add comments for the new columns
COMMENT ON COLUMN demo_users_schema.users.created_at IS 'Timestamp when the user account was created';

COMMENT ON COLUMN demo_users_schema.users.updated_at IS 'Timestamp when the user account was last updated';