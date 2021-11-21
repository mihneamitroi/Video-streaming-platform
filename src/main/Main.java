package main;

import action.Command;
import action.Query;
import action.Recommendation;
import actor.Actor;
import checker.Checkstyle;
import checker.Checker;
import common.Constants;
import entertainment.Video;
import fileio.*;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import user.User;
import entertainment.Movie;
import entertainment.Serial;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The entry point to this homework. It runs the checker that tests your implementation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * Call the main checker and the coding style checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(Constants.TESTS_PATH);
        Path path = Paths.get(Constants.RESULT_PATH);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        File outputDirectory = new File(Constants.RESULT_PATH);

        Checker checker = new Checker();
        checker.deleteFiles(outputDirectory.listFiles());

        for (File file : Objects.requireNonNull(directory.listFiles())) {

            String filepath = Constants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getAbsolutePath(), filepath);
            }
        }

        checker.iterateFiles(Constants.RESULT_PATH, Constants.REF_PATH, Constants.TESTS_PATH);
        Checkstyle test = new Checkstyle();
        test.testCheckstyle();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        InputLoader inputLoader = new InputLoader(filePath1);
        Input input = inputLoader.readData();

        Writer fileWriter = new Writer(filePath2);
        JSONArray arrayResult = new JSONArray();

        //TODO add here the entry point to your implementation
        Command command = new Command();
        Query query = new Query();
        Recommendation recommendation = new Recommendation();
        ArrayList<Video> videos = new ArrayList<>();

        for (MovieInputData currMovie : input.getMovies()) {
            Movie movie = new Movie(currMovie);
            videos.add(movie);
        }

        for (SerialInputData currSerial : input.getSerials()) {
            Serial serial = new Serial(currSerial);
            videos.add(serial);
        }

        ArrayList<Actor> actors = new ArrayList<>();
        for (ActorInputData currActor : input.getActors()) {
            Actor actor = new Actor(currActor);
            actors.add(actor);
        }

        ArrayList<User> users = new ArrayList<>();
        for (UserInputData currUser : input.getUsers()) {
            User user = new User(currUser);
            users.add(user);
        }

        List<ActionInputData> actions = input.getCommands();
        for (ActionInputData action : actions) {
            String result;
            if (action.getActionType().equals("command")) {
                result = command.findType(action, users, videos);
            } else if (action.getActionType().equals("query")) {
                result = query.findObjectType(action, users, videos, actors);
            } else {
                result = recommendation.findType(action, users, videos);
            }
            JSONObject object = new JSONObject();
            object.put("id", action.getActionId());
            object.put("message", result);

            arrayResult.add(object);
        }

        fileWriter.closeJSON(arrayResult);
    }
}
