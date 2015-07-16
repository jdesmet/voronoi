/*
 * Created on May 11, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package desmet.yo.voronoi.common;

import java.util.logging.Handler;
import java.util.logging.Logger;

/**
 * @author jd3714
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public abstract class LogHandler extends Handler {
	private String context = null;
	private String context2 = null;
	private String context3 = null;
	private String context4 = null;
	private String context5 = null;
	private int currentRun = 0;

	/**
	 * @return Returns the context.
	 */
	public String getContext() {
		return context;
	}
	/**
	 * @param context The context to set.
	 */
	public void setContext(String context) {
		this.context = context;
	}
	
	/**
	 * @return Returns the context2.
	 */
	public String getContext2() {
		return context2;
	}
	/**
	 * @param context2 The context2 to set.
	 */
	public void setContext2(String context2) {
		this.context2 = context2;
	}
	/**
	 * @return Returns the context3.
	 */
	public String getContext3() {
		return context3;
	}
	/**
	 * @param context3 The context3 to set.
	 */
	public void setContext3(String context3) {
		this.context3 = context3;
	}

	public abstract int nextRun();
	
	/**
	 * @return Returns the currentRun.
	 */
	public int getCurrentRun() {
		return currentRun;
	}
	/**
	 * @param currentRun The currentRun to set.
	 */
	protected void setCurrentRun(int currentRun) {
		this.currentRun = currentRun;
	}
	
	/**
	 * Helper Function to set the context on all Handlers of type
	 * com.mobile.util.LogHandler.
	 * @param logger the logger to use for iterating the Handlers through.
	 * @param context the context string to set.
	 */
	static public void setContext(Logger logger,String context) {
		Handler [] handlers = logger.getHandlers();
		for (int i = 0; i < handlers.length; i++)
			if (handlers[i] instanceof LogHandler) ((LogHandler)handlers[i]).setContext(context);
	}
	/**
	 * Helper Function to set the context on all Handlers of type
	 * com.mobile.util.LogHandler.
	 * @param logger the logger to use for iterating the Handlers through.
	 * @param context the context string to set.
	 */
	static public void setContext2(Logger logger,String context2) {
		Handler [] handlers = logger.getHandlers();
		for (int i = 0; i < handlers.length; i++)
			if (handlers[i] instanceof LogHandler) ((LogHandler)handlers[i]).setContext2(context2);
	}
	/**
	 * Helper Function to set the context on all Handlers of type
	 * com.mobile.util.LogHandler.
	 * @param logger the logger to use for iterating the Handlers through.
	 * @param context the context string to set.
	 */
	static public void setContext3(Logger logger,String context3) {
		Handler [] handlers = logger.getHandlers();
		for (int i = 0; i < handlers.length; i++)
			if (handlers[i] instanceof LogHandler) ((LogHandler)handlers[i]).setContext3(context3);
	}
	/**
	 * Helper Function to set the context on all Handlers of type
	 * com.mobile.util.LogHandler.
	 * @param logger the logger to use for iterating the Handlers through.
	 * @param context the context string to set.
	 */
	static public void setContext4(Logger logger,String context4) {
		Handler [] handlers = logger.getHandlers();
		for (int i = 0; i < handlers.length; i++)
			if (handlers[i] instanceof LogHandler) ((LogHandler)handlers[i]).setContext4(context4);
	}
	/**
	 * Helper Function to set the context on all Handlers of type
	 * com.mobile.util.LogHandler.
	 * @param logger the logger to use for iterating the Handlers through.
	 * @param context the context string to set.
	 */
	static public void setContext5(Logger logger,String context5) {
		Handler [] handlers = logger.getHandlers();
		for (int i = 0; i < handlers.length; i++)
			if (handlers[i] instanceof LogHandler) ((LogHandler)handlers[i]).setContext5(context5);
	}
	/**
	 * @return Returns the context4.
	 */
	public String getContext4() {
		return context4;
	}
	/**
	 * @param context4 The context4 to set.
	 */
	public void setContext4(String context4) {
		this.context4 = context4;
	}
	/**
	 * @return Returns the context5.
	 */
	public String getContext5() {
		return context5;
	}
	/**
	 * @param context5 The context5 to set.
	 */
	public void setContext5(String context5) {
		this.context5 = context5;
	}
}
