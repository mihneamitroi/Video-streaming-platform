package user;

import fileio.UserInputData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    private final String username;
    private final String subscriptionType;
    private final Map<String, Integer> history;
    private final ArrayList<String> favoriteMovies;
    private final Map<String, Double> rating = new HashMap<>();

    public User(final String username, final String subscriptionType,
                         final Map<String, Integer> history,
                         final ArrayList<String> favoriteMovies) {
        this.username = username;
        this.subscriptionType = subscriptionType;
        this.history = history;
        this.favoriteMovies = favoriteMovies;
    }

    public User(final UserInputData user) {
        this.username = user.getUsername();
        this.subscriptionType = user.getSubscriptionType();
        this.history = user.getHistory();
        this.favoriteMovies = user.getFavoriteMovies();
    }

    public String getUsername() {
        return username;
    }

    public Map<String, Integer> getHistory() {
        return history;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public ArrayList<String> getFavoriteMovies() {
        return favoriteMovies;
    }

    public int addFavorite(String title) {
        if (this.favoriteMovies.contains(title)) {
            return 0;
        } else if (!this.history.containsKey(title)) {
            return 1;
        } else {
            this.favoriteMovies.add(title);
            return 2;
        }
    }

    public void addView(String title) {
        if (this.history.containsKey(title))
            this.history.put(title, this.history.get(title) + 1);
        else
            this.history.put(title, 1);
    }

    public int addRating(String title, int season, double grade) {
        String ratingTitle = title + season;
        if (this.rating.containsKey(ratingTitle)) {
            return 0;
        }
        if (this.history.containsKey(title)) {
            this.rating.put(ratingTitle, grade);
            return 1;
        }
        return 2;
    }

    public int numberRating() {
        return this.rating.size();
    }

    @Override
    public String toString() {
        return this.username;
    }
}
