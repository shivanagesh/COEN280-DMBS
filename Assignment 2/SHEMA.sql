--------------------------------------------------------
--  File created - Wednesday-February-03-2016   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table CATEGORY_SUB_CATEGORY
--------------------------------------------------------

  CREATE TABLE "SYS"."CATEGORY_SUB_CATEGORY" ("CATEGORY_ID" VARCHAR2(10), "SUB_CATEGORY_ID" VARCHAR2(10)) ;
--------------------------------------------------------
--  DDL for Table CONTENT
--------------------------------------------------------

  CREATE TABLE "SYS"."CONTENT" ("ID" VARCHAR2(10), "URL" VARCHAR2(50), "TYPE" VARCHAR2(20)) ;
--------------------------------------------------------
--  DDL for Table COMPLEMENTS
--------------------------------------------------------

  CREATE TABLE "SYS"."COMPLEMENTS" ("ID" VARCHAR2(10), "MESSAGE" VARCHAR2(50), "TO_USER" VARCHAR2(10), "BY_USER" VARCHAR2(10)) ;
--------------------------------------------------------
--  DDL for Table USERS
--------------------------------------------------------

  CREATE TABLE "SYS"."USERS" ("FIRST_NAME" VARCHAR2(50), "LAST_NAME" VARCHAR2(45), "YELP_ID" VARCHAR2(10), "EMAIL" VARCHAR2(50), "BIRTH_PLACE" VARCHAR2(50), "BIRTH_DATE" DATE, "GENDER" VARCHAR2(5)) ;
--------------------------------------------------------
--  DDL for Table FRIEND_IDS
--------------------------------------------------------

  CREATE TABLE "SYS"."FRIEND_IDS" ("USER_ID" VARCHAR2(10), "FRIEND_ID" VARCHAR2(10)) ;
--------------------------------------------------------
--  DDL for Table REVIEWS
--------------------------------------------------------

  CREATE TABLE "SYS"."REVIEWS" ("ID" VARCHAR2(10), "PUBLISH_DATE" TIMESTAMP (0) WITH TIME ZONE, "STARS" VARCHAR2(10), "AUTHOR" VARCHAR2(10), "BELONGS_TO_BUSINESS" VARCHAR2(10)) ;
--------------------------------------------------------
--  DDL for Table PHOTO
--------------------------------------------------------

  CREATE TABLE "SYS"."PHOTO" ("UNIQUEID" VARCHAR2(10), "DESCRIPTION" VARCHAR2(100), "LATITUDE" VARCHAR2(100), "BELONGS_TO_USER" VARCHAR2(10), "LONGITUDE" VARCHAR2(100), "UPLOADEDBY" VARCHAR2(10), "BELONGS_TO_BUSINES" VARCHAR2(10)) ;
--------------------------------------------------------
--  DDL for Table COMMENTS
--------------------------------------------------------

  CREATE TABLE "SYS"."COMMENTS" ("REVIEW_ID" VARCHAR2(10), "AUTHOR_ID" VARCHAR2(10), "TEXTUAL_CONTENT" VARCHAR2(100), "COMMENT_DATE" TIMESTAMP (6)) ;
--------------------------------------------------------
--  DDL for Table SUB_CATEGORY
--------------------------------------------------------

  CREATE TABLE "SYS"."SUB_CATEGORY" ("ID" VARCHAR2(10), "NAME" VARCHAR2(255)) ;
--------------------------------------------------------
--  DDL for Table CATEGORY
--------------------------------------------------------

  CREATE TABLE "SYS"."CATEGORY" ("ID" VARCHAR2(10), "NAME" VARCHAR2(255)) ;
--------------------------------------------------------
--  DDL for Table BUSINESS
--------------------------------------------------------

  CREATE TABLE "SYS"."BUSINESS" ("ID" VARCHAR2(10), "NAME" VARCHAR2(100), "STREET" VARCHAR2(100), "STATE" VARCHAR2(45), "ZIPCODE" VARCHAR2(45), "LATITUDE" VARCHAR2(100), "LONGITUDE" VARCHAR2(100), "CATEGORY" VARCHAR2(10), "HOURS" VARCHAR2(45), "DAYS_OF_OPERATION" VARCHAR2(100)) ;
