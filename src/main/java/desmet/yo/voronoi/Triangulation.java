//import java.io.*;
package desmet.yo.voronoi;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;


/**
 * Implements an incremental algorithm for calculating Delaunay Triangulations
 * and their Voronoi Duals based on a feed of datapoints. This is based on the
 * pseudocode from Guebas and Stolfi (1985) p.120.
 * Bugfixes and optimized by Jo Desmet. 
 */
public class Triangulation {
	
	private static final long serialVersionUID = -3967176215749113937L;
	
	Vector<QuadEdge> quadEdges = null;

	//Vector voronoiPolygons = null;

	private boolean voronoiEdgesBuilt = false;

	//public static final double TOLLERANCE = 0.0000001;

	public static final int FLAG_EDGE_LEFT_USED = 0x01;
	public static final int FLAG_EDGE_DELETED   = 0x02;
	public static final int FLAG_POINT_INFINITE = 0x01;
	
	/**
	 * Initiates the triangulation with the given clipping rectangle. The
	 * clipping rectangle has a strict horizontal/vertical orientation and
	 * is given by 2 opposed vertices. In stead of adding 4 points for each
	 * vertex, the rectangle will be converted to a triangle containing the
	 * clipping rectangle. The used triangle is not necesairaly the one with
	 * the smallest surface possible.
	 * @param p1 first opposing vertex of the clipping rectangle.
	 * @param p2 second opposing vertex of the clipping rectangle.
	 */
	public Triangulation (Point ul,Point lr) {
		double ulx = Math.min(ul.getX(),lr.getX());
		double uly = Math.max(ul.getY(),lr.getY());
		double lrx = Math.max(lr.getX(),lr.getX());
		double lry = Math.min(lr.getY(),lr.getY());
		double w = lrx - ulx;
		double h = uly - lry;
		
		Point e1 = new Point(ulx-h,lry);
		Point e2 = new Point(lrx+h,lry);
		Point e3 = new Point(ulx+w/2,uly+w/2);
		
		init(e1,e2,e3);
		
	}
	
	/**
	 * Initiates the triangulation with a default clipping triangle. The default
	 * vertices for the clipping triangle are (0,-10000), (-20000,10000), (20000,10000).
	 */
	public Triangulation () {
		//e1.setOrg(new Point(0, -8192));
		//e2.setOrg(new Point(-16384, 8192));
		//e3.setOrg(new Point(16384, 8192));
		//this(
		//		new Point(     0, -10000),
		//		new Point(-20000,  10000),
		//		new Point( 20000,  10000)
		//);
//		this(
//				new Point(-360, 90),
//				new Point( 360,-90)
//		);
//		this(
//				new Point(-360,-90),
//				new Point(   0,-90),
//				new Point(-180, 90)
//		);
		this(
		  new Point(-150, 50),
		  new Point(  50,  0)
        );
	}
	
	/**
	 * Initiates the triangulation with the given clipping triangle. The
	 * clipping triangle is given by its three vertices.
	 * @param p1 first vertex of the clipping triangle.
	 * @param p2 second vertex of the clipping triangle.
	 * @param p3 third vertex of the clipping triangle.
	 */
	public Triangulation (Point p1,Point p2,Point p3) {
		init(p1,p2,p3);
	}
	
	private void init(Point p1,Point p2,Point p3) {
		quadEdges = new Vector<QuadEdge>();
		
		QuadEdge e1 = new QuadEdge();
		QuadEdge e2 = new QuadEdge();
		QuadEdge e3 = new QuadEdge();
		
		p1.setFlag(FLAG_POINT_INFINITE);
		p2.setFlag(FLAG_POINT_INFINITE);
		p3.setFlag(FLAG_POINT_INFINITE);

		e1.setOrg(p1);
		e2.setOrg(p2);
		e3.setOrg(p3);

		e1.setDest(e2.getOrg());
		e2.setDest(e3.getOrg());
		e3.setDest(e1.getOrg());

		e1.setOnext(e3.sym());
		e1.rot().setOnext(e3.rot());
		e1.sym().setOnext(e2);
		e1.invrot().setOnext(e2.invrot());

		e2.setOnext(e1.sym());
		e2.rot().setOnext(e1.rot());
		e2.sym().setOnext(e3);
		e2.invrot().setOnext(e3.invrot());

		e3.setOnext(e2.sym());
		e3.rot().setOnext(e2.rot());
		e3.sym().setOnext(e1);
		e3.invrot().setOnext(e1.invrot());
		
		quadEdges.add(e1);
		quadEdges.add(e2);
		quadEdges.add(e3);
	}

