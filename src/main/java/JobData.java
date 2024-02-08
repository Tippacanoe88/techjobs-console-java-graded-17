import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

public class JobData {

    private static final String DATA_FILE = "src/main/resources/job_data.csv";
    private static boolean isDataLoaded = false;

    private static ArrayList<HashMap<String, String>> allJobs;

    // Fetch list of all values from loaded data, without duplicates, for a given column
    public static ArrayList<String> findAll(String field) {
        loadData();
        ArrayList<String> values = new ArrayList<>();
        for (HashMap<String, String> row : allJobs) {
            String aValue = row.get(field);
            if (!values.contains(aValue)) {
                values.add(aValue);
            }
        }
        return values;
    }

    // Returns results of searching the jobs data by key/value, using inclusion of the search term
    public static ArrayList<HashMap<String, String>> findByColumnAndValue(String column, String value) {
        loadData();
        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();
        for (HashMap<String, String> row : allJobs) {
            String aValue = row.get(column);
            if (aValue.contains(value)) {
                jobs.add(row);
            }
        }
        return jobs;
    }

    // Search all columns for the given term
    public static ArrayList<HashMap<String, String>> findByValue(String value) {
        loadData();
        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();
        for (HashMap<String, String> row : allJobs) {
            for (String key : row.keySet()) {
                String cellValue = row.get(key);
                if (cellValue != null && cellValue.toLowerCase().contains(value.toLowerCase())) {
                    jobs.add(row);
                    break;
                }
            }
        }
        return jobs;
    }

    // Method to display job details
    public static void displayJobDetails(HashMap<String, String> job) {
        for (Map.Entry<String, String> entry : job.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        System.out.println("---------------");
    }

    // Method to sort job listings by a given field
    public static void sortJobs(String field) {
        loadData();
        Comparator<HashMap<String, String>> comparator = Comparator.comparing(job -> job.get(field));
        allJobs.sort(comparator);
    }

    // Read in data from a CSV file and store it in a list
    private static void loadData() {
        if (isDataLoaded) {
            return;
        }
        try {
            Reader in = new FileReader(DATA_FILE);
            CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            List<CSVRecord> records = parser.getRecords();
            int numberOfColumns = records.get(0).size();
            String[] headers = parser.getHeaderMap().keySet().toArray(new String[numberOfColumns]);

            allJobs = new ArrayList<>();

            for (CSVRecord record : records) {
                HashMap<String, String> newJob = new HashMap<>();
                for (String headerLabel : headers) {
                    newJob.put(headerLabel, record.get(headerLabel));
                }
                allJobs.add(newJob);
            }
            isDataLoaded = true;
        } catch (IOException e) {
            System.out.println("Failed to load job data");
            e.printStackTrace();
        }
    }
}
