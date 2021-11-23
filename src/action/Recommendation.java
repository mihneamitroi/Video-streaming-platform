package action;

import entertainment.Video;
import fileio.ActionInputData;
import user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Recommendation {
    /**
     *
     */
    public String findType(final ActionInputData action, final ArrayList<User> users,
                         final ArrayList<Video> videos) {
        ArrayList<String> genres = new ArrayList<>();
        genres.add("Action");
        genres.add("Adventure");
        genres.add("Drama");
        genres.add("Comedy");
        genres.add("Crime");
        genres.add("Romance");
        genres.add("War");
        genres.add("History");
        genres.add("Thriller");
        genres.add("Mystery");
        genres.add("Family");
        genres.add("Horror");
        genres.add("Fantasy");
        genres.add("Science Fiction");
        genres.add("Action & Adventure");
        genres.add("Sci-Fi & Fantasy");
        genres.add("Animation");
        genres.add("Kids");
        genres.add("Western");
        genres.add("Tv Movie");

        User user = null;
        boolean ok = false;
        for (User curUser : users) {
            if (curUser.getUsername().equals(action.getUsername())) {
                user = curUser;
                ok = true;
            }
        }
        if (!ok) {
            return "";
        }
        switch (action.getType()) {
            case "standard" -> {
                for (Video video : videos) {
                    if (!user.getHistory().containsKey(video.getTitle())
                            && video.getViewCnt() != 0) {
                        return "StandardRecommendation result: " + video.getTitle();
                    }
                }
                return "StandardRecommendation cannot be applied!";
            }
            case "best_unseen" -> {
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
                ArrayList<Video> popular = new ArrayList<>();
                Map<String, Integer> genreMap = new HashMap<>();
                if (user.getSubscriptionType().equals("PREMIUM")) {
                    for (String genre : genres) {
                        genreMap.put(genre, 0);
                    }
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
                    ArrayList<Map.Entry<String, Integer>> genreList
                            = new ArrayList<>(genreMap.entrySet());
                    genreList.sort((genre1, genre2) -> genre2.getValue() - genre1.getValue());
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
