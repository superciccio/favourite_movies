# fetch all movies
curl -v -u 'user1:user1' -L http://localhost:8080/api/movies/

printf '\n'

# make movie number 2 as favourite (there are a total of 1000 movies, any number till 1000 is fine)
curl -v -u 'user1:user1' -X POST http://localhost:8080/api/movies/asFavourite/2

printf '\n'

# this endpoints shows that your previous call worked (movie is not marked as favourite)
curl -v -u 'user1:user1' http://localhost:8080/api/movies/favourites

printf '\n'

# unmark from favourites
curl -v -u 'user1:user1' -X DELETE http://localhost:8080/api/movies/removeAsFavourite/2

printf '\n'

# movie is not favourite anymore
curl -v -u 'user1:user1' http://localhost:8080/api/movies/favourites