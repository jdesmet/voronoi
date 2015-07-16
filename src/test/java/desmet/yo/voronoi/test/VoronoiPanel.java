/*
 * Created on Sep 15, 2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package desmet.yo.voronoi.test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.Iterator;
import java.util.Vector;


import javax.swing.JPanel;

import desmet.yo.voronoi.Point;
import desmet.yo.voronoi.VoronoiPolygon;
import desmet.yo.voronoi.QuadEdge;
import desmet.yo.voronoi.Triangulation;

public class VoronoiPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3169934243814043533L;
	Vector points=null;
	Vector<VoronoiPolygon> voronoiPolygons=null;
	Vector voronoiEdges=null;
	Vector delaunayEdges=null;
	Triangulation tri = null;
	
	//private double scalex = 500;
	//private double scaley = -500;
	private double scalex = 20;
	private double scaley = -20;
	private double offsetx = 122.009224; //longitude
	private double offsety = -47.608307; //latitude

	public void setViewPort(double offsetx,double offsety,double scalex,double scaley) {
		this.scalex = scalex;
		this.scaley = scaley;
		this.offsetx = offsetx;
		this.offsety = offsety;
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		
	    Insets insets = getInsets();
	    
	    Graphics2D g2d = (Graphics2D)g.create();

	    Rectangle clipRect = new Rectangle();
		g.getClipBounds(clipRect);
		
		
		int sizex = getWidth() - insets.right - insets.left - 1;
		int sizey = getHeight() - insets.bottom - insets.top - 1;
		int centerx = (sizex)/2;
		int centery = (sizey)/2;
		
		//Vector polygons = getTriangulation().buildVoronoiPolygons();
		
		if (points != null) {
			Iterator i = points.iterator();
			while (i.hasNext()) {
				Point p = (Point)i.next();
				g2d.fill(new Ellipse2D.Double(scalex*(p.getX()+offsetx)+centerx-3,scaley*(p.getY()+offsety)+centery-3,6,6));
			}
		}
		
		if (voronoiPolygons != null) {
			Iterator i = voronoiPolygons.iterator();
			while (i.hasNext()) {
				VoronoiPolygon polygon = (VoronoiPolygon)i.next();
				Iterator j = polygon.edges().iterator();
				Point f = null;
				Point t = null;
				QuadEdge edge = null;
				while (j.hasNext()) {
					edge = (QuadEdge)j.next();
					f = edge.getOrg();
					t = edge.getDest();
					g2d.draw(new Line2D.Double(
							scalex*(f.getX()+offsetx)+centerx, 
							scaley*(f.getY()+offsety)+centery, 
							scalex*(t.getX()+offsetx)+centerx, 
							scaley*(t.getY()+offsety)+centery
							));
					f = t;
					
				}
			}
		} 
		
		if (voronoiEdges != null) {
			Iterator i = voronoiEdges.iterator();
			g2d.setColor(Color.blue);
			while (i.hasNext()) {
				QuadEdge edge = (QuadEdge)i.next();
				//if (edge.isInfinite()) continue;
				double fx = edge.getOrg().getX();
				double fy = edge.getOrg().getY();
				double tx = edge.getDest().getX();
				double ty = edge.getDest().getY();
				g2d.draw(new Line2D.Double(
						scalex*(fx+offsetx)+centerx, 
						scaley*(fy+offsety)+centery, 
						scalex*(tx+offsetx)+centerx, 
						scaley*(ty+offsety)+centery
						));
				//double x = fx + (tx-fx)/2;
				//double y = fy + (ty-fy)/2;
				//g2d.drawString(edge.getCoordName(),(float)(scalex*(x+offsetx)+centerx),(float)(scaley*(y+offsety)+centery));
			}
		}
		
		if (delaunayEdges != null) {
			Iterator i = delaunayEdges.iterator();
			g2d.setColor(Color.red);
			while (i.hasNext()) {
				QuadEdge edge = (QuadEdge)i.next();
				//if (edge.isInfinite()) continue;
				double fx = edge.getOrg().getX();
				double fy = edge.getOrg().getY();
				double tx = edge.getDest().getX();
				double ty = edge.getDest().getY();
				g2d.draw(new Line2D.Double(
						scalex*(fx+offsetx)+centerx, 
						scaley*(fy+offsety)+centery, 
						scalex*(tx+offsetx)+centerx, 
						scaley*(ty+offsety)+centery
						));
				//double x = fx + (tx-fx)/2;
				//double y = fy + (ty-fy)/2;
				//g2d.drawString(edge.getCoordName(),(float)(scalex*(x+offsetx)+centerx),(float)(scaley*(y+offsety)+centery));
			}
		}
		
		//{
		//	g2d.draw(new Line2D.Double(0, 0, sizex, sizey));
		//	g2d.draw(new Line2D.Double(sizex, 0, 0, sizey));
		//}
		g2d.dispose(); //clean up
	}
	
	/**
	 * This is the default constructor
	 */
	public VoronoiPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(422, 329);
	}

	/**
	 * @param triangulation The triangulation to set.
	 */
	public void setTriangulation(Triangulation triangulation) {
		this.voronoiPolygons = triangulation.getVoronoiPolygons();
		//this.voronoiEdges = triangulation.getVoronoiEdges();
		this.delaunayEdges = triangulation.getDelaunayEdges();
		this.tri = triangulation;
	}
	
	public void setPoints(Vector points) {
		//this.points = points;
	}


}  //  @jve:decl-index=0:visual-constraint="10,10"
