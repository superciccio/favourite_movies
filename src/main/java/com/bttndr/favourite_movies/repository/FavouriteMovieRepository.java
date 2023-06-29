package com.bttndr.favourite_movies.repository;

import com.bttndr.favourite_movies.entity.FavouriteMovie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface FavouriteMovieRepository extends JpaRepository<FavouriteMovie, Long> {
    @Query("select f from FAV_MOVIES f where f.username = :username")
    List<FavouriteMovie> byUsername(@Param("username") String username);

    @Query("select f from FAV_MOVIES f where f.movie.movieId = :movieId and f.username = :username")
    Optional<FavouriteMovie> byMovieId(@Param("movieId") String movieId, @Param("username") String username);

}
