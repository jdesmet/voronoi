/*
 * Created on Sep 15, 2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package desmet.yo.voronoi.test;

import java.awt.BorderLayout;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;

import desmet.yo.voronoi.Triangulation;


public class VoronoiVisualiser extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8076405882528889503L;
	private JPanel jContentPane = null;
	private VoronoiPanel jDrawPanel = null;
	private JPanel jPanel = null;
	private JButton jButton = null;

	/**
	 * This is the default constructor
	 */
	public VoronoiVisualiser() {
		super();
		initialize();
	}

	public void setViewPort(double offsetx,double offsety,double scalex,double scaley) {
		jDrawPanel.setViewPort(offsetx, offsety, scalex, scaley);
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(381, 318);
		this.setContentPane(getJContentPane());
		this.setTitle("JFrame");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJDrawPanel(), java.awt.BorderLayout.CENTER);
			jContentPane.add(getJPanel(), java.awt.BorderLayout.EAST);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jDrawPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJDrawPanel() {
		if (jDrawPanel == null) {
			jDrawPanel = new VoronoiPanel();
		}
		return jDrawPanel;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.add(getJButton(), null);
		}
		return jPanel;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setText("Refresh");
		}
		return jButton;
	}
	
	public void setTriangulation(Triangulation triangulation) {
		this.jDrawPanel.setTriangulation(triangulation);
	}

	public void setPoints(Vector points) {
		this.jDrawPanel.setPoints(points);
		
	}
	


}  //  @jve:decl-index=0:visual-constraint="22,30"
