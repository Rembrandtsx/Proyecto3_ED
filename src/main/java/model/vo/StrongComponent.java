package model.vo;

import model.data_structures.IArrayList;
import model.data_structures.Vertex;

public class StrongComponent {
	
	private int color;
	
	private IArrayList<Vertex<String, AdjacentServices, ArcServices>> vertices;

	public StrongComponent(int color, IArrayList<Vertex<String, AdjacentServices, ArcServices>> vertices) {
		this.color = color;
		this.vertices = vertices;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public IArrayList<Vertex<String, AdjacentServices, ArcServices>> getVertices() {
		return vertices;
	}

	public void setVertices(IArrayList<Vertex<String, AdjacentServices, ArcServices>> vertices) {
		this.vertices = vertices;
	}
	
	
	
}
