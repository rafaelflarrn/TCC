import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

@Getter
@Setter
public class Reader {

    ArrayList<Position> phone;
    ArrayList<Position> ublox;

    public Reader() {
        ArrayList<String> difference = new ArrayList();
        Reader reader = new Reader();

        try {
            File phoneFile = new File("C:\\Users\\analu\\Documents\\Rafael\\TCC\\Dados\\0921\\Dentro\\NMEA FILES\\output\\celular.csv");
            File ubloxFile = new File("C:\\Users\\analu\\Documents\\Rafael\\TCC\\Dados\\0921\\Dentro\\NMEA FILES\\output\\ublox.csv");

            Scanner phoneFileReader = new Scanner(phoneFile);
            Scanner ubloxFileReader = new Scanner(ubloxFile);
            reader.setPhone(getGPGGA(phoneFileReader));
            reader.setUblox(getGPGGA(ubloxFileReader));
            ArrayList<Double> x = new ArrayList<>();
            ArrayList<Double> y = new ArrayList<>();
            ArrayList<Double> z = new ArrayList<>();

            for (int i = 0; i < phone.size(); i++) {
                x.add(i, phone.get(i).time);
                y.add(i, phone.get(i).latitude);
                z.add(i, phone.get(i).longitude);
            }

            Double[] xt = x.toArray(new Double[0]);
            Double[] yt = y.toArray(new Double[0]);
            Double[] zt = y.toArray(new Double[0]);

            double size = ublox.size()/phone.size();

            double[] yy = new double[(int) (yt.length*size)];

            SplineInterpolator splineInterpY = new SplineInterpolator();
            SplineInterpolator splineInterpZ = new SplineInterpolator();

            PolynomialSplineFunction cubicSplineY = splineInterpY.interpolate(ArrayUtils.toPrimitive(xt), ArrayUtils.toPrimitive(yt));
            PolynomialSplineFunction cubicSplineZ = splineInterpZ.interpolate(ArrayUtils.toPrimitive(xt), ArrayUtils.toPrimitive(zt));

//           for(int i = 0; i < yy.length - 2; i++){
//               double distance = distance(cubicSplineY.value(i/size), ublox.get(i).latitude,
//                       cubicSplineZ.value(i/size), ublox.get(i).longitude) / 1000;
//               difference.add(i, distance);
//           }
            for (int i = 0; i < phone.size(); i++) {
                double distance = distance(phone.get(i).latitude, ublox.get(i).latitude,
                        phone.get(i).longitude, ublox.get(i).longitude) * 1000;
                difference.add(String.valueOf(distance));
            }


            phoneFileReader.close();
            ubloxFileReader.close();
        } catch (
                FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public ArrayList<Position> getGPGGA(Scanner myReader) {
        ArrayList<Position> values = new ArrayList<>();
        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            String[] data = line.split(",");
            double latitude = Double.parseDouble(data[0]);
            double longitude = Double.parseDouble(data[1]);
            Position pos = new Position(latitude, longitude);
            values.add(pos);
        }
        return values;
    }

    public double distance(double lat1,
                                  double lat2, double lon1,
                                  double lon2)
    {
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        double r = 6371;

        return(c * r);
    }

}
