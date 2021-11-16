package user;

import fileio.UserInputData;

import java.util.ArrayList;
import java.util.Map;

public class User {
    private final String username;
    private final String subscriptionType;
    private final Map<String, Integer> history;
    private final ArrayList<String> favoriteMovies;
    private int ratingCnt;

    public User(final String username, final String subscriptionType,
                         final Map<String, Integer> history,
                         final ArrayList<String> favoriteMovies) {
        this.username = username;
        this.subscriptionType = subscriptionType;
        this.history = history;
        this.favoriteMovies = favoriteMovies;
        this.ratingCnt = 0;
    }

    public User(final UserInputData user) {
        this.username = getUsername();
        this.subscriptionType = user.getSubscriptionType();
        this.history = user.getHistory();
        this.favoriteMovies = user.getFavoriteMovies();
        this.ratingCnt = 0;
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

    public void addFavorite(String title) {
        this.favoriteMovies.add(title);
    }

    public void addView(String title) {
        if (this.history.containsKey(title))
            this.history.put(title, this.history.get(title) + 1);
        else
            this.history.put(title, 1);
    }

    public void addRating() {
        this.ratingCnt++;
    }
}
