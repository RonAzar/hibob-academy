-- Create feedback_response table
CREATE TABLE if not exists feedback_response
(
    id            BIGSERIAL PRIMARY KEY,
    response_text VARCHAR(300) NOT NULL,
    responder_id  BIGINT       NOT NULL,
    feedback_id   BIGINT       NOT NULL,
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_feedback_response_employee_id ON feedback_response (feedback_id);
CREATE INDEX idx_feedback_response_responder_id ON feedback_response (responder_id);