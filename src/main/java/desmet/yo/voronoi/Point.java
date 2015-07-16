package desmet.yo.voronoi;

import java.util.HashMap;
import java.util.Map;

import desmet.yo.voronoi.common.FlagableImpl;

public class Point extends FlagableImpl {
	
	private Map<String,String> properties = null;
	
	private double x, y;
	
	//private boolean infinite = false;
	
	// Any tolarance will always be to big ... Use Zero.
	// Considder changing double arithmic into integer arithmic.
	//static final double TOLLERANCE = 0.0000001;
	static final double TOLLERANCE = 0;

	public Point (double a, double b) {
		x = a;
		y = b;
	}

	public Point (Point pt) {
		this(pt.x,pt.y);
	}

	public Point () {
		super();
	}
	
	public void setProperty(String key,String value) {
		if (properties == null) properties = new HashMap<String,String>();
		properties.put(key,value);
	}
	
	public String getProperty(String key) {
		if (properties == null) return null;
		else return (String)properties.get(key);
	}
	
	public boolean equals(Object o) {
		if (o == null) return false;
		if (this == o) return true;
		if (!(o instanceof Point)) return false;
		Point p = (Point)o;
		return (Math.abs(this.x-p.x) < TOLLERANCE) && (Math.abs(this.y-p.y) < TOLLERANCE);
	}

	boolean isOn (QuadEdge e) {
		return false;
		//return (Math.abs(calculateTriArea(this, e.getDest(), e.getOrg())) <= TOLLERANCE);
	}

	boolean isRightOf (QuadEdge e) {
		return (calculateTriArea(this,e.getDest(), e.getOrg()) > TOLLERANCE);
	}

	boolean isLeftOf (QuadEdge e) {
		return (calculateTriArea(this, e.getOrg(),e.getDest()) > TOLLERANCE);
	}

	/**
	 * Returns twice the area of the oriented triangle (this,p2,p3). This means
	 * that the result is positive if the triangle is oriented counterclockwise.
	 * 
	 * @param p2
	 * @param p3
	 * @return
	 */
	private static double calculateTriArea (Point p1, Point p2, Point p3) {
		return ((p2.x - p1.x) * (p3.y - p1.y) - (p3.x - p1.x) * (p2.y - p1.y));
	}
	
//	private static boolean isCCW(Point p1, Point p2, Point p3) {
//		return calculateTriArea(p1,p2,p3) > TOLLERANCE;
//	}

	boolean isInCircle(Point a, Point b, Point c) {
		// Below is the original formula. We will optimize this further,
		// not for speed, but for integer calculation. We do not want to
		// saturate the integer registers, so we will need to move our 
		// point of reference. This will not affect calculation of 
		// calculateTriArea (area calculation). We choose our new point of
		// reference as this.
		//		return (
		//			+ (a.x*a.x + a.y*a.y) * calculateTriArea(b,c,this)
		//			- (b.x*b.x + b.y*b.y) * calculateTriArea(a,c,this)
		//	 		+ (c.x*c.x + c.y*c.y) * calculateTriArea(a,b,this)
		//			- (this.x*this.x + this.y*this.y) * calculateTriArea(a,b,c)
		//			) > 0;
		//		
		// New formula:
		return (
				+ ((a.x - this.x)*(a.x - this.x) + (a.y - this.y)*(a.y - this.y)) * calculateTriArea(b,c,this)
				- ((b.x - this.x)*(b.x - this.x) + (b.y - this.y)*(b.y - this.y)) * calculateTriArea(a,c,this)
				+ ((c.x - this.x)*(c.x - this.x) + (c.y - this.y)*(c.y - this.y)) * calculateTriArea(a,b,this)
				) > 0;
	}

//	public void draw(MyCanvas canvas, double a, double b) {
//		System.out.println("Point.draw() - Point (" + a + ", " + b + ")");
//	}
	
//	/**
//	 * @return Returns the infinite.
//	 */
//	boolean isInfinite() {
//		return infinite;
//	}
//	
//	/**
//	 * @param infinite The infinite to set.
//	 */
//	void setInfinite(boolean infinite) {
//		this.infinite = infinite;
//	}
	/**
	 * @return Returns the x.
	 */
	public double getX() {
		return x;
	}
	/**
	 * @param x The x to set.
	 */
	public void setX(double x) {
		this.x = x;
	}
	/**
	 * @return Returns the y.
	 */
	public double getY() {
		return y;
	}
	/**
	 * @param y The y to set.
	 */
	public void setY(double y) {
		this.y = y;
	}
}