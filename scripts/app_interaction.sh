# login in the application as user1
curl --cookie-jar cookie -d username=user1 -d password=user1 -L http://localhost:8080/login

printf '\n'

# make movie number 2 as favourite (there are a total of 1000 movies, any number till 1000 is fine)
curl -sL -b cookie -X POST http://localhost:8080/movies/asFavourite/2

printf '\n'

# this endpoints shows that your previous call worked (movie is not marked as favourite)
curl -sL -b cookie -v http://localhost:8080/movies/favourites

printf '\n'

# unmark from favourites
curl -sL -b cookie -X POST http://localhost:8080/movies/removeAsFavourite/2

printf '\n'

# movie is not favourite anymore
curl -sL -b cookie -v http://localhost:8080/movies/favourites