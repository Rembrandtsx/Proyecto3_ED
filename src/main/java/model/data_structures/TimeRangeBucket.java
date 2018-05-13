package model.data_structures;

import org.joda.time.DateTime;

import model.vo.DateRange;
import model.vo.Service;

public class TimeRangeBucket{
	
	private IArrayList<LinkedList<Service>> buckets;
	
	private int windowSize;
	
	private static int rangeBetweenBuckets;
		
	private static DateTime startingPoint;
	
	private DateRange timeRange;
	
	public TimeRangeBucket(DateRange range, int windowSize) {
		this.windowSize = windowSize;
		this.timeRange = range;
		calculateStartingPoint();
		calculateRangeBetweenBuckets();
		buckets = new ArrayList<>(windowSize);
		initializeLists(windowSize);
	}
	

	public IArrayList<LinkedList<Service>> getBuckets(){
		return buckets;
	}

	public void add(Service s) {
		DateRange r = s.getTimeStamp();
		int bucketIndex = getBucketKey(r);
		LinkedList<Service> l = null;
		try{
			l = buckets.get(bucketIndex);
		} catch (IndexOutOfBoundsException e){

			throw e;
		}

		if(l.isEmpty()) {
			l.add(s);
		}else {
			boolean found = false;
			for (int i = 0; i < l.size() && !found; i++) {
				Service curr = l.getCurrent();
				if(curr.equals(s)) {
					found = true;
				}
			}
			if(!found) {
				l.add(s);
			}
		}
	}
	
	public LinkedList<Service> get(DateRange r) {
		Service val = null;
		
		int location = getBucketKey(r);
		return buckets.get(location);
	}
	
	private void initializeLists(int capacity) {
		for (int i = 0; i < capacity; i++) {
			buckets.add(new List<Service>());
		}
	}

	private void calculateRangeBetweenBuckets() {
		rangeBetweenBuckets = timeRange.getDiferenceInSeconds()/(windowSize-1);
		System.out.println(timeRange.getFechaInicial());
		System.out.println(timeRange.getFechaFinal());
		System.out.println("range between buckets " + rangeBetweenBuckets);
		System.out.println("range difference " + timeRange.getDiferenceInSeconds());
		System.out.println("window size " + windowSize);
	}
	
	private void calculateStartingPoint() {
		DateTime date = timeRange.getFechaInicial();
		int year = date.getYear();
		int month = date.getMonthOfYear();
		int day = date.getDayOfMonth();
		
		startingPoint = new DateTime(year, month, day, 0, 0, 0, 0);
	}

	
	public static int getBucketKey(DateRange r) {
		DateTime dateToCompare = r.getFechaInicial();
		int differenceInSeconds = DateRange.getDiferenceInSeconds(dateToCompare, startingPoint);
		return differenceInSeconds/rangeBetweenBuckets;
	}
}
