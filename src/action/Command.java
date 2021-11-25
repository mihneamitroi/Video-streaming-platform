package action;

import entertainment.Video;
import fileio.ActionInputData;
import user.User;

import java.util.ArrayList;
import java.util.Locale;

public class Command {
    /**
     * Metoda cauta si gaseste userul parsat in comanda in lista de useri data ca parametru.
     * Apoi in functie de caz, apeleaza cate o metoda pentru user si video, prima adaugand
     * in lista de filme favorite sau vizionate a userului video-ul precizat sau nota acestuia
     * in hashmap. In functie de raspunsul oferit (video-ul e deja adaugat, trebuie doar
     * actualizat), video-ului i se incrementeaza contorul de vizualizari sau adaugari la favorite
     * sau i se adauga o noua nota.
     */
    public String findType(final ActionInputData action, final ArrayList<User> users,
                           final ArrayList<Video> videos) {
        int result;
        User user = null;
        for (User curUser : users) {
            if (curUser.getUsername().equals(action.getUsername())) {
                user = curUser;
            }
        }
        switch (action.getType()) {
            case "favorite" -> {
                result = user.addFavorite(action.getTitle());
                if (result == 2) {
                   for (Video video : videos) {
                       if (video.getTitle().equals(action.getTitle())) {
                           video.addFavorite();
                           break;
                       }
                   }
                   return "success -> " + action.getTitle() + " was added as favourite";
                } else if (result == 1) {
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
                result = user.addRating(action.getTitle(), action.getSeasonNumber(),
                        action.getGrade());
                if (result == 2) {
                    return "error -> " + action.getTitle() + " is not seen";
                } else if (result == 1) {
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
