package desmet.yo.voronoi;
import desmet.yo.voronoi.common.Flagable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;




/**
 * <p>This represents a Voronoi polygon. Due to the way how flag and attribute
 * setting is implemented, it is <b>not</b> suitable for representing the Delaynay
 * Triangulation: Voronoi Polygons are represented by the single point they
 * circumfence. Standard is Left or Counter Clock Wise (CCW) circumfencing.
 * It is allowed for the this polygon to be open (no final closing edge).</p>
 * <p>Note that the attributes and flags are also stored on the contained
 * tiangulation point (the dual origin of any Voronoi Edge).
 * 
 * @author Jo Desmet
 * 
 */
public class VoronoiPolygon implements Flagable {
	private LinkedList<QuadEdge> edges;
        
        private boolean isInnerOuterDetermined = false;
        private boolean isInnerFlag = false;
        
        private Bounds bounds = new Bounds();

	VoronoiPolygon () {
		edges = new LinkedList<QuadEdge>();
		// do nothing
	}

	/**
	 * adds a new edge to the polygon. The polygon will not be closed, it is the
	 * responsibility of the caller to add a final edge to close the polygon.
	 * @param edge
	 */
	void add (QuadEdge edge) {
		edges.add(edge);
                bounds.include(edge.getOrg());
	}
        
        public boolean contains(Point pt) {
            // Adapted from the following source:
            // http://local.wasp.uwa.edu.au/~pbourke/geometry/insidepoly/
            if (!bounds.contains(pt)) return false;
            boolean inside = false;
            for (QuadEdge edge:edges) {
                if (
                        ( 
                            edge.getDest().getY() <= pt.getY() && pt.getY() < edge.getOrg().getY() ||
                            edge.getOrg().getY() <= pt.getY() && pt.getY() < edge.getDest().getY()
                        ) &&
                        pt.getX() < ((edge.getOrg().getX() - edge.getDest().getX()) * (pt.getY() - edge.getDest().getY()) / (edge.getOrg().getY() - edge.getDest().getY()) + edge.getDest().getX())
                        )
                    inside = !inside;
                
            }
            return inside;
        }
        
        public boolean contains(Bounds bounds) {
            if (!this.bounds.contains(bounds)) return false;
            if (!this.bounds.contains(bounds.getLowerRight())) return false;
            if (!this.bounds.contains(bounds.getUpperLeft())) return false;
            return contains(bounds.getLowerRight()) && contains(bounds.getUpperLeft());
        }
        
        public boolean contains(VoronoiPolygon polygon) {
            /*
             * Luckely we can make the assumption that polygons will not be
             * intersecting, simplifying this algorithm a lot. Now we can also
             * assume that when all vertices (points) are contained within the
             * suspect outer polygon, the suspect outer polygon is in fact the 
             * outer polygon.
             */
            for (QuadEdge edge:polygon.edges) {
                if (!contains(edge.getOrg())) return false;
            }
            return true;
        }
	
	/**
	 * Returns an iterator that traverses the edges of the Polygon. The edges are of
	 * type QuadEdge.
	 * @return
	 * @see net.cingular.nsg.voronoi.QuadEdge
	 */
	public LinkedList<QuadEdge> edges() {
		return edges;
	}

	/**
	 * Returns an iterator that traverses the vertices of the Polygon. There is no way
	 * to know by using this iterator whether a polygon is open or closed.
	 * @return
	 * @see net.cingular.nsg.voronoi.Point
	 */
	/*public Iterator verticesIterator() {
		return new Iterator () {
			private Iterator iterator = edges().iterator();

			public void remove() {
				iterator.remove();
			}

			public boolean hasNext() {
				return iterator.hasNext();
			}

			public Object next() {
				QuadEdge edge = (QuadEdge)iterator.next();
				return edge.getOrg();
			}
		};
	}*/

