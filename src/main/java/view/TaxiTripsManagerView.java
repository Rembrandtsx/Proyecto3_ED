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
				System.out.println("Information about vertices (clusters) in the graph: ");
				Iterator<String> iter = graph.keys();
				int count = 1;
				int countServices = 0;
				while (iter.hasNext()){
					String id = iter.next();
					AdjacentServices as = (AdjacentServices)graph.getInfoVertex(id);
					System.out.println( count + ") Cluster id: " + as.getLatRef() + "-" + as.getLonRef() + " Vertex array length: " + as.getServices().size());
					Vertex<String, AdjacentServices, ArcServices> v = graph.getVertex(id);
					System.out.println("number of adjacent: " + v.getAdj().size());
					countServices += as.getServices().size();
					count++;
				}
				System.out.println(countServices);
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
					System.out.println("-- Cargando archivo de calles en './data/StreetLines.csv' ");
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
						dibujo.dibujoRequerimiento2(ns.getVertices(), ns.getColor());
					}
					
					break;	
				case 6:
					LinkedList<StrongComponent> componentesR3 = Controller.getStrongComponents();
					IHashMap<String, Double> verticesRadius= Controller.getVerticesRadius();
					IArrayList<IHashMap<String, Double>> arrayParaMapa= new ArrayList<>();
					IArrayList<String> colores = new ArrayList<>();
					try {
					for(int i =0; i<componentesR3.size();i++) {
							StrongComponent componente = componentesR3.get(i);
							colores.add(componente.getColor());
							IHashMap<String, Double> verticesPercentage = new SeparateChainingHashMap<>();
							for(int j=0;j<componente.getVertices().size();j++) {
								AdjacentServices verticesComponente=componente.getVertices().get(j);
								String key = verticesComponente.getLatRef()+"|"+verticesComponente.getLonRef();
								Double val= verticesRadius.get(key);
								verticesPercentage.put(key, val);
							}
							arrayParaMapa.add(verticesPercentage);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					dibujo.dibujoRequerimiento3(arrayParaMapa,colores);
					
					
					
					
					
					break;	
				case 7:
				
						
						String[] callesIni = Controller.getRandomStreets();
						System.out.println(callesIni[0]+"*****"+callesIni[1]);
						String[] callesFin= Controller.getRandomStreets();
						System.out.println(callesFin[0]+"*****"+callesFin[1]);
						String[] vertices=Controller.getRandomVertices(callesIni, callesFin);
						System.out.println(vertices[0]+"****"+vertices[1]);

						
						LinkedList<ArcServices> lista = Controller.getShortestPathByDistance(vertices[0], vertices[1]);
						if(lista.isEmpty()){
							System.out.println("No hay caminos entre los vértices");
						}else {
							System.out.println("Camino de costo minimo: ");
							System.out.println("-----------------------------------");
							System.out.println("-----------EL CAMINO---------------");
							try {
								lista.listing();
								for(int i =0; i<lista.size();i++) {
									System.out.println("|");
									System.out.println("V");
									System.out.println("********************************");
									System.out.println("Vertice Inicio:"+lista.getCurrent().getIniVertex());
									System.out.println("Vertice Final:"+lista.getCurrent().getEndVertex());
									System.out.println("********************************");
									lista.next();
								}
							}catch(Exception e) {e.printStackTrace();}
							System.out.println("-----------FIN DEL CAMINO----------");
							System.out.println("-----------------------------------");


							

						}
						dibujo.dibujoRequerimiento4(lista, callesIni, callesFin);
					
					break;	
				case 8:
					String[] callesIniR5 = Controller.getRandomStreets();
					
					String[] callesFinR5= Controller.getRandomStreets();
					
					String[] verticesR5=Controller.getRandomVertices(callesIniR5, callesFinR5);
					

					
					ArrayList<LinkedList<ArcServices>> listaR5 = Controller.getShortestPathByTime(verticesR5[0], verticesR5[1]);
					if(listaR5.get(0).isEmpty()){
						System.out.println("No hay caminos entre los vértices de IDA");
					}else {
						System.out.println("Camino de costo minimo: ");
						System.out.println("-----------------------------------");
						System.out.println("-----------EL CAMINO IDA------------");
						try {
							listaR5.get(0).listing();
							for(int i =0; i<listaR5.get(0).size();i++) {
								System.out.println("|");
								System.out.println("V");
								System.out.println("********************************");
								System.out.println("Vertice Inicio:"+listaR5.get(0).getCurrent().getIniVertex());
								System.out.println("Vertice Final:"+listaR5.get(0).getCurrent().getEndVertex());
								System.out.println("********************************");
								listaR5.get(0).next();
							}
						}catch(Exception e) {e.printStackTrace();}
						System.out.println("-----------FIN DEL CAMINO----------");
						System.out.println("-----------------------------------");
					}
					if(listaR5.get(1).isEmpty()){
						System.out.println("No hay caminos entre los vértices de REGRESO");
					}else {
						System.out.println("Camino de costo minimo: ");
						System.out.println("-----------------------------------");
						System.out.println("-----------EL CAMINO REGRESO------------");
						try {
							listaR5.get(1).listing();
							for(int i =0; i<listaR5.get(1).size();i++) {
								System.out.println("|");
								System.out.println("V");
								System.out.println("********************************");
								System.out.println("Vertice Inicio:"+listaR5.get(1).getCurrent().getIniVertex());
								System.out.println("Vertice Final:"+listaR5.get(1).getCurrent().getEndVertex());
								System.out.println("********************************");
								listaR5.get(1).next();
							}
						}catch(Exception e) {e.printStackTrace();}
						System.out.println("-----------FIN DEL CAMINO----------");
						System.out.println("-----------------------------------");
					}
					
					
					
					dibujo.dibujoRequerimiento5(listaR5, callesIniR5, callesFinR5);
					break;
				case 9:
					String[] callesIniR6 = Controller.getRandomStreets();

					String[] callesFinR6 = Controller.getRandomStreets();

					String[] verticesR6 = Controller.getRandomVertices(callesIniR6, callesFinR6);


					// test case
					IHeap<Path> pathsDistance = Controller.getSortedPathsWithNoTollsByDistance("41.80908443|-87.632424524", "41.795430631|-87.696435232");
					IHeap<Path> pathsTime = Controller.getSortedPathsWithNoTollsByTime("41.80908443|-87.632424524", "41.795430631|-87.696435232");
					if(pathsDistance.isEmpty()){
						System.out.println("No hay caminos sin peajes");
					}else {
						System.out.println("Caminos Ordenados por distancia ascendentemente (Caso de Prueba) : ");

						for (int i = 0; i < pathsDistance.size(); i++) {
							System.out.println("Path " + (i + 1) + " : ");
							Path path = pathsDistance.get(i + 1);
							ListIterator<ArcServices> iterPathDistanceTest = new ListIterator(path.getEdges());

							for (ArcServices e : iterPathDistanceTest) {
								System.out.println("|");
								System.out.println("V");
								System.out.println("********************************");
								System.out.println("Vertice Inicio:" + e.getIniVertex());
								System.out.println("Vertice Final:" + e.getEndVertex());
								System.out.println("********************************");
							}
							System.out.println("Distancia media del camino: " + path.getTotalDistance());


						}

					}
						///CIERRE CASO DE PRUEBA
					pathsDistance = Controller.getSortedPathsWithNoTollsByDistance(verticesR6[0], verticesR6[1]);
					pathsTime = Controller.getSortedPathsWithNoTollsByTime(verticesR6[0], verticesR6[1]);
					LinkedList<ArcServices> enviarDistance = null;
					LinkedList<ArcServices> enviarTime = null;
					if(pathsDistance.isEmpty()){
						System.out.println("No hay caminos sin peajes");
					}else {
						System.out.println("Caminos Ordenados por distancia ascendentemente: ");
						//A REVISAR #####################################
						enviarDistance = pathsDistance.get(1).getEdges();
						for (int i = 0; i < pathsDistance.size(); i++) {

							System.out.println("Path " + (i + 1) + " : ");
							Path path = pathsDistance.get(i + 1);
							ListIterator<ArcServices> iterPathDistance = new ListIterator(path.getEdges());
					
							for (ArcServices e : iterPathDistance) {

								System.out.println("|");
								System.out.println("V");
								System.out.println("********************************");
								System.out.println("Vertice Inicio:"+e.getIniVertex());
								System.out.println("Vertice Final:"+e.getEndVertex());
								System.out.println("********************************");
							}
							System.out.println("Distancia media del camino: " + path.getTotalDistance());
						}

					}
					if(pathsTime.isEmpty()){
						System.out.println("No hay caminos sin peajes");
					}else {
						System.out.println("Caminos Ordenados por tiempo descendentemente");
						//A REVISAR #####################################
						enviarTime = pathsTime.get(1).getEdges();
						for (int i = 0; i < pathsTime.size(); i++) {
							System.out.println("Path " + (i + 1) + " : ");
							Path path = pathsTime.get(i + 1);
							ListIterator<ArcServices> iterPathTime = new ListIterator(path.getEdges());
						
							for (ArcServices e : iterPathTime) {
								System.out.println("|");
								System.out.println("V");
								System.out.println("********************************");
								System.out.println("Vertice Inicio:"+e.getIniVertex());
								System.out.println("Vertice Final:"+e.getEndVertex());
								System.out.println("********************************");
							}
							System.out.println("Tiempo medio del camino: " + path.getTotalTime());
						}
					}
					
					ArrayList<LinkedList<ArcServices>> listaR6 = new ArrayList<>(2);
					listaR6.add(new List());
					if(enviarDistance !=null) {
						listaR6.remove(0);
						listaR6.add(enviarDistance);	
						listaR6.get(0).listing();
					}
					listaR6.add(new List());
					if(enviarTime !=null) {
						listaR6.remove(1);
						listaR6.add(enviarTime);
						listaR6.get(1).listing();
						
					}
						
					
					dibujo.dibujoRequerimiento6(listaR6, callesIniR6, callesFinR6);
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
		System.out.println("4. Obtener vértice más congestionado en Chicago");
		System.out.println("5. Obtener componentes fuertemente conexoss");
		System.out.println("6. Generar mapa coloreado de la red vial de Chicago en Google Maps");
		System.out.println("7. Encontrar camino de menor distancia para dos puntos aleatorios");
		System.out.println("8. Hallar caminos de mayor y menor duracion entre dos puntos aleatorios");
		System.out.println("9. Encontrar caminos sin peaje entre dos puntos aleatorios");
		System.out.println("10. Visualizar Google Maps");
		System.out.println("11. Salir");
		System.out.println("Ingrese el numero de la opcion seleccionada y presione <Enter> para confirmar: (e.g., 1):");
		
	

	}

	private static void printMenuCargar()
	{
		System.out.println("-- Cargando archivo de calles en './data/StreetLines.csv' ");
		System.out.println("-- Que fuente de datos desea cargar?");
		System.out.println("-- 1. Small");
		System.out.println("-- 2. Medium");
		System.out.println("-- 3. Large");
		System.out.println("-- Type the option number for the task, then press enter: (e.g., 1)");
	}

}
