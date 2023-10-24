CREATE TABLE usuarios(

    id  SERIAL not null,
    login varchar(100) not null,
    senha varchar not null,

    primary key (id)

);