package utils;

public class TimeCalculator {
    long t1, t2;

    public void start() {
	t1 = System.currentTimeMillis();
    }

    public long end() {
	t2 = System.currentTimeMillis();
	return t2 - t1;
    }
}
