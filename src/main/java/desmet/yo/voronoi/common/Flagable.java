/*
 * Created on Sep 23, 2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package desmet.yo.voronoi.common;

public interface Flagable {

	/**
	 * clears a flag status. 
	 * @see net.cingular.nsg.common.Flagable#clearFlag(int)
	 * @param flag flag to be cleared.
	 */
	public void clearFlag(int flag);

	/**
	 * gets the flag status using a bitmask.
	 * @see net.cingular.nsg.common.Flagable#getFlag(int)
	 * @param mask bitmask to be used.
	 * @return the flag status.
	 */
	public int getFlag(int mask);

	/**
	 * gets the flag status without using a bitmask. 
	 * @see net.cingular.nsg.common.Flagable#getFlags()
	 * @return the flag status.
	 */
	public int getFlags();

	/**
	 * checks wether the bits of the flag are set without using a bitmask.
	 * @see net.cingular.nsg.common.Flagable#isFlagSet(int)
	 * @param flag the flag to be checked.
	 * @return true, only the in case the bits for the given flag are set.
	 */
	public boolean isFlagSet(int flag);

	/**
	 * checks wether the bits of the flag are set using a bitmask.
	 * @see net.cingular.nsg.common.Flagable#isFlagSet(int)
	 * @param flag the flag to be checked.
	 * @param mask the bitmask to be used.
	 * @return true, only in the case the bits for the given flag are set.
	 */
	public boolean isFlagSet(int flag,int mask);

	/**
	 * sets the bits of the flag using a bitmask.
	 * @see net.cingular.nsg.common.Flagable#setFlag(int, int)
	 * @param flag bits to be set
	 * @param mask bitmask to be used.
	 */
	public void setFlag(int flag, int mask);

	/**
	 * sets the bits of the flag without using a bitmask.
	 * @see net.cingular.nsg.common.Flagable#setFlag(int)
	 * @param flag bits to be set
	 */
	public void setFlag(int flag);

}