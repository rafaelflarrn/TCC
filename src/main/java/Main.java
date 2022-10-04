import com.opencsv.CSVWriter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) throws IOException {
        ArrayList<String> difference = reader();
        writer(difference);

    }


    public static void writer(ArrayList<String> difference) throws IOException {
        String[] header = {"Difference"};

        ArrayList<String[]> lines = new ArrayList<>();
        difference.forEach(s -> lines.add(new String[]{s}));

        Writer writer = Files.newBufferedWriter(Paths.get(".\\output\\output.csv"));
        CSVWriter csvWriter = new CSVWriter(writer);

        csvWriter.writeNext(header);
        csvWriter.writeAll(lines);

        csvWriter.flush();
        writer.close();
    }

    public static ArrayList<String> reader() {
        ArrayList<String> difference = new ArrayList();
        ArrayList<Position> phone;
        ArrayList<Position> ublox;

        try {
            File phoneFile = new File(".\\input\\celular.nmea");
            File ubloxFile = new File(".\\input\\ublox.nmea");

            Scanner phoneFileReader = new Scanner(phoneFile);
            Scanner ubloxFileReader = new Scanner(ubloxFile);
            phone = getGPGGA(phoneFileReader);
            ublox = getGPGGA(ubloxFileReader);
            ArrayList<Double> x = new ArrayList<>();
            ArrayList<Double> y = new ArrayList<>();
            ArrayList<Double> z = new ArrayList<>();

            for (int i = 0; i < phone.size(); i++) {
                x.add(i, (double) i);
                y.add(i, phone.get(i).latitude);
                z.add(i, phone.get(i).longitude);
            }

            Double[] xt = x.toArray(new Double[0]);
            Double[] yt = y.toArray(new Double[0]);
            Double[] zt = y.toArray(new Double[0]);

            double size = ublox.size() / phone.size();

            double[] yy = new double[(int) (yt.length * size)];

            SplineInterpolator splineInterpY = new SplineInterpolator();
            SplineInterpolator splineInterpZ = new SplineInterpolator();

            PolynomialSplineFunction cubicSplineY = splineInterpY.interpolate(ArrayUtils.toPrimitive(xt), ArrayUtils.toPrimitive(yt));
            PolynomialSplineFunction cubicSplineZ = splineInterpZ.interpolate(ArrayUtils.toPrimitive(xt), ArrayUtils.toPrimitive(zt));

            for (int i = 0; i < yy.length - 2; i++) {
                double distance = distance(cubicSplineY.value(i / size), ublox.get(i).latitude,
                        cubicSplineZ.value(i / size), ublox.get(i).longitude) / 1000;
                difference.add(i, String.valueOf(distance));
            }

            phoneFileReader.close();
            ubloxFileReader.close();
        } catch (
                FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        return difference;
    }

    public static ArrayList<Position> getGPGGA(Scanner myReader) {
        ArrayList<Position> values = new ArrayList<>();
        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            String[] data = line.split(",");
            if (data[0].contains("$GPGGA")) {
                double time = Double.parseDouble(data[1]);
                double latitude = converter(data[2]);
                double longitude = converter(data[4]);
                Position pos = new Position(time, latitude, longitude);
                values.add(pos);
            }
        }
        return values;
    }

    public static double distance(double lat1,
                                  double lat2, double lon1,
                                  double lon2) {
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2), 2);

        double c = 2 * Math.asin(Math.sqrt(a));

        double r = 6371;

        return (c * r);
    }

    public static Double converter(String coordinate) {
        String[] separate = coordinate.split("\\.");
        Double coordinateFormated;
        if (separate[0].length() == 4)
            coordinateFormated = Double.parseDouble(separate[0].substring(0, 2)) + (Double.parseDouble(separate[0].substring(2, 4) + "." + separate[1]) / 60);
        else
            coordinateFormated = Double.parseDouble(separate[0].substring(0, 3)) + (Double.parseDouble(separate[0].substring(3, 5) + "." + separate[1]) / 60);

        return coordinateFormated;
    }


}


