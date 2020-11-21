import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An abstract super class that provides basic instance variables and
 * methods shared by both StandingsReader and StandingsWriter subclasses
 */
public abstract class StandingsIO implements AutoCloseable {

    RandomAccessFile file;
    Map<Integer, List<Long>> hockeyData;

    final int INT_SIZE = Integer.BYTES;
    final int LONG_SIZE = Long.BYTES;

    final int RECORD_SIZE = INT_SIZE + (3 * LONG_SIZE);

    /**
     * Construct a default StandingsIO
     */
    public StandingsIO(){
        file = null;
        hockeyData = new HashMap<>();
    }

    /**
     * Opens a file  from provided filepath
     * @param filePath the root path of a binary file
     * @throws IOException if file does not exist
     */
    public abstract void open(String filePath) throws IOException;

    /**
     * Closes an open binary file
     * @throws IOException if file does not exist
     */
    public abstract void close()throws IOException;
}

