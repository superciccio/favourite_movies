drop table if exists MOVIES cascade;

create table MOVIES (
                        movie_id NUMERIC,
                        title VARCHAR(320),
                        genre VARCHAR(240),
                        PRIMARY KEY ( movie_id )
);

drop table if exists FAV_MOVIES cascade;

create table FAV_MOVIES (
                        fav_movie_id NUMERIC,
                        username VARCHAR(20),
                        movie_id numeric,
                        PRIMARY KEY ( fav_movie_id )
);