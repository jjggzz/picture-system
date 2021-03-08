create database picture;
use picture;

create table t_image (
    id bigint auto_increment,
    create_time datetime not null ,
    modified_time datetime not null ,
    deleted boolean default 0 not null ,
    access_key bigint unique not null comment '业务id',
    upload_time datetime not null comment '上传时间',
    file_name varchar(255) not null comment '文件名',
    file_path varchar(255) not null comment '文件路径',
    file_size bigint not null comment '文件大小',
    focus boolean not null default 0 comment '是否关注 0未关注 1已关注',
    primary key(id)
) comment '图片表';

create table t_tag (
     id bigint auto_increment,
     create_time datetime not null ,
     modified_time datetime not null ,
     deleted boolean default 0 not null ,
     access_key bigint unique not null comment '业务id',
     tag_name varchar(64) unique not null comment '标签名',
     primary key(id)
) comment '标签表';

create table t_tag_image (
     id bigint auto_increment,
     create_time datetime not null ,
     modified_time datetime not null ,
     deleted boolean default 0 not null ,
     access_key bigint unique not null comment '业务id',
     tag_id bigint not null comment '标签id',
     image_id bigint not null comment '图片id',
     primary key(id)
)

