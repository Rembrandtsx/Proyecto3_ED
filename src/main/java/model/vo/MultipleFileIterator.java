package model.vo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import com.google.gson.stream.JsonReader;

public class MultipleFileIterator implements Iterator<Service> {

	private String[] files;

	private boolean hasNext;

	private int currentFile;

	private JsonReader reader;

	public MultipleFileIterator(String[] files) {
		this.files = files;
		if (files.length == 0) {
			hasNext = false;
		} else {
			currentFile = -1;
			try {
				openNextFile();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void openNextFile() throws IOException {
		hasNext = false;
		currentFile++;
		while(!hasNext && currentFile < files.length) {
			System.out.println("File about to open: " + files[currentFile]);
			initializeStream(files[currentFile]);
			initializeArray();
			hasNext = reader.hasNext();
			if (!hasNext) {
				finalizeArray();
				currentFile ++;
			}
		}
	}

	private void initializeStream(String file) throws FileNotFoundException, UnsupportedEncodingException {
		InputStream stream =  this.getClass().getResourceAsStream(file);
		reader =  new JsonReader(new InputStreamReader(stream, "UTF-8"));
	}
	
	private void initializeArray() throws IOException {
		reader.beginArray();
	}
	
	private void finalizeArray() throws IOException {
		reader.endArray();
		reader.close();
	}
	
	private Service buildObject() throws IOException {
		String taxiId = "";
		String company = "";
		String tripID = "";
		int tripSeconds = 0;
		double tripMiles = 0.0;
		double tripTotal = 0.0;
		double tolls = 0.0;
		int dropoffArea = 0;
		int pickupArea = 0;
		double pickupLatitude = 0.0;
		double pickupLongitude = 0.0;
		double dropOffLatitude = 0.0;
		double dropOffLongitude = 0.0;
		String startTimestamp = "";
		String endTimestamp = "";
		
		reader.beginObject();
		while (reader.hasNext()) {
			
			String name = reader.nextName();
			if(name.equals("company")){
				company = reader.nextString();
			}else if(name.equals("taxi_id")) {
				taxiId = reader.nextString();
			}else if(name.equals("trip_id")) {
				tripID = reader.nextString();
			}else if(name.equals("trip_seconds")) {
				tripSeconds = reader.nextInt();
			}else if(name.equals("trip_miles")) {
				tripMiles = reader.nextDouble();
			}else if(name.equals("trip_total")) {
				tripTotal = reader.nextDouble();
			}else if (name.equals("trip_end_timestamp")) {
				endTimestamp = reader.nextString();
			}else if (name.equals("trip_start_timestamp")) {
				startTimestamp = reader.nextString();
			}else if (name.equals("pickup_community_area")){
				pickupArea = reader.nextInt();
			}else if (name.equals("dropoff_community_area")){
				dropoffArea = reader.nextInt();
			}
			else if(name.equals("pickup_centroid_latitude")){
				pickupLatitude = reader.nextDouble();
			}else if(name.equals("pickup_centroid_longitude")){
				pickupLongitude = reader.nextDouble();
			}
			else if(name.equals("dropoff_centroid_latitude")){
				dropOffLatitude = reader.nextDouble();
			}else if(name.equals("dropoff_centroid_longitude")){
				dropOffLongitude = reader.nextDouble();
			}else if(name.equals("tolls")){
				tolls = reader.nextDouble();
			}

			else {
				reader.skipValue();
			}	
		}
		reader.endObject();
		return new Service(taxiId, tripID, company, tripSeconds, tripMiles, tripTotal,
				startTimestamp, endTimestamp, pickupArea, dropoffArea, pickupLatitude, pickupLongitude, dropOffLatitude, dropOffLongitude, tolls);
	}

	@Override
	public boolean hasNext() {
		return hasNext;
	}

	@Override
	public Service next() {
		if(hasNext) {
			try {
				Service s =  buildObject();
				if (!reader.hasNext()) {
					openNextFile();
				}
				return s;
			} catch (IOException e) {
				e.printStackTrace();
				throw new NoSuchElementException();
			}
		}else {
			throw new NoSuchElementException();
		}
	}

}
