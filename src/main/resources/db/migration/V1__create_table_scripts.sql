create schema if not exists office;
alter database office_management set search_path to 'office';

CREATE TABLE if not exists "people" (
                          "id" serial primary key,
                          "name" varchar   NOT NULL,
                          "email" varchar   NOT NULL,
                          "phone_number" varchar,
                          "created" timestamp   NOT NULL,
                          "updated" timestamp   NOT NULL,
                          CONSTRAINT "uc_people_email" UNIQUE (
                                                               "email"
                              )
);

CREATE TABLE if not exists "departments" (
                               "id" serial primary key,
                               "name" varchar NOT NULL,
                               CONSTRAINT "uc_departments_name" UNIQUE (
                                                                        "name"
                                   )
);

CREATE TABLE if not exists "category" (
                            "id" serial primary key,
                            "name" varchar  NOT NULL,
                            "department_id" integer NOT NULL,
                            CONSTRAINT "uc_category_name" UNIQUE (
                                                                  "name"
                                )
);

CREATE TABLE if not exists "people_departments" (
                                      "people_id" integer   NOT NULL,
                                      "department_id" integer   NOT NULL
);

CREATE TABLE if not exists "people_category" (
                                   "people_id" integer   NOT NULL,
                                   "category_id" integer   NOT NULL,
                                   primary key (people_id, category_id)
);

create table if not exists "leaders_departments" (
    "people_id" integer NOT NULL,
    "department_id" integer NOT NULL
);

ALTER TABLE "category" ADD CONSTRAINT "fk_category_department_id" FOREIGN KEY("department_id")
    REFERENCES "departments" ("id");

ALTER TABLE "people_departments" ADD CONSTRAINT "fk_people_departments_people_id" FOREIGN KEY("people_id")
    REFERENCES "people" ("id");

ALTER TABLE "people_departments" ADD CONSTRAINT "fk_people_departments_department_id" FOREIGN KEY("department_id")
    REFERENCES "departments" ("id");

ALTER TABLE "people_category" ADD CONSTRAINT "fk_people_category_people_id" FOREIGN KEY("people_id")
    REFERENCES "people" ("id");

ALTER TABLE "people_category" ADD CONSTRAINT "fk_people_category_category_id" FOREIGN KEY("category_id")
    REFERENCES "category" ("id");

ALTER TABLE "leaders_departments" ADD CONSTRAINT "fk_leaders_departments_people_id" FOREIGN KEY("people_id")
    REFERENCES "people" ("id");

ALTER TABLE "leaders_departments" ADD CONSTRAINT "fk_leaders_departments_department_id" FOREIGN KEY("department_id")
    REFERENCES "departments" ("id");
