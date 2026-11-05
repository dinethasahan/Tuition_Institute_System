-- Flyway V2: Seed Baseline Reference Data

-- Subjects
INSERT INTO subjects (id, name, medium) VALUES
(1, 'Quantum Physics', 'EN'),
(2, 'Pure Mathematics', 'EN'),
(3, 'Organic Chemistry', 'EN'),
(4, 'Computer Science & AI', 'EN');

-- Grading Scales
INSERT INTO grading_scales (id, grade, min_percentage, max_percentage, grade_point, description) VALUES
(1, 'A+', 90.00, 100.00, 4.00, 'Outstanding Academic Mastery'),
(2, 'A',  80.00, 89.99,  3.70, 'Excellent Comprehension'),
(3, 'B+', 75.00, 79.99,  3.30, 'Very Good Performance'),
(4, 'B',  70.00, 74.99,  3.00, 'Good Understanding'),
(5, 'C',  60.00, 69.99,  2.00, 'Satisfactory / Competent'),
(6, 'F',  0.00,  59.99,  0.00, 'Unsatisfactory / Needs Retake');

-- Academic Year Configurations
INSERT INTO academic_year_configs (id, name, start_date, end_date, current_term, is_active) VALUES
(1, 'Academic Year 2026 - 2027', '2026-09-01', '2027-06-30', 'Term 1 (Fall Session)', TRUE),
(2, 'Academic Year 2025 - 2026', '2025-09-01', '2026-06-30', 'Term 3 (Completed)', FALSE);

-- SMS Templates
INSERT INTO sms_templates (id, title, type, content, variables, last_updated) VALUES
(1, 'Daily Student Absence Notification', 'Attendance', 'Dear {parent_name}, this is to inform you that {student_name} was marked absent from {subject} class on {date}. Please contact the institute if this is in error.', '{parent_name},{student_name},{subject},{date}', '2026-10-10'),
(2, 'Term Tuition Fee Due Reminder', 'Fee Alert', 'Notice from Apex Institute: Tuition fee invoice {invoice_number} for {student_name} of amount Rs. {amount} is due by {due_date}. Please clear via student portal.', '{invoice_number},{student_name},{amount},{due_date}', '2026-10-05'),
(3, 'Exam Results Published Notification', 'Exam Results', 'Apex Institute: Term examination marks for {student_name} in {subject} have been published. Scored: {marks}% ({grade}). View full report on portal.', '{student_name},{subject},{marks},{grade}', '2026-10-15');
