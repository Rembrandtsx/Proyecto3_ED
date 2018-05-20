package model.vo;

import java.awt.Color;

import model.data_structures.ArrayList;
import model.data_structures.IArrayList;

public class StrongComponent implements Comparable<StrongComponent> {
	
	private String color;
	private int numID;


	private IArrayList<AdjacentServices> vertices;

	public StrongComponent(String color, String vertexId, IArrayList<AdjacentServices> vertices) {
		this.color = color;
		this.vertices = vertices;
	}

	public StrongComponent(String color, int numID) {
		this.color = color;
		this.numID = numID;
		this.vertices = new ArrayList<>();
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public IArrayList<AdjacentServices> getVertices() {
		return vertices;
	}
	

	public void setVertices(IArrayList<AdjacentServices> vertices) {
		this.vertices = vertices;
	}
	public int getNumID() {
		return this.numID;
	}
	public void addVertex(AdjacentServices as){
		vertices.add(as);
	}

	public static String makeRandomColor() {
		Color random = new Color(((int)(Math.random()*256)), ((int)(Math.random()*256)), ((int)(Math.random()*256)));
		String hex = "#"+Integer.toHexString(random.getRGB()).substring(2);
		
		
		return hex;
	}
	
	
	
	
	
	@Override
	public int compareTo(StrongComponent o) {
		return vertices.size() - o.getVertices().size();
	}
}
