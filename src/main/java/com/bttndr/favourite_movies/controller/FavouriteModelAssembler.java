package com.bttndr.favourite_movies.controller;

import com.bttndr.favourite_movies.entity.FavouriteMovie;
import com.bttndr.favourite_movies.entity.Movie;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

@Component
public class FavouriteModelAssembler implements RepresentationModelAssembler<FavouriteMovie, EntityModel<Movie>> {

    @Override
    public EntityModel<Movie> toModel(FavouriteMovie entity) {
       return EntityModel.of(entity.getMovie(),
               linkTo(methodOn(MovieController.class).unMarkAsFavourite(entity.getMovie().getMovieId().toString())).withSelfRel()
        );
    }

    @Override
    public CollectionModel<EntityModel<Movie>> toCollectionModel(Iterable<? extends FavouriteMovie> entities) {

        List< EntityModel<Movie>> transformed = new ArrayList<>();

        for (FavouriteMovie entity : entities) {
            EntityModel<Movie> movieEntityModel = toModel(entity);
            transformed.add(movieEntityModel);
        }
            return CollectionModel.of(transformed);
    }
}
