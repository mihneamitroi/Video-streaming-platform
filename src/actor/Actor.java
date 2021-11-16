package actor;

import entertainment.Video;
import fileio.ActorInputData;

import java.util.ArrayList;
import java.util.Map;

public class Actor {
    private String name;
    private String careerDescription;
    private ArrayList<String> filmography;
    private Map<ActorsAwards, Integer> awards;
    private double rating;
    private int ratingCnt = 0;

    public Actor(final String name, final String careerDescription,
                          final ArrayList<String> filmography,
                          final Map<ActorsAwards, Integer> awards) {
        this.name = name;
        this.careerDescription = careerDescription;
        this.filmography = filmography;
        this.awards = awards;
    }

    public Actor(final ActorInputData actor) {
        this.name = actor.getName();
        this.careerDescription = actor.getCareerDescription();
        this.filmography = actor.getFilmography();
        this.awards = actor.getAwards();
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public ArrayList<String> getFilmography() {
        return filmography;
    }

    public void setFilmography(final ArrayList<String> filmography) {
        this.filmography = filmography;
    }

    public Map<ActorsAwards, Integer> getAwards() {
        return awards;
    }

    public String getCareerDescription() {
        return careerDescription;
    }

    public void setCareerDescription(final String careerDescription) {
        this.careerDescription = careerDescription;
    }

    public double calculateRating(ArrayList<Video> videos) {
        for (String title : filmography) {
            for (Video video : videos) {
                if (video.getTitle().equals(title)) {
                    if (this.rating == 0) {
                        this.rating = video.getRating();
                        this.ratingCnt++;
                    } else {
                        this.rating = this.rating * (double) ratingCnt;
                        this.rating = this.rating + video.getRating();
                        this.ratingCnt++;
                        this.rating = this.rating / (double) ratingCnt;
                    }
                }
            }
        }
        return this.rating;
    }
}
