package gfx_panel;
import gfx_panel.Snapper.SnapResults;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;


import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;



public class GfxPanel {

	private ArrayList<Shape> shp_arr;
	private Frame awt_frame;
	private int width,height,ini_x,ini_y;
	private boolean drawing_now;
	private Graphics gfx;
	private Shape shp_being_drawn,shp_selected,shp_to_draw;
	private ArrayList<GfxPanel_Notifiee> arr_notifiee;
	protected boolean moving/*,snapped_x,snapped_y*/;
	protected boolean resizing_x_right;
	protected boolean resizing_y_down;
	protected boolean resizing_x_left;
	protected boolean resizing_y_up;
	private Composite composite;
	private Composite awt_composite;
	private int cursor_pos_in_shp_x;
	private int cursor_pos_in_shp_y;
	private int[] bg;
	private BufferedImage bg_buff_img;
	private int img_w,img_h;
	private Button btn_add_rect_shape;
	private Button btn_add_circle_shape;
	private Button chk_enable_snap;
	private Point current_click_pos;
	private Snapper snapper;

	private void createMainComposite(final Shell parent_shell,Composite parent,int w,int h)
	{
		composite = new Composite(parent, 0);
		composite.setLayout(null);
		composite.setBounds(new Rectangle(0, 0, w, h));
	}

	private int generateNewShapeNumber()
	{
		if(shp_arr.size()==0) return 0;

		int[] taken_numbers= new int[shp_arr.size()];
		for(int y=0;y<shp_arr.size();y++)
			taken_numbers[y]=shp_arr.get(y).getShape_number();

		iterate_i:
			for(int i=0;i<1000;i++) //we can handle up to 1000 of shapes
			{
				for(int a:taken_numbers)
				{
					if(a==i) //this i is rejected .. go to next i
						continue iterate_i;
				}
				return i;
			}
		return -1;
	}

	private void initializeBackground(int rgb_value)
	{
		for(int i=0;i<bg.length;i++)
		{
			bg[i]= rgb_value;
		}
	}

	public void enableDraw(boolean en)
	{
		btn_add_rect_shape.setEnabled(en);
		btn_add_circle_shape.setEnabled(en);
	}

	public boolean startDrawingShape(Shape shp)
	{
		shp_to_draw = shp;  
		return true;
	}

