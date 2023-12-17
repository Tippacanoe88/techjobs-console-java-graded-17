import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import resources.job_data.csv;

/**
 * Created by LaunchCode
 */
public class TechJobs {

    static Scanner in = new Scanner(System.in);

    public static void main(String[] args) {

        // Initialize our field map with key/name pairs
        HashMap<String, String> columnChoices = new HashMap<>();
        columnChoices.put("core competency", "Skill");
        columnChoices.put("employer", "Employer");
        columnChoices.put("location", "Location");
        columnChoices.put("position type", "Position Type");
        columnChoices.put("all", "All");

        // Top-level menu options
        HashMap<String, String> actionChoices = new HashMap<>();
        actionChoices.put("search", "Search");
        actionChoices.put("list", "List");

        System.out.println("Welcome to LaunchCode's TechJobs App!");

        // Allow the user to search until they manually quit
        while (true) {

            String actionChoice = getUserSelection("View jobs by (type 'x' to quit):", actionChoices);

            if (actionChoice == null) {
                break;
            } else if (actionChoice.equals("list")) {

                String columnChoice = getUserSelection("List", columnChoices);

                if (columnChoice.equals("all")) {
                    printAllColumns();
                } else {
                    printColumnValues(columnChoice);
                }

            } else { // choice is "search"

                // What is their search term?
                System.out.println("\nSearch term:");
                String searchTerm = in.nextLine();

                printSearchResults(searchTerm);
            }
        }
    }

    // Returns the key of the selected item from the choices Dictionary
    private static String getUserSelection(String menuHeader, HashMap<String, String> choices) {

        int choiceIdx = -1;
        boolean validChoice = false;
        String[] choiceKeys = new String[choices.size()];

        // Put the choices in an ordered structure so we can
        // associate an integer with each one
        int i = 0;
        for (String choiceKey : choices.keySet()) {
            choiceKeys[i] = choiceKey;
            i++;
        }

        do {

            System.out.println("\n" + menuHeader);

            // Print available choices
            for (int j = 0; j < choiceKeys.length; j++) {
                System.out.println("" + j + " - " + choices.get(choiceKeys[j]));
            }

            if (in.hasNextInt()) {
                choiceIdx = in.nextInt();
                in.nextLine();
            } else {
                String line = in.nextLine();
                boolean shouldQuit = line.equals("x");
                if (shouldQuit) {
                    return null;
                }
            }

            // Validate user's input
            if (choiceIdx < 0 || choiceIdx >= choiceKeys.length) {
                System.out.println("Invalid choice. Try again.");
            } else {
                validChoice = true;
            }

        } while (!validChoice);

        return choiceKeys[choiceIdx];
    }

    // Print all columns
    private static void printAllColumns() {
        for (String key : JobData.findAll().get(0).keySet()) {
            System.out.println(key);
        }
    }

    // Print specific column values
    private static void printColumnValues(String columnChoice) {
        ArrayList<String> results = JobData.findAll(columnChoice);

        System.out.println("\n*** All " + columnChoice + " Values ***");

        if (results.isEmpty()) {
            System.out.println("No results found.");
        } else {
            for (String item : results) {
                System.out.println(item);
            }
        }
    }

    // Print search results across all columns
    private static void printSearchResults(String searchTerm) {
        ArrayList<HashMap<String, String>> searchResults = JobData.findByValue(searchTerm);

        if (searchResults.isEmpty()) {
            System.out.println("No results found for '" + searchTerm + "'.");
        } else {
            System.out.println("\nSearch Results:");
            for (HashMap<String, String> job : searchResults) {
                for (Map.Entry<String, String> entry : job.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    System.out.println(key + ": " + value);
                }
                System.out.println("---------------");
            }
        }
    }
}
