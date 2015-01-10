package flightsSearch;

public class Utils {

	public static void waitTimeToLoad(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
