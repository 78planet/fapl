ALTER TABLE point DROP user_id;
ALTER TABLE `user` ADD point_id integer NOT NULL;
ALTER TABLE `user` ADD FOREIGN KEY (`point_id`) REFERENCES `point` (`id`);
