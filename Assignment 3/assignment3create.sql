

create table users(
yelping_since  date,
name varchar2(150),
review_count number,
user_id varchar2(100),
average_stars float,
friends_count number,
fans number,
PRIMARY KEY(user_id)
);



create table votes(
user_id varchar2(100) NOT NULL,
type varchar2(20),
count_of_votes number,
PRIMARY KEY(USER_ID,TYPE),
  CONSTRAINT USER_VOTES
    FOREIGN KEY (user_id)
    REFERENCES users (user_id)
    ON DELETE CASCADE
);

create table compliments(
user_id varchar2(100),
type varchar2(20),
count_of_votes number,
PRIMARY KEY(USER_ID,TYPE),
  CONSTRAINT USER_COMPLIMENTS
    FOREIGN KEY (user_id)
    REFERENCES users (user_id)
    ON DELETE CASCADE
);

create table friends(
user_id varchar2(100),
friend_id varchar2(100),
PRIMARY KEY (user_id, friend_id)
 ,
  CONSTRAINT user_id_friend
    FOREIGN KEY (user_id)
    REFERENCES users (user_id) ON DELETE CASCADE
   ,
  CONSTRAINT friend_id_of_user
    FOREIGN KEY (friend_id)
    REFERENCES users (user_id) ON DELETE CASCADE
   );


Create table business (
business_id VARCHAR2(100),
full_address VARCHAR2(300),
open SMALLINT,
city varchar2(100),
review_count NUMBER,
stars number(5,3),
state varchar2(15),
name varchar2(150),
longitude NUMBER(30,15),
latitude NUMBER(30,15),
neighborhoods LONG,
PRIMARY KEY (business_id)
);




create table hours(
day varchar2(100),
open NUMBER(4,2),
close number(4,2),
business_id VARCHAR2(100),
PRIMARY KEY(business_id,day),
CONSTRAINT business_hours
    FOREIGN KEY (business_id)
    REFERENCES business (business_id)
    ON DELETE CASCADE
);



--------------------------------------------------------
--  DDL for Table CATEGORY
--------------------------------------------------------

CREATE TABLE category (
  category_id VARCHAR2(100) NOT NULL,
  name VARCHAR2(255) NOT NULL,
  PRIMARY KEY (category_id),
  CONSTRAINT unique_categiry UNIQUE (name));




--------------------------------------------------------
--  DDL for Table SUB_CATEGORY
--------------------------------------------------------

CREATE TABLE sub_category (
  sub_category_id VARCHAR2(100) NOT NULL,
  name VARCHAR2(255) NOT NULL,
  PRIMARY KEY (sub_category_id),
   CONSTRAINT unique_sub_categiry UNIQUE (name));


  --------------------------------------------------------
  --  DDL for Table CATEGORY_SUB_CATEGORY
  --------------------------------------------------------

CREATE TABLE category_sub_category (
  category_id VARCHAR2(100) NOT NULL,
  sub_category_id VARCHAR2(100) NOT NULL,
  PRIMARY KEY (category_id, sub_category_id)
 ,
  CONSTRAINT sub_category
    FOREIGN KEY (sub_category_id)
    REFERENCES sub_category (sub_category_id)
    ON DELETE CASCADE
   ,
  CONSTRAINT category
    FOREIGN KEY (category_id)
    REFERENCES category (category_id)
    ON DELETE CASCADE
   );



create table business_category(
category_id varchar2(100),
business_id varchar2(100),
PRIMARY KEY(business_id,category_id),
CONSTRAINT business_cat
    FOREIGN KEY (business_id)
    REFERENCES business (business_id)
    ON DELETE CASCADE,
CONSTRAINT category_cat
    FOREIGN KEY (category_id)
    REFERENCES category (category_id)
    ON DELETE CASCADE

);


CREATE TABLE check_in (
  dayandtime NUMBER(8,5),
  IN_COUNT NUMBER(5),
  business_id VARCHAR2(100) NULL,
  PRIMARY KEY (HOUR,DAY,business_id)
 ,
  CONSTRAINT checkin_business
    FOREIGN KEY (business_id)
    REFERENCES business (business_id)
   )
;


CREATE TABLE review (
  review_id VARCHAR2(100) NOT NULL,
  date_of_review date NULL,
  stars NUMBER(5,3) NULL,
  user_id VARCHAR2(100) NULL,
  business_id  VARCHAR2(100) NULL,
  votes number(5),
  text LONG,
  PRIMARY KEY (review_id)
 ,
  CONSTRAINT review_author
    FOREIGN KEY (user_id)
    REFERENCES users (user_id)
   ,
  CONSTRAINT business_review
    FOREIGN KEY (business_id)
    REFERENCES business (business_id) ON DELETE CASCADE
   )
;




create table review_votes(
review_id varchar2(100) NOT NULL,
type varchar2(20),
count_of_votes number,
PRIMARY KEY(review_id,TYPE),
  CONSTRAINT review_vote
    FOREIGN KEY (review_id)
    REFERENCES review (review_id)
    ON DELETE CASCADE
);

create table business_cat_sub(
category_name VARCHAR2(150) NOT NULL,
sub_category_name VARCHAR2(150) NOT NULL,
business_id VARCHAR2(100) NULL,
PRIMARY KEY(business_id,sub_category_name,category_name)
  ,
  CONSTRAINT business_business_cat_sub
    FOREIGN KEY (business_id)
    REFERENCES business (business_id) ON DELETE CASCADE
);

create sequence category_id;

create trigger trg_cate_id
  before insert on category
   for each row
    begin
     select category_id.nextval
       into :new.CATEGORY_ID
        from dual;
    end;
    /

create sequence sub_category_id;

create trigger trg_sub_cate_id
  before insert on SUB_CATEGORY
   for each row
    begin
     select sub_category_id.nextval
       into :new.SUB_CATEGORY_ID
        from dual;
    end;
    /
