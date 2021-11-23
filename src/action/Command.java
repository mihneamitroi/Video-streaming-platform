package action;

import entertainment.Video;
import fileio.ActionInputData;
import user.User;

import java.util.ArrayList;
import java.util.Locale;

public class Command {
    /**
     *
     */
    public String findType(final ActionInputData action, final ArrayList<User> users,
                           final ArrayList<Video> videos) {
        int output;
        User user = null;
        boolean ok = false;
        for (User curUser : users) {
            if (curUser.getUsername().equals(action.getUsername())) {
                user = curUser;
                ok = true;
            }
        }
        if (!ok) {
            return "";
        }
        switch (action.getType()) {
            case "favorite" -> {
                output = user.addFavorite(action.getTitle());
                if (output == 2) {
                   for (Video video : videos) {
                       if (video.getTitle().equals(action.getTitle())) {
                           video.addFavorite();
                           break;
                       }
                   }
                   return "success -> " + action.getTitle() + " was added as favourite";
                } else if (output == 1) {
                    return "error -> " + action.getTitle() + " is not seen";
                }
                return "error -> " + action.getTitle() + " is already in favourite list";
            }
            case "view" -> {
                for (Video video : videos) {
                    if (video.getTitle().equals(action.getTitle())) {
                        video.addView();
                        break;
                    }
                }
                user.addView(action.getTitle());
                int views = user.getHistory().get(action.getTitle());
                return "success -> " + action.getTitle() + " was viewed with total views of "
                        + views;
            }
            case "rating" -> {
                output = user.addRating(action.getTitle(), action.getSeasonNumber(),
                        action.getGrade());
                if (output == 2) {
                    return "error -> " + action.getTitle() + " is not seen";
                } else if (output == 1) {
                    for (Video video : videos) {
                        if (video.getTitle().equals(action.getTitle())) {
                            video.addRating(action.getGrade(), action.getSeasonNumber());
                            break;
                        }
                    }
                    return "success -> " + action.getTitle() + " was rated with "
                            + String.format(Locale.US, "%.1f", action.getGrade())
                            + " by " + action.getUsername();
                }
                return "error -> " + action.getTitle() + " has been already rated";
            }
            default -> {
                return "";
            }
        }
    }
}
