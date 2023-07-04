package com.bttndr.favourite_movies.controller;

import com.bttndr.favourite_movies.entity.Movie;
import com.bttndr.favourite_movies.service.FavouriteMovieService;
import com.bttndr.favourite_movies.service.MovieService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;
    private final FavouriteMovieService favouriteMovieService;
    private final MovieModelAssembler movieModelAssembler;
    private final FavouriteModelAssembler favouriteModelAssembler;


    public MovieController(MovieService movieService, FavouriteMovieService favouriteMovieService, MovieModelAssembler movieModelAssembler, FavouriteModelAssembler favouriteModelAssembler) {
        this.movieService = movieService;
        this.favouriteMovieService = favouriteMovieService;
        this.movieModelAssembler = movieModelAssembler;
        this.favouriteModelAssembler = favouriteModelAssembler;
    }

    @GetMapping("/")
    public CollectionModel<EntityModel<Movie>> getAll() {
        List<Movie> all = this.movieService.getAll();
        return movieModelAssembler.toCollectionModel(all);
    }

    @GetMapping("/filter/{genre}")
    public CollectionModel<EntityModel<Movie>> getByGenre(@PathVariable("genre") String[] genre){
       return movieModelAssembler.toCollectionModel(this.movieService.getByGenre(genre));
    }

    @PostMapping("/asFavourite/{movieId}")
    public EntityModel<CollectionModel<EntityModel<Movie>>> markAsFavourite(@PathVariable("movieId") String movieId) {
        var favouriteMovies = this.favouriteMovieService.markMovie(new Long[]{Long.valueOf(movieId)}, usernameCurrentUser());
        return EntityModel.of(favouriteModelAssembler.toCollectionModel(favouriteMovies));
    }

    @DeleteMapping("/removeAsFavourite/{movieId}")
    public ResponseEntity unMarkAsFavourite(@PathVariable("movieId") String movieId) {
        this.favouriteMovieService.unMarkAsFavourite(new Long[]{Long.valueOf(movieId)}, usernameCurrentUser());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/favourites")
    public CollectionModel<EntityModel<Movie>> fetchUserFavourites() {
        return favouriteModelAssembler.toCollectionModel(this.favouriteMovieService.byUser(usernameCurrentUser()));

    }

    public String usernameCurrentUser(){
        return ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }


    @GetMapping("/{movieId}")
    public EntityModel<Movie> findOne(@PathVariable String movieId) {
        Optional<Movie> byId = this.movieService.findById(Long.valueOf(movieId));
        return this.movieModelAssembler.toModel(byId.orElseThrow());
    }
}
