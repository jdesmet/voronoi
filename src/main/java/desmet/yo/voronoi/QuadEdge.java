package desmet.yo.voronoi;
import desmet.yo.voronoi.common.FlagableImpl;

/* XXX: Flags are implemented at the QuadEdge itself, to allow marking
 * duals and delaunays individualy.
 * to use the flags from this edge's origin, or the origin of this edge's dual.
 */
/**
 * A quad-edge exists only of an origin point, and its duals. The destination point
 * can be retreived by getting the origin of one of its duals.
 * 
 * @author jd3714
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class QuadEdge extends FlagableImpl
{
	
	private int type;
	
	public static final int TYPE_ORIGINAL  = 0;
	public static final int TYPE_ROTATED   = 1;
	public static final int TYPE_SYMMETRIC = 2;
	public static final int TYPE_INVROT    = 3;

	Point org = null;
	
	//public static final int MASK_INTERNAL = 0xFF; // Reserved
	//public static final int MASK_PUBLIC   = ~MASK_INTERNAL;
	//public static final int FLAG_INTERNAL_START = getFlagStartFromMask(MASK_INTERNAL);
	//public static final int FLAG_PUBLIC_START = getFlagStartFromMask(MASK_PUBLIC);
	
	private QuadEdge next = null;
	private QuadEdge dual = null;

	//public int index = -1;
	
	//private static int s_index = 0;
	
	protected QuadEdge(int type) {
		super();
		this.type = type;
		//this.index = s_index++;
	}
	
	public QuadEdge() {
		this(TYPE_ORIGINAL);
		QuadEdge e0 = this;
		QuadEdge e1 = new QuadEdge(TYPE_ROTATED);
		QuadEdge e2 = new QuadEdge(TYPE_SYMMETRIC);
		QuadEdge e3 = new QuadEdge(TYPE_INVROT);
		
		e0.setRot(e1);
		e1.setRot(e2);
		e2.setRot(e3);
		e3.setRot(e0);
		e0.setOnext(e0);
		e1.setOnext(e3);
		e2.setOnext(e2);
		e3.setOnext(e1);
	}
        
        public boolean isLeftRegionEdge(RegionFilter filter) {
                String lRegion = filter.getRegionName(this.invrot().getOrg());
                String rRegion = filter.getRegionName(this.rot().getOrg());
                boolean lOpen = this.invrot().getOrg().isFlagSet(Triangulation.FLAG_POINT_INFINITE);
                boolean rOpen = this.rot().getOrg().isFlagSet(Triangulation.FLAG_POINT_INFINITE);
                if (!lOpen && rOpen) return true;
                if (lOpen) return false;
                if (lRegion == null && rRegion != null || lRegion != null && rRegion == null) return true;
                if (lRegion == null && rRegion == null) return false;
                return !lRegion.equals(rRegion);
        }
	
        public boolean isRightRegionEdge(RegionFilter filter) {
                String lRegion = filter.getRegionName(this.invrot().getOrg());
                String rRegion = filter.getRegionName(this.rot().getOrg());
                boolean lOpen = this.invrot().getOrg().isFlagSet(Triangulation.FLAG_POINT_INFINITE);
                boolean rOpen = this.rot().getOrg().isFlagSet(Triangulation.FLAG_POINT_INFINITE);
                if (lOpen && !rOpen) return true;
                if (rOpen) return false;
                if (lRegion == null && rRegion != null || lRegion != null && rRegion == null) return true;
                if (lRegion == null && rRegion == null) return false;
                return !lRegion.equals(rRegion);
        }
	
	/**
	 * set the Origin
	 * @param pt
	 */
	public void setOrg (Point pt) {
		org = pt;
	}

	/**
	 * set the Destinatoin. The Destination is set by setting the origin of its Symetric-Dual origin.
	 * @param pt
	 */
	public void setDest (Point pt) {
		dual.dual.setOrg(pt);
	}

	/**
	 * set the Rotated. Use this only to initialize the
	 * QuadEdge the first time.
	 * @param e
	 */
	public void setRot (QuadEdge e) {
		dual = e;
	}

	/**
	 * set the Symmetrical (inverse). Use this only to initialize the
	 * QuadEdge the first time.
	 * @param e
	 */
	public void setSym (QuadEdge e) {
		dual.dual = e;
	}

	/**
	 * set the Inverse Rotated. Use this only to initialize the
	 * QuadEdge the first time.
	 * @param e
	 */
	public void setInvrot (QuadEdge e) {
		dual.dual.dual = e;
	}

	public void setOnext (QuadEdge e) {
		next = e;
	}

	public void setLnext (QuadEdge e) {
		dual.dual.dual.next.dual = e;
	}

	public void setOprev (QuadEdge e) {
		dual.next.dual = e;
	}

	public void setRnext (QuadEdge e) {
		dual.next.dual.dual.dual = e;
	}

	public Point getOrg() {
		return org;
	}

	public Point getDest() {
		return dual.dual.org;
	}

	public QuadEdge rot() {
		return dual;
	}

	public QuadEdge sym() {
		return dual.dual;
	}

	public QuadEdge invrot() {
		return dual.dual.dual;
	}

	public QuadEdge onext() {
		return next;
	}

	public QuadEdge lnext() {
		return dual.dual.dual.next.dual;
	}

	public QuadEdge oprev() {
		return dual.next.dual;
	}

