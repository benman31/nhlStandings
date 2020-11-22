import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;

/**
 * A class for processing raw hockey game data and storing/updating it in a binary file called standings.bin,
 * this class contains a single public static method parseResult which takes the filePath of a csv file as a parameter,
 * scans the results, parses the data and updates the standings.bin file with the data
 */
public class HockeyData {
    /**
     *Takes the results contained in the csv file provided in the filePath parameter and parses it for specific data,
     * namely, the teamID of the home team and the away team, and the string containing the result of the game. Data
     * is converted into a usable format and written to the standings.bin file if it exists. If a file containing
     * an invalid data format is passed, the method returns without performing any changes to standings.bin
     * @param filePath the file path of the .csv file containing NHL hockey game results for a given month in the season
     * @throws IOException if file does not exist
     */
    public static void parseResult(String filePath) throws IOException {
        List<List<String>> gameResults = GameScanner.scan(filePath);
        if (gameResults == null)
        {
            System.out.println("No changes have been made to records");
            return;
        }
        for(List<String> list: gameResults){
            if (isTeamID(list.get(0)) && isTeamID(list.get(1))) {
                int awayTeamID = Integer.parseInt(list.get(0));
                int homeTeamID = Integer.parseInt(list.get(1));
                String[] results = list.get(2).toUpperCase().split(" ");

                if (results.length != 3){
                    System.out.println("No changes have been made to records");
                    return;
                }

                if (results[0].equals("HOME")){
                    update(homeTeamID, 2);
                    if (results[2].equals("OT") || results[2].equals("SO")){
                        update(awayTeamID, 1);
                    }
                    else if (results[2].equals("REG")){
                        update(awayTeamID, 0);
                    }
                }
                else if (results[0].equals("AWAY")){
                    update(awayTeamID, 2);
                    if (results[2].equals("OT") || results[2].equals(("SO"))){
                        update(homeTeamID, 1);
                    }
                    else if (results[2].equals("REG")){
                        update(homeTeamID, 0);
                    }
                }
                else { System.out.println("File contains invalid data format.");}
            }


        }
    }

    /**
     * Accesses the binary file standings.bin if it exists, and updates the appropriate entry with the outcome
     * of a given hockey game
     * @param teamId the ID number of a given team
     * @param outcome the outcome of a given hockey game. A 0 increments the team's losses field in the record by 1,
     *                a 1 increments the lossesOT field in the record by 1, and a 2 increments the wins field by 1.
     * @throws IOException if file does not exist
     */
    private static void update(int teamId, int outcome) throws IOException {
        StandingsReader reader = new StandingsReader();
        reader.open("nhl-game-data/standings.bin");
        reader.read(teamId - 1);
        Map<Integer, List<Long>> teamRecord = reader.getData();
        reader.close();
        if (outcome == 2){
            long wins = teamRecord.get(teamId).get(0);
            wins ++;
            teamRecord.get(teamId).set(0, wins);
        }
        else if (outcome == 1){
            long lossesOT = teamRecord.get(teamId).get(2);
            lossesOT ++;
            teamRecord.get(teamId).set(2, lossesOT);
        }
        else if (outcome == 0){
            long losses = teamRecord.get(teamId).get(1);
            losses ++;
            teamRecord.get(teamId).set(1, losses);
        }
        StandingsWriter writer = new StandingsWriter(teamRecord);
        writer.open("nhl-game-data/standings.bin");
        writer.write(teamId -1);
        writer.close();
    }

    /**
     * Checks if input argument represents a valid NHL team ID - an integer from 1 to 30 inclusive
     * @param arg the String to be checked
     * @return true if arg is an integer from 1 to 30 inclusive
     */
    public static boolean isTeamID(String arg){
        boolean result = false;
        if (arg.length() == 1){
            if(Character.isDigit(arg.charAt(0))){
                result = true;
            }
        }
        else if (arg.length() == 2){
            if(Character.isDigit(arg.charAt(0)) && Character.isDigit(arg.charAt(1))){
                result = true;
            }
        }

        return result;
    }
}
