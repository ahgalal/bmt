package gfx_panel;
import java.awt.Color;
import java.awt.Graphics;

import org.eclipse.swt.graphics.RGB;

public class OvalShape extends Shape {
	
	private double PI = 3.14159;
	private int diameter_x;
	private int diameter_y;
	
	public OvalShape (int rx,int ry, int x, int y, RGB c ){
		super(x, y, rx, ry, c);
		this.diameter_x = rx;
		this.diameter_y = ry;
	}
	
	public OvalShape() {
		this(0, 0, 0, 0,null);
	}
	
	@Override
	public void setHeight(int height) {
		super.setHeight(height);
		diameter_y=height;
	}

	@Override
	public void setWidth(int width) {
		super.setWidth(width);
		diameter_x=width;
	}

	public int getArea()
	{
		return (int)(PI*(diameter_x/2)*(diameter_y/2));
	}
	
	public void Draw(Graphics g){
		g.setColor(new Color(rgb_color.red,rgb_color.green,rgb_color.blue));
		g.drawOval(x, y,diameter_x, diameter_y);
	}
}
