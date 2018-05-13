package model.vo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import com.google.gson.stream.JsonReader;


public class FileIterator implements Iterator<Service>{

	
	private JsonReader reader;
	
	private boolean hasNext;
	
	public FileIterator(String file) {
		try {
			reader = initializeStream(file);
			initializeArray();
			hasNext = reader.hasNext();
			if(!hasNext) {
				finalizeArray();
			}
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private JsonReader initializeStream(String file) throws FileNotFoundException, 
		UnsupportedEncodingException {
		InputStream stream =  new FileInputStream(new File(file));
		return new JsonReader(new InputStreamReader(stream, "UTF-8"));
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
		int dropoffArea = 0;
		int pickupArea = 0;
		String startTimestamp = "";
		String endTimestamp = "";
		double pickupLatitude = 0.0;
		double pickupLongitude = 0.0;
		
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
				startTimestamp = reader.nextString();
			}else if (name.equals("trip_start_timestamp")) {
				endTimestamp = reader.nextString();
			}else if (name.equals("pickup_community_area")){
				pickupArea = reader.nextInt();
			}else if (name.equals("dropoff_community_area")){
				dropoffArea = reader.nextInt();
			}			else if(name.equals("pickup_centroid_latitude")){
				pickupLatitude = reader.nextDouble();
			}else if(name.equals("pickup_centroid_longitude")){
				pickupLongitude = reader.nextDouble();
			}
			
			else {
				reader.skipValue();
			}	
		}
		reader.endObject();
		return new Service(taxiId, tripID, tripSeconds, tripMiles, tripTotal, startTimestamp, endTimestamp);
	}

	@Override
	public boolean hasNext(){
		return hasNext;
	}

	@Override
	public Service next() {

		if(hasNext) {
			try {
				Service s =  buildObject();
				if (!reader.hasNext()) {
					hasNext = false;
					finalizeArray();
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
