CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE users (
     id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     username        VARCHAR(50)  NOT NULL,
     email           VARCHAR(150) NOT NULL,
     password        VARCHAR(255) NOT NULL,
     enabled         BOOLEAN      NOT NULL DEFAULT TRUE,
     created_at      TIMESTAMPTZ  NOT NULL DEFAULT now(),
     updated_at      TIMESTAMPTZ  NOT NULL DEFAULT now(),
     CONSTRAINT uq_users_username UNIQUE (username),
     CONSTRAINT uq_users_email UNIQUE (email)
);

CREATE TABLE user_roles (
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role    VARCHAR(20) NOT NULL,
    CONSTRAINT pk_user_roles PRIMARY KEY (user_id, role),
    CONSTRAINT chk_user_roles_valid CHECK (role IN ('ROLE_USER', 'ROLE_ADMIN'))
);

CREATE TABLE categories (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id     UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uq_categories_user_name UNIQUE (user_id, name)
);

CREATE TABLE expenses (
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id       UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    category_id   UUID REFERENCES categories(id) ON DELETE SET NULL,
    amount        NUMERIC(12, 2) NOT NULL CHECK (amount > 0),
    currency      VARCHAR(3) NOT NULL DEFAULT 'USD',
    description   VARCHAR(500),
    expense_date  DATE NOT NULL,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_expenses_user_id ON expenses (user_id);
CREATE INDEX idx_expenses_user_date ON expenses (user_id, expense_date DESC);
CREATE INDEX idx_expenses_category_id ON expenses (category_id);
CREATE INDEX idx_categories_user_id ON categories (user_id);


CREATE OR REPLACE FUNCTION set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = now();
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_users_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_expenses_updated_at
    BEFORE UPDATE ON expenses
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();