package entertainment;

import fileio.MovieInputData;

import java.util.ArrayList;

public class Movie  extends Video {
    private int duration;
    private ArrayList<Double> ratings;

    public Movie(final String title, final ArrayList<String> cast,
                          final ArrayList<String> genres, final int year,
                          final int duration) {
        super(title, year, cast, genres);
        this.duration = duration;
    }

    public Movie(final MovieInputData movie) {
        super(movie.getTitle(), movie.getYear(), movie.getCast(), movie.getGenres());
        this.duration = movie.getDuration();
    }

    public int getDuration() {
        return duration;
    }

    public void addRating(double rating) {
        this.ratings.add(rating);
    }

    public double getRating() {
        double sum = 0;
        for (Double rating : ratings)
            sum += rating;
        return sum / (double) ratings.size();
    }
}
