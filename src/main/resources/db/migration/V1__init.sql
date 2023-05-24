CREATE TABLE grade
(
    id    integer      NOT NULL auto_increment,
    grade varchar(255) NOT NULL,
    PRIMARY KEY (id)
) engine = InnoDB
  default charset = utf8mb4;


CREATE TABLE point
(
    id    bigint NOT NULL auto_increment,
    point bigint NOT NULL,
    PRIMARY KEY (id)
) engine = InnoDB
  default charset = utf8mb4;


CREATE TABLE user
(
    id            bigint       NOT NULL auto_increment,
    email         varchar(255) NOT NULL,
    nickname      varchar(255) NOT NULL,
    password      varchar(255) NOT NULL,
    grade_id      integer      NOT NULL,
    profile_image varchar(255),
    point_id      bigint       NOT NULL,
    created_at    datetime     NOT NULL,
    updated_at    datetime     NOT NULL,
    PRIMARY KEY (id)
) engine = InnoDB
  default charset = utf8mb4;

ALTER TABLE user
    ADD FOREIGN KEY (grade_id) REFERENCES grade (id);

ALTER TABLE user
    ADD FOREIGN KEY (point_id) REFERENCES point (id);


CREATE TABLE follow
(
    id                bigint NOT NULL auto_increment,
    following_user_id bigint NOT NULL,
    followed_user_id  bigint NOT NULL,
    PRIMARY KEY (id)
) engine = InnoDB
  default charset = utf8mb4;

ALTER TABLE follow
    ADD FOREIGN KEY (following_user_id) REFERENCES user (id);

ALTER TABLE follow
    ADD FOREIGN KEY (followed_user_id) REFERENCES user (id);



CREATE TABLE post
(
    id          bigint   NOT NULL auto_increment,
    content     text,
    user_id     bigint   NOT NULL,
    like_cnt    bigint   NOT NULL,
    dislike_cnt bigint   NOT NULL,
    created_at  datetime NOT NULL,
    updated_at  datetime NOT NULL,
    PRIMARY KEY (id)
) engine = InnoDB
  default charset = utf8mb4;

ALTER TABLE post
    ADD FOREIGN KEY (user_id) REFERENCES user (id);


CREATE TABLE post_like_user
(
    id      bigint NOT NULL auto_increment,
    post_id bigint NOT NULL,
    user_id bigint NOT NULL,
    PRIMARY KEY (id)
) engine = InnoDB
  default charset = utf8mb4;

ALTER TABLE post_like_user
    ADD FOREIGN KEY (post_id) REFERENCES post (id);


CREATE TABLE post_dislike_user
(
    id      bigint NOT NULL auto_increment,
    post_id bigint NOT NULL,
    user_id bigint NOT NULL,
    PRIMARY KEY (id)
) engine = InnoDB
  default charset = utf8mb4;

ALTER TABLE post_dislike_user
    ADD FOREIGN KEY (post_id) REFERENCES post (id);


CREATE TABLE post_image
(
    id        bigint       NOT NULL auto_increment,
    post_id   bigint       NOT NULL,
    image_url varchar(255) NOT NULL,
    PRIMARY KEY (id)
) engine = InnoDB
  default charset = utf8mb4;

ALTER TABLE post_image
    ADD FOREIGN KEY (post_id) REFERENCES post (id);


CREATE TABLE hashtag
(
    id      bigint       NOT NULL auto_increment,
    post_id bigint,
    hashtag varchar(255) NOT NULL,
    PRIMARY KEY (id)
) engine = InnoDB
  default charset = utf8mb4;

ALTER TABLE hashtag
    ADD FOREIGN KEY (post_id) REFERENCES post (id);


CREATE TABLE post_hashtag
(
    id         bigint NOT NULL auto_increment,
    post_id    bigint,
    hashtag_id bigint,
    PRIMARY KEY (id)
) engine = InnoDB
  default charset = utf8mb4;

ALTER TABLE post_hashtag
    ADD FOREIGN KEY (post_id) REFERENCES post (id);

ALTER TABLE post_hashtag
    ADD FOREIGN KEY (hashtag_id) REFERENCES hashtag (id);


CREATE TABLE comment
(
    id        bigint       NOT NULL auto_increment,
    post_id   bigint       NOT NULL,
    user_id   bigint       NOT NULL,
    content   varchar(255) NOT NULL,
    hierarchy bigint       NOT NULL,
    group_id  bigint       NOT NULL,
    order_num bigint       NOT NULL,
    PRIMARY KEY (id)
) engine = InnoDB
  default charset = utf8mb4;

ALTER TABLE comment
    ADD FOREIGN KEY (post_id) REFERENCES post (id);


