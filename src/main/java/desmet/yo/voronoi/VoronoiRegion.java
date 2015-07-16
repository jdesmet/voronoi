/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package desmet.yo.voronoi;

import java.util.Vector;

/**
 *
 * @author jd3714
 */
public class VoronoiRegion {
    private VoronoiPolygon outerPolygon = null;
    private Vector<VoronoiPolygon> innerPolygons = null;
    //String name;
    VoronoiRegion() {
        //this.name = name;
    }
    
    void setOuterPolygon(VoronoiPolygon polygon) {
        this.outerPolygon = polygon;
    }
    
    void addInnerPolygon(VoronoiPolygon polygon) {
        if (polygon.edges().size() < 3) return;
        if (innerPolygons == null) innerPolygons = new Vector<VoronoiPolygon>();
        innerPolygons.add(polygon);
    }
    
    public boolean hasInnerPolygons() {
        return innerPolygons != null;
    }
    
    public VoronoiPolygon getOuterPolygon() {
        return this.outerPolygon;
    }
    
    public Vector<VoronoiPolygon> getInnerPolygons() {
        return this.innerPolygons;
    }
    
    public String getProperty(String key) {
        return outerPolygon.getProperty(key);
    }
}
