package entertainment;

import java.util.ArrayList;

public class Video {
    private String title;
    private int year;
    private ArrayList<String> cast;
    private ArrayList<String> genres;
    private int favouriteCnt = 0;
    private int viewCnt = 0;

    public Video(final String title, final int year,
                 final ArrayList<String> cast, final ArrayList<String> genres) {
        this.title = title;
        this.year = year;
        this.cast = cast;
        this.genres = genres;
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

    public void addView() {
        this.viewCnt++;
    }

    public double getRating() {
        return 0;
    }
}
