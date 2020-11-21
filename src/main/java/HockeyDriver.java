import java.io.File;
import java.io.IOException;


/**
 * Handle the assignment command line arguments
 */
public class HockeyDriver {

    /**
     * main program entry point
     * @param args command line arguments (see output below)
     */
    public static void main(String [] args) {
        //parse the args and take action
        if (args.length < 2) {
            System.out.println("Please provide 2 program arguments");
            System.out.println("Example: -t 11 (to display the current standings for team with ID = 11");
            System.out.println("Example: -f nhl-game-data" + File.separator + "games-nov.csv (to load new game results and display resulting standings");
            System.exit(0);
        }
        else{
            if (args[0].equals("-t")){
                if (HockeyData.isTeamID(args[1])) {

                    int teamID = Integer.parseInt(args[1]);

                    try {
                        StandingsPrinter.print(teamID);
                    } catch (IOException exception) {
                        System.out.println(exception.getMessage());
                    }
                }
                else {
                    System.out.println("Invalid number, please use an integer from 1 to 30 inclusive as second argument");
                }

            }
            else if (args[0].equals("-f")){
                for(int i= 1; i < args.length; i++){
                    try{
                        HockeyData.parseResult(args[i]);
                    }
                    catch(IOException exception){
                        System.out.println(exception.getMessage());
                    }
                 }
                try {
                    StandingsPrinter.printAll();
                }
                catch (IOException exception){
                    System.out.println(exception.getMessage());
                }
            }
            else {
                System.out.println("Invalid command");
            }
        }

    }

}
