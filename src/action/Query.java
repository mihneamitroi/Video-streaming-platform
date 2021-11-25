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
    /**
     * Metoda selecteaza mai intai obiectul pe care va face interogarea. In cazul actorilor,
     * fie ca e vorba de note, premii sau descriere, se adauga actori intr-un array list,
     * acestia fiind filtrati inainte de a fi adaugati daca este cazul. Apoi se sorteaza
     * in functie de criteriul precizat si se afiseaza lista rezultata in urma sortarii si
     * a eliminarii surplusului de elemente. Asemanator procedez si in cazul userilor, filmelor
     * sau al serialelor.
     */
    public String findObjectType(final ActionInputData action, final ArrayList<User> users,
                               final ArrayList<Video> videos, final ArrayList<Actor> actors) {
        List<List<String>> filters = action.getFilters();
        switch (action.getObjectType()) {
            case "actors":
                switch (action.getCriteria()) {
                    case "average" -> {
                        /* Adaug doar actorii care au rating-ul calculat pe lista de filme diferit
                        * de 0. */
                        ArrayList<Actor> actorsRating = new ArrayList<>();
                        for (Actor actor : actors) {
                            if (actor.calculateRating(videos) != 0) {
                                actorsRating.add(actor);
                            }
                        }
                        /* Sortez crescator sau descrescator dupa rating, iar in caz de egalitate
                        * criteriul de departajare este numele. */
                        if (action.getSortType().equals("asc")) {
                            actorsRating.sort((actor1, actor2) ->  {
                                if (actor1.calculateRating(videos)
                                        == actor2.calculateRating(videos)) {
                                    return actor1.getName().compareTo(actor2.getName());
                                } else {
                                    return Double.compare(actor1.calculateRating(videos),
                                            actor2.calculateRating(videos));
                                }
                            });
                        } else {
                            actorsRating.sort((actor1, actor2) -> {
                                if (actor1.calculateRating(videos)
                                        == actor2.calculateRating(videos)) {
                                    return actor2.getName().compareTo(actor1.getName());
                                } else {
                                    return Double.compare(actor2.calculateRating(videos),
                                            actor1.calculateRating(videos));
                                }
                            });
                        }
                        while (actorsRating.size() > action.getNumber()) {
                            actorsRating.remove(actorsRating.size() - 1);
                        }
                        /* Elimin elementele in surplus si returnez array-ul. */
                        return "Query result: " + actorsRating;
                    }
                    /* */
                    case "awards" -> {
                        /* Adaug doar actorii care contin toate premiile precizate. */
                        ArrayList<Actor> filteredActors = new ArrayList<>();
                        for (Actor actor : actors) {
                            boolean ok = true;
                            final int x = 3;
                            for (String award : filters.get(x)) {
                                ActorsAwards awardType = Utils.stringToAwards(award);
                                if (!actor.getAwards().containsKey(awardType)) {
                                    ok = false;
                                    break;
                                }
                            }
                            if (ok) {
                                filteredActors.add(actor);
                            }
                        }
                        /* Sortez in functie de suma premiilor din hashmap-ul de premii si, la
                        * egalitate, in functie de nume. */
                        if (action.getSortType().equals("asc")) {
                            filteredActors.sort((actor1, actor2) -> {
                                int sum1 = 0, sum2 = 0;
                                for (Map.Entry<ActorsAwards, Integer> entry
                                        : actor1.getAwards().entrySet()) {
                                    sum1 += entry.getValue();
                                }
                                for (Map.Entry<ActorsAwards, Integer> entry
                                        : actor2.getAwards().entrySet()) {
                                    sum2 += entry.getValue();
                                }
                                if (sum1 == sum2) {
                                    return actor1.getName().compareTo(actor2.getName());
                                }
                                return sum1 - sum2;
                            });
                        } else {
                            filteredActors.sort((actor1, actor2) -> {
                                int sum1 = 0, sum2 = 0;
                                for (Map.Entry<ActorsAwards, Integer> entry
                                        : actor1.getAwards().entrySet()) {
                                    sum1 += entry.getValue();
                                }
                                for (Map.Entry<ActorsAwards, Integer> entry
                                        : actor2.getAwards().entrySet()) {
                                    sum2 += entry.getValue();
                                }
                                if (sum1 == sum2) {
                                    return actor2.getName().compareTo(actor1.getName());
                                }
                                return sum2 - sum1;
                            });
                        }
                        return "Query result: " + filteredActors;
                    }
                    case "filter_description" -> {
                        ArrayList<Actor> actorsDescription = new ArrayList<>();
                        for (Actor actor : actors) {
                            boolean ok = true;
                            for (String filter : filters.get(2)) {
                                String search = " " + filter.toLowerCase() + " ";
                                String careerDescription = actor.getCareerDescription();
                                careerDescription = careerDescription.replace(",", " ");
                                careerDescription = careerDescription.replace("\n", " ");
                                careerDescription = careerDescription.replace(".", " ");
                                careerDescription = careerDescription.replace("-", " ");
                                careerDescription = " " + careerDescription.toLowerCase() + " ";
                                if (!careerDescription.contains(search)) {
                                    ok = false;
                                    break;
                                }
                            }
                            if (ok) {
                                actorsDescription.add(actor);
                            }
                        }
                        /* Sortez actorii in functie de nume si returnez array-ul. */
                        if (action.getSortType().equals("asc")) {
                            actorsDescription.sort((actor1, actor2) ->
                                    actor1.getName().compareTo(actor2.getName()));
                        } else {
                            actorsDescription.sort((actor1, actor2) ->
                                    actor2.getName().compareTo(actor1.getName()));
                        }
                        return "Query result: " + actorsDescription;
                    }
                    default -> {
                        return "";
                    }
                }
            case "users":
                /* Adaug toti userii cu un numar de rating-uri date mai mare decat 0. */
                ArrayList<User> usersRating = new ArrayList<>();
                for (User user : users) {
                    if (user.numberRating() != 0) {
                        usersRating.add(user);
                    }
                }
                /* Sortez in functie de numarul de rating-uri si in functie de nume. */
                if (action.getSortType().equals("asc")) {
                    usersRating.sort((user1, user2) ->  {
                        if (user1.numberRating() == user2.numberRating()) {
                            return user1.getUsername().compareTo(user2.getUsername());
                        } else {
                            return user1.numberRating() - user2.numberRating();
                        }
                    });
                } else {
                    usersRating.sort((user1, user2) ->  {
                        if (user1.numberRating() == user2.numberRating()) {
                            return user2.getUsername().compareTo(user1.getUsername());
                        } else {
                            return user2.numberRating() - user1.numberRating();
                        }
                    });
                }
                /* Elimin userii in plus si returnez array-ul. */
                while (usersRating.size() > action.getNumber()) {
                    usersRating.remove(usersRating.size() - 1);
                }
                return "Query result: " + usersRating;
            case "shows":
                /* Adaug toate video-urile care sunt de tip Serial. */
                ArrayList<Serial> serials = new ArrayList<>();
                for (Video video : videos) {
                    if (video instanceof Serial) {
                        serials.add((Serial) video);
                    }
                }
                /* Adaug in alt array videoclipurile care indeplinesc conditiile cerute (anul si
                * genul, daca acestea exista). */
                ArrayList<Serial> filteredSerials = new ArrayList<>();
                for (Serial serial : serials) {
                    boolean ok = true;
                    if (filters.get(0).get(0) != null) {
                        if (serial.getYear() != Integer.parseInt(filters.get(0).get(0))) {
                            ok = false;
                        }
                    }
                    if (filters.get(1).get(0) != null) {
                        if (!serial.getGenres().contains(filters.get(1).get(0))) {
                            ok = false;
                        }
                    }
                    if (ok) {
                        filteredSerials.add(serial);
                    }
                }
                switch (action.getCriteria()) {
                    /* Sortez ascendent sau descendent in functie de criteriu (rating, numar
                    * de adaugari la favorite, durata sau vizualizari. Dupa acest prim criteriu,
                    * in caz de egalitate, sortez in functie de nume. */
                    case "ratings" -> {
                        /* Elimin videoclipurile cu rating 0. */
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
                                    return serial2.getTitle().compareTo(serial1.getTitle());
                                } else {
                                    return (int) (serial2.getRating() - serial1.getRating());
                                }
                            });
                        }
                    }
                    case "favorite" -> {
                        /* Elimin videoclipurile cu numar de favorite nul. */
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
                                    return serial2.getTitle().compareTo(serial1.getTitle());
                                } else {
                                    return serial2.getFavouriteCnt() - serial1.getFavouriteCnt();
                                }
                            });
                        }
                    }
                    case "longest" -> {
                        if (action.getSortType().equals("asc")) {
                            filteredSerials.sort((serial1, serial2) ->  {
                                if (serial1.getDuration() == serial2.getDuration()) {
                                    return serial1.getTitle().compareTo(serial2.getTitle());
                                } else {
                                    return serial1.getDuration() - serial2.getDuration();
                                }
                            });
                        } else {
                            filteredSerials.sort((serial1, serial2) -> {
                                if (serial1.getDuration() == serial2.getDuration()) {
                                    return serial2.getTitle().compareTo(serial1.getTitle());
                                } else {
                                    return serial2.getDuration() - serial1.getDuration();
                                }
                            });
                        }
                    }
                    case "most_viewed" -> {
                        /* Elimin videoclipurile cu numar de vizualizari nul. */
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
                                    return serial2.getTitle().compareTo(serial1.getTitle());
                                } else {
                                    return serial2.getViewCnt() - serial1.getViewCnt();
                                }
                            });
                        }
                    }
                    default -> {
                        return "";
                    }
                }
                /* Pastrez doar numarul de seriale parsat ca parametru in actiune si returnez. */
                while (filteredSerials.size() > action.getNumber()) {
                    filteredSerials.remove(filteredSerials.size() - 1);
                }
                return "Query result: " + filteredSerials;
            case "movies":
                /* Procedeu analog cu cel de la seriale. */
                ArrayList<Movie> movies = new ArrayList<>();
                ArrayList<Movie> filteredMovies = new ArrayList<>();
                for (Video video : videos) {
                    if (video instanceof Movie) {
                        movies.add((Movie) video);
                    }
                }
                for (Movie movie : movies) {
                    boolean ok = true;
                    if (filters.get(0).get(0) != null) {
                        if (movie.getYear() != Integer.parseInt(filters.get(0).get(0))) {
                            ok = false;
                        }
                    }
                    if (filters.get(1).get(0) != null) {
                        if (!movie.getGenres().contains(filters.get(1).get(0))) {
                            ok = false;
                        }
                    }
                    if (ok) {
                        filteredMovies.add(movie);
                    }
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
                                    return movie2.getTitle().compareTo(movie1.getTitle());
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
                                    return movie2.getTitle().compareTo(movie1.getTitle());
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
                                    return movie2.getTitle().compareTo(movie1.getTitle());
                                } else {
                                    return movie2.getDuration() - movie1.getDuration();
                                }
                            });
                        }
                    }
                    case "most_viewed" -> {
                        filteredMovies.removeIf(serial -> serial.getViewCnt() == 0);
                        if (action.getSortType().equals("asc")) {
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
                                    return movie2.getTitle().compareTo(movie1.getTitle());
                                } else {
                                    return movie2.getViewCnt() - movie1.getViewCnt();
                                }
                            });
                        }
                    }
                    default -> {
                        return "";
                    }
                }
                while (filteredMovies.size() > action.getNumber()) {
                    filteredMovies.remove(filteredMovies.size() - 1);
                }
                return "Query result: " + filteredMovies;
            default:
                return "";
        }
    }
}
