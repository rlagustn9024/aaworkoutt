-- Member 데이터 삽입
INSERT INTO member (member_id, name, email, password, status, auth) VALUES (1, 'member1', 'member1@naver.com', 'hashed_password_1', 'ONLINE', 'USER');
INSERT INTO member (member_id, name, email, password, status, auth) VALUES (2, 'member2', 'member2@naver.com', 'hashed_password_2', 'ONLINE', 'ADMIN');
INSERT INTO member (member_id, name, email, password, status, auth) VALUES (3, 'admin', 'admin@naver.com', '$2a$10$AxcE//bZGlzwpRVnuq0kv.7KCV83puKLf6aqqwfOYUGYEwCuobmnS', 'ONLINE', 'ADMIN');

-- 운동 타입 (EXERCISE_TYPE) 테이블 데이터 삽입
INSERT INTO exercise_type (exercise_type_id, name) VALUES (1, 'Push-Up');
INSERT INTO exercise_type (exercise_type_id, name) VALUES (2, 'Pull-Up');
INSERT INTO exercise_type (exercise_type_id, name) VALUES (3, 'Squat');

-- 테스트 (TEST) 테이블 데이터 삽입
INSERT INTO test (test_id, name, exercise_type_id) VALUES (1, 'Police Physical Test', 1);
INSERT INTO test (test_id, name, exercise_type_id) VALUES (2, 'Firefighter Physical Test', 2);
INSERT INTO test (test_id, name, exercise_type_id) VALUES (3, 'Military Physical Test', 3);

-- 점수 기준 (SCORE_CRITERIA) 테이블 데이터 삽입
INSERT INTO score_criteria (score_criteria_id, test_id, gender, min_range, max_range, score) VALUES (1, 1, 'MALE', 1, 50, 5);
INSERT INTO score_criteria (score_criteria_id, test_id, gender, min_range, max_range, score) VALUES (2, 1, 'MALE', 51, 100, 10);
INSERT INTO score_criteria (score_criteria_id, test_id, gender, min_range, max_range, score) VALUES (3, 1, 'FEMALE', 1, 50, 7);
INSERT INTO score_criteria (score_criteria_id, test_id, gender, min_range, max_range, score) VALUES (4, 1, 'FEMALE', 51, 100, 9);

-- 비디오 (VIDEO) 테이블 데이터 삽입
INSERT INTO video (video_id, url) VALUES (1, '/videos/test1.mp4');
INSERT INTO video (video_id, url) VALUES (2, '/videos/test2.mp4');

---- 운동 (EXERCISE) 테이블 데이터 삽입
INSERT INTO exercise (exercise_id, member_id, exercise_type_id, video_id, start_time, end_time, target_time, total_count, target_count ,created_date) VALUES (1, 1, 1, 1, '2023-10-01T10:00:00', '2023-10-01T10:05:00', '00:05:00', 50, 60, '2023-11-02T11:11:00');
INSERT INTO exercise (exercise_id, member_id, exercise_type_id, video_id, start_time, end_time, target_time, total_count, target_count ,created_date) VALUES (2, 2, 2, 2, '2023-10-02T11:00:00', '2023-10-02T11:10:00', '00:10:00', 30, 40, '2023-11-02T11:12:00');
INSERT INTO exercise (exercise_id, member_id, exercise_type_id, video_id, start_time, end_time, target_time, total_count, target_count ,created_date) VALUES (3, 3, 3, null, '2023-11-02T11:11:00', '2023-11-02T11:30:00', '00:11:00', 110, 140, '2023-11-02T11:13:00');
INSERT INTO exercise (exercise_id, member_id, exercise_type_id, video_id, start_time, end_time, target_time, total_count, target_count ,created_date) VALUES (4, 3, 3, null, '2023-11-02T11:11:00', '2023-11-02T11:30:00', '00:12:00', 120, 140, '2023-11-02T11:14:00');
INSERT INTO exercise (exercise_id, member_id, exercise_type_id, video_id, start_time, end_time, target_time, total_count, target_count ,created_date) VALUES (5, 3, 3, null, '2023-11-02T11:11:00', '2023-11-02T11:30:00', '00:13:00', 130, 140, '2023-11-02T11:15:00');
INSERT INTO exercise (exercise_id, member_id, exercise_type_id, video_id, start_time, end_time, target_time, total_count, target_count ,created_date) VALUES (6, 3, 3, null, '2023-11-02T11:11:00', '2023-11-02T11:30:00', '00:14:00', 140, 140, '2023-11-02T11:16:00');
INSERT INTO exercise (exercise_id, member_id, exercise_type_id, video_id, start_time, end_time, target_time, total_count, target_count ,created_date) VALUES (7, 3, 3, null, '2023-11-02T11:11:00', '2023-11-02T11:30:00', '00:15:00', 150, 140, '2023-11-02T11:17:00');
INSERT INTO exercise (exercise_id, member_id, exercise_type_id, video_id, start_time, end_time, target_time, total_count, target_count ,created_date) VALUES (8, 3, 3, null, '2023-11-02T11:11:00', '2023-11-02T11:30:00', '00:16:00', 160, 140, '2023-11-02T11:18:00');
INSERT INTO exercise (exercise_id, member_id, exercise_type_id, video_id, start_time, end_time, target_time, total_count, target_count ,created_date) VALUES (9, 3, 3, null, '2023-11-02T11:11:00', '2023-11-02T11:30:00', '00:17:00', 170, 140, '2023-11-02T11:19:00');
INSERT INTO exercise (exercise_id, member_id, exercise_type_id, video_id, start_time, end_time, target_time, total_count, target_count ,created_date) VALUES (10, 3, 3, null, '2023-11-02T11:11:00', '2023-11-02T11:30:00', '00:18:00', 180, 140, '2023-11-02T11:20:00');

