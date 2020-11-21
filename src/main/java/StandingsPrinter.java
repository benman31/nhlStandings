import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A class containing two public static methods for printing NHL team standings from binary file standings.bin
 */
public class StandingsPrinter {

    /**
     * Print the current standings for a given NHL team
     * @param teamID the ID number of the team record to be printed. Should be an integer from 1 to 30 inclusive
     * @throws IOException if file does not exist
     */
    public static void print(int teamID) throws IOException {
        StandingsReader reader = new StandingsReader();
        reader.open("nhl-game-data/standings.bin");
        reader.read(teamID - 1);
        Map<Integer, List<Long>> teamRecord = reader.getData();
        reader.close();
        String teamName = translate(teamID);
        long wins = teamRecord.get(teamID).get(0);
        long losses = teamRecord.get(teamID).get(1);
        long lossesOT = teamRecord.get(teamID).get(2);
        long points = (wins * 2) + lossesOT;
        System.out.printf("%14s %10s %12s %8s %10s %n", "TEAM", "WINS", "LOSSES OT", "LOSSES", "POINTS");
        System.out.printf("%-23s %-12s %-8s %-9s %s %n", teamName, wins, lossesOT, losses, points);
    }

    /**
     * Print the current standings of all NHL teams for the season in order of highest total points to lowest
     * @throws IOException if file does not exist
     */
    public static void printAll() throws IOException {
        StandingsReader reader = new StandingsReader();
        reader.open("nhl-game-data/standings.bin");
        reader.readAll();
        Map<Integer, List<Long>> teamRecord = reader.getData();
        reader.close();

        List<List<String>> rankedList = rankTeams(teamRecord);

        System.out.printf("%14s %10s %12s %8s %10s %n", "TEAM", "WINS", "LOSSES OT", "LOSSES", "POINTS");

        for(List<String> list: rankedList){
            System.out.printf("%-23s %-12s %-8s %-9s %s %n", list.get(0), list.get(1), list.get(2), list.get(3), list.get(4));
        }
    }

    /**
     * Translates teamID number to corresponding team Name
     * @param teamID the ID number of the team to be translated. Should be an integer from 1 to 30 inclusive
     * @return the name of the team, or "No Such Team" if provided with an invalid teamId
     * @throws IOException if file does not exist
     */
    private static String translate(int teamID) throws IOException {
        String teamName;
        try (Stream<String> lineStream = Files.lines(Paths.get("nhl-game-data/team_ids.csv"))){
            teamName = lineStream
                    .skip(teamID)
                    .map(w->w.substring(w.indexOf(",") + 1))
                    .findFirst()
                    .orElse("No Such Team");
        }
        return teamName;
    }

    /**
     * Ranks teams in order of most to least points earned so far in the season
     * @param teamRecord A Map<Integer>, List<Long>> containing unranked list of teams listed in order of teamID from lowest to highest
     * @return A ranked List<List<String>> from highest to lowest points earned in the season
     * @throws IOException if file does not exist
     */
    private static List<List<String>> rankTeams(Map <Integer, List<Long>> teamRecord) throws IOException {

        List<List<String>> unrankedList = new ArrayList<>();
        Set<Integer> keySet = teamRecord.keySet();

        for(Integer key: keySet){
            String teamName = translate(key);
            long wins = teamRecord.get(key).get(0);
            long losses = teamRecord.get(key).get(1);
            long lossesOT = teamRecord.get(key).get(2);
            long points = (wins * 2) + lossesOT;
            List<String> toPrint = new ArrayList<>();
            toPrint.add(teamName);
            toPrint.add("" + wins);
            toPrint.add("" + losses);
            toPrint.add("" + lossesOT);
            toPrint.add("" + points);
            unrankedList.add(toPrint);
        }
        return unrankedList.stream()
                .sorted((q,p)-> Integer.parseInt(p.get(4)) - Integer.parseInt(q.get(4)))
                .collect(Collectors.toList());
    }
}
