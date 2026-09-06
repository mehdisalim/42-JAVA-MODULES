--  User
--      User ID
--      Login
--      Password
--      List of created rooms
--      List of chatrooms where a user socializes
--  Chatroom
--      Chatroom id
--      Chatroom name
--      Chatroom owner
--      List of messages in a chatroom
--  Message
--      Message id
--      Message author
--      Message room
--      Message text
--      Message date/time
--
--
--  Create schema.sql file where you will describe CREATE TABLE operations to create
--  tables for the project.

-- Remove tables if they already exist
DROP TABLE IF EXISTS chatroom_users;
DROP TABLE IF EXISTS messages;
DROP TABLE IF EXISTS chatrooms;
DROP TABLE IF EXISTS users;

-- =========================================
-- USERS
-- =========================================
CREATE TABLE users (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    login VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- =========================================
-- CHATROOMS
-- =========================================
CREATE TABLE chatrooms (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(255) NOT NULL,
    owner_id BIGINT NOT NULL,

    CONSTRAINT fk_chatroom_owner
        FOREIGN KEY (owner_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

-- =========================================
-- USERS <-> CHATROOMS (Participation)
-- =========================================
CREATE TABLE chatroom_users (
    user_id BIGINT NOT NULL,
    chatroom_id BIGINT NOT NULL,

    PRIMARY KEY (user_id, chatroom_id),

    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_chatroom
        FOREIGN KEY (chatroom_id)
        REFERENCES chatrooms(id)
        ON DELETE CASCADE
);

-- =========================================
-- MESSAGES
-- =========================================
CREATE TABLE messages (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    author_id BIGINT,
    room_id BIGINT,
    text TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_message_author
        FOREIGN KEY (author_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_message_room
        FOREIGN KEY (room_id)
        REFERENCES chatrooms(id)
        ON DELETE CASCADE
);