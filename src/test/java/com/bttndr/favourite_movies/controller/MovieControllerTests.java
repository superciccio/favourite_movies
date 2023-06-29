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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
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

import static org.mockito.BDDMockito.given;


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
    public void user_not_logged() throws Exception {
        mvc.perform(get("/")).andExpect(status().is(302));
    }

    @Test
    public void user_logged() throws Exception {
        mvc.perform(get("/login")
                .param("username", "user1")
                .param("password", "user1"))
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


        given(movieService.getAll()).willReturn(Arrays.asList(movie1, movie2));

        mvc.perform(get("/movies/")).andExpect(content().json(getResourceFileAsString(LIST_OF_MOVIE_JSON)));
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


        given(movieService.getByGenre(new String[]{"Fantasy"})).willReturn(Collections.singletonList(movie1));

        mvc.perform(get("/movies/filter/Fantasy")).andExpect(content().json(getResourceFileAsString("filtered_list.json")));
    }


    @Test
    @WithMockUser
    public void should_return_a_movie() throws Exception {
        Movie movie1 = new Movie();
        movie1.setMovieId(1L);
        movie1.setGenre("Fantasy");
        movie1.setTitle("Harry potter 1");


        given(movieService.findById(1L)).willReturn(java.util.Optional.of(movie1));

        mvc.perform(get("/movies/1")).andExpect(content().json(getResourceFileAsString(SINGLE_MOVIE_JSON)));
    }

    @Test
    @WithMockUser(username = "user1")
    public void mark_a_movie_as_favourite() throws Exception {
        Movie movie1 = new Movie();
        movie1.setMovieId(1L);
        movie1.setGenre("Fantasy");
        movie1.setTitle("Harry potter 1");

        FavouriteMovie fv = new FavouriteMovie();
        fv.setMovie(movie1);
        fv.setUsername("user1");
        fv.setFavMovieId(1L);

        given(favouriteMovieService.markMovie(new Long[]{1L}, "user1")).willReturn(List.of(fv));


        mvc.perform(post("/movies/asFavourite/1")).andExpect(content().json(getResourceFileAsString("fav_movie.json")));
    }

    @Test
    @WithMockUser(username = "user1")
    public void unmark_a_movie_from_favourite() throws Exception {
        Movie movie1 = new Movie();
        movie1.setMovieId(1L);
        movie1.setGenre("Fantasy");
        movie1.setTitle("Harry potter 1");

        FavouriteMovie fv = new FavouriteMovie();
        fv.setMovie(movie1);
        fv.setUsername("user1");
        fv.setFavMovieId(1L);

       doNothing().when(favouriteMovieService).unMarkAsFavourite(new Long[]{1L}, "user1");

       when(favouriteMovieService.findByMovieId(movie1.getMovieId().toString(), "user1")).thenReturn(Optional.empty());

        mvc.perform(post("/movies/removeAsFavourite/1")).andExpect(content().json("{}"));
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