//	public QuadEdge rnext() {
//		return dual.next.dual.dual.dual;
//	}

	public QuadEdge dprev() {
		return dual.dual.dual.next.dual.dual.dual;
	}

	public QuadEdge lprev() {
		return next.dual.dual;
	}

//	public boolean isInfinite() {
//		return (getOrg() == null) || (getOrg().isInfinite()) || (getDest() == null) || (getDest().isInfinite());
//	}
	
	/**
	 * @return Returns the dual.
	 */
	public QuadEdge dual() {
		return dual;
	}
	/**
	 * @return Returns the next.
	 */
	public QuadEdge next() {
		return next;
	}
	/**
	 * @return Returns the type.
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(int type) {
		this.type = type;
	}
	
	/*
	 * Basic QuadEdge Operators:
	 * 
	 *   dual: pointer to the dual edge (assume rotated 90 degrees ccw)
	 *   next: pointer to next edge ccw around the origin
	 *   
	 * Composed Operators:
	 * 
	 *   rot = dual
	 * 
	 *   sym = dual.dual 
	 *     symmetrical edge, or edge in reverse direction (180 degree turn).
	 *     
	 *   onext = next
	 *     next edge (ccw) around origin.
	 *     
	 *   oprev = dual.next.dual
	 *     previous edge (cw) around origin
	 *    
	 *   dnext = sym.onext.sym
	 *   
	 *   dprev = dual.dual.dual.next.dual.dual.dual = invrot.onext.invrot = sym.oprev.sym
	 *     previous edge (cw) around destination
	 *     
	 *   lnext = dual.dual.dual.next.dual = invrot.onext.rot = sym.oprev
	 *     next edge (ccw) around Left Face
	 *     
	 *   lprev = next.dual.dual = onext.sym
	 *     previous edge (cw) around Left Face
	 *     
	 *   rnext = dual.next.dual.dual.dual = rot.onext.invrot = oprev.sym
	 *     next edge (ccw) around Right Face
	 *     
	 *   rprev = dual.dual.next = sym.onext
	 *     prev edge (cw) around Right Face
	 *     
	 */
	
	
	/*	
	 *  Relations Between Edges: Edge Algebra
	 *
	 *	Elements in Edge Algebra: 
	 *	Each of 8 ways to look at each edge
	 *
	 *	Operators in Edge Algebra: 
	 *	Rot: Bug rotates 90 degrees to its right 
	 *	Sym: Bug turns around 180 degrees 
	 *	Flip: Bug flips up-side down 
	 *	Onext: Bug rotates CCW about its origin (either Vertex or Face) 
	 *
	 *	Some Properties of Flip, Rot, and Onext: 
	 *	e Rot4 = e 
	 *	e Rot2 != e 
	 *	e Flip2 = e 
	 *	e Flip Rot Flip Rot = e 
	 *	e Rot Flip Rot Flip = e 
	 *	e Rot Onext Rot Onext = e 
	 *	e Flip Onext Flip Onext = e 
	 *
	 *	Properties of Edge Algebra deduced from those above: 
	 *	e Flip-1 = e Flip 
	 *	e Sym = e Rot2 
	 *	e Rot-1 = e Rot3 
	 *	e Rot-1 = e Flip Rot Flip 
	 *	e Onext-1 = e Rot Onext Rot 
	 *	e Onext-1 = e Flip Onext Flip 
	 *
	 *	Other Useful Definitions: 
	 *	e Lnext = e Rot-1 Onext Rot 
	 *	e Rnext = e Rot Onext Rot-1 
	 *	e Dnext = e Sym Onext Sym
	 *
	 *	Even More Useful Definitions: 
	 *	e Oprev = e Onext-1 = e Rot Onext Rot 
	 *	e Lprev = e Lnext-1 = e Onext Sym 
	 *	e Rprev = e Rnext-1 = e Sym Onext 
	 *	e Dprev = e Dnext-1 = e Rot-1 Onext Rot-1	
	 */
	
//	public void print(String prefix) {
//		System.out.println(prefix);
//		System.out.println(" > "+getCoordName()+" = "+getCoordString());
//		System.out.println(" > "+getCoordName()+".sym = "+sym().getCoordName()+" = "+sym().getCoordString());
//		System.out.println(" > "+getCoordName()+".onext = "+onext().getCoordName()+" = "+onext().getCoordString());
//		System.out.println(" > "+getCoordName()+".oprev = "+oprev().getCoordName()+" = "+oprev().getCoordString());
//		System.out.println(" > "+getCoordName()+".lnext = "+lnext().getCoordName()+" = "+lnext().getCoordString());
//		System.out.println(" > "+getCoordName()+".lprev = "+lprev().getCoordName()+" = "+lprev().getCoordString());
//	}
	
//	public String getCoordName() {
//		return "e"+index;
//	}
//	
	public String getCoordString() {
		if ((getOrg() != null) || (getDest() != null)) {
			return "(" + getOrg().getX() + ", " + getOrg().getY() + ") - (" + getDest().getX() + ", " + getDest().getY() + ")";
		} else {
			return "(null)";
		}
	}
}