--------------------------------------------------------
--  DDL for Table AMBIENT
--------------------------------------------------------

  CREATE TABLE "SYS"."AMBIENT" ("TYPE" VARCHAR2(50), "BELONGS_TO_BUSSINES" VARCHAR2(10)) ;
--------------------------------------------------------
--  DDL for Table VOTES
--------------------------------------------------------

  CREATE TABLE "SYS"."VOTES" ("REVIEW_ID" VARCHAR2(10), "AUTHOR_ID" VARCHAR2(10), "USEFUL_OR_NOT" NUMBER(3,0)) ;
--------------------------------------------------------
--  DDL for Table PARKING
--------------------------------------------------------

  CREATE TABLE "SYS"."PARKING" ("TYPE" VARCHAR2(50), "BELONGS_TO_BUSSINES" VARCHAR2(10)) ;
--------------------------------------------------------
--  DDL for Table CHECK_IN_LOG
--------------------------------------------------------

  CREATE TABLE "SYS"."CHECK_IN_LOG" ("ID" VARCHAR2(10), "USER_ID" VARCHAR2(10), "BUSINESS_ID" VARCHAR2(10)) ;
--------------------------------------------------------
--  DDL for Index SYS_C007664
--------------------------------------------------------

  CREATE UNIQUE INDEX "SYS"."SYS_C007664" ON "SYS"."CATEGORY_SUB_CATEGORY" ("CATEGORY_ID", "SUB_CATEGORY_ID") ;
--------------------------------------------------------
--  DDL for Index SYS_C007703
--------------------------------------------------------

  CREATE UNIQUE INDEX "SYS"."SYS_C007703" ON "SYS"."CONTENT" ("ID") ;
--------------------------------------------------------
--  DDL for Index SYS_C007684
--------------------------------------------------------

  CREATE UNIQUE INDEX "SYS"."SYS_C007684" ON "SYS"."COMPLEMENTS" ("ID") ;
--------------------------------------------------------
--  DDL for Index SYS_C007646
--------------------------------------------------------

  CREATE UNIQUE INDEX "SYS"."SYS_C007646" ON "SYS"."USERS" ("YELP_ID") ;
--------------------------------------------------------
--  DDL for Index SYS_C007677
--------------------------------------------------------

  CREATE UNIQUE INDEX "SYS"."SYS_C007677" ON "SYS"."FRIEND_IDS" ("USER_ID", "FRIEND_ID") ;
--------------------------------------------------------
--  DDL for Index SYS_C007687
--------------------------------------------------------

  CREATE UNIQUE INDEX "SYS"."SYS_C007687" ON "SYS"."REVIEWS" ("ID") ;
--------------------------------------------------------
--  DDL for Index SYS_C007655
--------------------------------------------------------

  CREATE UNIQUE INDEX "SYS"."SYS_C007655" ON "SYS"."PHOTO" ("UNIQUEID", "LATITUDE") ;
--------------------------------------------------------
--  DDL for Index SYS_C007699
--------------------------------------------------------

  CREATE UNIQUE INDEX "SYS"."SYS_C007699" ON "SYS"."COMMENTS" ("REVIEW_ID", "AUTHOR_ID") ;
--------------------------------------------------------
--  DDL for Index SYS_C007661
--------------------------------------------------------

  CREATE UNIQUE INDEX "SYS"."SYS_C007661" ON "SYS"."SUB_CATEGORY" ("ID") ;
--------------------------------------------------------
--  DDL for Index SYS_C007649
--------------------------------------------------------

  CREATE UNIQUE INDEX "SYS"."SYS_C007649" ON "SYS"."CATEGORY" ("ID") ;
--------------------------------------------------------
--  DDL for Index SYS_C007651
--------------------------------------------------------

  CREATE UNIQUE INDEX "SYS"."SYS_C007651" ON "SYS"."BUSINESS" ("ID") ;
