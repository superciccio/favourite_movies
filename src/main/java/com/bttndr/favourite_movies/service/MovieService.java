package com.bttndr.favourite_movies.service;

import com.bttndr.favourite_movies.entity.Movie;
import com.bttndr.favourite_movies.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> getAll(){
        return this.movieRepository.findAll();
    }

    public List<Movie> getByGenre(String[] genre) {
        return this.movieRepository.byGenre(genre);
    }

    public Optional<Movie> findById(Long movieId) {
        return this.movieRepository.findById(movieId);
    }
}
