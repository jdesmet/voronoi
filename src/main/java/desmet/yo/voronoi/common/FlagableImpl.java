/*
 * Created on Aug 3, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */

package desmet.yo.voronoi.common;

/**
 * @author jd3714
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class FlagableImpl implements Flagable {
    // NOTE: XXX: Flags are for temporary use only, they 
	// will never be persisted to file or database.
    private int flags = 0;
    
    public static int getFlagStartFromMask(int mask) {
    	return ((~mask)+1) & mask;
    }
	/* (non-Javadoc)
	 * @see net.cingular.nsg.common.Flagable#getFlags()
	 */
	public int getFlags() {
		return this.flags;
	}
	/* (non-Javadoc)
	 * @see net.cingular.nsg.common.Flagable#getFlag(int)
	 */
	public int getFlag(int mask) {
		return (this.flags & mask);
	}
	
	/* (non-Javadoc)
	 * @see net.cingular.nsg.common.Flagable#isFlagSet(int)
	 */
	public boolean isFlagSet(int flag) {
		return (this.flags & flag) != 0;
	}
	
	/* (non-Javadoc)
	 * @see net.cingular.nsg.common.Flagable#isFlagSet(int)
	 */
	public boolean isFlagSet(int flag,int mask) {
		return ((this.flags & mask) & flag) != 0;
	}
	
	/* (non-Javadoc)
	 * @see net.cingular.nsg.common.Flagable#setFlag(int)
	 */
	public void setFlag(int flag) {
		this.flags = this.flags | flag;
	}
	
	/* (non-Javadoc)
	 * @see net.cingular.nsg.common.Flagable#clearFlag(int)
	 */
	public void clearFlag(int flag) {
		this.flags = this.flags & (~flag);
	}
	
	/* (non-Javadoc)
	 * @see net.cingular.nsg.common.Flagable#setFlag(int, int)
	 */
	public void setFlag(int flag,int mask) {
		this.flags = (this.flags & (~mask)) | flag;
	}


}
