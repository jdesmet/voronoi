/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package desmet.yo.voronoi;

/**
 *
 * @author jd3714
 */
abstract public class RegionFilter {
    /**
     * identifies whether the given edge marks a boundary.
     * @param edge the edge to be verified
     * @return
     */
    abstract public String getRegionName(Point pt);
}