	static private void splice(QuadEdge a, QuadEdge b) {
		QuadEdge aa = a.onext().rot();
		QuadEdge bb = b.onext().rot();

		QuadEdge t1 = b.onext();
		QuadEdge t2 = a.onext();
		QuadEdge t3 = bb.onext();
		QuadEdge t4 = aa.onext();
		
		a.setOnext(t1);
		b.setOnext(t2);

		aa.setOnext(t3);
		bb.setOnext(t4);
	}

	/**
	 * Add a new edge e connecting the destination of a to the 
	 * origin of b, in such a way that all three have the same
	 * left face after the connection is complete.
	 * Additionally, the data pointers of the new edge are set.
	 */
	private QuadEdge connect (QuadEdge a, QuadEdge b) {
		QuadEdge e = new QuadEdge();
		quadEdges.add(e);
		
		e.setOrg(a.getDest());
		e.setDest(b.getOrg());

		splice(e,a.lnext());
		splice(e.sym(),b);

		return e;
	}

	private void deleteEdge (QuadEdge e) {
		splice(e, e.oprev());
		splice(e.sym(), e.sym().oprev());
		
		e.setFlag(FLAG_EDGE_DELETED);
		e.rot().setFlag(FLAG_EDGE_DELETED);
		e.sym().setFlag(FLAG_EDGE_DELETED);
		e.invrot().setFlag(FLAG_EDGE_DELETED);
	}
	
	private void sweepEdges() {
		// NOTE: Can not get rid of the pre-6 syntax. Still need to use
		// iterator in order to use the modifyable interface (i.remove).
		Iterator i = quadEdges.iterator();
		int c = 0;
		
		while (i.hasNext()) {
			QuadEdge e = (QuadEdge) i.next();
			if (e.isFlagSet(FLAG_EDGE_DELETED)) { i.remove(); c++; }
		}
		System.out.println("Deleted "+c+" Edges");

	}

	/**
	 * swaps an edge to fulfill the Delaunay condition.
	 * @param e
	 */
	static private void swap (QuadEdge e) {
		QuadEdge a = e.oprev();
		QuadEdge b = e.sym().oprev();

		// Disconnect edge
		splice(e, a);
		splice(e.sym(), b);
		
		// In stead of deleting, reuse the edge as the
		// swapped version.
		splice(e, a.lnext());
		splice(e.sym(), b.lnext());
		e.setOrg(a.getDest());
		e.setDest(b.getDest());
	}

	public void insertSite(double x,double y) {
		insertSite(new Point(x, y));
	}
	
