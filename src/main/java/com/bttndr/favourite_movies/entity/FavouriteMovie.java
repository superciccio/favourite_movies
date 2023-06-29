package com.bttndr.favourite_movies.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "FAV_MOVIES")
public class FavouriteMovie {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "fav_movie_id")
    private Long favMovieId;
    private String username;

    @OneToOne
    @JoinColumn(name = "movieId")
    private Movie movie;

}
