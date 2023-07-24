CREATE TABLE fapl.member
(
    id            bigint       NOT NULL auto_increment,
    email         varchar(255) NOT NULL,
    nickname      varchar(255) NOT NULL,
    password      varchar(255) NOT NULL,
    grade         integer      NOT NULL,
    profile_image varchar(255),
    point         bigint       NOT NULL,
    created_at    datetime     NOT NULL,
    updated_at    datetime     NOT NULL,
    PRIMARY KEY (id)
) engine = InnoDB
  default charset = utf8mb4;


CREATE TABLE fapl.follow
(
    id                  bigint NOT NULL auto_increment,
    following_member_id bigint NOT NULL,
    follower_member_id  bigint NOT NULL,
    PRIMARY KEY (id)
) engine = InnoDB
  default charset = utf8mb4;

ALTER TABLE follow
    ADD FOREIGN KEY (following_member_id) REFERENCES member (id);

ALTER TABLE follow
    ADD FOREIGN KEY (follower_member_id) REFERENCES member (id);


CREATE TABLE fapl.post
(
    id          bigint   NOT NULL auto_increment,
    content     text,
    member_id   bigint   NOT NULL,
    like_cnt    bigint   NOT NULL,
    dislike_cnt bigint   NOT NULL,
    created_at  datetime NOT NULL,
    updated_at  datetime NOT NULL,
    PRIMARY KEY (id)
) engine = InnoDB
  default charset = utf8mb4;

ALTER TABLE post
    ADD FOREIGN KEY (member_id) REFERENCES member (id);


CREATE TABLE fapl.post_like_member
(
    id        bigint NOT NULL auto_increment,
    post_id   bigint NOT NULL,
    member_id bigint NOT NULL,
    PRIMARY KEY (id)
) engine = InnoDB
  default charset = utf8mb4;

ALTER TABLE post_like_member
    ADD FOREIGN KEY (post_id) REFERENCES post (id);


CREATE TABLE fapl.post_dislike_member
(
    id        bigint NOT NULL auto_increment,
    post_id   bigint NOT NULL,
    member_id bigint NOT NULL,
    PRIMARY KEY (id)
) engine = InnoDB
  default charset = utf8mb4;

ALTER TABLE post_dislike_member
    ADD FOREIGN KEY (post_id) REFERENCES post (id);


CREATE TABLE fapl.post_image
(
    id        bigint       NOT NULL auto_increment,
    post_id   bigint       NOT NULL,
    image_url varchar(255) NOT NULL,
    PRIMARY KEY (id)
) engine = InnoDB
  default charset = utf8mb4;

ALTER TABLE post_image
    ADD FOREIGN KEY (post_id) REFERENCES post (id);


CREATE TABLE fapl.hashtag
(
    id    bigint       NOT NULL auto_increment,
    name varchar(255) NOT NULL,
    PRIMARY KEY (id)
) engine = InnoDB
  default charset = utf8mb4;


CREATE TABLE fapl.post_hashtag
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


CREATE TABLE fapl.comment
(
    id        bigint       NOT NULL auto_increment,
    post_id   bigint       NOT NULL,
    member_id bigint       NOT NULL,
    content   varchar(255) NOT NULL,
    hierarchy bigint       NOT NULL,
    group_id  bigint       NOT NULL,
    order_num bigint       NOT NULL,
    PRIMARY KEY (id)
) engine = InnoDB
  default charset = utf8mb4;

ALTER TABLE comment
    ADD FOREIGN KEY (post_id) REFERENCES post (id);