	/* Debugging Only */
	/*
	private void printAllElements() {
		System.out.println("======================================");
		for (int i=0; i < size(); i++) {
			Object element = elementAt(i);
			QuadEdge e = (QuadEdge)element;
			if (e != null) e.print("QuadEdge");
		}
		System.out.println("======================================");
	}
	*/

	
	/**
	 * Inserts a new point into a subdivision representing a Delaunay
	 * triangulation, and fixes the affected edges so that the result
	 * is still a Delaunay triangulation. This is based on the pseudocode 
	 * from Guebas and Stolfi (1985) p.120.
	 * Bugfixes and optimized by Jo Desmet. 
	 * @param pt
	 */
	public void insertSite (Point pt)
	{
		QuadEdge e = locateContainingTriangleEdge(pt);
		
		if (e == null) return; //Could not locate Containing Triangle!
		
		// I think it should work without below logic ... or needs different logic ...
		// Maybe we need to keep a list of overlapping points. Just skipping means
		// potentially having attributes missing, but otherwise could mean
		// having conflicting attributes.
		//if (pt.equals(e.getOrg()) || pt.equals(e.getDest()))
		//	return;
		
		// I think it should work without below logic ...
		//if (pt.isOn(e)) {
		//	QuadEdge t = e.oprev();
		//	deleteEdge(e);
		//	e = t;
		//}

		/*
		 * Connect the new point to the vertices of the containing
		 * triangle (or quadrilateral, if the new point fell on an
		 * existing edge). 
		 */
		
		// Add first new Edge Connecting to vertice.
		QuadEdge newEdge = new QuadEdge();
		QuadEdge startingEdge = newEdge;
		quadEdges.add(newEdge);
		newEdge.setOrg(e.getOrg());
		newEdge.setDest(pt);
		splice(newEdge, e);
		
		// Connect the other 2 vertices.
		newEdge = connect(e,newEdge.sym());
		newEdge = connect(newEdge.oprev(),newEdge.sym());
		
		e = newEdge.oprev();
		while (true) {
			QuadEdge t = e.oprev();
			if (pt.isInCircle(e.getOrg(), t.getDest(), e.getDest())) {
				//X is in the circle. Should delete an old edge and add a new one.
				swap(e);
				e = e.oprev();
			} else if (e.onext() == startingEdge) {
				// No more susspect edges.
				return;
			} else {
				// pop a suspect edge.
				e = e.onext().lprev();
			}
		}
	}

	/**
	 * Returns an edge e, where point p is on the edge e, or e is an edge of a triangle
	 * containing point p.
	 * @param p
	 * @return
	 */
	private QuadEdge locateContainingTriangleEdge(Point p) {
		QuadEdge start = (QuadEdge)quadEdges.get(0);
		QuadEdge e = start;
		int cnt = 0;

		while(true) {
			if (p.equals(e.getOrg()) || p.equals(e.getDest()) || p.isOn(e)) {
				return e;
			} else if (p.isRightOf(e)) {
				e = e.sym();
			} else if (!p.isRightOf(e.onext())) {
				e = e.onext();
			} else if (!p.isRightOf(e.dprev())) {
				e = e.dprev();
			} else {
				return e;
			}
			if (e == start || cnt > 1000) return null; // Could not locate Containing Triangle!
			cnt++;
		}
	}

	/**
	 * Builds the individual edges of the dual form. This will be
	 * executed before getting the Voronoi Edges, or before getting
	 * the Voronoi Polygons.
	 */
	private void buildVoronoiEdges() {
		Point pl, pr;

		/*
		 * Add this point, it is assumed the user has done adding sites.
		 * So first do a clean sweep to remove all edges marked for deletion.
		 */
		sweepEdges();
		
		for (QuadEdge e:quadEdges) {
			pl = getVoronoiVertex(e, e.onext());
			pr = getVoronoiVertex(e, e.oprev());

			e.dual().setOrg(pr);
			e.dual().setDest(pl);
		}
		voronoiEdgesBuilt  = true;
	}

	private static Point getVoronoiVertex(QuadEdge e1, QuadEdge e2) {
		Point pnt = new Point(0, 0);
		double x2 = (double)e1.getOrg().getX();
		double y2 = (double)e1.getOrg().getY();
		double x1 = (double)e1.getDest().getX();
		double y1 = (double)e1.getDest().getY();
		double x3 = (double)e2.getDest().getX();
		double y3 = (double)e2.getDest().getY();

		double det = (y2 - y3) * (x2 - x1) - (y2 - y1) * (x2 - x3);
		double c1 = (x1 + x2) * (x2 - x1) / 2 + (y2 - y1) * (y1 + y2) / 2;
		double c2 = (x2 + x3) * (x2 - x3) / 2 + (y2 - y3) * (y2 + y3) / 2;
		pnt.setX((double)((c1 * (y2 - y3) - c2 * (y2 - y1)) / det));
		pnt.setY((double)((c2 * (x2 - x1) - c1 * (x2 - x3)) / det));
		return pnt;
	}

	
	//public Vector getVoronoiPolygons() {
	//	if (voronoiPolygons != null) return voronoiPolygons;
	//	voronoiPolygons = buildVoronoiPolygons();
	//	return voronoiPolygons;
	//}
        
