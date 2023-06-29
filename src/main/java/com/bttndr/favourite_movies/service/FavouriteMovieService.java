package com.bttndr.favourite_movies.service;

import com.bttndr.favourite_movies.entity.FavouriteMovie;
import com.bttndr.favourite_movies.entity.Movie;
import com.bttndr.favourite_movies.repository.FavouriteMovieRepository;
import com.bttndr.favourite_movies.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FavouriteMovieService {

    private final FavouriteMovieRepository favouriteMovieRepository;
    private final MovieRepository movieRepository;

    public FavouriteMovieService(FavouriteMovieRepository favouriteMovieRepository, MovieRepository movieRepository) {
        this.favouriteMovieRepository = favouriteMovieRepository;
        this.movieRepository = movieRepository;
    }

    public List<FavouriteMovie> byUser(String username){
        return this.favouriteMovieRepository.byUsername(username);
    }

    public List<FavouriteMovie> markMovie(Long[] movies, String username) {
        var collect = Arrays.stream(movies).map(m -> {
                    Movie movie = this.movieRepository.findById(m).get();
                    FavouriteMovie favouriteMovie = new FavouriteMovie();
                    favouriteMovie.setMovie(movie);
                    favouriteMovie.setUsername(username);
                    return favouriteMovie;
                }).collect(Collectors.toList());
        this.favouriteMovieRepository.saveAll(collect);
        return collect;
    }

    public void unMarkAsFavourite(Long[] movies, String username) {
        for (Long movie : movies) {
            findByMovieId(movie.toString(), username).ifPresent(this.favouriteMovieRepository::delete);
        }
    }

    public Optional<FavouriteMovie> findByMovieId(String movieId, String username){
        return this.favouriteMovieRepository.byMovieId(movieId, username);
    }
}
