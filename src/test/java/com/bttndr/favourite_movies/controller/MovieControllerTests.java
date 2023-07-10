package com.bttndr.favourite_movies.controller;

import com.bttndr.favourite_movies.entity.FavouriteMovie;
import com.bttndr.favourite_movies.entity.Movie;
import com.bttndr.favourite_movies.service.FavouriteMovieService;
import com.bttndr.favourite_movies.service.MovieService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MovieController.class)
@Import({ MovieModelAssembler.class, FavouriteModelAssembler.class })
class MovieControllerTests {

    public static final String LIST_OF_MOVIE_JSON = "list_of_movie.json";
    public static final String SINGLE_MOVIE_JSON = "single_movie.json";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MovieService movieService;

    @MockBean
    private FavouriteMovieService favouriteMovieService;

    @Test
    public void user_not_authorized() throws Exception {
        mvc.perform(get("/api/")).andExpect(status().is(401));
    }

    @Test
    @WithMockUser
    public void user_logged() throws Exception {
        mvc.perform(get("/api/movies/"))
                .andExpect(status().is(200));
    }

    @Test
    @WithMockUser
    public void should_return_a_list_of_movie() throws Exception {
        Movie movie1 = new Movie();
        movie1.setMovieId(1L);
        movie1.setGenre("Fantasy");
        movie1.setTitle("Harry potter 1");

        Movie movie2 = new Movie();
        movie2.setMovieId(2L);
        movie2.setGenre("Dramatic");
        movie2.setTitle("The Wale");


        when(movieService.getAll()).thenReturn(Arrays.asList(movie1, movie2));

        mvc.perform(get("/api/movies/"))
                .andExpect(content().json(getResourceFileAsString(LIST_OF_MOVIE_JSON)))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$._embedded.movies").isArray())
                .andExpect(jsonPath("$._embedded.movies[0]._links").isMap())
                .andExpect(jsonPath("$._embedded.movies[0]._links.filterByGenre").exists())
                .andExpect(jsonPath("$._embedded.movies[0]._links.byId").exists())
                .andExpect(jsonPath("$._embedded.movies[0]._links.as_favourite").exists())
                .andExpect(jsonPath("$._embedded.movies[0]._links.remove_from_favourites").exists());
    }

    @Test
    @WithMockUser
    public void should_return_a_list_of_movie_filtered_by_genre() throws Exception {
        Movie movie1 = new Movie();
        movie1.setMovieId(1L);
        movie1.setGenre("Fantasy");
        movie1.setTitle("Harry potter 1");

        Movie movie2 = new Movie();
        movie2.setMovieId(2L);
        movie2.setGenre("Dramatic");
        movie2.setTitle("The Wale");


        when(movieService.getByGenre(new String[]{"Fantasy"})).thenReturn(Collections.singletonList(movie1));

        mvc.perform(get("/api/movies/filter/Fantasy"))
                .andExpect(content().json(getResourceFileAsString("filtered_list.json")));
    }


    @Test
    @WithMockUser
    public void should_return_a_movie() throws Exception {
        Movie movie1 = new Movie();
        movie1.setMovieId(1L);
        movie1.setGenre("Fantasy");
        movie1.setTitle("Harry potter 1");


        when(movieService.findById(1L)).thenReturn(java.util.Optional.of(movie1));

        mvc.perform(get("/api/movies/1")).andExpect(content().json(getResourceFileAsString(SINGLE_MOVIE_JSON)));
    }

    @Test
    @WithMockUser //user is the default username
    public void mark_unmark_a_movie_as_favourite() throws Exception {
        Movie movie1 = new Movie();
        movie1.setMovieId(1L);
        movie1.setGenre("Fantasy");
        movie1.setTitle("Harry potter 1");

        FavouriteMovie fv = new FavouriteMovie();
        fv.setMovie(movie1);
        fv.setUsername("user");
        fv.setFavMovieId(1L);

        when(favouriteMovieService.markMovie(new Long[]{1L}, "user")).thenReturn(List.of(fv));

        mvc.perform(post("/api/movies/asFavourite/1").with(csrf()))
                .andExpect(content().json(getResourceFileAsString("fav_movie.json")));

        doNothing().when(favouriteMovieService).unMarkAsFavourite(new Long[]{1L}, "user");

        when(favouriteMovieService.findByMovieId(movie1.getMovieId().toString(), "user")).thenReturn(Optional.empty());

        mvc.perform(delete("/api/movies/removeAsFavourite/1"));
    }


    public static String getResourceFileAsString(String fileName) {
        InputStream is = getResourceFileAsInputStream(fileName);
        if (is != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } else {
            throw new RuntimeException("resource not found");
        }
    }

    public static InputStream getResourceFileAsInputStream(String fileName) {
        ClassLoader classLoader = MovieControllerTests.class.getClassLoader();
        return classLoader.getResourceAsStream(fileName);
    }

}