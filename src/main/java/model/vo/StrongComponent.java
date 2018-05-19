package model.vo;

import model.data_structures.ArrayList;
import model.data_structures.IArrayList;

public class StrongComponent implements Comparable<StrongComponent> {
	
	private int color;


	private IArrayList<AdjacentServices> vertices;

	public StrongComponent(int color, String vertexId, IArrayList<AdjacentServices> vertices) {
		this.color = color;
		this.vertices = vertices;
	}

	public StrongComponent(int color) {
		this.color = color;
		this.vertices = new ArrayList<>();
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public IArrayList<AdjacentServices> getVertices() {
		return vertices;
	}


	public void setVertices(IArrayList<AdjacentServices> vertices) {
		this.vertices = vertices;
	}

	public void addVertex(AdjacentServices as){
		vertices.add(as);
	}

	@Override
	public int compareTo(StrongComponent o) {
		return vertices.size() - o.getVertices().size();
	}
}
