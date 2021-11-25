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
     * Metoda intoarce numele actorului.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Metoda seteaza numele actorului.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Metoda intoarce lista de filme in care a jucat actorul.
     */
    public ArrayList<String> getFilmography() {
        return this.filmography;
    }

    /**
     * Metoda seteaza lista de filme in care a jucat actorul.
     */
    public void setFilmography(final ArrayList<String> filmography) {
        this.filmography = filmography;
    }

    /**
     * Metoda intoarce hashmap-ul in care sunt stocate premiile actorului.
     */
    public Map<ActorsAwards, Integer> getAwards() {
        return this.awards;
    }

    /**
     * Functia intoarce descrierea carierei actorului.
     */
    public String getCareerDescription() {
        return this.careerDescription;
    }

    /**
     * Functia calculeaza rating-ul actorului bazat pe lista de video-uri data ca parametru.
     * Se iau in calcul toate video-urile cu rating diferit de 0.
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
     * Override pe metoda toString.
     */
    @Override
    public String toString() {
        return this.name;
    }
}
