package com.bttndr.favourite_movies.controller;

import com.bttndr.favourite_movies.entity.Movie;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MovieModelAssembler implements RepresentationModelAssembler<Movie, EntityModel<Movie>> {


    @Override
    public EntityModel<Movie> toModel(Movie entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(MovieController.class).getByGenre(entity.getGeneres())).withRel("filterByGenre"),
                linkTo(methodOn(MovieController.class).findOne(entity.getMovieId().toString())).withRel("byId"),
                linkTo(methodOn(MovieController.class).markAsFavourite(entity.getMovieId().toString())).withRel("as_favourite"),
                linkTo(methodOn(MovieController.class).unMarkAsFavourite(entity.getMovieId().toString())).withRel("remove_from_favourites")
        );
    }

    @Override
    public CollectionModel<EntityModel<Movie>> toCollectionModel(Iterable<? extends Movie> entities) {
        List< EntityModel<Movie>> transformed = new ArrayList<>();

        for (Movie entity : entities) {
            EntityModel<Movie> movieEntityModel = toModel(entity);
            transformed.add(movieEntityModel);
        }
        return CollectionModel.of(transformed);
    }
}
