import com.opencsv.CSVWriter;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        Reader reader = new Reader();
        writer();

    }


    public static void writer() throws IOException {
        String[] header = {"Difference"};

        ArrayList<String[]> lines = new ArrayList<>();
        //difference.forEach(s -> lines.add(new String[]{s.substring(0, 4)}));

        Writer writer = Files.newBufferedWriter(Paths.get("C:\\Users\\analu\\Documents\\Rafael\\TCC\\Dados\\output\\outputdentroaguas.csv"));
        CSVWriter csvWriter = new CSVWriter(writer);

        csvWriter.writeNext(header);
        csvWriter.writeAll(lines);

        csvWriter.flush();
        writer.close();
    }




}


