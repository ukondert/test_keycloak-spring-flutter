-- Initialize database for Keycloak Demo Application
-- This script creates the necessary schemas and basic setup

-- Create schema for the backend application
CREATE SCHEMA IF NOT EXISTS demo_users_schema;

-- Grant privileges to the application user
GRANT ALL PRIVILEGES ON SCHEMA demo_users_schema TO keycloak_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA demo_users_schema TO keycloak_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA demo_users_schema TO keycloak_user;

-- Set search path
ALTER DATABASE keycloak_demo SET search_path TO demo_users_schema, public;

-- Log initialization
DO $$
BEGIN
  RAISE NOTICE 'Database initialized successfully for Keycloak Demo Application';
END $$;