        private VoronoiPolygon buildRegionPolygon(QuadEdge firstRegionEdge,RegionFilter filter) {
            VoronoiPolygon polygon = new VoronoiPolygon();
            QuadEdge regionEdge = firstRegionEdge;
            QuadEdge edge;
            QuadEdge firstEdge;
            String regionName = filter.getRegionName(firstRegionEdge.rot().getOrg());
            System.out.println("Building Region "+regionName);
            //polygon.setProperty("REGION", regionName);
            while (true) {
                regionEdge.setFlag(FLAG_EDGE_LEFT_USED);
                polygon.add(regionEdge);

                //Find next Edge around Region
                edge = regionEdge.lnext();
                firstEdge = regionEdge.lnext();
                while (true) {
                    if (edge.isLeftRegionEdge(filter)) {
                        regionEdge = edge;
                        break;
                    }
                    edge = edge.oprev();
                    
                    // Should never happen
                    assert (edge != firstEdge);

                }
                if (regionEdge == firstRegionEdge) break;
            }
            if (!polygon.isOuter()) System.out.println("  >> Inner Polygon!");
            return polygon;
        }
        
        private static class VoronoiRegionContainer {
            private Vector<VoronoiRegion> regions = new Vector<VoronoiRegion>();
            
            public VoronoiRegionContainer() {
                
            }
            
            private void addOuter(Vector<VoronoiPolygon> polygons) {
                for (VoronoiPolygon polygon:polygons) {
                    if (!polygon.isOuter()) continue;
                    VoronoiRegion region = new VoronoiRegion();
                    region.setOuterPolygon(polygon);
                    regions.add(region);
                }
            }
            
            /* addInner will only work AFTER addOuter has been ran!! */
            private void addInner(Vector<VoronoiPolygon> polygons) {
                INNER_POLYGONS:
                for (VoronoiPolygon polygon:polygons) {
                    if (polygon.isOuter()) continue;
                    for (VoronoiRegion region:regions) {
                        if (region.getOuterPolygon().contains(polygon)) {
                            region.addInnerPolygon(polygon);
                            // any inner polygon is only contained by exactly
                            // one Outer polygon.
                            continue INNER_POLYGONS;
                        }
                    }
                    // This is an illegal status -- orphan inner polygon!
                    assert(false);
                }
            }

            private void process(Vector<VoronoiPolygon> polygons) {
                addOuter(polygons);
                addInner(polygons);
            }
            
            public Vector<VoronoiRegion> getVoronoiRegions() {
                return regions;
            }
        }
        
        
        
        public Vector<VoronoiRegion> getVoronoiRegions(RegionFilter filter) {
            //QuadEdgeList edges = getVoronoiEdges();
            //Hashtable<String,Region> regions = new Hashtable<String,Region>();
            Vector<VoronoiPolygon> polygons = new Vector<VoronoiPolygon>();

            if (!voronoiEdgesBuilt) buildVoronoiEdges();
            for (QuadEdge edge:quadEdges) { 
                edge.rot().clearFlag(FLAG_EDGE_LEFT_USED);
                edge.invrot().clearFlag(FLAG_EDGE_LEFT_USED);
            }

            for (QuadEdge edge:quadEdges) {
                QuadEdge faceEdge = edge.rot();
                if (!faceEdge.isFlagSet(FLAG_EDGE_LEFT_USED) && faceEdge.isLeftRegionEdge(filter)) {
                    polygons.add(buildRegionPolygon(faceEdge,filter));
                }

                faceEdge = edge.invrot();
                if (!faceEdge.isFlagSet(FLAG_EDGE_LEFT_USED) && faceEdge.isLeftRegionEdge(filter)) {
                    polygons.add(buildRegionPolygon(faceEdge,filter));
                }
                
            }
            
            VoronoiRegionContainer container = new VoronoiRegionContainer();
            container.process(polygons);
            //return new Vector<Region>(regions.values());
            return container.getVoronoiRegions();
        }
	
