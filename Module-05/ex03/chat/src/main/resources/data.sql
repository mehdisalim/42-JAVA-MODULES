-- =========================================
-- USERS
-- =========================================
INSERT INTO users (login, password) VALUES
('alice', 'alice123'),
('bob', 'bob123'),
('charlie', 'charlie123'),
('david', 'david123'),
('eva', 'eva123');

-- =========================================
-- CHATROOMS
-- owner_id references users.id
-- =========================================
INSERT INTO chatrooms (name, owner_id) VALUES
('General', 1),
('Java', 2),
('Gaming', 3),
('Movies', 4),
('Music', 5);

-- =========================================
-- USERS <-> CHATROOMS
-- =========================================
INSERT INTO chatroom_users (user_id, chatroom_id) VALUES
(1, 1),
(1, 2),
(2, 1),
(2, 3),
(3, 2),
(3, 3),
(4, 4),
(4, 5),
(5, 1),
(5, 5);

-- =========================================
-- MESSAGES
-- author_id references users.id
-- room_id references chatrooms.id
-- =========================================
INSERT INTO messages (author_id, room_id, text, created_at) VALUES
(1, 1, 'Hello everyone!', '2026-08-05 10:00:00'),
(2, 1, 'Hi Alice!', '2026-08-05 10:01:00'),
(3, 2, 'Anyone studying Java?', '2026-08-05 10:05:00'),
(4, 4, 'What movie did you watch?', '2026-08-05 11:00:00'),
(5, 5, 'I love rock music!', '2026-08-05 12:00:00');