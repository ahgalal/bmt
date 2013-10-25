/***************************************************************************
 * Copyright 2010,2011 by Ahmed Galal, Ahmed Mohammed Aly, Sarah Hamid and
 * Mohammed Ahmed Ramadan contact: ceng.ahmedgalal@gmail.com This file is part
 * of Behavioral Monitoring Tool. Behavioral Monitoring Tool is free software:
 * you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, version 3 of the
 * License. Behavioral Monitoring Tool is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU
 * General Public License along with Behavioral Monitoring Tool. If not, see
 * <http://www.gnu.org/licenses/>.
 **************************************************************************/

package modules.headmotion;

import modules.ModuleData;
import modules.experiment.Constants;

/**
 * Data of the Rearing Module.
 * 
 * @author Creative
 */
public class HeadMotionModuleData extends ModuleData {
	public static final int	ANGLE_BREADTH	= 240;
	public static final int	MIN_ANGLE	= -120;
	private int	angle;
	public final static String dataID=Constants.MODULE_ID+".headmotion.data";
	public void setAngle(int angle) {
		this.angle = angle;
	}
	
	public static class HistogramSection{
		public int min;
		public int max;
	}
	
	public static class Histogram{
		private HistogramSection[] sections;
		private int[] frequencies;
		
		public Histogram(int numberOfSections) {
			setSections(new HistogramSection[numberOfSections]);
			setFrequencies(new int[numberOfSections]);
		}
		private int[] getFrequencies() {
			return frequencies;
		}
		private void setFrequencies(int[] frequencies) {
			this.frequencies = frequencies;
		}
		private HistogramSection[] getSections() {
			return sections;
		}
		private void setSections(HistogramSection[] sections) {
			this.sections = sections;
			
			for(int i=0;i<sections.length;i++)
				this.sections[i] = new HistogramSection();
		}
	}
	
	private Histogram histogram;
	public int getAngle() {
		return angle;
	}
	@Override
	public String getId() {
		return dataID;
	}
	public Histogram getAngleHistogram() {
		return histogram;
	}
	
	public HeadMotionModuleData(int numberOfSections) {
		histogram = new Histogram(numberOfSections);

		int sectionWidth = ANGLE_BREADTH/numberOfSections;
		int min=MIN_ANGLE;
		for(int i=0;i<numberOfSections;i++){
			histogram.getSections()[i].min = min;
			histogram.getSections()[i].max = min + sectionWidth;
			min = histogram.getSections()[i].max;
		}
	}
	
	public void addAngleValue(int angle){
		this.angle=angle;
		for(int i=0;i<histogram.getSections().length;i++){
			if(angle < histogram.getSections()[i].max && angle > histogram.getSections()[i].min){
				histogram.getFrequencies()[i]++;
				break;
			}
		}
	}
	public int[] getHistogramFrequencies() {
		return histogram.getFrequencies();
	}
}
