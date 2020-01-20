create table MODE_REGLEMENT
(
    ID_MODE_REGLEMENT NUMBER generated as identity
        constraint MODE_REGLEMENT_PK
            primary key,
    LIBELLE_MODE      VARCHAR2(255) not null
)
/

create unique index MODE_REGLEMENT_LIBELLE_MODE_UINDEX
    on MODE_REGLEMENT (LIBELLE_MODE)
/

create table CONSULTATION
(
    ID_CONSULTATION    NUMBER generated as identity
        constraint CONSULTATION_PK
            primary key,
    DATE_CONSULTATION  DATE           not null,
    HEURE_CONSULTATION TIMESTAMP(6)   not null,
    RETARD             CHAR default 0 not null,
    ANXIETE            NUMBER,
    MOTS_CLES          VARCHAR2(4000),
    COMPORTEMENTS      VARCHAR2(10000),
    PRIX               FLOAT,
    FK_MODE_REGLEMENT  NUMBER
        constraint CONSULTATION_MODE_REGLEMENT_ID_MODE_REGLEMENT_FK
            references MODE_REGLEMENT,
    POSTURES           VARCHAR2(10000)
)
/

create table CLASSIFICATION
(
    ID_CLASSIFICATION      NUMBER generated as identity
        constraint CLASSIFICATION_PK
            primary key,
    LIBELLE_CLASSIFICATION VARCHAR2(255) not null
)
/

create table PROFESSION
(
    ID_PROFESSION      NUMBER generated as identity
        constraint PROFESSION_PK
            primary key,
    LIBELLE_PROFESSION VARCHAR2(255) not null
)
/

create table PATIENT
(
    ID_PATIENT        NUMBER generated as identity
        constraint PATIENT_PK
            primary key,
    NOM               VARCHAR2(255) not null,
    PRENOM            VARCHAR2(255) not null,
    MOYENCONNAISSANCE VARCHAR2(255) not null,
    FK_CLASSIFICATION NUMBER        not null
        constraint PATIENT_CLASSIFICATION_ID_CLASSIFICATION_FK
            references CLASSIFICATION
)
/

create table LOGIN
(
    ID         NUMBER generated as identity
        constraint LOGIN_PK
            primary key,
    EMAIL      VARCHAR2(255) not null,
    PASSWORD   VARCHAR2(255) not null,
    FK_PATIENT NUMBER
        constraint LOGIN_PATIENT_ID_PATIENT_FK
            references PATIENT
)
/

create table HISTORIQUE_PROFESSION
(
    FK_PATIENT    NUMBER not null
        constraint HISTORIQUE_PROFESSION_PATIENT_ID_PATIENT_FK
            references PATIENT,
    FK_PROFESSION NUMBER not null
        constraint HISTORIQUE_PROFESSION_PROFESSION_ID_PROFESSION_FK
            references PROFESSION,
    DATE_DEBUT    DATE   not null,
    DATE_FIN      DATE
)
/

create table HISTORIQUE_CONSULTATION
(
    FK_CONSULTATION NUMBER not null
        constraint HISTORIQUE_CONSULTATION_CONSULTATION_ID_CONSULTATION_FK
            references CONSULTATION,
    FK_PATIENT      NUMBER not null
        constraint HISTORIQUE_CONSULTATION_PATIENT_ID_PATIENT_FK
            references PATIENT,
    constraint HISTORIQUE_CONSULTATION_PK
        primary key (FK_PATIENT, FK_CONSULTATION)
)
/


