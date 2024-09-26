CREATE INDEX IF NOT EXISTS idx_feedback_employee_id ON feedback (employee_id);

CREATE INDEX IF NOT EXISTS idx_feedback_created_at ON feedback (created_at);

CREATE INDEX IF NOT EXISTS idx_feedback_is_anonymous ON feedback (is_anonymous);

CREATE INDEX IF NOT EXISTS idx_feedback_department ON feedback (department);