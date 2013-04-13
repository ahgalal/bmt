package utils;

public class TimeCalculator {
	private long	t1, t2;

	public long end() {
		t2 = System.currentTimeMillis();
		return t2 - t1;
	}

	public void start() {
		t1 = System.currentTimeMillis();
	}
}
