package com.bttndr.favourite_movies.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "MOVIES")
public class Movie {

    @Id
    @Column(name = "movie_id")
    private Long movieId;
    private String title;
    private String genre;


    @Transient
    private String[] generes;

    public String[] getGeneres() {
        if(getGenre().contains("|")){
            return getGenre().split("\\|");
        }
        return new String[]{getGenre()};
    }
}
