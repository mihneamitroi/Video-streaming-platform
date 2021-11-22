package entertainment;

import java.util.ArrayList;

abstract public class Video {
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
        return title;
    }

    public final int getYear() {
        return year;
    }

    public final ArrayList<String> getCast() {
        return cast;
    }

    public final ArrayList<String> getGenres() {
        return genres;
    }

    public void addFavorite() {
        this.favouriteCnt++;
    }

    public int getFavouriteCnt() {
        return this.favouriteCnt;
    }

    public int getViewCnt() {
        return this.viewCnt;
    }

    public void addView() {
        this.viewCnt++;
    }

    public void setViewCnt(int viewCnt) {
        this.viewCnt = viewCnt;
    }

    public void addRating(double rating, int seasonNumber) {}

    public double getRating() {
        return this.rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return this.title;
    }
}
