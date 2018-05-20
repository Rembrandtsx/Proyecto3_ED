package view;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

import mapsDraw.MapsDrawer;
import controller.Controller;
import model.data_structures.*;
import model.logic.TaxiTripsManager;
import model.vo.*;

/**
 * view del programa
 */
public class TaxiTripsManagerView 
{
	private static MapsDrawer dibujo = new MapsDrawer();
	private static double refDistance;
	public static void main(String[] args) 
	{
		dibujo.resetActivos();
		Scanner sc = new Scanner(System.in);
		boolean fin=false;
		while(!fin)
		{
			//imprime menu
			printMenu();
			

			//opcion req
			int option = Integer.parseInt(sc.nextLine());

			switch(option)
			{
			//1C cargar informacion dada
			case 1:

				//imprime menu cargar
				printMenuCargar();
				dibujo.resetActivos();
				

				//opcion cargar
				int optionCargar = Integer.parseInt(sc.nextLine());

				//directorio json
				String[] linksJson = null;
				switch (optionCargar)
				{
				//direccion json pequeno
				case 1:
					linksJson = new String[] {TaxiTripsManager.DIRECCION_SMALL_JSON};
					break;

					//direccion json mediano
				case 2:
					linksJson = new String[] {TaxiTripsManager.DIRECCION_MEDIUM_JSON};
					break;

					//direccion json grande
				case 3:

					linksJson = TaxiTripsManager.DIRECCION_LARGE_JSON;
					break;
				}

				// refDIstance
				System.out.println("Ingrese la distancia de referencia con la cual quiere crear los vértices (metros): ");
				refDistance = Double.parseDouble(sc.nextLine());

				//Memoria y tiempo
				long memoryBeforeCase1 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
				long startTime = System.nanoTime();

				//Cargar data
				IDiGraph graph = new DiGraph();
				try {
					graph = Controller.cargarSistema(linksJson, refDistance);
				} catch (Exception e) {
					e.printStackTrace();
				}

				//Tiempo en cargar
				long endTime = System.nanoTime();
				long duration = (endTime - startTime)/(1000000);

				//Memoria usada
				long memoryAfterCase1 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
				System.out.println("Tiempo en cargar: " + duration + " milisegundos \nMemoria utilizada:  "+ ((memoryAfterCase1 - memoryBeforeCase1)/1000000.0) + " MB");

				System.out.println("The Graph was created ");
				System.out.println("----------------------------------------------------------------------");
				System.out.println("Number of vertices: " + graph.numVertices() + "\n" +
									"Number of edges: " + graph.numEdges() + "\n" +
									"Reference distances for vertex creation (dx): " + refDistance + "\n");
				System.out.println("----------------------------------------------------------------------");
//				System.out.println("Information about vertices (clusters) in the graph: ");
//				Iterator<String> iter = graph.keys();
//				int count = 1;
//				int countServices = 0;
//				while (iter.hasNext()){
//					AdjacentServices as = (AdjacentServices)graph.getInfoVertex(iter.next());
//					System.out.println( count + ") Cluster id: " + as.getLatRef() + "-" + as.getLonRef() + " Vertex array length: " + as.getServices().size());
//					countServices += as.getServices().size();
//					count++;
//				}
//				System.out.println(countServices);
				break;

				case 2:
					
					
					dibujo.resetActivos();
					
					boolean save = Controller.saveJson();
				
						if (save)
							 System.out.println("Se pudo Cargar");
						else
							System.out.println("No se pudo Cargar");
				
					break;
				case 3:
					DiGraph serviceGraph = Controller.loadJson();
					dibujo.resetActivos();
					if(serviceGraph == null){
						System.out.println("No se pudo cargar desde archivo");
					}else {
						System.out.println("The Graph was loaded from file ");
						System.out.println("----------------------------------------------------------------------");
						System.out.println("Number of vertices: " + serviceGraph.numVertices() + "\n" +
								"Number of edges: " + serviceGraph.numEdges() + "\n");
						System.out.println("----------------------------------------------------------------------");
					}
					break;
				case 4:
					
						
					
						AdjacentServices m = Controller.mostCongestedVertex();
						Double lat = m.getLatRef();
						Double lon = m.getLonRef();
						System.out.println(lat);
						System.out.println(lon);
						dibujo.dibujoRequerimiento1(lat, lon);
						
					
					
					break;	
				case 5:
					LinkedList<StrongComponent> componentes = Controller.getStrongComponents();
					System.out.println("Componentes fuertemente conexos: " + componentes.size());
					StrongComponent ns = new StrongComponent(StrongComponent.makeRandomColor(), -1);
					int n = 1;
					for (int i =0; i<componentes.size(); i++) {
						System.out.println("Componente "+(n++)+":");
						try {
							System.out.println("Color: "+componentes.get(i).getColor());
							System.out.println("Número de vertices: "+componentes.get(i).getVertices().size());
							if(ns.getVertices().size()<componentes.get(i).getVertices().size()) {
								ns = componentes.get(i);
							}
							
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						dibujo.dibujoRequerimiento2(ns.getVertices());
					}
					
					break;	
				case 6:
					
					break;	
				case 7:
					
					break;	
				case 8:
					
					break;
				case 9:
					
					break;	
				case 10:
					
					dibujo.pintarIndex();
					try 
					{
						File f = new File(MapsDrawer.RUTAPRINCIPAL);
						java.awt.Desktop.getDesktop().browse(f.toURI());
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
					}
					break;	
				case 11:
					fin=true;
					sc.close();
					break;	

			}
		}
	}
	/**
	 * Menu 
	 */
	private static void printMenu() //
	{
		System.out.println("---------ISIS 1206 - Estructuras de datos----------");
		System.out.println("---------------------Proyecto 3: Grafos----------------------");
		System.out.println("Iniciar la Fuente de Datos a Consultar :");
		System.out.println("1. Cargar toda la informacion del sistema de una fuente de datos (small, medium o large).");
		System.out.println("2. Guardar Json");
		System.out.println("3. Cargar grafo del Json");
		System.out.println("4. Dar vertice mas congestionado");
		System.out.println("5. Componentes fuertemente conexos en el grafo");
		System.out.println("6. Generar mapa con componentes fuertemente conexas de distintos colores");
		System.out.println("7. Camino de costo minimo");
		System.out.println("8. Caminos mayor y menor duración");
		System.out.println("9. Caminos sin peaje");
		System.out.println("10. Visualizar Google Maps");
		System.out.println("11. Salir");
		System.out.println("Ingrese el numero de la opcion seleccionada y presione <Enter> para confirmar: (e.g., 1):");
		
	

	}

	private static void printMenuCargar()
	{
		System.out.println("-- Que fuente de datos desea cargar?");
		System.out.println("-- 1. Small");
		System.out.println("-- 2. Medium");
		System.out.println("-- 3. Large");
		System.out.println("-- Type the option number for the task, then press enter: (e.g., 1)");
	}

}
