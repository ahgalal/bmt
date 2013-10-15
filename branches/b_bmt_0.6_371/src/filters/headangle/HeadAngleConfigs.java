package filters.headangle;

import org.eclipse.swt.graphics.Color;

import filters.CommonFilterConfigs;
import filters.FilterConfigs;

public class HeadAngleConfigs extends FilterConfigs {

	private Color bpColor1;
	private Color bpColor2;
	private Color earColor1;
	private Color earColor2;
	
	public HeadAngleConfigs(String name, String filterId,
			CommonFilterConfigs commonConfigs) {
		super(name, filterId, commonConfigs);
	}

	@Override
	public FilterConfigs newInstance(String filterName,
			CommonFilterConfigs commonConfigs) {
		return new HeadAngleConfigs(filterName, HeadAngleFilter.ID, commonConfigs);
	}

	public Color getBpColor1() {
		return bpColor1;
	}

	public void setBpColor1(Color bpColor1) {
		this.bpColor1 = bpColor1;
	}

	public Color getBpColor2() {
		return bpColor2;
	}

	public void setBpColor2(Color bpColor2) {
		this.bpColor2 = bpColor2;
	}

	public Color getEarColor1() {
		return earColor1;
	}

	public void setEarColor1(Color earColor1) {
		this.earColor1 = earColor1;
	}

	public Color getEarColor2() {
		return earColor2;
	}

	public void setEarColor2(Color earColor2) {
		this.earColor2 = earColor2;
	}

}
