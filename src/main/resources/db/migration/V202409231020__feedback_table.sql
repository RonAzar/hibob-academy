-- Create feedback table
CREATE TABLE if not exists feedback
(
    id            BIGSERIAL PRIMARY KEY,
    company_id    BIGINT       NOT NULL,
    employee_id   BIGINT                DEFAULT NULL,
    feedback_text VARCHAR(300) NOT NULL,
    is_anonymous  BOOLEAN      NOT NULL,
    department    VARCHAR(100) NOT NULL,
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW(),
    status        BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE INDEX IF NOT EXISTS idx_feedback_company_id ON feedback (company_id);