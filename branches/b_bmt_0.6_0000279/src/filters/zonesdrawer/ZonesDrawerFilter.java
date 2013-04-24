/**
 * 
 */
package filters.zonesdrawer;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import modules.zones.ShapeCollection;
import modules.zones.ZonesCollection;
import modules.zones.ZonesModule;
import modules.zones.ZonesModuleConfigs;
import modules.zones.ZonesModuleGUI;
import utils.PManager.ProgramState;
import filters.FilterData;
import filters.Link;
import filters.VideoFilter;

/**
 * @author Creative
 */
public class ZonesDrawerFilter extends
		VideoFilter<ZonesDrawerConfigs, ZonesDrawerData> {
	public static final String ID = "filters.zonesdrawer";
	private BufferedImage	bufferedImage;
	private int[]			bufferedImageData;
	private Graphics		gfx;

	public ZonesDrawerFilter(final String name, final Link linkIn, final Link linkOut) {
		super(name, linkIn, linkOut);
		//initializeBufferedImage();
	}

	private void initializeBufferedImage() {
		bufferedImage = new BufferedImage(
				configs.getCommonConfigs().getWidth(), configs
						.getCommonConfigs().getHeight(),
				BufferedImage.TYPE_INT_RGB);
		gfx = bufferedImage.getGraphics();
		bufferedImageData = ((DataBufferInt) bufferedImage.getRaster()
				.getDataBuffer()).getData();
	}

	/*
	 * (non-Javadoc)
	 * @see filters.VideoFilter#newInstance(java.lang.String)
	 */
	@Override
	public VideoFilter<?,?> newInstance(final String filterName) {
		return new ZonesDrawerFilter(filterName, null, null);
	}
	private static final int		DEFAULT_CANVAS_HEIGHT	= 480;
	private static final int		DEFAULT_CANVAS_WIDTH	= 640;
	/*
	 * (non-Javadoc)
	 * @see filters.VideoFilter#process()
	 */
	@Override
	public void process() {
		final ShapeCollection shapeController = configs.getShapeController();
		System.arraycopy(linkIn.getData(),0 , bufferedImageData, 0, linkIn.getData().length);
		// draw zones
		int width = configs.getCommonConfigs().getWidth();
		int height = configs.getCommonConfigs().getHeight();
		shapeController.drawAllShapes(gfx,width/(double)DEFAULT_CANVAS_WIDTH,height/(double)DEFAULT_CANVAS_HEIGHT);
		
		linkOut.setData(bufferedImageData);
	}

	/*
	 * (non-Javadoc)
	 * @see filters.VideoFilter#registerDependentData(filters.FilterData)
	 */
	@Override
	public void registerDependentData(final FilterData data) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see utils.Configurable#updateConfigs(utils.Configuration)
	 */
	@Override
	public void updateConfigs(final ZonesDrawerConfigs config) {
		super.updateConfigs(config);
		initializeBufferedImage();
	}

	/*
	 * (non-Javadoc)
	 * @see utils.StateListener#updateProgramState(utils.PManager.ProgramState)
	 */
	@Override
	public void updateProgramState(final ProgramState state) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public String getID() {
		return ID;
	}

}
