package action;

import actor.Actor;
import actor.ActorsAwards;
import entertainment.Movie;
import entertainment.Serial;
import entertainment.Video;
import fileio.ActionInputData;
import user.User;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Query {
    public String findObjectType(final ActionInputData action, ArrayList<User> users,
                               ArrayList<Video> videos, ArrayList<Actor> actors) {
        List<List<String>> filters = action.getFilters();
        switch (action.getObjectType()) {
            case "actors":
                switch (action.getCriteria()) {
                    case "average" -> {
                        ArrayList<Actor> actorsRating = new ArrayList<>();
                        for (Actor actor : actors) {
                            if (actor.calculateRating(videos) != 0)
                                actorsRating.add(actor);
                        }
                        if (action.getSortType().equals("asc")) {
                            actorsRating.sort((actor1, actor2) ->  {
                                if (actor1.calculateRating(videos) == actor2.calculateRating(videos)) {
                                    return actor1.getName().compareTo(actor2.getName());
                                } else {
                                    return (int) (actor1.calculateRating(videos) - actor2.calculateRating(videos));
                                }
                            });
                        } else {
                            actorsRating.sort((actor1, actor2) -> {
                                if (actor1.calculateRating(videos) == actor2.calculateRating(videos)) {
                                    return actor1.getName().compareTo(actor2.getName());
                                } else {
                                    return (int) (actor2.calculateRating(videos) - actor1.calculateRating(videos));
                                }
                            });
                        }
                        while (actorsRating.size() > action.getNumber())
                            actorsRating.remove(actorsRating.size() - 1);
                        return "Query result: " + actorsRating;
                    }
                    case "awards" -> {
                        ArrayList<Actor> filteredActors = new ArrayList<>();
                        for (Actor actor : actors) {
                            boolean ok = true;
                            for (String award : filters.get(3)) {
                                ActorsAwards awardType = Utils.stringToAwards(award);
                                if (!actor.getAwards().containsKey(awardType)) {
                                    ok = false;
                                    break;
                                }
                            }
                            if (ok)
                                filteredActors.add(actor);
                        }
                        if (action.getSortType().equals("asc")) {
                            filteredActors.sort((actor1, actor2) -> {
                                int sum1 = 0, sum2 = 0;
                                for (Map.Entry<ActorsAwards, Integer> entry : actor1.getAwards().entrySet())
                                    sum1 += entry.getValue();
                                for (Map.Entry<ActorsAwards, Integer> entry : actor2.getAwards().entrySet())
                                    sum2 += entry.getValue();
                                if (sum1 > sum2)
                                    return 1;
                                return 0;
                            });
                        } else {
                            filteredActors.sort((actor1, actor2) -> {
                                int sum1 = 0, sum2 = 0;
                                for (Map.Entry<ActorsAwards, Integer> entry : actor1.getAwards().entrySet())
                                    sum1 += entry.getValue();
                                for (Map.Entry<ActorsAwards, Integer> entry : actor2.getAwards().entrySet())
                                    sum2 += entry.getValue();
                                if (sum1 < sum2)
                                    return 1;
                                return 0;
                            });
                        }
                        return "Query result: " + filteredActors;
                    }
                    case "filter_description" -> {
                        ArrayList<Actor> actorsDescription = new ArrayList<>();
                        for (Actor actor : actors) {
                            boolean ok = true;
                            for (String filter : filters.get(2)) {
                                String word = " " + filter + " ";
                                String careerDescription = actor.getCareerDescription().toLowerCase().replace(","," ");
                                careerDescription = careerDescription.replace("\n"," ");
                                careerDescription = careerDescription.replace("."," ");
                                careerDescription = careerDescription.replace("-"," ");
                                careerDescription = " " + careerDescription;
                                if (!careerDescription.toLowerCase().contains(word.toLowerCase())) {
                                    ok = false;
                                    break;
                                }
                            }
                            if (ok)
                                actorsDescription.add(actor);
                        }
                        if (action.getSortType().equals("asc")) {
                            actorsDescription.sort((actor1, actor2) -> {
                                if (actor1.getName().compareTo(actor2.getName()) < 0)
                                    return 1;
                                return 0;
                            });
                        } else {
                            actorsDescription.sort((actor1, actor2) -> {
                                if (actor1.getName().compareTo(actor2.getName()) > 0)
                                    return 1;
                                return 0;
                            });
                        }
                        return "Query result: " + actorsDescription;
                    }
                }
            case "users":
                ArrayList<User> usersRating = new ArrayList<>();
                for (User user : users) {
                    if (user.numberRating() != 0)
                        usersRating.add(user);
                }
                if (action.getSortType().equals("asc")) {
                    users.sort((user1, user2) -> {
                        if (user1.numberRating() > user2.numberRating())
                            return 1;
                        return 0;
                    });
                } else {
                    users.sort((user1, user2) -> {
                        if (user1.numberRating() < user2.numberRating())
                            return 1;
                        return 0;
                    });
                }
                while (usersRating.size() > action.getNumber())
                    usersRating.remove(usersRating.size() - 1);
                return "Query result: " + usersRating;
            case "shows":
                ArrayList<Serial> serials = new ArrayList<>();
                for (Video video : videos) {
                    if (video instanceof Serial)
                        serials.add((Serial) video);
                }
                ArrayList<Serial> filteredSerials = new ArrayList<>();
                for (Serial serial : serials) {
                    boolean ok = true;
                    if (filters.get(0).get(0) != null)
                        if (serial.getYear() != Integer.parseInt(filters.get(0).get(0)))
                            ok = false;
                    if (filters.get(1).get(0) != null)
                        if (!serial.getGenres().contains(filters.get(1).get(0)))
                            ok = false;
                    if (ok)
                        filteredSerials.add(serial);
                }
                switch (action.getCriteria()) {
                    case "ratings" -> {
                        filteredSerials.removeIf(serial -> serial.getRating() == 0);
                        if (action.getSortType().equals("asc")) {
                            filteredSerials.sort((serial1, serial2) -> {
                                if (serial1.getRating() == serial2.getRating()) {
                                    return serial1.getTitle().compareTo(serial2.getTitle());
                                } else {
                                    return (int) (serial1.getRating() - serial2.getRating());
                                }
                            });
                        } else {
                            filteredSerials.sort((serial1, serial2) -> {
                                if (serial1.getRating() == serial2.getRating()) {
                                    return -serial1.getTitle().compareTo(serial2.getTitle());
                                } else {
                                    return (int) (serial2.getRating() - serial1.getRating());
                                }
                            });
                        }
                    }
                    case "favorite" -> {
                        filteredSerials.removeIf(serial -> serial.getFavouriteCnt() == 0);
                        if (action.getSortType().equals("asc")) {
                            filteredSerials.removeIf(serial -> serial.getFavouriteCnt() == 0);
                            filteredSerials.sort((serial1, serial2) -> {
                                if (serial1.getFavouriteCnt() == serial2.getFavouriteCnt()) {
                                    return serial1.getTitle().compareTo(serial2.getTitle());
                                } else {
                                    return serial1.getFavouriteCnt() - serial2.getFavouriteCnt();
                                }
                            });
                        } else {
                            filteredSerials.sort((serial1, serial2) -> {
                                if (serial1.getFavouriteCnt() == serial2.getFavouriteCnt()) {
                                    return -serial1.getTitle().compareTo(serial2.getTitle());
                                } else {
                                    return serial2.getFavouriteCnt() - serial1.getFavouriteCnt();
                                }
                            });
                        }
                    }
                    case "longest" -> {
                        if (action.getSortType().equals("asc")) {
                            filteredSerials.sort((movie1, movie2) ->  {
                                if (movie1.getDuration() == movie2.getDuration()) {
                                    return movie1.getTitle().compareTo(movie2.getTitle());
                                } else {
                                    return movie1.getDuration() - movie2.getDuration();
                                }
                            });
                        } else {
                            filteredSerials.sort((movie1, movie2) -> {
                                if (movie1.getDuration() == movie2.getDuration()) {
                                    return -movie1.getTitle().compareTo(movie2.getTitle());
                                } else {
                                    return movie2.getDuration() - movie1.getDuration();
                                }
                            });
                        }
                    }
                    case "most_viewed" -> {
                        filteredSerials.removeIf(serial -> serial.getViewCnt() == 0);
                        if (action.getSortType().equals("asc")) {
                            filteredSerials.sort((serial1, serial2) ->  {
                                if (serial1.getViewCnt() == serial2.getViewCnt()) {
                                    return serial1.getTitle().compareTo(serial2.getTitle());
                                } else {
                                    return serial1.getViewCnt() - serial2.getViewCnt();
                                }
                            });
                        } else {
                            filteredSerials.sort((serial1, serial2) -> {
                                if (serial1.getViewCnt() == serial2.getViewCnt()) {
                                    return -serial1.getTitle().compareTo(serial2.getTitle());
                                } else {
                                    return serial2.getViewCnt() - serial1.getViewCnt();
                                }
                            });
                        }
                    }
                }
                while (filteredSerials.size() > action.getNumber())
                    filteredSerials.remove(filteredSerials.size() - 1);
                return "Query result: " + filteredSerials;
            case "movies":
                ArrayList<Movie> movies = new ArrayList<>();
                ArrayList<Movie> filteredMovies = new ArrayList<>();
                for (Video video : videos) {
                    if (video instanceof Movie)
                        movies.add((Movie) video);
                }
                for (Movie movie : movies) {
                    boolean ok = true;
                    if (filters.get(0).get(0) != null)
                        if (movie.getYear() != Integer.parseInt(filters.get(0).get(0)))
                            ok = false;
                    if (filters.get(1).get(0) != null)
                        if (!movie.getGenres().contains(filters.get(1).get(0)))
                            ok = false;
                    if (ok)
                        filteredMovies.add(movie);
                }
                switch (action.getCriteria()) {
                    case "ratings" -> {
                        filteredMovies.removeIf(movie -> movie.getRating() == 0);
                        if (action.getSortType().equals("asc")) {
                            filteredMovies.sort((movie1, movie2) -> {
                                if (movie1.getRating() == movie2.getRating()) {
                                    return movie1.getTitle().compareTo(movie2.getTitle());
                                } else {
                                    return (int) (movie1.getRating() - movie2.getRating());
                                }
                            });
                        } else {
                            filteredMovies.sort((movie1, movie2) -> {
                                if (movie1.getRating() == movie2.getRating()) {
                                    return -movie1.getTitle().compareTo(movie2.getTitle());
                                } else {
                                    return (int) (movie2.getRating() - movie1.getRating());
                                }
                            });
                        }
                    }
                    case "favorite" -> {
                        filteredMovies.removeIf(movie -> movie.getFavouriteCnt() == 0);
                        if (action.getSortType().equals("asc")) {
                            filteredMovies.sort((movie1, movie2) -> {
                                if (movie1.getFavouriteCnt() == movie2.getFavouriteCnt()) {
                                    return movie1.getTitle().compareTo(movie2.getTitle());
                                } else {
                                    return movie1.getFavouriteCnt() - movie2.getFavouriteCnt();
                                }
                            });
                        } else {
                            filteredMovies.sort((movie1, movie2) -> {
                                if (movie1.getFavouriteCnt() == movie2.getFavouriteCnt()) {
                                    return -movie1.getTitle().compareTo(movie2.getTitle());
                                } else {
                                    return movie2.getFavouriteCnt() - movie1.getFavouriteCnt();
                                }
                            });
                        }
                    }
                    case "longest" -> {
                        if (action.getSortType().equals("asc")) {
                            filteredMovies.sort((movie1, movie2) ->  {
                                if (movie1.getDuration() == movie2.getDuration()) {
                                    return movie1.getTitle().compareTo(movie2.getTitle());
                                } else {
                                    return movie1.getDuration() - movie2.getDuration();
                                }
                            });
                        } else {
                            filteredMovies.sort((movie1, movie2) -> {
                                if (movie1.getDuration() == movie2.getDuration()) {
                                    return -movie1.getTitle().compareTo(movie2.getTitle());
                                } else {
                                    return movie2.getDuration() - movie1.getDuration();
                                }
                            });
                        }
                    }
                    case "most_viewed" -> {
                        filteredMovies.removeIf(serial -> serial.getViewCnt() == 0);
                        if (action.getSortType().equals("asc")) {
                            filteredMovies.removeIf(movie -> movie.getViewCnt() == 0);
                            filteredMovies.sort((movie1, movie2) ->  {
                                if (movie1.getViewCnt() == movie2.getViewCnt()) {
                                    return movie1.getTitle().compareTo(movie2.getTitle());
                                } else {
                                    return movie1.getViewCnt() - movie2.getViewCnt();
                                }
                            });
                        } else {
                            filteredMovies.sort((movie1, movie2) -> {
                                if (movie1.getViewCnt() == movie2.getViewCnt()) {
                                    return -movie1.getTitle().compareTo(movie2.getTitle());
                                } else {
                                    return movie2.getViewCnt() - movie1.getViewCnt();
                                }
                            });
                        }
                    }
                }
                while (filteredMovies.size() > action.getNumber())
                    filteredMovies.remove(filteredMovies.size() - 1);
                return "Query result: " + filteredMovies;
            }
        return "";
    }
}
