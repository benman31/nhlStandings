import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Reads records for NHL team standings from a binary file
 */
public class StandingsReader extends StandingsIO {

    RandomAccessFile file;
    Map<Integer, List<Long>> hockeyData;

    /**
     * Construct a default StandingsReader
     */
    public StandingsReader(){
        file = null;
        hockeyData = new HashMap<>();
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
     * Reads record at a given index in a binary file and stores data in a Map
     * @param index the index of the record that is to be read. The index is equal to the teamID - 1
     * @throws IOException if file does not exist
     */
    public void read(int index) throws IOException{
        file.seek(index * RECORD_SIZE);

        int teamID = file.readInt();
        long wins = file.readLong();
        long losses = file.readLong();
        long lossesOT = file.readLong();

        List<Long> results = new ArrayList<>();
        results.add(wins);
        results.add(losses);
        results.add(lossesOT);

        hockeyData.put(teamID, results);
    }

    /**
     * Reads an entire binary file and stores all records in a Map
     * @throws IOException if file does not exist
     */
    public void readAll()throws IOException{
        for(int i = 0; i < file.length()/RECORD_SIZE; i++){
            read(i);
        }
    }

    /**
     * Gets the hockeyData
     * @return a map containing NHL team standings
     */
    public Map<Integer, List<Long>> getData(){return hockeyData;}
}
