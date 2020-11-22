import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A class containing a single static method that converts a csv file containing formatted Hockey game results
 * into a List<List<String>> containing each data point as a String
 */
public class GameScanner {

    /**
     * Scans a given csv file if the filePath exists, stores the contents of each line in a List<List<String>>
     * where the comma separated fields of each line are stored in the inner List<String>. A basic check is performed to
     * ensure each line contains the appropriate fields, returning null if the number of fields is not 6
     *
     * @param filePath the file path of the csv file to be scanned
     * @return a List<List<String>> containing each line, further subdivided into a List<String> where each field is an
     * element in the List<String>. Returns null if fields do not use comma separators or if number of fields is invalid
     * @throws IOException if file does not exist
     */
    public static List<List<String>> scan(String filePath) throws IOException {
        List<List<String>> gameResults = new ArrayList<>();

        try (Stream<String> lineStream = Files.lines(Paths.get(filePath))){
            List<String> rawData = lineStream
                    .skip(1)
                    .collect(Collectors.toList());
            for(String line: rawData){
                if (!line.contains(","))
                {
                    System.out.println("File contains invalid data format");
                    return null;
                }
                String[] data = line.split(",");
                if (data.length != 6)
                {
                    System.out.println("File contains invalid data format");
                    return null;
                }
                List<String> results = new ArrayList<>();
                results.add(data[1]);
                results.add(data[2]);
                results.add(data[5]);
                gameResults.add(results);
            }
        }
        return gameResults;
    }
}
