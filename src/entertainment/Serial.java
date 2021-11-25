package entertainment;

import fileio.SerialInputData;

import java.util.ArrayList;

public class Serial extends Video {
    private final int numberOfSeasons;
    private final ArrayList<Season> seasons;

    public Serial(final SerialInputData serial) {
        super(serial.getTitle(), serial.getYear(), serial.getCast(), serial.getGenres(), 0);
        int x = 0;
        for (Season season : serial.getSeasons()) {
            x += season.getDuration();
        }
        this.setDuration(x);
        this.numberOfSeasons = serial.getNumberSeason();
        this.seasons = serial.getSeasons();
    }

    /**
     * Metoda intoarce numarul de sezoane al serialului.
     */
    public int getNumberSeason() {
        return this.numberOfSeasons;
    }

    /**
     * Metoda intoarce lista de sezoane a serialului.
     */
    public ArrayList<Season> getSeasons() {
        return this.seasons;
    }

    /**
     * Metoda adauga rating in lista de ratinguri a sezonului, apoi recalculeaza ratingul
     * acestuia si al intregului serial. Apoi il seteaza.
     */
    @Override
    public void addRating(final double rating, final int seasonNumber) {
        this.seasons.get(seasonNumber - 1).getRatings().add(rating);
        double serialRating = 0;
        for (Season season : this.seasons) {
            double seasonRating = 0;
            for (double curRating : season.getRatings()) {
                seasonRating += curRating;
            }
            if (season.getRatings().size() != 0) {
                seasonRating /= season.getRatings().size();
                serialRating += seasonRating;
            }
        }
        super.setRating(serialRating / this.seasons.size());
    }

    /**
     * Override pe metoda toString.
     */
    @Override
    public String toString() {
        return super.toString();
    }
}
