package com.bttndr.favourite_movies.repository;

import com.bttndr.favourite_movies.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("SELECT m FROM MOVIES m WHERE m.genre in (:genre)")
    List<Movie> byGenre(@Param("genre") String[] genre);
}
