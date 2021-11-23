package actor;

import entertainment.Video;
import fileio.ActorInputData;

import java.util.ArrayList;
import java.util.Map;

public class Actor {
    private String name;
    private final String careerDescription;
    private ArrayList<String> filmography;
    private final Map<ActorsAwards, Integer> awards;

    public Actor(final ActorInputData actor) {
        this.name = actor.getName();
        this.careerDescription = actor.getCareerDescription();
        this.filmography = actor.getFilmography();
        this.awards = actor.getAwards();
    }

    /**
     *
     */
    public String getName() {
        return name;
    }

    /**
     *
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     *
     */
    public ArrayList<String> getFilmography() {
        return this.filmography;
    }

    /**
     *
     */
    public void setFilmography(final ArrayList<String> filmography) {
        this.filmography = filmography;
    }

    /**
     *
     */
    public Map<ActorsAwards, Integer> getAwards() {
        return this.awards;
    }

    /**
     *
     */
    public String getCareerDescription() {
        return this.careerDescription;
    }

    /**
     *
     */
    public double calculateRating(final ArrayList<Video> videos) {
        double rating = 0;
        int ratingCnt = 0;
        for (String title : this.filmography) {
            for (Video video : videos) {
                if (video.getTitle().equals(title)) {
                    if (video.getRating() != 0) {
                        rating += video.getRating();
                        ratingCnt++;
                    }
                }
            }
        }
        if (rating != 0) {
            rating = rating / ratingCnt;
        }
        return rating;
    }

    /**
     *
     */
    @Override
    public String toString() {
        return this.name;
    }
}
