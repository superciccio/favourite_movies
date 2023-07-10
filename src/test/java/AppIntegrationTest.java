import com.bttndr.favourite_movies.FavouriteMoviesApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = FavouriteMoviesApplication.class)
@AutoConfigureMockMvc
public class AppIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Test()
    @WithMockUser
    void given_an_authenticated_user_perform_end_to_end_journey() throws Exception {

        // returns a list of movies
        ResultActions movies = mvc.perform(get("/api/movies/"));
        movies.andExpect(status().is(200));
        movies.andExpect(jsonPath("$._embedded.movies").isArray());

        // no favourites already saved
        ResultActions favMovies = mvc.perform(get("/api/movies/favourites"));
        favMovies.andExpect(status().is(200));
        favMovies.andExpect(content().string("{ }"));


        ResultActions favMovie = mvc.perform(post("/api/movies/asFavourite/2"));
        favMovie.andExpect(status().is(200));
        favMovie.andExpect(jsonPath("$._embedded.movies").isArray());
        favMovie.andExpect(jsonPath("$._embedded.movies[0].title").exists());
        favMovie.andExpect(jsonPath("$._embedded.movies[0].genre").exists());
        favMovie.andExpect(jsonPath("$._embedded.movies[0].generes").isArray());
        favMovie.andExpect(jsonPath("$._embedded.movies[0]._links").isMap());

        // no favourites already saved
        favMovies = mvc.perform(get("/api/movies/favourites"));
        favMovies.andExpect(status().is(200));
        favMovie.andExpect(jsonPath("$._embedded.movies").isArray());

        // remove from favourite list
        ResultActions removeFavouriteMovie = mvc.perform(delete("/api/movies/removeAsFavourite/2"));
        removeFavouriteMovie.andExpect(status().is(204));

        favMovies = mvc.perform(get("/api/movies/favourites"));
        favMovies.andExpect(status().is(200));
        favMovies.andExpect(content().string("{ }"));
    }
}
