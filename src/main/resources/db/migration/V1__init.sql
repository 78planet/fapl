CREATE TABLE `follows` (
  `following_user_id` integer,
  `followed_user_id` integer
);

CREATE TABLE `user` (
  `id` integer PRIMARY KEY,
  `email` varchar(255),
  `nickname` varchar(255),
  `password` varchar(255),
  `grade` integer,
  `profile_image` varchar(255),
  `created_at` timestamp,
  `updated_at` timestamp
);

CREATE TABLE `grade` (
  `id` integer PRIMARY KEY,
  `grade` varchar(255)
);

CREATE TABLE `post` (
  `id` integer PRIMARY KEY,
  `content` text COMMENT 'Content of the post',
  `user_id` integer,
  `like_cnt` integer,
  `dislike_cnt` integer,
  `created_at` timestamp,
  `updated_at` timestamp
);

CREATE TABLE `post_like_users` (
  `id` integer PRIMARY KEY,
  `post_id` integer,
  `user_id` integer
);

CREATE TABLE `post_dislike_users` (
  `id` integer PRIMARY KEY,
  `post_id` integer,
  `user_id` integer
);

CREATE TABLE `post_image` (
  `id` integer PRIMARY KEY,
  `post_id` integer,
  `image_url` varchar(255)
);

CREATE TABLE `hashtag` (
  `id` integer PRIMARY KEY,
  `post_id` integer,
  `hashtag` varchar(255)
);

CREATE TABLE `comment` (
  `id` integer PRIMARY KEY,
  `post_id` integer,
  `user_id` integer,
  `content` varchar(255),
  `class` integer,
  `group_id` integer,
  `order` integer
);

CREATE TABLE `point` (
  `id` integer PRIMARY KEY,
  `user_id` integer,
  `point` integer
);

ALTER TABLE `post` ADD FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

ALTER TABLE `comment` ADD FOREIGN KEY (`post_id`) REFERENCES `post` (`id`);

ALTER TABLE `post_image` ADD FOREIGN KEY (`post_id`) REFERENCES `post` (`id`);

ALTER TABLE `hashtag` ADD FOREIGN KEY (`post_id`) REFERENCES `post` (`id`);

ALTER TABLE `post_like_users` ADD FOREIGN KEY (`post_id`) REFERENCES `post` (`id`);

ALTER TABLE `post_dislike_users` ADD FOREIGN KEY (`post_id`) REFERENCES `post` (`id`);

ALTER TABLE `user` ADD FOREIGN KEY (`grade`) REFERENCES `grade` (`id`);

ALTER TABLE `point` ADD FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

ALTER TABLE `follows` ADD FOREIGN KEY (`following_user_id`) REFERENCES `user` (`id`);

ALTER TABLE `follows` ADD FOREIGN KEY (`followed_user_id`) REFERENCES `user` (`id`);
