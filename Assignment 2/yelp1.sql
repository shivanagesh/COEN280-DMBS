

-- ------------...
-- Schema Yelp
-- ------------...

-- ------------...
-- Table `users...
-- ------------...




BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE category_sub_category';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/


BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE sub_category';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/


BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE photo';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE parking';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE ambient';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/



BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE votes';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE comments';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE content';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE reviews';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE complements';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE friend_ids';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE check_in_log';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE business';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/


BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE category';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE users';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/


--------------------------------------------------------
--  DDL for Table USERS
--------------------------------------------------------

CREATE TABLE users (
  first_name VARCHAR2(50) NULL,
  last_name VARCHAR2(45) NULL,
  yelp_id VARCHAR2(10) NOT NULL,
  email VARCHAR2(50) NULL,
  birth_place VARCHAR2(50) NULL,
  birth_date DATE NOT NULL,
  gender varchar2(5) NULL,
  PRIMARY KEY (yelp_id))
;



--------------------------------------------------------
--  DDL for Table CATEGORY
--------------------------------------------------------

CREATE TABLE category (
  id VARCHAR2(10) NOT NULL,
  name VARCHAR2(255) NOT NULL,
  PRIMARY KEY (id));


  --------------------------------------------------------
  --  DDL for Table BUSINESS
  --------------------------------------------------------


CREATE TABLE business (
  id VARCHAR2(10) NOT NULL,
  name VARCHAR2(100) NOT NULL,
  street VARCHAR2(100) NULL,
  state VARCHAR2(45) NULL,
  zipcode VARCHAR2(45) NULL,
  latitude VARCHAR2(100) NULL,
  longitude VARCHAR2(100) NULL,
  category VARCHAR2(10) NULL,
  hours VARCHAR2(45) NULL,
  days_of_operation VARCHAR2(100) NULL,
  PRIMARY KEY (id)
 ,
  CONSTRAINT business_category
    FOREIGN KEY (category)
    REFERENCES category (id)
   )
;


--------------------------------------------------------
--  DDL for Table PHOTO
--------------------------------------------------------


CREATE TABLE photo (
  uniqueId VARCHAR2(10) NOT NULL,
  description VARCHAR2(100) NULL,
  latitude VARCHAR2(100) NOT NULL,
  belongs_to_user VARCHAR2(10) NULL,
  longitude VARCHAR2(100) NULL,
  uploadedby VARCHAR2(10) NULL,
  belongs_to_busines VARCHAR2(10) NULL,
  PRIMARY KEY (uniqueId)
 ,
  CONSTRAINT Author
    FOREIGN KEY (uploadedby)
    REFERENCES users (yelp_id)
    ON DELETE CASCADE
   ,
  CONSTRAINT personal_photo
    FOREIGN KEY (belongs_to_user)
    REFERENCES users (yelp_id)
   ,
  CONSTRAINT business_photo
    FOREIGN KEY (belongs_to_busines)
    REFERENCES business (id)
   )
;


--------------------------------------------------------
--  DDL for Table SUB_CATEGORY
--------------------------------------------------------

CREATE TABLE sub_category (
  id VARCHAR2(10) NOT NULL,
  name VARCHAR2(255) NOT NULL,
  PRIMARY KEY (id));


  --------------------------------------------------------
  --  Constraints for Table CATEGORY_SUB_CATEGORY
  --------------------------------------------------------

CREATE TABLE category_sub_category (
  category_id VARCHAR2(10) NOT NULL,
  sub_category_id VARCHAR2(10) NOT NULL,
  PRIMARY KEY (category_id, sub_category_id)
 ,
  CONSTRAINT sub_category
    FOREIGN KEY (sub_category_id)
    REFERENCES sub_category (id)
    ON DELETE CASCADE
   ,
  CONSTRAINT category
    FOREIGN KEY (category_id)
    REFERENCES category (id)
    ON DELETE CASCADE
   );



   --------------------------------------------------------
   --  DDL for Table PARKING
   --------------------------------------------------------

CREATE TABLE parking (
  type VARCHAR2(50) NOT NULL,
  belongs_to_bussines VARCHAR2(10) NOT NULL,
  PRIMARY KEY (type, belongs_to_bussines)
 ,
  CONSTRAINT belongs
    FOREIGN KEY (belongs_to_bussines)
    REFERENCES business (id)
    ON DELETE CASCADE
   )
;


--------------------------------------------------------
--  DDL for Table AMBIENT
--------------------------------------------------------


CREATE TABLE ambient (
  type VARCHAR2(50) NOT NULL,
  belongs_to_bussines VARCHAR2(10) NOT NULL,
  PRIMARY KEY (type, belongs_to_bussines)
 ,
  CONSTRAINT belongs0
    FOREIGN KEY (belongs_to_bussines)
    REFERENCES business (id)
    ON DELETE CASCADE
   )
;



--------------------------------------------------------
--  DDL for Table FRIEND_IDS
--------------------------------------------------------


CREATE TABLE friend_ids (
  user_id VARCHAR2(10) NOT NULL,
  friend_id VARCHAR2(10) NOT NULL,
  PRIMARY KEY (user_id, friend_id)
 ,
  CONSTRAINT user_id_friend
    FOREIGN KEY (user_id)
    REFERENCES users (yelp_id) ON DELETE CASCADE
   ,
  CONSTRAINT friend_id_of_user
    FOREIGN KEY (friend_id)
    REFERENCES users (yelp_id) ON DELETE CASCADE
   )
;



--------------------------------------------------------
--  DDL for Table COMPLEMENTS
--------------------------------------------------------


CREATE TABLE complements (
  id VARCHAR2(10) NOT NULL,
  message VARCHAR2(50) NULL,
  to_user VARCHAR2(10) NOT NULL,
  by_user VARCHAR2(10) NOT NULL,
  PRIMARY KEY (id)
 ,
  CONSTRAINT owner_of_complements
    FOREIGN KEY (to_user , by_user)
    REFERENCES friend_ids (user_id , friend_id)
   )
;


--------------------------------------------------------
--  DDL for Table REVIEWS
--------------------------------------------------------


CREATE TABLE reviews (
  id VARCHAR2(10) NOT NULL,
  publish_date TIMESTAMP (0) WITH TIME ZONE NULL,
  stars VARCHAR2(10) NULL,
  author VARCHAR2(10) NULL,
  belongs_to_business VARCHAR2(10) NULL,
  PRIMARY KEY (id)
 ,
  CONSTRAINT review_author
    FOREIGN KEY (author)
    REFERENCES users (yelp_id)
   ,
  CONSTRAINT business_review
    FOREIGN KEY (belongs_to_business)
    REFERENCES business (id) ON DELETE CASCADE
   )
;


--------------------------------------------------------
--  DDL for Table VOTES
--------------------------------------------------------

CREATE TABLE votes (
  review_id VARCHAR2(10) NOT NULL,
  author_id VARCHAR2(10) NOT NULL,
  useful_or_not NUMBER(3) NOT NULL,
  PRIMARY KEY (review_id, author_id)
 ,
  CONSTRAINT review_vote
    FOREIGN KEY (review_id)
    REFERENCES reviews (id)
    ON DELETE CASCADE
   ,
  CONSTRAINT vote_author
    FOREIGN KEY (author_id)
    REFERENCES users (yelp_id)
   )
;



--------------------------------------------------------
--  DDL for Table COMMENTS
--------------------------------------------------------


CREATE TABLE comments (
  review_id VARCHAR2(10) NOT NULL,
  author_id VARCHAR2(10) NOT NULL,
  textual_content VARCHAR2(100) NULL,
  comment_date TIMESTAMP  NULL,
  PRIMARY KEY (review_id, author_id)
 ,
  CONSTRAINT review_comment
    FOREIGN KEY (review_id)
    REFERENCES reviews (id)
    ON DELETE CASCADE
   ,
  CONSTRAINT comment_owner
    FOREIGN KEY (author_id)
    REFERENCES users (yelp_id)
   )
;


--------------------------------------------------------
--  DDL for Table CONTENT
--------------------------------------------------------

CREATE TABLE content (
  id VARCHAR2(10) NOT NULL,
  url VARCHAR2(50) NULL,
  type VARCHAR2(20) NULL,
  PRIMARY KEY (id),
  CONSTRAINT review_id
    FOREIGN KEY (id)
    REFERENCES reviews (id) ON DELETE CASCADE
   )
;


--------------------------------------------------------
--  DDL for Table CHECK_IN_LOG
--------------------------------------------------------


CREATE TABLE check_in_log (
  id VARCHAR2(10) NOT NULL,
  user_id VARCHAR2(10) NULL,
  business_id VARCHAR2(10) NULL,
  PRIMARY KEY (id)
 ,
  CONSTRAINT checkin_user
    FOREIGN KEY (user_id)
    REFERENCES users (yelp_id)
   ,
  CONSTRAINT checkin_business
    FOREIGN KEY (business_id)
    REFERENCES business (id)
   )
;

1) Count the number of businesses having business category name as “National Parks” situated in Arizona.

SELECT COUNT(*) AS NUMBER_OF_BUSINESS from BUSINESS B,CATEGORY C where B.STATE='AZ' AND C.NAME='National Parks';

2) Find the users who were born in CA but never visited CA.

SELECT US.YELP_ID,U.FIRST_NAME FROM USERS US WHERE US.BIRTH_PLACE='CA' MINUS
SELECT DISTINCT U.YELP_ID FROM USERS U, CHECK_IN_LOG C, BUSINESS B
WHERE BIRTH_PLACE='CA' AND B.STATE ='CA' AND C.USER_ID=U.YELP_ID AND B.ID=C.BUSINESS_ID;

3) List the users who are Male and complimented at least one female friend.
SELECT DISTINCT U.YELP_ID,U.FIRST_NAME,U.LAST_NAME FROM USERS U WHERE U.GENDER='M' AND U.YELP_ID IN(SELECT C.BY_USER FROM COMPLEMENTS C, USERS U WHERE U.GENDER ='F' AND C.TO_USER = U.YELP_ID);

4) Find all the businesses whose reviewers have at least 1 friend in their friend list. Order by number of reviews (decreasing), break ties by business ID (increasing).
SELECT R.BELONGS_TO_BUSINESS, COUNT(R.ID) FROM REVIEWS R WHERE R.AUTHOR IN(
SELECT F.USER_ID FROM FRIEND_IDS F GROUP BY F.USER_ID HAVING COUNT(F.FRIEND_ID) > 0) GROUP BY R.BELONGS_TO_BUSINESS ORDER BY COUNT(R.ID) DESC,R.BELONGS_TO_BUSINESS;



5) List top 10 5-star businesses that are reviewed by users between the ages of 20 and 25.
Top means, businesses with the most number of reviews. Order by number of reviews (decreasing),
break ties by business ID (increasing). For each business, print its bid (business id), name, average number of stars, and number of reviews.


BEGIN
   EXECUTE IMMEDIATE 'DROP FUNCTION USER_AGE';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/

CREATE FUNCTION USER_AGE(DOB IN DATE)
   RETURN NUMBER
   IS AGE NUMBER(5);
   BEGIN
      SELECT (MONTHS_BETWEEN(SYSDATE, DOB) / 12)
      INTO AGE
      FROM DUAL;
      RETURN(AGE);
    END;
/

SELECT TOPBUSI.ID, B.NAME, TOPBUSI.STARS, TOPBUSI.NUMBER_OF_REVIEWS FROM (
SELECT B.ID,AVG(R.STARS) AS STARS, COUNT(R.ID) AS NUMBER_OF_REVIEWS FROM BUSINESS B,REVIEWS R WHERE B.ID = R.BELONGS_TO_BUSINESS AND R.AUTHOR IN (
SELECT U.YELP_ID FROM USERS U WHERE USER_AGE(U.BIRTH_DATE) >= 20 AND USER_AGE(U.BIRTH_DATE) <=25)
GROUP BY B.ID HAVING AVG(R.STARS) = 5 ORDER BY COUNT(R.ID) DESC,B.ID) TOPBUSI, BUSINESS B WHERE B.ID=TOPBUSI.ID AND ROWNUM <= 10;


6) List top 10 most traveled yelp users who checked in to 5-star businesses.
Most travelled yelp users are those who have checked in businesses located in more than 5 distinct states. Top means, yelp user with the highest number of checkins, break the ties by user ID.

SELECT RE.USER_ID,U.FIRST_NAME,U.LAST_NAME FROM USERS U,(
SELECT C.USER_ID,COUNT(C.USER_ID) FROM USERS U,CHECK_IN_LOG C, BUSINESS B WHERE C.USER_ID=U.YELP_ID AND C.BUSINESS_ID=B.ID  AND U.YELP_ID  IN(
SELECT C.USER_ID FROM CHECK_IN_LOG C, BUSINESS B, REVIEWS R
WHERE C.BUSINESS_ID=B.ID
GROUP BY C.USER_ID HAVING COUNT(DISTINCT B.STATE) >= 5
) AND B.ID IN(
SELECT B.ID FROM BUSINESS B, REVIEWS R
WHERE B.ID =R.BELONGS_TO_BUSINESS
GROUP BY B.ID
HAVING AVG(R.STARS) = 5)
GROUP BY C.USER_ID
ORDER BY COUNT(C.USER_ID) DESC,C.USER_ID) RE WHERE ROWNUM <= 10;

7) List businesses where their categories have at most 1 distinct subcategory.

SELECT B.ID, B.NAME FROM BUSINESS B WHERE B.CATEGORY IN(
SELECT CSC.CATEGORY_ID FROM CATEGORY_SUB_CATEGORY CSC  GROUP BY CSC.CATEGORY_ID
HAVING COUNT(CSC.SUB_CATEGORY_ID) >= 1)
;




8) List all businesses in CA that are closed on Sunday and have touristy ambient with the highest number of reviews from users not from CA.
Order by the number of reviews from non-CA authors, break ties business ID.
SELECT SB.ID, COUNT(R.ID) AS REVIEW_COUNT FROM REVIEWS R,
(Select B.ID from BUSINESS B, AMBIENT A WHERE B.STATE='CA' AND B.DAYS_OF_OPERATION NOT LIKE '%Sun%' AND A.BELONGS_TO_BUSSINES = B.ID AND A.TYPE='Touristy') SB,
(SELECT U.YELP_ID FROM USERS U WHERE U.BIRTH_PLACE !='CA')  US
WHERE R.AUTHOR=US.YELP_ID AND R.BELONGS_TO_BUSINESS = SB.ID
GROUP BY SB.ID
ORDER BY COUNT(R.ID) DESC, SB.ID;


9) Find the businesses whose average rating was doubled from May 2015 to June 2015 and has the most no. of checkins.
Average rating is the average numbers of stars from all reviews given to a particular business.

SELECT B.ID, B.NAME, CK.CHECK_IN_COUNT FROM BUSINESS B,(
SELECT C.BUSINESS_ID, COUNT(C.BUSINESS_ID) AS CHECK_IN_COUNT FROM CHECK_IN_LOG C WHERE C.BUSINESS_ID IN(
SELECT R.BELONGS_TO_BUSINESS FROM REVIEWS R WHERE TO_CHAR(R.PUBLISH_DATE,'MON-YY')  = 'MAY-15' GROUP BY R.BELONGS_TO_BUSINESS
HAVING (2*AVG(R.STARS)) = (
SELECT AVG(RS.STARS) FROM REVIEWS RS
WHERE TO_CHAR(RS.PUBLISH_DATE,'MON-YY')  = 'JUN-15' AND R.BELONGS_TO_BUSINESS = RS.BELONGS_TO_BUSINESS
GROUP BY RS.BELONGS_TO_BUSINESS))
GROUP BY C.BUSINESS_ID) CK WHERE CK.BUSINESS_ID=B.ID;


10) List all yelp users who haven't reviewed any businesses but have provided at least 2 comments on other user's reviews.
SELECT U.YELP_ID,U.FIRST_NAME,U.LAST_NAME FROM USERS U WHERE U.YELP_ID NOT IN(SELECT R.AUTHOR FROM REVIEWS R) AND U.YELP_ID IN(
SELECT C.AUTHOR_ID FROM COMMENTS C);
