package entertainment;

import java.util.ArrayList;

public abstract class Video {
    private final String title;
    private final int year;
    private final ArrayList<String> cast;
    private final ArrayList<String> genres;
    private int favouriteCnt = 0;
    private int viewCnt = 0;
    private int duration;
    private double rating = 0;

    public Video(final String title, final int year,
                 final ArrayList<String> cast, final ArrayList<String> genres,
                 final int duration) {
        this.title = title;
        this.year = year;
        this.cast = cast;
        this.genres = genres;
        this.duration = duration;
    }

    public final String getTitle() {
        return this.title;
    }

    public final int getYear() {
        return this.year;
    }

    public final ArrayList<String> getCast() {
        return this.cast;
    }

    public final ArrayList<String> getGenres() {
        return this.genres;
    }

    /**
     * Metoda incrementeaza contorul de adaugari la favorite al video-ului.
     */
    public void addFavorite() {
        this.favouriteCnt++;
    }

    /**
     * Metoda intoarce contorul de adaugari la favorite al video-ului.
     */
    public int getFavouriteCnt() {
        return this.favouriteCnt;
    }

    /**
     * Metoda intoarce numarul de vizualizari al video-ului.
     */
    public int getViewCnt() {
        return this.viewCnt;
    }

    /**
     * Metoda incrementeaza contorul de vizualizari al video-ului.
     */
    public void addView() {
        this.viewCnt++;
    }

    /**
     * Metoda seteaza numarul de vizualizari al video-ului.
     */
    public void setViewCnt(final int viewCnt) {
        this.viewCnt = viewCnt;
    }

    /**
     * Metoda pe care se face override in Movie si Serial.
     */
    public void addRating(final double ratingParameter, final int seasonNumber) {
    }

    /**
     * Metoda intoarce ratingul video-ului.
     */
    public double getRating() {
        return this.rating;
    }

    /**
     * Metoda seteaza ratingul video-ului.
     */
    public void setRating(final double rating) {
        this.rating = rating;
    }

    /**
     * Metoda intoarce durata video-ului.
     */
    public int getDuration() {
        return this.duration;
    }

    /**
     * Metoda seteaza durata video-ului.
     */
    public void setDuration(final int duration) {
        this.duration = duration;
    }

    /**
     * Override pe metoda toString.
     */
    @Override
    public String toString() {
        return this.title;
    }
}