--------------------------------------------------------
--  DDL for Index SYS_C007673
--------------------------------------------------------

  CREATE UNIQUE INDEX "SYS"."SYS_C007673" ON "SYS"."AMBIENT" ("TYPE", "BELONGS_TO_BUSSINES") ;
--------------------------------------------------------
--  DDL for Index SYS_C007693
--------------------------------------------------------

  CREATE UNIQUE INDEX "SYS"."SYS_C007693" ON "SYS"."VOTES" ("REVIEW_ID", "AUTHOR_ID") ;
--------------------------------------------------------
--  DDL for Index SYS_C007669
--------------------------------------------------------

  CREATE UNIQUE INDEX "SYS"."SYS_C007669" ON "SYS"."PARKING" ("TYPE", "BELONGS_TO_BUSSINES") ;
--------------------------------------------------------
--  DDL for Index SYS_C007706
--------------------------------------------------------

  CREATE UNIQUE INDEX "SYS"."SYS_C007706" ON "SYS"."CHECK_IN_LOG" ("ID") ;
--------------------------------------------------------
--  Constraints for Table CATEGORY_SUB_CATEGORY
--------------------------------------------------------

  ALTER TABLE "SYS"."CATEGORY_SUB_CATEGORY" ADD PRIMARY KEY ("CATEGORY_ID", "SUB_CATEGORY_ID") ENABLE;
  ALTER TABLE "SYS"."CATEGORY_SUB_CATEGORY" MODIFY ("SUB_CATEGORY_ID" NOT NULL ENABLE);
  ALTER TABLE "SYS"."CATEGORY_SUB_CATEGORY" MODIFY ("CATEGORY_ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table CONTENT
--------------------------------------------------------

  ALTER TABLE "SYS"."CONTENT" ADD PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "SYS"."CONTENT" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table COMPLEMENTS
--------------------------------------------------------

  ALTER TABLE "SYS"."COMPLEMENTS" ADD PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "SYS"."COMPLEMENTS" MODIFY ("BY_USER" NOT NULL ENABLE);
  ALTER TABLE "SYS"."COMPLEMENTS" MODIFY ("TO_USER" NOT NULL ENABLE);
  ALTER TABLE "SYS"."COMPLEMENTS" MODIFY ("MESSAGE" NOT NULL ENABLE);
  ALTER TABLE "SYS"."COMPLEMENTS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table USERS
--------------------------------------------------------

  ALTER TABLE "SYS"."USERS" ADD PRIMARY KEY ("YELP_ID") ENABLE;
  ALTER TABLE "SYS"."USERS" MODIFY ("BIRTH_DATE" NOT NULL ENABLE);
  ALTER TABLE "SYS"."USERS" MODIFY ("YELP_ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table FRIEND_IDS
--------------------------------------------------------

  ALTER TABLE "SYS"."FRIEND_IDS" ADD PRIMARY KEY ("USER_ID", "FRIEND_ID") ENABLE;
  ALTER TABLE "SYS"."FRIEND_IDS" MODIFY ("FRIEND_ID" NOT NULL ENABLE);
  ALTER TABLE "SYS"."FRIEND_IDS" MODIFY ("USER_ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table REVIEWS
--------------------------------------------------------

  ALTER TABLE "SYS"."REVIEWS" ADD PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "SYS"."REVIEWS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PHOTO
--------------------------------------------------------

  ALTER TABLE "SYS"."PHOTO" ADD PRIMARY KEY ("UNIQUEID", "LATITUDE") ENABLE;
  ALTER TABLE "SYS"."PHOTO" MODIFY ("LATITUDE" NOT NULL ENABLE);
  ALTER TABLE "SYS"."PHOTO" MODIFY ("UNIQUEID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table COMMENTS
--------------------------------------------------------

  ALTER TABLE "SYS"."COMMENTS" ADD PRIMARY KEY ("REVIEW_ID", "AUTHOR_ID") ENABLE;
  ALTER TABLE "SYS"."COMMENTS" MODIFY ("AUTHOR_ID" NOT NULL ENABLE);
  ALTER TABLE "SYS"."COMMENTS" MODIFY ("REVIEW_ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table SUB_CATEGORY
--------------------------------------------------------

  ALTER TABLE "SYS"."SUB_CATEGORY" ADD PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "SYS"."SUB_CATEGORY" MODIFY ("NAME" NOT NULL ENABLE);
  ALTER TABLE "SYS"."SUB_CATEGORY" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table CATEGORY
--------------------------------------------------------

  ALTER TABLE "SYS"."CATEGORY" ADD PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "SYS"."CATEGORY" MODIFY ("NAME" NOT NULL ENABLE);
  ALTER TABLE "SYS"."CATEGORY" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table BUSINESS
--------------------------------------------------------

  ALTER TABLE "SYS"."BUSINESS" ADD PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "SYS"."BUSINESS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table AMBIENT
--------------------------------------------------------

  ALTER TABLE "SYS"."AMBIENT" ADD PRIMARY KEY ("TYPE", "BELONGS_TO_BUSSINES") ENABLE;
  ALTER TABLE "SYS"."AMBIENT" MODIFY ("BELONGS_TO_BUSSINES" NOT NULL ENABLE);
  ALTER TABLE "SYS"."AMBIENT" MODIFY ("TYPE" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table VOTES
--------------------------------------------------------

  ALTER TABLE "SYS"."VOTES" ADD PRIMARY KEY ("REVIEW_ID", "AUTHOR_ID") ENABLE;
  ALTER TABLE "SYS"."VOTES" MODIFY ("USEFUL_OR_NOT" NOT NULL ENABLE);
  ALTER TABLE "SYS"."VOTES" MODIFY ("AUTHOR_ID" NOT NULL ENABLE);
  ALTER TABLE "SYS"."VOTES" MODIFY ("REVIEW_ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PARKING
--------------------------------------------------------

  ALTER TABLE "SYS"."PARKING" ADD PRIMARY KEY ("TYPE", "BELONGS_TO_BUSSINES") ENABLE;
  ALTER TABLE "SYS"."PARKING" MODIFY ("BELONGS_TO_BUSSINES" NOT NULL ENABLE);
  ALTER TABLE "SYS"."PARKING" MODIFY ("TYPE" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table CHECK_IN_LOG
--------------------------------------------------------

  ALTER TABLE "SYS"."CHECK_IN_LOG" ADD PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "SYS"."CHECK_IN_LOG" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Ref Constraints for Table CATEGORY_SUB_CATEGORY
--------------------------------------------------------

  ALTER TABLE "SYS"."CATEGORY_SUB_CATEGORY" ADD CONSTRAINT "CATEGORY" FOREIGN KEY ("CATEGORY_ID") REFERENCES "SYS"."CATEGORY" ("ID") ON DELETE CASCADE ENABLE;
  ALTER TABLE "SYS"."CATEGORY_SUB_CATEGORY" ADD CONSTRAINT "SUB_CATEGORY" FOREIGN KEY ("SUB_CATEGORY_ID") REFERENCES "SYS"."SUB_CATEGORY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table CONTENT
--------------------------------------------------------

  ALTER TABLE "SYS"."CONTENT" ADD CONSTRAINT "REVIEW_ID" FOREIGN KEY ("ID") REFERENCES "SYS"."REVIEWS" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table COMPLEMENTS
--------------------------------------------------------

  ALTER TABLE "SYS"."COMPLEMENTS" ADD CONSTRAINT "OWNER_OF_COMPLEMENTS" FOREIGN KEY ("TO_USER", "BY_USER") REFERENCES "SYS"."FRIEND_IDS" ("USER_ID", "FRIEND_ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table FRIEND_IDS
--------------------------------------------------------

  ALTER TABLE "SYS"."FRIEND_IDS" ADD CONSTRAINT "FRIEND_ID_OF_USER" FOREIGN KEY ("FRIEND_ID") REFERENCES "SYS"."USERS" ("YELP_ID") ENABLE;
  ALTER TABLE "SYS"."FRIEND_IDS" ADD CONSTRAINT "USER_ID_FRIEND" FOREIGN KEY ("USER_ID") REFERENCES "SYS"."USERS" ("YELP_ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table REVIEWS
--------------------------------------------------------

  ALTER TABLE "SYS"."REVIEWS" ADD CONSTRAINT "BUSINESS_REVIEW" FOREIGN KEY ("BELONGS_TO_BUSINESS") REFERENCES "SYS"."BUSINESS" ("ID") ENABLE;
  ALTER TABLE "SYS"."REVIEWS" ADD CONSTRAINT "REVIEW_AUTHOR" FOREIGN KEY ("AUTHOR") REFERENCES "SYS"."USERS" ("YELP_ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PHOTO
--------------------------------------------------------

  ALTER TABLE "SYS"."PHOTO" ADD CONSTRAINT "AUTHOR" FOREIGN KEY ("UPLOADEDBY") REFERENCES "SYS"."USERS" ("YELP_ID") ON DELETE CASCADE ENABLE;
  ALTER TABLE "SYS"."PHOTO" ADD CONSTRAINT "BUSINESS_PHOTO" FOREIGN KEY ("BELONGS_TO_BUSINES") REFERENCES "SYS"."BUSINESS" ("ID") ENABLE;
  ALTER TABLE "SYS"."PHOTO" ADD CONSTRAINT "PERSONAL_PHOTO" FOREIGN KEY ("BELONGS_TO_USER") REFERENCES "SYS"."USERS" ("YELP_ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table COMMENTS
--------------------------------------------------------

  ALTER TABLE "SYS"."COMMENTS" ADD CONSTRAINT "COMMENT_OWNER" FOREIGN KEY ("AUTHOR_ID") REFERENCES "SYS"."USERS" ("YELP_ID") ENABLE;
  ALTER TABLE "SYS"."COMMENTS" ADD CONSTRAINT "REVIEW_COMMENT" FOREIGN KEY ("REVIEW_ID") REFERENCES "SYS"."REVIEWS" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table BUSINESS
--------------------------------------------------------

  ALTER TABLE "SYS"."BUSINESS" ADD CONSTRAINT "BUSINESS_CATEGORY" FOREIGN KEY ("CATEGORY") REFERENCES "SYS"."CATEGORY" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table AMBIENT
--------------------------------------------------------

  ALTER TABLE "SYS"."AMBIENT" ADD CONSTRAINT "BELONGS0" FOREIGN KEY ("BELONGS_TO_BUSSINES") REFERENCES "SYS"."BUSINESS" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table VOTES
--------------------------------------------------------

  ALTER TABLE "SYS"."VOTES" ADD CONSTRAINT "REVIEW_VOTE" FOREIGN KEY ("REVIEW_ID") REFERENCES "SYS"."REVIEWS" ("ID") ON DELETE CASCADE ENABLE;
  ALTER TABLE "SYS"."VOTES" ADD CONSTRAINT "VOTE_AUTHOR" FOREIGN KEY ("AUTHOR_ID") REFERENCES "SYS"."USERS" ("YELP_ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PARKING
--------------------------------------------------------

  ALTER TABLE "SYS"."PARKING" ADD CONSTRAINT "BELONGS" FOREIGN KEY ("BELONGS_TO_BUSSINES") REFERENCES "SYS"."BUSINESS" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table CHECK_IN_LOG
--------------------------------------------------------

  ALTER TABLE "SYS"."CHECK_IN_LOG" ADD CONSTRAINT "CHECKIN_BUSINESS" FOREIGN KEY ("BUSINESS_ID") REFERENCES "SYS"."BUSINESS" ("ID") ENABLE;
  ALTER TABLE "SYS"."CHECK_IN_LOG" ADD CONSTRAINT "CHECKIN_USER" FOREIGN KEY ("USER_ID") REFERENCES "SYS"."USERS" ("YELP_ID") ENABLE;
