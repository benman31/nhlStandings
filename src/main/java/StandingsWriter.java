import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.Map;

/**
 * Writes records for NHL team standings to a binary file
 */
public class StandingsWriter extends StandingsIO {

    RandomAccessFile file;
    Map<Integer, List<Long>> hockeyData;

    /**
     * Construct a default StandingsWriter
     */
    public StandingsWriter(){
        super();
    }

    /**
     * Construct a Standings writer with a Map containing hockey data as a parameter
     * @param hockeyData a Map containing hockey data to be written to the standings binary file
     */
    public StandingsWriter(Map<Integer, List<Long>> hockeyData){
        this.hockeyData = hockeyData;
    }

    /**
     * Opens a file  from provided filepath
     * @param filePath the root path of a binary file
     * @throws IOException if file does not exist
     */
    public void open(String filePath) throws IOException {
        if (file != null) {file.close();}
        file = new RandomAccessFile(filePath, "rw");
    }

    /**
     * Closes an open binary file
     * @throws IOException if file does not exist
     */
    public void close()throws IOException{
        if (file !=null ) {file.close();}
        file = null;
    }

    /**
     * Writes records of NHL team wins and losses to a binary file called Standings.bin
     * @param index the index in the file where the record to be written. Index corresponds to teamID -1
     * @throws IOException if file does not exist
     */
    public void write(int index) throws IOException {

        int teamID = index +1;
        long wins = hockeyData.get(teamID).get(0);
        long losses = hockeyData.get(teamID).get(1);
        long lossesOT = hockeyData.get(teamID).get(2);

        file.seek(index * RECORD_SIZE);
        file.writeInt(teamID);
        file.writeLong(wins);
        file.writeLong(losses);
        file.writeLong(lossesOT);
    }

}
