package entertainment;

import fileio.SerialInputData;

import java.util.ArrayList;

public class Serial extends Video {
    private final int numberOfSeasons;
    private final ArrayList<Season> seasons;

    public Serial(Serial serial) {
        super(serial.getTitle(), serial.getYear(), serial.getCast(), serial.getGenres(), 0);
        int x = 0;
        for (Season season : serial.getSeasons()) {
            x =+ season.getDuration();
        }
        this.setDuration(x);
        this.numberOfSeasons = serial.getNumberSeason();
        this.seasons = serial.getSeasons();
    }

    public Serial(final SerialInputData serial) {
        super(serial.getTitle(), serial.getYear(), serial.getCast(), serial.getGenres(), 0);
        int x = 0;
        for (Season season : serial.getSeasons()) {
            x =+ season.getDuration();
        }
        this.setDuration(x);
        this.numberOfSeasons = serial.getNumberSeason();
        this.seasons = serial.getSeasons();
    }

    public int getNumberSeason() {
        return numberOfSeasons;
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
    }

    @Override
    public void addRating(double rating, int seasonNumber) {
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

    @Override
    public String toString() {
        return super.toString();
    }
}
