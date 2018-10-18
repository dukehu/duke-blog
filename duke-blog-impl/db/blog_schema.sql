drop table if exists blog_article;
drop table if exists blog_article_label_r;
drop table if exists blog_article_type_r;
drop table if exists blog_label;
drop table if exists blog_type;
drop table if exists storage;

create table blog_article
(
   id                   varchar(50) not null,
   title                varchar(500) not null,
   navigation           longtext not null,
   summary              varchar(500) not null,
   html_content         longtext not null,
   md_content           longtext not null,
   article_views        int(10) not null,
   status               int(1) not null comment '0：删除
            1：发布
            2：存草稿',
   create_time          datetime not null,
   modify_time          datetime not null,
   user_id              varchar(50) not null comment '此字段先预留',
   primary key (id)
);
alter table blog_article comment '博客文章表';

create table blog_article_label_r
(
   id                   varchar(50) not null,
   article_id           varchar(50) not null,
   label_id             varchar(50) not null,
   primary key (id)
);
alter table blog_article_label_r comment '博文标签关系表';

create table blog_article_type_r
(
   article_id           varchar(50) not null,
   type_id              varchar(50) not null
);
alter table blog_article_type_r comment '博文类别关系表';

create table blog_label
(
   id                   varchar(50) not null,
   name                 varchar(50) not null,
   create_time          datetime not null,
   modify_time          datetime not null,
   user_id              varchar(50) not null comment '此字段先预留',
   status               int(1) not null,
   primary key (id)
);
alter table blog_label comment '博文标签表';

create table blog_type
(
   id                   varchar(50) not null,
   name                 varchar(10) not null,
   create_time          datetime not null,
   modify_time          datetime not null,
   user_id              varchar(50) not null comment '此字段先预留',
   status               int(1) not null,
   primary key (id)
);
alter table blog_type comment '博文类别表';

create table storage
(
  id                   varchar(50) not null,
  md5                  varbinary(50) not null comment '文件md5值',
  name                 varchar(200) not null,
  suffix               varchar(10) not null,
  service_id           varchar(20) not null,
  path                 varchar(200) not null,
  size                 int(20) not null,
  status               int(1) not null,
  user_id              varchar(50) not null,
  type                 int(1) not null comment '文档，视频，音频，图片，其他',
  upload_time          datetime not null,
  delete_time          datetime not null,
  primary key (id)
);
alter table storage comment '文件存储表';