        public boolean isOuter() {
            if (isInnerOuterDetermined) return !isInnerFlag;
            double area = 0.0;
            for (QuadEdge edge:edges) {
                Point src = edge.getOrg();
                Point dst = edge.getDest();
                area += (src.getY()+dst.getY())*(src.getX()-dst.getX())/2;
            }
            isInnerFlag = (area < 0); // cw ==> area negative ==> Outer Polygon
            isInnerOuterDetermined = true;
            return !isInnerFlag;
        }
        
	/**
	 * clears a flag status. 
	 * The flags are stored on the contained tiangulation 
	 * point. This is the same as the dual origin of any Voronoi Edge.
	 * @see net.cingular.nsg.common.Flagable#clearFlag(int)
	 * @param flag flag to be cleared.
	 */
	public void clearFlag(int flag) {
		((QuadEdge)edges.getFirst()).invrot().getOrg().clearFlag(flag);
	}

	/**
	 * gets the flag status using a bitmask.
	 * The flags are stored on the contained tiangulation 
	 * point. This is the same as the dual origin of any Voronoi Edge.
	 * @see net.cingular.nsg.common.Flagable#getFlag(int)
	 * @param mask bitmask to be used.
	 * @return the flag status.
	 */
	public int getFlag(int mask) {
		return ((QuadEdge)edges.getFirst()).invrot().getOrg().getFlag(mask);
	}

	/**
	 * gets the flag status without using a bitmask. 
	 * The flags are stored on the contained tiangulation 
	 * point. This is the same as the dual origin of any Voronoi Edge.
	 * @see net.cingular.nsg.common.Flagable#getFlags()
	 * @return the flag status.
	 */
	public int getFlags() {
		return ((QuadEdge)edges.getFirst()).invrot().getOrg().getFlags();
	}

	/**
	 * checks wether the bits of the flag are set without using a bitmask.
	 * The flags are stored on the contained tiangulation 
	 * point. This is the same as the dual origin of any Voronoi Edge.
	 * @see net.cingular.nsg.common.Flagable#isFlagSet(int)
	 * @param flag the flag to be checked.
	 * @return true, only the in case the bits for the given flag are set.
	 */
	public boolean isFlagSet(int flag) {
		return ((QuadEdge)edges.getFirst()).invrot().getOrg().isFlagSet(flag);
	}

	/**
	 * checks wether the bits of the flag are set using a bitmask.
	 * The flags are stored on the contained tiangulation 
	 * point. This is the same as the dual origin of any Voronoi Edge.
	 * @see net.cingular.nsg.common.Flagable#isFlagSet(int)
	 * @param flag the flag to be checked.
	 * @param mask the bitmask to be used.
	 * @return true, only in the case the bits for the given flag are set.
	 */
	public boolean isFlagSet(int flag,int mask) {
		return ((QuadEdge)edges.getFirst()).invrot().getOrg().isFlagSet(flag,mask);
	}

	/**
	 * sets the bits of the flag using a bitmask.
	 * The flags are stored on the contained tiangulation 
	 * point. This is the same as the dual origin of any Voronoi Edge.
	 * @see net.cingular.nsg.common.Flagable#setFlag(int, int)
	 * @param flag bits to be set
	 * @param mask bitmask to be used.
	 */
	public void setFlag(int flag, int mask) {
		((QuadEdge)edges.getFirst()).invrot().getOrg().setFlag(flag,mask);
	}

	/**
	 * sets the bits of the flag without using a bitmask.
         * The flags are stored on the contained tiangulation 
	 * point. This is the same as the dual origin of any Voronoi Edge.
	 * @see net.cingular.nsg.common.Flagable#setFlag(int)
	 * @param flag bits to be set
	 */
	public void setFlag(int flag) {
		((QuadEdge)edges.getFirst()).invrot().getOrg().setFlag(flag);
	}
	
//      Do not allow setProperty. Will fail when polygon is still empty ... maybe
//      better implement with its own property container in staid of depending on
//      property of the origin of the first edge within the polygon ...
//	public void setProperty(String key,String value) {
//		((QuadEdge)edges.getFirst()).invrot().getOrg().setProperty(key,value);
//	}

	public String getProperty(String key) {
		return ((QuadEdge)edges.getFirst()).invrot().getOrg().getProperty(key);
	}
}