-- 테스트 결과 (TEST_RESULT) 테이블 데이터 삽입
INSERT INTO test_result (test_result_id, member_id, test_id, gender, total_count, score, created_date) VALUES (1, 1, 1, 'MALE', 30, 30, '2024-10-01');
INSERT INTO test_result (test_result_id, member_id, test_id, gender, total_count, score, created_date) VALUES (2, 1, 1, 'MALE', 40, 40, '2024-10-02');
INSERT INTO test_result (test_result_id, member_id, test_id, gender, total_count, score, created_date) VALUES (3, 1, 1, 'MALE', 50, 50, '2024-10-03');
INSERT INTO test_result (test_result_id, member_id, test_id, gender, total_count, score, created_date) VALUES (4, 1, 1, 'MALE', 60, 60, '2024-10-04');
INSERT INTO test_result (test_result_id, member_id, test_id, gender, total_count, score, created_date) VALUES (5, 1, 1, 'MALE', 70, 70, '2024-10-05');

INSERT INTO test_result (test_result_id, member_id, test_id, gender, total_count, score, created_date) VALUES (6, 2, 2, 'FEMALE', 35, 35, '2024-10-02');
INSERT INTO test_result (test_result_id, member_id, test_id, gender, total_count, score, created_date) VALUES (7, 2, 2, 'FEMALE', 45, 45, '2024-10-02');
INSERT INTO test_result (test_result_id, member_id, test_id, gender, total_count, score, created_date) VALUES (8, 2, 2, 'FEMALE', 55, 55, '2024-10-02');
INSERT INTO test_result (test_result_id, member_id, test_id, gender, total_count, score, created_date) VALUES (9, 2, 2, 'FEMALE', 65, 65, '2024-10-02');
INSERT INTO test_result (test_result_id, member_id, test_id, gender, total_count, score, created_date) VALUES (10, 2, 2, 'FEMALE', 75, 75, '2024-10-02');

INSERT INTO test_result (test_result_id, member_id, test_id, gender, total_count, score, created_date) VALUES (11, 3, 1, 'MALE', 30, 30, '2024-10-01');
INSERT INTO test_result (test_result_id, member_id, test_id, gender, total_count, score, created_date) VALUES (12, 3, 1, 'MALE', 40, 40, '2024-10-02');
INSERT INTO test_result (test_result_id, member_id, test_id, gender, total_count, score, created_date) VALUES (13, 3, 1, 'MALE', 50, 50, '2024-10-03');
INSERT INTO test_result (test_result_id, member_id, test_id, gender, total_count, score, created_date) VALUES (14, 3, 1, 'MALE', 60, 60, '2024-10-04');
INSERT INTO test_result (test_result_id, member_id, test_id, gender, total_count, score, created_date) VALUES (15, 3, 1, 'MALE', 70, 70, '2024-10-05');

