
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JSlider;

import javax.swing.BoxLayout;
import java.awt.BorderLayout;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import java.util.ArrayList;
import java.util.List;

public class URPaint extends JFrame 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//size of objects will remain constant.
    private static final int rlength= 20;
    
	//Implementing MVC architecture 
    //Model Shape (Rect & Circle  - Model)
    List<Shape> shapes = null;
    //ControlPanel (issue commands) and PaintPanel (draw objects) - View
    ControlPanel controlPanel = null;
    PaintPanel paintPanel = null;
    //Controller - Controller
    Controller controller = null;
    
    abstract class Shape 
    {
    	//ID of the shape to track it.
    	int shapeID;
    	//Define if shape is selected or no
    	boolean active = false;
    	// location (posx, posy), size of the shape
		double posx, posy, length;
		//Shape is Circle or square
		String shapeName = "";
		//color of the shape
		Color color = null;
						
		public Shape(int shapeID, String shapeName, int posx, int posy, Color color) {
			this.shapeID = shapeID;
			this.shapeName = shapeName;
			this.posx = posx;
		    this.posy = posy;
		    this.length = rlength;
		    this.color = color;
		}
		public double getPosx() {
			return posx;
		}
		public void setPosx(double posx) {
			this.posx = posx;
		}
		public double getPosy() {
			return posy;
		}
		public void setPosy(double posy) {
			this.posy = posy;
		}
		public Color getColor() {
			return color;
		}
		public void setColor(Color color) {
			this.color = color;
		}
		
		public int getShapeID() {
			return shapeID;
		}
		public String getShapeName() {
			return shapeName;
		}
		public void setShapeName(String shapeName) {
			this.shapeName = shapeName;
		}
		public boolean isActive() {
			return active;
		}
		public void setActive(boolean active) {
			this.active = active;
		}
		public void setShapeID(int shapeID) {
			this.shapeID = shapeID;
		}
		abstract void paintComponent(Graphics g);
				
    }    
    private class Rect extends Shape
    {
		public Rect(int shapeID, String shapeName, int posx, int posy, Color c) 
		{
			super(shapeID,shapeName, posx, posy, c);
		    
		}
		void paintComponent(Graphics g)
		{
			
			g.setColor(color);
			g.fillRect((int)posx, (int)posy, (int)length, (int)length);
			//highlight active object
			if(isActive())
			{
				g.setColor(color.brighter());
				g.drawRect((int)posx, (int)posy, (int)length, (int)length);
			}
			
		}
    }
    private class Circle extends Shape
    {
		public Circle(int shapeID,String shapeName, int posx, int posy, Color c) 
		{
			super(shapeID,shapeName, posx, posy, c);
		}
		void paintComponent(Graphics g)
		{
			
			g.setColor(color);
			g.fillOval((int)posx, (int)posy, (int)length, (int)length);
			//highlight active object
			if(isActive())
			{
				g.setColor(color.brighter());
				g.drawOval((int)posx, (int)posy, (int)length, (int)length);
			}
		}			
    }		    
   
	class ControlPanel extends JPanel 
	{

	  /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	private Color background;
	  	  
	  JPanel buttonPanel = new JPanel();
	  JButton circle = new JButton("circle");
	  JButton square = new JButton("square");
	  JButton clear = new JButton("clear");
	  
	  JPanel sliderPanel = new JPanel();	  
	  JSlider red =  new JSlider(JSlider.HORIZONTAL, 0, 255, 0);	  
	  JSlider green = new JSlider(JSlider.HORIZONTAL, 0, 255, 255);
	  JSlider blue = new JSlider(JSlider.HORIZONTAL, 0, 255, 0);
	  
	  Controller controller = null;
	  
	  Action action = new Action();
	  
	  public ControlPanel (Color background, Controller controller) 
	  {	     
		  setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		 
		  this.controller = controller;
		  buttonPanel.add(circle);
		  circle.addActionListener(action);
		  buttonPanel.add(square);
		  square.addActionListener(action);
		  buttonPanel.add(clear);
		  add(buttonPanel);
		  clear.addActionListener(action);
		  
		  red.setMajorTickSpacing(50);  
		  red.setPaintTicks(true);  
		  red.setPaintLabels(true);  
		  red.addChangeListener(action);
				  		  
		  green.setMajorTickSpacing(50);  
		  green.setPaintTicks(true);  
		  green.setPaintLabels(true);
		  green.addChangeListener(action);
		  
		  blue.setMajorTickSpacing(50);  
		  blue.setPaintTicks(true);
		  blue.setPaintLabels(true);
		  blue.addChangeListener(action);

		  sliderPanel.add(new JLabel("R"));
		  sliderPanel.add(red);
		  sliderPanel.add(new JLabel("G"));
		  sliderPanel.add(green);
		  sliderPanel.add(new JLabel("B"));
		  sliderPanel.add(blue);
		  add(sliderPanel);
		  
	  }	  	  
	  
	  public void paintComponent (Graphics g)
	  {
		  super.paintComponent(g);	    
		  setBackground(background);
	  }
	  
	  //Delegate method handle events generated by clicking buttons (circle, square and clear), 
	  //Implement events related to changes in slider by setting right color for the shapes.
	  private class Action implements ActionListener, ChangeListener
	  {
			
			@Override
		    public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand().equals("clear"))
					controller.clear();
				
				else
					controller.setShapeType(e.getActionCommand());
			}

			@Override
			public void stateChanged(ChangeEvent e) {
		    	Color c = new Color(red.getValue(), green.getValue(), blue.getValue());
		    	controller.setColor(c);
				
			}
		}
	}

	class PaintPanel extends JPanel
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 6010095971265546207L;
		private Color color = new Color(0,0,0);
	    private String dbgMsg;
	    private Shape activeShape = null;
	    Controller controller = null;
		List<Shape> shapes;
	   
	 
		public PaintPanel(Color background, Controller controller) 
	    {
	    	this.controller = controller;
			setBackground(background);
			
			//Create and Add listener
			AddandMoveObject addAndMoveObjects= new AddandMoveObject();
			addMouseListener(addAndMoveObjects);			
			addMouseMotionListener(addAndMoveObjects);
			
			dbgMsg = "Nothing yet.";
			setFocusable(true);
	    }

	    public Color getColor() {
			return color;
		}

		public void setColor(Color color) {
			this.color = color;
		}

		public Shape getActiveShape() {
			return activeShape;
		}

		public void setActiveShape(Shape activeShape) {
			this.activeShape = activeShape;
		}

		public String getDbgMsg() {
			return dbgMsg;
		}

		public void setDbgMsg(String dbgMsg) {
			this.dbgMsg = dbgMsg;
		}

		public List<Shape> getShapes() {
			return shapes;
		}

		public void setShapes(List<Shape> shapes) {
			this.shapes = shapes;
		}

		public void paintComponent(Graphics g) 
	    {
			super.paintComponent(g);
			g.setColor(Color.GREEN);	    	
			for (Shape r : shapes) 
			{

			    r.paintComponent(g);
			    
			}
	
			if (dbgMsg != null) 
			{
			    int fontScale = 10;
			    int xLoc = (getWidth() - fontScale * dbgMsg.length()) / 2; // middle
			    int yLoc = getHeight() - fontScale;
			    ((Graphics2D) g).drawString(dbgMsg, xLoc, yLoc);
			}	    
	    }	
		
		//Delegate method that implements events generated from mouse (click, press, release, move)
		private class AddandMoveObject implements MouseListener, MouseWheelListener, MouseMotionListener 
	    {
			public void mouseClicked(MouseEvent e) {
				controller.addShape(e.getX(),e.getY());
				repaint();
			}
			public void mouseEntered(MouseEvent e) {
			}
			public void mouseExited(MouseEvent e) {
				
			}
			public void mousePressed(MouseEvent e) {
				activeShape = controller.activeShape(e.getX(), e.getY());
				if(activeShape != null)
				{
					dbgMsg = String.format("Moving %s(%d),at <%d,%d>.%n",activeShape.getShapeName(), (int)activeShape.shapeID, (int)activeShape.posx, (int)activeShape.posy);
					repaint();
				}
				
			}
			public void mouseReleased(MouseEvent e) {
				if(activeShape != null)
				{
					controller.releaseActiveShape(activeShape);
					dbgMsg = String.format("Moved %s(%d),at <%d,%d>.%n",activeShape.getShapeName(), (int)activeShape.shapeID, (int)activeShape.posx, (int)activeShape.posy);
					repaint();
					activeShape.active = false;
					activeShape = null;					
				}
			}	
	    	public void mouseWheelMoved(MouseWheelEvent e) {   		
	    		moveShape(e.getX() - 10, e.getY() - 10);
	    	 }
			public void mouseDragged(MouseEvent e) {
	    		moveShape(e.getX() - 10, e.getY() - 10);				
			}
			public void mouseMoved(MouseEvent e) {
	    		moveShape(e.getX() - 10, e.getY() - 10);		
			}
			void moveShape(int x, int y)
			{
	    	    if(activeShape != null)
	    	    {
	    	    	activeShape.setPosx(x);
	    	    	activeShape.setPosy(y);
	    	    	repaint();
	    	    }			
				
			}			
		} 

	}
	
	public class Controller 
	{
		//Model is List of shapes and active shape
		List<Shape> shapes;
    	Shape active = null;
    	
		//Default shape is Circle 
		String shapeType = "circle";		
		//Default color of shape is Green
		Color color = new Color(0,255,0);
		
		//View
		ControlPanel controlPanel = null;
		PaintPanel paintPanel = null;
		
		public Controller() {
			super();
		}
		
		//add shape to shape model(array list)
		public void addShape(int posx, int posy)
		{
			int shapeID = shapes.size() + 1;
			
			if(shapeType.equals("circle")) //draw circle
				shapes.add((Shape)new Circle(shapeID,shapeType,  posx - 10,posy - 10, getColor()));	
			if(shapeType.equals("square")) //draw square
				shapes.add((Shape)new Rect(shapeID,shapeType, posx -10, posy - 10, getColor()));
			paintPanel.dbgMsg = String.format("Adding %s(%d),at <%d,%d>.%n",shapeType, shapeID, posx, posy);
		}
		
		//clear all shape, initialize paint panel to original settings.
		public void clear()
		{
			shapes.clear();
			paintPanel.setBackground(Color.BLACK);
			paintPanel.dbgMsg = "Nothing yet.";
			repaint();
		}

		//Determine shape is active by measuring distance between center of the shape and current mouse position.
		//If the distance is less than than size of objects (x^2 - y^2) < rlength it means the object is active object. 
	    public Shape activeShape(int x, int y)
	    {
			for (Shape r : shapes) 
			{
				if(Math.sqrt((r.getPosx() - x)*(r.getPosx() - x) + (r.getPosy() - y)*(r.getPosy() - y) ) < rlength)
				{
					r.active = true;
					active = r;
				}
			    
			}
			return active;
	    }
	    
	    //Release the active shape and remove it from the eco system.
	    public void releaseActiveShape(Shape active)
	    {
			active.active = false;
			active = null;		
	    }

		public Color getColor() {
			return color;
		}

		public void setColor(Color color) {
			this.color = color;
		}

		public List<Shape> getShapes() {
			return shapes;
		}

		public void setShapes(List<Shape> shapes) {
			this.shapes = shapes;
		}

		public ControlPanel getControlPanel() {
			return controlPanel;
		}

		public void setControlPanel(ControlPanel controlPanel) {
			this.controlPanel = controlPanel;
		}

		public PaintPanel getPaintPanel() {
			return paintPanel;
		}

		public void setPaintPanel(PaintPanel paintPanel) {
			this.paintPanel = paintPanel;
		}

		public String getShapeType() {
			return shapeType;
		}

		public void setShapeType(String shapeType) {
			this.shapeType = shapeType;
		}
	
	}
	
	public void create()
	{
		//Set JFrame properties
		Color c = new Color(22);
		setTitle("PaintHW");
		setSize(750,750);
		
		//This is a MVC architecture Model View Controller
	    //Model - Collection of shapes (circle, square)
	    //List<Shape> shapes;
		shapes = new ArrayList<>();
		
		//Controller
		controller = new Controller();
		
		//View Classes ControlPanel and Paint Panel
		//ControlPanel control panel - All the commands will be issued from ControlPanel
		controlPanel = new ControlPanel(c, controller);
		//PaintPanel - Where circles, squares  are created and moved around
		//Create panel with black background
		paintPanel = new PaintPanel(Color.black, controller);
		
		//Initialize controller with model and view
		controller.shapes = shapes;
		controller.controlPanel = (ControlPanel) controlPanel;
		controller.paintPanel = (PaintPanel) paintPanel;
		paintPanel.shapes = shapes;
		
		//Add panels to JFrame and create view
		add(controlPanel, BorderLayout.PAGE_START);
		add(paintPanel, BorderLayout.CENTER);
		setVisible(true); 
	}

	public void delete()
	{
		controller.clear();
		shapes = null;
		controlPanel = null;
		paintPanel = null;	
		controller = null;
	}

	public static void main(String[] args)
	{
		URPaint paint = new URPaint();	 
		paint.create();
		
		//Delete all the objects when application is closed
		paint.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	paint.delete();
		    }
		});		
	}
}