	public GfxPanel(final Shell parent_shell,Composite parent,int width_,int height_) {
		this.width=width_;
		this.height=height_;
		current_click_pos=new Point();
		current_click_pos.x=-1;
		img_w=width_;
		img_h=height_-35;

		bg_buff_img = new BufferedImage(img_w,img_h,BufferedImage.TYPE_INT_RGB); 
		bg=((DataBufferInt)bg_buff_img.getRaster().getDataBuffer()).getData();
		initializeBackground(0xFFFFFF);
		createMainComposite(parent_shell,parent, width_, height_);
		parent_shell.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {
				if(e.character==java.awt.event.KeyEvent.VK_DELETE)
					deleteSelectedShape();
			}

			@Override
			public void keyPressed(KeyEvent arg0) {

			}
		});

		Button btn_setclr = new Button(composite, 0);
		btn_setclr.setBounds(10,composite.getBounds().height- 30, 80, 25);
		btn_setclr.setText("Color..");
		btn_setclr.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if(shp_selected!=null)
				{
					ColorDialog cd = new ColorDialog(parent_shell);
					cd.open();
					shp_selected.setColor(cd.getRGB());
					refreshDrawingArea();
					notifyShapeModified(shp_selected.getShape_number());
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		btn_add_rect_shape = new Button(composite, 0);
		btn_add_rect_shape.setBounds(100,composite.getBounds().height- 30, 80, 25);
		btn_add_rect_shape.setText("Add Rectangle");
		btn_add_rect_shape.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				startDrawingShape(new RectangleShape());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		chk_enable_snap = new Button(composite, SWT.CHECK);
		chk_enable_snap.setBounds(280, composite.getBounds().height- 30, 110, 25);
		chk_enable_snap.setText("Enable Snapping");
		chk_enable_snap.setSelection(true);
		chk_enable_snap.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				snapper.enableSnap(chk_enable_snap.getSelection());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});

		btn_add_circle_shape = new Button(composite, 0);
		btn_add_circle_shape.setBounds(190,composite.getBounds().height- 30, 80, 25);
		btn_add_circle_shape.setText("Add Circle");
		btn_add_circle_shape.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				startDrawingShape(new OvalShape());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});



		awt_composite = new Composite(composite, SWT.BORDER |SWT.EMBEDDED);
		awt_composite.setBounds(0, 0, composite.getBounds().width, composite.getBounds().height-35);

		awt_frame = SWT_AWT.new_Frame(awt_composite);
		gfx = awt_frame.getGraphics();
		shp_arr=new ArrayList<Shape>();
		snapper = new Snapper(shp_arr);
		arr_notifiee = new ArrayList<GfxPanel_Notifiee>();

		awt_frame.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				int x=e.getX(),y=e.getY();
				if(drawing_now)
				{
					drawing_now=false;
					int new_shape_number=addShape(shp_being_drawn);
					notifyShapeAdded(new_shape_number);
					shp_being_drawn=null;
				}
				/**
				 * the next code is for resizing or moving
				 */
				else if(resizing_x_right |resizing_y_down |
						resizing_x_left |resizing_y_up |
						moving)
				{
					resizing_x_right=resizing_y_down
					=resizing_x_left=resizing_y_up
					=moving=false;
					notifyShapeModified(shp_selected.getShape_number());

					Shape dragged_on_shape=getDraggedOnShape(x, y);
					notifyTargettedDragOperation(shp_selected, dragged_on_shape);
				}
				else
				{
					current_click_pos.x=e.getX();
					current_click_pos.y=e.getY();

					notifyMouseClick(current_click_pos);
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				ini_x=e.getX();
				ini_y=e.getY();	

				if(shp_to_draw!=null)
				{
					shp_being_drawn=shp_to_draw;
					shp_being_drawn.setX(ini_x);
					shp_being_drawn.setY(ini_y);
					shp_being_drawn.setColor( new RGB(0, 0, 0));
					drawing_now=true;
					shp_to_draw =null;
					shp_selected=null;
				}
				else
				{
					shp_selected=getShapeByPosition(ini_x, ini_y);
					if(shp_selected!=null)
					{
						notifyShapeSelected(shp_selected.getShape_number());
						cursor_pos_in_shp_x=e.getX()-shp_selected.getX();
						cursor_pos_in_shp_y=e.getY()-shp_selected.getY();
					}
					refreshDrawingArea();
				}
			}

			@Override
			public void mouseExited(MouseEvent arg0) {

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {

			}

			@Override
			public void mouseClicked(MouseEvent arg0) {

			}
		});

		awt_frame.addMouseMotionListener(new MouseMotionListener() {


			@Override
			public void mouseMoved(MouseEvent e) {

			}

			@Override
			public void mouseDragged(MouseEvent e) {
				//Drawing:
				if(drawing_now) 
				{
					int x=e.getX(),y=e.getY();
					updateNewShape(shp_being_drawn,x,y);
					refreshDrawingArea();
					shp_being_drawn.Draw(gfx);
				}
				//Modifying:
				else if(shp_selected!=null)
				{
					//Resizing:
					if(!moving & isResizing(shp_selected,e.getX(), e.getY()))
						resizingShape(shp_selected, e.getX(), e.getY());
					//Moving:
					else 
						movingShape(shp_selected,e.getX(),e.getY());
				}
			}
		});

	}
	private void movingShape(Shape tmp_shp,int x,int y)
	{
		moving=true;
		SnapResults snp_res;
		snp_res=snapper.prepareSnapPosition(shp_selected,x,y,
				cursor_pos_in_shp_x,cursor_pos_in_shp_y);
		if(!snp_res.snapped_x)
			tmp_shp.setX(x-cursor_pos_in_shp_x);
		if(!snp_res.snapped_y)
			tmp_shp.setY(y-cursor_pos_in_shp_y);
		refreshDrawingArea();
	}

	private Shape getDraggedOnShape(int x,int y)
	{
		Shape[] possible_shapes=getShapesByPosition(x, y);
		for(int i=0;i<possible_shapes.length;i++)
			if(possible_shapes[i]==shp_selected)
				possible_shapes[i]=null;

		return getShapeWithleastArea(possible_shapes);
	}

	private void notifyTargettedDragOperation(Shape shp_dragged,Shape shp_dragged_on)
	{
		int dragtarget=-1;
		if(shp_dragged_on!=null)
			dragtarget=shp_dragged_on.getShape_number();
		for(GfxPanel_Notifiee notifiee: arr_notifiee)
		{
			notifiee.dragOccured(shp_dragged.getShape_number(), dragtarget);
		}
	}


	private boolean isResizing(Shape tmp_sh,int x,int y)
	{
		int sel_x=tmp_sh.getX(),sel_y=tmp_sh.getY();
		int width = 0,height = 0;
		width=tmp_sh.getWidth();
		height=tmp_sh.getHeight();
		resizing_x_left = resizing_x_left | ((Math.abs(x- (sel_x))<5) &
				(y<(sel_y+height)) &
				(y>sel_y));
		resizing_y_up =resizing_y_up | ((Math.abs(y- (sel_y))<5) &
				(x<(sel_x+width)) &
				(x>sel_x));
		resizing_x_right = resizing_x_right | ((Math.abs(x- (sel_x+width))<5) &
				(y<(sel_y+height)) &
				(y>sel_y));
		resizing_y_down =resizing_y_down | ((Math.abs(y- (sel_y+height))<5) &
				(x<(sel_x+width)) &
				(x>sel_x));

		return (resizing_x_right | resizing_y_down |resizing_x_left|resizing_y_up);
	}

	private void resizingShape(Shape tmp_sh,int x, int y)
	{
		int sel_x=tmp_sh.getX(),sel_y=tmp_sh.getY();

		int w=tmp_sh.getWidth(),
		h=tmp_sh.getHeight();
		SnapResults snp_res;
		snp_res=snapper.prepareSnapSize(shp_selected, x,y);
		if(!snp_res.snapped_x)
		{
			if(resizing_x_right)
			{
				tmp_sh.setWidth(x-sel_x);
			}
			if(resizing_x_left)
			{
				tmp_sh.setX(x);
				tmp_sh.setWidth(w + sel_x -x);
			}
		}
		if(!snp_res.snapped_y)
		{
			if(resizing_y_down)
			{
				tmp_sh.setHeight(y-sel_y);
			}

			if(resizing_y_up)
			{
				tmp_sh.setY(y);
				tmp_sh.setHeight(h + sel_y-y);
			}
		}
		refreshDrawingArea();
	}

	private void updateNewShape(Shape tmp_shp,int x,int y)
	{
		if(x>ini_x)
			tmp_shp.setWidth(x-ini_x);
		if(x<ini_x)
		{
			tmp_shp.setX(x);
			tmp_shp.setWidth(ini_x -x);
		}

		if(y>ini_y)
			tmp_shp.setHeight(y-ini_y);
		if(y<ini_y)
		{
			tmp_shp.setY(y);
			tmp_shp.setHeight(ini_y -y);
		}
	}
	public void clearDrawingArea()
	{
		gfx.clearRect(0, 0, width, height);
	}

	public void redrawAllShapes()
	{
		final int cleaning_margin=30;
		int x_left = 0,x_right = img_w,y_up = 0,y_down = img_h;
		if(resizing_x_left|resizing_x_right|resizing_y_down|resizing_y_up|
				moving)
		{
			x_left=shp_selected.getX()-cleaning_margin;
			x_right=shp_selected.getX()+shp_selected.getWidth()+cleaning_margin;
			y_up=shp_selected.getY()-cleaning_margin;
			y_down=shp_selected.getY()+shp_selected.getHeight()+cleaning_margin;
			if(x_left<0)x_left=0;
			if(x_right>bg_buff_img.getWidth())x_right=bg_buff_img.getWidth();
			if(y_up<0)y_up=0;
			if(y_down>bg_buff_img.getHeight())y_down=bg_buff_img.getHeight();
		}
		gfx.drawImage(bg_buff_img,
				x_left, y_up	,x_right+x_left,y_down+y_up,
				x_left, y_up	,x_right+x_left,y_down+y_up, null);

		for(Shape sh: shp_arr)
			sh.Draw(gfx);
	}

	private int addShape(Shape shp)
	{
		int new_shp_number=generateNewShapeNumber();
		shp.setShape_number(new_shp_number);
		shp_arr.add(shp);
		return new_shp_number;
	}

	public void registerForNotifications(GfxPanel_Notifiee notifiee)
	{
		arr_notifiee.add(notifiee);
	}

	private void notifyShapeAdded(int shape_number)
	{
		for(GfxPanel_Notifiee notifiee: arr_notifiee)
		{
			notifiee.shapeAdded(shape_number);
		}
	}

	private void notifyMouseClick(Point pos)
	{
		for(GfxPanel_Notifiee notifiee: arr_notifiee)
		{
			notifiee.mouseClicked(pos);
		}
	}

	private void notifyShapeModified(int shape_number)
	{
		for(GfxPanel_Notifiee notifiee: arr_notifiee)
		{
			notifiee.shapeModified(shape_number);
		}
	}

	private void notifyShapeDeleted(int shape_number)
	{
		for(GfxPanel_Notifiee notifiee: arr_notifiee)
		{
			notifiee.shapeDeleted(shape_number);
		}
	}

	private void notifyShapeSelected(int shape_number)
	{
		for(GfxPanel_Notifiee notifiee: arr_notifiee)
		{
			notifiee.shapeSelected(shape_number);
		}
	}

	private Shape[] getShapesByPosition(int x,int y)
	{
		ArrayList<Shape> possible_shps=new ArrayList<Shape>();

		for(Shape shp:shp_arr)
		{
			if(x > shp.getX() & x< (shp.getX() + shp.getWidth())
					& y > shp.getY() & y< (shp.getY()+shp.getHeight()))
			{
				//return tmp_rect; //return the "FIRST" rectangle that meets the condition!!
				possible_shps.add(shp);
			}
		}
		Shape[] returned_array= new Shape[possible_shps.size()];
		possible_shps.toArray(returned_array);
		return returned_array; //returns all the shapes existing in this position
	}

	private Shape getShapeByPosition(int x,int y)
	{
		Shape[] possible_shps= getShapesByPosition(x, y);
		return getShapeWithleastArea(possible_shps);
	}

	private Shape getShapeWithleastArea(Shape[] possible_shps)
	{
		Shape tmp_shp = null;
		int leastarea=1000000;
		for(Shape shp:possible_shps)
		{
			if(shp!=null)
				if(shp.getArea()<leastarea)
				{
					leastarea=shp.getArea();
					tmp_shp = shp;
				}
		}
		return tmp_shp; //returns the shape having the smallest area
	}

	public void refreshDrawingArea()
	{
		//clearDrawingArea();
		redrawAllShapes();
		if(shp_selected!=null)
			draw4Corners(shp_selected, 5);
	}

	public void deleteShape(Shape shp)
	{
		shp_arr.remove(shp);
	}

	public void attachShapes(int attachedshape,int mainshape)
	{
		getShapeByNumber(mainshape).attachToMe(getShapeByNumber(attachedshape));
	}

	public boolean deattachShapes(int mainshape,int attachedshape)
	{
		getShapeByNumber(mainshape).deattachFromMe(getShapeByNumber(attachedshape));
		return false;
	}

	public boolean deleteSelectedShape()
	{
		if(shp_selected!=null)
		{
			shp_arr.remove(shp_selected);
			notifyShapeDeleted(shp_selected.getShape_number());
			shp_selected=null;
			refreshDrawingArea();
			return true;
		}
		return false;
	}

	private void draw4Corners(Shape shp,int l)
	{
		shp.select(gfx, l);
	}

	public ArrayList<Shape> getShapeArray() {
		return shp_arr;
	}

	public Shape getShapeByNumber(int shapenumber)
	{
		for(int i=0;i<shp_arr.size();i++)
			if(shp_arr.get(i).getShape_number()==shapenumber)
				return shp_arr.get(i);
		return null;
	}

	public int getSelectedShapeNumber()
	{
		if(shp_selected!=null)
		{
			return shp_selected.getShape_number();
		}
		return -100;
	}

	public void selectShape(int shape_number)
	{
		shp_selected=getShapeByNumber(shape_number);
		refreshDrawingArea();
	}

	public void setBackground(int[] bg_data)
	{
		System.arraycopy(bg_data, 0, bg, 0, bg_data.length);
	}
	public void setEnable_snap(boolean enable_snap) {
		chk_enable_snap.setSelection(enable_snap);
		snapper.enableSnap(enable_snap);
	}

	public enum Direction{
		LEFT,RIGHT,UP,DOWN;
	}

}
