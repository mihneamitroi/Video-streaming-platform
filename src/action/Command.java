package action;

import entertainment.Video;
import fileio.ActionInputData;
import user.User;
import entertainment.Movie;
import entertainment.Serial;

import java.util.ArrayList;
import java.util.Locale;

public class Command {
    public String findType(final ActionInputData action, final ArrayList<User> users,
                           final ArrayList<Video> videos, final ArrayList<Movie> movies,
                           final ArrayList<Serial> serials) {
        String result = null;
        int output;
        String username = action.getUsername();
        String title = action.getTitle();
        switch (action.getType()) {
            case "favorite" -> {
                for (User user : users) {
                    if (user.getUsername().equals(username)) {
                        output = user.addFavorite(title);
                        if (output == 2) {
                           result = "success -> " + title + " was added as favourite";
                           for (Video video : videos) {
                               if (video.getTitle().equals(title)) {
                                   video.addFavorite();
                                   break;
                               }
                           }
                        } else if (output == 1) {
                            result = "error -> " + title + " is not seen";
                        } else {
                            result = "error -> " + title + " is already in favourite list";
                        }
                    }
                }
                return result;
            }
            case "view" -> {
                for (User user : users) {
                    if (user.getUsername().equals(username)) {
                        user.addView(title);
                        int views = user.getHistory().get(title);
                        result =  "success -> " + title + " was viewed with total views of " + views;
                    }
                }
                for (Video video : videos) {
                    if (video.getTitle().equals(title)) {
                        video.addView();
                        break;
                    }
                }
                return result;
            }
            case "rating" -> {
                double grade = action.getGrade();
                int season = action.getSeasonNumber();
                for (User user : users) {
                    if (user.getUsername().equals(username)) {
                        output = user.addRating(title, season, grade);
                        if (output == 2) {
                            result = "error -> " + title + " is not seen";
                        } else if (output == 1) {
                            result = "success -> " + title + " was rated with "
                                    + String.format(Locale.US, "%.1f", grade) + " by " + username;
                            if (season == 0) {
                                for (Movie movie : movies) {
                                    if (movie.getTitle().equals(title)) {
                                        movie.addRating(grade);
                                        break;
                                    }
                                }
                            } else {
                                for (Serial serial : serials) {
                                    if (serial.getTitle().equals(title)) {
                                        serial.addRating(grade, action.getSeasonNumber());
                                        break;
                                    }
                                }
                            }
                        } else {
                            result = "error -> " + title + " has been already rated";
                        }
                        break;
                    }
                }
                return result;
            }
        }
        return "";
    }
}
