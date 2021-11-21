package action;

import entertainment.Video;
import fileio.ActionInputData;
import user.User;

import java.util.ArrayList;

public class Recommendation {
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

        switch (action.getType()) {
            case "standard":
                for (User user : users) {
                    if (user.getUsername().equals(action.getUsername())) {
                        for (Video video : videos) {
                            if (!user.getHistory().containsKey(video.getTitle())) {
                                return "StandardRecommendation result: " + video.getTitle();
                            }
                        }
                    }
                }
                return "StandardRecommendation cannot be applied!";
            case "best_unseen":
                ArrayList<Video> best_unseen = new ArrayList<>();
                for (User user : users)
                    if (user.getUsername().equals(action.getUsername())) {
                        for (Video video : videos) {
                            if (!user.getHistory().containsKey(video.getTitle())) {
                                best_unseen.add(video);
                            }
                        }
                        best_unseen.sort((video1, video2) -> {
                            if (video1.getRating() < video2.getRating())
                                return 1;
                            return 0;
                        });
                        if (best_unseen.size() == 0)
                            return "BestRatedUnseenRecommendation cannot be applied!";
                        return "BestRatedUnseenRecommendation result: " + best_unseen.get(0).getTitle();
                    }
            case "popular":
                ArrayList<Video> popular = new ArrayList<>();
                for (User user : users)
                    if (user.getUsername().equals(action.getUsername())) {
                        if (user.getSubscriptionType().equals("PREMIUM")) {
                            int maxViews = 0;
                            String maxViewsGenre = null;
                            for (String genre : genres) {
                                int views = 0;
                                for (Video video : videos) {
                                    for (String videoGenre : video.getGenres()) {
                                        if (videoGenre.equals(genre))
                                            views += video.getViewCnt();
                                    }
                                    if (views > maxViews) {
                                        maxViews = views;
                                        maxViewsGenre = genre;
                                    }
                                }
                            }
                            for (Video video : videos) {
                                for (String genre : video.getGenres()) {
                                    if (genre.equals(maxViewsGenre))
                                        popular.add(video);
                                }
                            }
                            popular.sort((video1, video2) -> {
                                if (video1.getViewCnt() < video2.getViewCnt())
                                    return 1;
                                return 0;
                            });
                            while(true) {
                                for (Video video : videos) {
                                    if (!user.getHistory().containsKey(video.getTitle()))
                                        return "PopularRecommendation result: " + video.getTitle();
                                }
                                break;
                            }
                        }
                        return "PopularRecommendation cannot be applied!";
                    }
                return "PopularRecommendation cannot be applied!";
            case "favorite":
                ArrayList<Video> favorite = new ArrayList<>(videos);
                for (User user : users)
                    if (user.getUsername().equals(action.getUsername())) {
                        if (user.getSubscriptionType().equals("PREMIUM")) {
                            favorite.sort((video1, video2) -> {
                                if (video1.getFavouriteCnt() < video2.getFavouriteCnt())
                                    return 1;
                                return 0;
                            });
                            for (Video video : favorite) {
                                if (!user.getHistory().containsKey(video.getTitle()))
                                    return "FavoriteRecommendation result: " + video.getTitle();
                            }
                        }
                        return "FavoriteRecommendation cannot be applied!";
                    }
                return "FavoriteRecommendation cannot be applied!";
            case "search":
                ArrayList<Video> search = new ArrayList<>();
                for (User user : users)
                    if (user.getUsername().equals(action.getUsername())) {
                        if (user.getSubscriptionType().equals("PREMIUM")) {
                            for (Video video : videos) {
                                for (String videoGenre : video.getGenres()) {
                                    if (videoGenre.equals(action.getGenre()))
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
                            if (search.size() == 0)
                                return "SearchRecommendation cannot be applied!";
                            return "SearchRecommendation result: " + search;
                        }
                        return "SearchRecommendation cannot be applied!";
                    }
                return "SearchRecommendation cannot be applied!";
        }
        return "";
    }
}
