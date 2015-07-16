/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package desmet.yo.voronoi;

/**
 *
 * @author jd3714
 */
public class Bounds {
    double topY,bottomY,leftX,rightX;
    private boolean initialized = false;
    
    public void include(Point pt) {
        if (!initialized) {
            leftX = pt.getX();
            rightX = pt.getX();
            topY = pt.getY();
            bottomY = pt.getY();
            initialized = true;
            return;
        }
        leftX   = Math.min(leftX, pt.getX());
        topY    = Math.max(topY, pt.getY());
        rightX  = Math.max(rightX, pt.getX());
        bottomY = Math.min(bottomY, pt.getY());
    }
    
    public void include(Bounds bounds) {
        if (!initialized) {
            this.leftX = bounds.leftX;
            this.rightX = bounds.rightX;
            this.topY = bounds.topY;
            this.bottomY = bounds.bottomY;
            initialized = true;
            return;
        }
        this.leftX = Math.min(bounds.leftX,this.leftX);
        this.rightX = Math.max(bounds.rightX,this.rightX);
        this.topY = Math.max(bounds.topY,this.topY);
        this.bottomY = Math.min(bounds.bottomY,this.bottomY);
    }
    
    public boolean contains(double x,double y) {
        return 
                x > leftX
                && x < rightX
                && y > bottomY
                && y < topY;
    }
    
    public boolean contains(Point point) {
        return contains(point.getX(),point.getY());
    }
    
    public boolean contains(Bounds bounds) {
        return contains(bounds.rightX,bounds.topY) && contains(bounds.leftX,bounds.bottomY);
    }
    
    public Point getUpperLeft() {
        return new Point(leftX,topY);
    }
    
    public Point getLowerRight() {
        return new Point(rightX,bottomY);
    }

}