	/**
	 * gets the unique Edges that build the Voronoi Dual.
	 * @return
	 */
	public Vector<QuadEdge> getVoronoiEdges() {
		Vector<QuadEdge> edges = new Vector<QuadEdge>();
                
		if (!voronoiEdgesBuilt) buildVoronoiEdges();
		// Only loop through the Vertixes.
		for (QuadEdge edge:quadEdges) {
			if (
					edge.getOrg().isFlagSet(FLAG_POINT_INFINITE)
					&&edge.sym().getOrg().isFlagSet(FLAG_POINT_INFINITE)
					) continue;
			edges.add(edge.rot());
		}
		return edges;
	}
	
	/**
	 * gets the unique Edges that build the Delaunay Triangulation.
	 * @return
	 */
	public Vector getDelaunayEdges() {
		Vector<QuadEdge> edges = new Vector<QuadEdge>();

		if (!voronoiEdgesBuilt) buildVoronoiEdges();
		// Only loop through the Vertixes.
		for (QuadEdge edge : quadEdges) {
			if (
					edge.getOrg().isFlagSet(FLAG_POINT_INFINITE)
					&&edge.sym().getOrg().isFlagSet(FLAG_POINT_INFINITE)
					) continue;
			edges.add(edge);
		}
		return edges;
	}
	
//	public QuadEdge findEdge(int id) {
//		Iterator i = quadEdges.iterator();
//		while (i.hasNext()) {
//			QuadEdge edge = (QuadEdge)i.next();
//			if (edge.index == id) return edge;
//		}	
//		return null;
//	}
//	
	
	/**
	 * builds polygon around origin of firstEdge.
	 * @param firstEdge the edge that will be used for the circumfencing polygon.
	 * @return the circumfencing polygon, or null if the polygon is already processed.
	 */
	private VoronoiPolygon buildVoronoiPolygon(QuadEdge firstEdge) {
		if (firstEdge.isFlagSet(FLAG_EDGE_LEFT_USED)) return null; // Polygon already built!
		if (firstEdge.getOrg().isFlagSet(FLAG_POINT_INFINITE)) return null; // Polygon on Infinite!

		VoronoiPolygon polygon = new VoronoiPolygon();
		
		QuadEdge nextEdge = firstEdge;
		QuadEdge edge = null;
		QuadEdge dual = null;
		do {
			edge = nextEdge;
			dual = edge.rot();
			polygon.add(dual);
			edge.setFlag(FLAG_EDGE_LEFT_USED);
			nextEdge = edge.onext();
		} while(nextEdge != firstEdge);
		
		return polygon;
	}
	
	/**
	 * gets the Pologons that build the Voronoi dual by left rotating around each
	 * Original origin. By definition there will also be no duplication, except for
	 * its inverse form, between multiple polygons.
	 * @return vector of polygons (polygon is vector of QuadEdge).
	 */
	public Vector<VoronoiPolygon> getVoronoiPolygons()
	{
		Vector<VoronoiPolygon> polygons = new Vector<VoronoiPolygon>();
		
		if (!voronoiEdgesBuilt) buildVoronoiEdges();
		
		//populate a new vector with voronoi elements

		VoronoiPolygon polygon;
		
		for (QuadEdge edge:quadEdges) {
			polygon = buildVoronoiPolygon(edge);
			if (polygon != null) polygons.add(polygon);
			polygon = buildVoronoiPolygon(edge.sym());
			if (polygon != null) polygons.add(polygon);
		}
		return polygons;
	}
	

}