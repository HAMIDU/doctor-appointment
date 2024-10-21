create table "SecurityUser"
(
    ID                 BIGINT      not null,
    "RegistrationDate" DATETIME    not null DEFAULT CURRENT_TIMESTAMP(),
    "Username"         VARCHAR(60) not null,
    "Password"         BINARY(32)  not null,
    "IsEnabled"        BIT                  default 1 not null,
    constraint SECURITYUSER_PK
        primary key (ID)
);

create unique index SECURITYUSER_USERNAME_UINDEX
    on "SecurityUser" ("Username");


create table "Patient"
(
    Id            bigint primary key not null,
    "CreateDate"  DateTime           not null DEFAULT CURRENT_TIMESTAMP(),
    "FirstName"   nvarchar2(50)      not null,
    "LastName"    nvarchar2(50)      not null,
    "PhoneNumber" varchar(11)        not null
);

create table "Doctor"
(
    Id               bigint primary key not null,
    "FirstName"      nvarchar2(50)      not null,
    "LastName"       nvarchar2(50)      not null,
    "MobileNumber"   varchar(11),
    "SecurityUserId" bigint,
    FOREIGN KEY ("SecurityUserId") REFERENCES "SecurityUser"

);

create table "AppointmentSchedule"
(
    Id         bigint primary key not null,
    "DueDate"  DATE               not null DEFAULT CURRENT_DATE(),
    "FromTime" DATETIME           not null,
    "ToTime"   DATETIME           not null,
    "DoctorId" bigint             not null,
    "Status"   tinyint,
    FOREIGN KEY ("DoctorId") REFERENCES "Doctor"

);

create table "ReservedAppointment"
(
    Id                      bigint primary key not null,
    "CreateDate"            DATETIME           not null DEFAULT CURRENT_TIMESTAMP(),
    "PatientId"             bigint             not null,
    "AppointmentScheduleId" bigint             not null,
    FOREIGN KEY ("PatientId") REFERENCES "Patient",
    FOREIGN KEY ("AppointmentScheduleId") REFERENCES "AppointmentSchedule"

);
