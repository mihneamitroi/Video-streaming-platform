package action;

import entertainment.Video;
import fileio.ActionInputData;
import user.User;

import java.util.ArrayList;
import java.util.Locale;

public class Command {
    public String findType(final ActionInputData action, ArrayList<User> users,
                           ArrayList<Video> videos) {
        String result = "";
        int output;
        switch (action.getType()) {
            case "favorite" -> {
                for (User user : users) {
                    if (user.getUsername().equals(action.getUsername())) {
                        output = user.addFavorite(action.getTitle());
                        if (output == 2) {
                           result = "success -> " + action.getTitle() + " was added as favourite";
                           for (Video video : videos) {
                               if (video.getTitle().equals(action.getTitle())) {
                                   video.addFavorite();
                                   break;
                               }
                           }
                        } else if (output == 1) {
                            result = "error -> " + action.getTitle() + " is not seen";
                        } else {
                            result = "error -> " + action.getTitle() + " is already in favourite list";
                        }
                    }
                }
                return result;
            }
            case "view" -> {
                for (User user : users) {
                    if (user.getUsername().equals(action.getUsername())) {
                        user.addView(action.getTitle());
                        int views = user.getHistory().get(action.getTitle());
                        result =  "success -> " + action.getTitle() + " was viewed with total views of " + views;
                    }
                }
                for (Video video : videos) {
                    if (video.getTitle().equals(action.getTitle())) {
                        video.addView();
                        break;
                    }
                }
                return result;
            }
            case "rating" -> {
                for (User user : users) {
                    if (user.getUsername().equals(action.getUsername())) {
                        output = user.addRating(action.getTitle(), action.getSeasonNumber(), action.getGrade());
                        if (output == 2) {
                            result = "error -> " + action.getTitle() + " is not seen";
                        } else if (output == 1) {
                            result = "success -> " + action.getTitle() + " was rated with "
                                    + String.format(Locale.US, "%.1f", action.getGrade()) + " by " + action.getUsername();
                            for (Video video : videos) {
                                if (video.getTitle().equals(action.getTitle())) {
                                    video.addRating(action.getGrade(), action.getSeasonNumber());
                                    break;
                                }
                            }
                        } else {
                            result = "error -> " + action.getTitle() + " has been already rated";
                        }
                        break;
                    }
                }
                return result;
            }
        }
        return result;
    }
}
