CREATE TABLE "STUDENT" (
    "ID" BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    "FULLNAME" VARCHAR(50),
    "SKILL" INTEGER NOT NULL,
    "REGION" VARCHAR(255),
    "PRICE" DECIMAL(19,2)
 );
CREATE TABLE "TEACHER" (
    "ID" BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    "FULLNAME" VARCHAR(50),
    "SKILL" INTEGER NOT NULL,
    "REGION" VARCHAR(255),
    "PRICE" DECIMAL(19,2)
 );
CREATE TABLE "LESSON" (
    "ID" BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    "PRICE" DECIMAL(19,2),
    "SKILL" INTEGER NOT NULL,
    "REGION" VARCHAR(255),
    "STUDENTID" BIGINT REFERENCES "STUDENT" (ID),
    "TEACHERID" BIGINT REFERENCES "TEACHER" (ID)
 );