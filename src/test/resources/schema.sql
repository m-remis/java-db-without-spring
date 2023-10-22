drop table if exists "SUSERS";
create table "SUSERS"
(
    "USER_ID"   int primary key,
    "USER_GUID" varchar(50) not null,
    "USER_NAME" varchar(50) not null
);