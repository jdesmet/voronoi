package desmet.yo.voronoi.test;

//~--- non-JDK imports --------------------------------------------------------

import desmet.yo.voronoi.*;

//~--- JDK imports ------------------------------------------------------------

//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.Vector;

import javax.swing.JFrame;

//~--- classes ----------------------------------------------------------------

//import com.mobile.mapping.triangulation.*;

/*
* Created on Jun 2, 2004
*
* To change the template for this generated file go to
* Window - Preferences - Java - Code Generation - Code and Comments
 */

/**
 * @author jd3714
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class SimpleTestCase {
    Vector<Point> points = new Vector<Point>();
    Triangulation tri;

    //~--- constructors -------------------------------------------------------

    public SimpleTestCase() {

        // delaunay = new Delaunay();
        // tri = new Triangulation(new Point(-125,50),new Point(-113,32));
        tri = new Triangulation();
    }

    //~--- methods ------------------------------------------------------------

    void addPoint(double longitude, double latitude) {
        Point point = new Point(longitude, latitude);

        // No logic added to exclude equal points.
        tri.insertSite(point);
        points.add(point);

        // delaunay.addPoint(longitude,latitude);
    }

    void addPoints() {
        addPoint(-10, 10);
        addPoint(-10, -10);
        addPoint(10, -10);
        addPoint(10, 10);
        addPoint(8, 10);
        addPoint(5, 4);

        // Duplicate:
        addPoint(5, 4);
        addPoint(-2, 5);
        addPoint(7, -3);

        // addPoint(10,10);
        // addPoint(10,10);
        // addPoint(10,10);
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {

        // Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        SimpleTestCase c = new SimpleTestCase();

        // Create and set up the window.
        VoronoiVisualiser frame = new VoronoiVisualiser();

        // Choose (uncomment) one of both below:
        // Small hardcoded subset of points:
        // c.addPoints();
        // frame.setViewPort(0,0,15,15);
        // Set of points parsed out of a csv file:
        c.readFile(SimpleTestCase.class.getResourceAsStream("/points.csv"));

        // ViewPort focussing on a proeblem area (shows artifacts that do not belong).
        frame.setViewPort(122.009224, -47.608307, 1000, -1000);

        // ViewPort showing a nice zome-out of the area:
        // frame.setViewPort(122.009224, -47.608307, 20, -20);
        frame.setTriangulation(c.tri);
        frame.setPoints(c.points);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Display the window.
        // frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {

        // SimpleTestCase c = new SimpleTestCase();
        // c.addPoints();
        // c.printAllElements();
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    public void readFile(InputStream in) {
        try {
            BufferedReader reader =
                new BufferedReader(new InputStreamReader(in));
            String  line;
            double  longitude = 0;
            double  latitude  = 0;
            boolean first     = true;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                String[] elems;

                if (line.equals("")) {
                    continue;
                }

                elems = line.split(",");

                if (elems.length != 2) {
                    continue;
                }

                try {
                    longitude = Double.parseDouble(elems[0]);
                    latitude  = Double.parseDouble(elems[1]);
                } catch (NumberFormatException x) {
                    continue;
                }

                if (first) {
                    first = false;
                } else {}

                addPoint(longitude, latitude);
            }

            reader.close();
        } catch (IOException x) {
            x.printStackTrace();
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
