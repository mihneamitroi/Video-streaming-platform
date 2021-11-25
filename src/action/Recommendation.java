package action;

import entertainment.Video;
import fileio.ActionInputData;
import user.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Recommendation {
    /**
     * Metoda cauta si gaseste userul parsat in comanda in lista de useri data ca parametru si
     * apoi, in functie de comanda, intoarce un video.
     */
    public String findType(final ActionInputData action, final ArrayList<User> users,
                         final ArrayList<Video> videos) {
        ArrayList<String> genres = new ArrayList<>();
        Collections.addAll(genres, "Tv Movie", "Drama", "Fantasy", "Comedy", "Family", "War",
                "Sci-Fi & Fantasy", "Crime", "Animation", "Science Fiction", "Action", "Horror",
                "Mystery", "Western", "Adventure", "Action & Adventure", "Romance", "Thriller",
                "Kids", "History");

        User user = null;
        for (User curUser : users) {
            if (curUser.getUsername().equals(action.getUsername())) {
                user = curUser;
            }
        }
        switch (action.getType()) {
            case "standard" -> {
                /* Din lista de videoclipuri pasata ca parametru, intoarce primul videoclip care
                * nu este in lista de vizionate a utilizatorului si care are un numar de
                * vizualizari nenul. */
                for (Video video : videos) {
                    if (!user.getHistory().containsKey(video.getTitle())
                            && video.getViewCnt() != 0) {
                        return "StandardRecommendation result: " + video.getTitle();
                    }
                }
                return "StandardRecommendation cannot be applied!";
            }
            case "best_unseen" -> {
                /* Adaug intr-un array toate video-urile nevazute de user si apoi il sortez.
                * Intorc primul element. */
                ArrayList<Video> bestUnseen = new ArrayList<>();
                for (Video video : videos) {
                    if (!user.getHistory().containsKey(video.getTitle())) {
                        bestUnseen.add(video);
                    }
                }
                bestUnseen.sort((video1, video2) -> (int) (video2.getRating()
                        - video1.getRating()));
                if (bestUnseen.size() == 0) {
                    return "BestRatedUnseenRecommendation cannot be applied!";
                }
                return "BestRatedUnseenRecommendation result: " + bestUnseen.get(0).getTitle();
            }
            case "popular" -> {
                /* Dupa verificarea tipului de abonament, creez un hashmap in care adaug toate
                * genurile si pun numarul de vizualizari pe 0. */
                ArrayList<Video> popular = new ArrayList<>();
                Map<String, Integer> genreMap = new HashMap<>();
                if (user.getSubscriptionType().equals("PREMIUM")) {
                    for (String genre : genres) {
                        genreMap.put(genre, 0);
                    }
                    /*  Parcurg toata lista de video-uri si, cand un video contine genul precizat,
                    * adaug numarul de vizualizari al acestuia la valoarea genului din hashmap. */
                    for (Map.Entry<String, Integer> entry: genreMap.entrySet()) {
                        for (Video video : videos) {
                            for (String videoGenre : video.getGenres()) {
                                if (videoGenre.equals(entry.getKey())) {
                                    genreMap.put(entry.getKey(), entry.getValue()
                                            + video.getViewCnt());
                                }
                            }
                        }
                    }
                    /* Creez un array ce contine toate perechile de gen si numar de vizualizari pe
                    * care il sortez apoi in functie de vizualizari. */
                    ArrayList<Map.Entry<String, Integer>> genreList
                            = new ArrayList<>(genreMap.entrySet());
                    genreList.sort((genre1, genre2) -> genre2.getValue() - genre1.getValue());
                    /* Parcurg toate genurile, apoi adaug toate videoclipurile din genul curent,
                    * sortez array-ul si intorc primul video nevizualizat de user. */
                    for (Map.Entry<String, Integer> popularGenre : genreList) {
                        for (Video video : videos) {
                            if (video.getGenres().contains(popularGenre.getKey())) {
                                popular.add(video);
                            }
                        }
                        popular.sort((video1, video2) -> {
                            if (video1.getViewCnt() < video2.getViewCnt()) {
                                return 1;
                            }
                            return 0;
                        });
                        for (Video video : popular) {
                            if (!user.getHistory().containsKey(video.getTitle())) {
                                return "PopularRecommendation result: " + video.getTitle();
                            }
                        }
                    }
                }
                return "PopularRecommendation cannot be applied!";
            }
            case "favorite" -> {
                /* Verific daca userul e premium, apoi adaug toate videoclipurile intr-un array.
                * Sterg video-urile cu un numar nul de adaugari la favorite si sortez in functie
                * de numarul de adaugari la favorite. Intorc primul video nevizualizat de user. */
                ArrayList<Video> favorite = new ArrayList<>(videos);
                favorite.removeIf(video -> video.getFavouriteCnt() == 0);
                if (user.getSubscriptionType().equals("PREMIUM")) {
                    favorite.sort((video1, video2) -> {
                        return video2.getFavouriteCnt() - video1.getFavouriteCnt();
                    });
                    for (Video video : favorite) {
                        if (!user.getHistory().containsKey(video.getTitle())) {
                            return "FavoriteRecommendation result: " + video.getTitle();
                        }
                    }
                }
                return "FavoriteRecommendation cannot be applied!";
            }
            case "search" -> {
                /* Daca userul e premium, adaug toate video-urile care apartin genului precizat
                si sunt nevazute de user intr-un array. Sortez video-urile dupa rating la egalitate
                dupa nume) si apoi returnez lista. */
                ArrayList<Video> search = new ArrayList<>();
                if (user.getSubscriptionType().equals("PREMIUM")) {
                    for (Video video : videos) {
                        if (video.getGenres().contains(action.getGenre())
                                && !user.getHistory().containsKey(video.getTitle())) {
                            search.add(video);
                        }
                    }
                    search.sort((video1, video2) -> {
                        if (video1.getRating() == video2.getRating()) {
                            return video1.getTitle().compareTo(video2.getTitle());
                        } else {
                            return Double.compare(video1.getRating(), video2.getRating());
                        }
                    });
                    if (search.size() == 0) {
                        return "SearchRecommendation cannot be applied!";
                    }
                    return "SearchRecommendation result: " + search;
                }
                return "SearchRecommendation cannot be applied!";
            }
            default -> {
                return "";
            }
        }
    }
}
