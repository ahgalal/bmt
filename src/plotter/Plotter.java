package plotter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Plotter {
    private final Composite parent;
    private Canvas canvas;
    private Composite mainComposite;
    private GC gc;
    private Point lastPoint;

    private final int axisOffsetX = 20;
    private final int axisOffsetY = 20;

    public Plotter(final Composite parent) {
	this.parent = parent;
	// TODO Auto-generated constructor stub
    }

    private Point shiftAxis(final int x, final int y) {
	final Point ret = new Point(0, 0);

	ret.x = x + axisOffsetX;
	ret.y = canvas.getSize().y - axisOffsetY - y;

	return ret;
    }

    public void initialize() {
	mainComposite = new Composite(parent, SWT.BORDER);
	mainComposite.setBounds(0, 0, parent.getBounds().width,
		parent.getBounds().height);

	canvas = new Canvas(mainComposite, 0);
	gc = new GC(canvas);
	canvas.setBounds(0, 0, mainComposite.getSize().x,
		mainComposite.getSize().y);
	canvas.setBackground(new Color(null, new RGB(255, 255, 255)));

	final Button btnNewButton = new Button(mainComposite, SWT.NONE);
	btnNewButton.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(final SelectionEvent e) {
		/*
		 * addPoint(20, 20); addPoint((int)(Math.random()*200),
		 * (int)(Math.random()*200)); addPoint((int)(Math.random()*200),
		 * (int)(Math.random()*200)); addPoint((int)(Math.random()*200),
		 * (int)(Math.random()*200));
		 */
		drawAxes();
	    }
	});
	btnNewButton.setBounds(29, 316, 75, 25);
	btnNewButton.setText("New Button");
	lastPoint = shiftAxis(0, 0);
	drawAxes();
    }

    public void addPoint(final int x, final int y) {
	final Point tmp = shiftAxis(x, y);
	gc.drawLine(lastPoint.x, lastPoint.y, tmp.x, tmp.y);
	lastPoint.x = tmp.x;
	lastPoint.y = tmp.y;
	drawAxes();
    }

    public void drawAxes() {
	gc.drawLine(axisOffsetX, 10, axisOffsetX,
		canvas.getBounds().height - 10);
	gc.drawLine(10, canvas.getBounds().height - axisOffsetY,
		canvas.getBounds().width - 10, canvas.getBounds().height
			- axisOffsetY);
    }

    public static void main(final String... strings) {
	final Shell s = new Shell();
	s.setSize(687, 487);
	final Plotter p = new Plotter(s);
	p.initialize();
	s.open();
	while (!s.isDisposed())
	    if (!Display.getDefault().readAndDispatch())
		Display.getDefault().sleep();
    }
}
