package entertainment;

import fileio.MovieInputData;

import java.util.ArrayList;

public class Movie extends Video {
    private final ArrayList<Double> ratings = new ArrayList<>();

    public Movie(final MovieInputData movie) {
        super(movie.getTitle(), movie.getYear(), movie.getCast(),
                movie.getGenres(), movie.getDuration());
    }

    /**
     * Functia intoarce durata filmului.
     */
    public int getDuration() {
        return super.getDuration();
    }

    /**
     * Functia adauga ratingul in lista de ratinguri si apoi calculeaza noul rating. Apoi il
     * seteaza.
     */
    @Override
    public void addRating(final double rating, final int seasonNumber) {
        this.ratings.add(rating);
        double sum = 0;
        for (Double curRating : this.ratings) {
            sum += curRating;
        }
        super.setRating(sum / (double) this.ratings.size());
    }

    /**
     * Functia intoarce lista de ratinguri.
     */
    public ArrayList<Double> getRatings() {
        return ratings;
    }

    /**
     * Override pe metoda toString.
     */
    @Override
    public String toString() {
        return super.toString();
    }
}
