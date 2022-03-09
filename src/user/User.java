package user;

import entertainment.Video;
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

    public User(final UserInputData user, final ArrayList<Video> videos) {
        this.username = user.getUsername();
        this.subscriptionType = user.getSubscriptionType();
        this.history = user.getHistory();
        this.favoriteMovies = user.getFavoriteMovies();
        for (String movie : this.favoriteMovies) {
            for (Video video : videos) {
                if (video.getTitle().equals(movie)) {
                    video.addFavorite();
                }
            }
        }
        for (Map.Entry<String, Integer> entry : this.history.entrySet()) {
            for (Video video : videos) {
                if (video.getTitle().equals(entry.getKey())) {
                    video.setViewCnt(video.getViewCnt() + entry.getValue());
                }
            }
        }
    }

    /**
     * Metoda intoarce numele userului.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Metoda intoarce hashmap-ul ce contine filmele vizionate de user.
     */
    public Map<String, Integer> getHistory() {
        return this.history;
    }

    /**
     * Metoda intoarce tipul de abonament al userului.
     */
    public String getSubscriptionType() {
        return this.subscriptionType;
    }

    /**
     * Metoda verifica daca un film se afla in istoricul de favorite, in caz ca nu verifica daca
     * videoclipul a fost vizionat. In functie de situatie, intoarce un cod folosit in clasa
     * Command.
     */
    public int addFavorite(final String title) {
        if (this.favoriteMovies.contains(title)) {
            return 0;
        } else if (!this.history.containsKey(title)) {
            return 1;
        } else {
            this.favoriteMovies.add(title);
            return 2;
        }
    }

    /**
     * Metoda verifica daca un video a fost vazut deja de un user, iar in caz ca da, incrementeaza
     * numarul de vizualizari al acestuia. Altfel, il adauga in hashmap. In functie de situatie,
     * intoarce un cod folosit in clasa Command.
     */
    public void addView(final String title) {
        if (this.history.containsKey(title)) {
            this.history.put(title, this.history.get(title) + 1);
        } else {
            this.history.put(title, 1);
        }
    }

    /**
     * Metoda verifica daca filmul a fost deja notat de user. Daca nu, verifica daca l-a vizionat
     * si ii acorda o nota. In functie de situatie, intoarce un cod folosit in clasa Command.
     */
    public int addRating(final String title, final int season, final double grade) {
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

    /**
     * Metoda intoarce numarul de ratinguri pe care le-a acordat userul.
     */
    public int numberRating() {
        return this.rating.size();
    }

    /**
     * Override pe metoda toString.
     */
    @Override
    public String toString() {
        return this.username;
    }
}
