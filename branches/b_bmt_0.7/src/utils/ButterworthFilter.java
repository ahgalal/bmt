/**
 * 
 */
package utils;

/**
 * @author Creative
 */
public class ButterworthFilter {

	private double[]		denCeoffs;
	private double[]		numCeoffs;
	private final double[]	oldVals;

	public double[] getOldVals() {
		return oldVals;
	}

	public ButterworthFilter(final double[] numCeoffs, final double[] denCeoffs) {
		this.numCeoffs=numCeoffs;
		this.denCeoffs=denCeoffs;
		oldVals = new double[numCeoffs.length - 1];
	}

	// TODO: 2nd order filter is only supported!
	public double processSample(final double newVal) {
		
		final double num = numCeoffs[0] * newVal + numCeoffs[1] * oldVals[0]
				+ numCeoffs[2] * oldVals[1];
		final double part2 = denCeoffs[1] * oldVals[0] + denCeoffs[2]
				* oldVals[1];
		
		updateOldVals(newVal);

		return num - part2;
	}

	private void updateOldVals(double newVal) {
		for(int i=oldVals.length-1;i>0;i--){
			oldVals[i] = oldVals[i-1];
		}
		oldVals[0]=newVal;
	}

}
