package mapsDraw;


import java.io.File;
import java.io.IOException;


import org.apache.commons.io.FileUtils;

public class MapsDrawer {
	public static final String RUTAPRINCIPAL = "viewMap/index.html";

	private boolean reqMade[] = new boolean[6];
	
	public void dibujoRequerimiento1(double lat, double lon){
		System.out.println("Imprimio Mapa");
		
		try {
			reqMade[0]=true;
			System.out.println(reqMade[0]);
			File htmlTemplateFile = new File("viewMap/templates/templateMap.html");
			String htmlString;
			htmlString = FileUtils.readFileToString(htmlTemplateFile);
			String requerimiento = "Requerimiento1";
			String listaElemento = "<li>El vertice FLAG PARA SABER SI MODIFICA congestionado</li>";
			String scriptTag = "var myLatLng = {lat: "+lat+", lng: "+lon+"};" + 
					"var marker = new google.maps.Marker({" + 
					"    position: myLatLng," + 
					"    map: map," + 
					"    title: 'Vertice mas congestionado',"+
					"  animation: google.maps.Animation.DROP," + 
					"  });";
			
			htmlString = htmlString.replace("$Requerimiento", requerimiento);
			htmlString = htmlString.replace("<!--$ListaElemento-->", listaElemento);
			htmlString = htmlString.replace("//$VARIABLESCRIPT", scriptTag);
			File newHtmlFile = new File("viewMap/"+requerimiento+".html");
			FileUtils.writeStringToFile(newHtmlFile, htmlString);		
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void resetActivos() {
		reqMade = new boolean[6];
	}


	public void pintarIndex() {
		try {
			System.out.println(reqMade[0]);
			File htmlTemplateFile = new File("viewMap/templates/templateHub.html");
			String htmlString;
			htmlString = FileUtils.readFileToString(htmlTemplateFile);
			String requerimiento = "index";
			htmlString = htmlString.replace("$Requerimiento", requerimiento);
			String vacio= "";
			if(reqMade[0]) {
				htmlString = htmlString.replace("disabled1",vacio);
			}
			if(reqMade[1]) {
				htmlString = htmlString.replace("disabled2",vacio);
			}
			if(reqMade[2]) {
				htmlString = htmlString.replace("disabled3",vacio);
			}
			if(reqMade[3]) {
				htmlString = htmlString.replace("disabled4",vacio);
			}
			if(reqMade[4]) {
				htmlString = htmlString.replace("disabled5",vacio);
			}
			if(reqMade[5]) {
				htmlString = htmlString.replace("disabled6",vacio);
			}
			
			File newHtmlFile = new File("viewMap/"+requerimiento+".html");
			FileUtils.writeStringToFile(newHtmlFile, htmlString);		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//------------------------------PRUEBA --------------//	
//	File htmlTemplateFile = new File("viewMap/templates/templateMap.html");
//	String htmlString;
//	htmlString = FileUtils.readFileToString(htmlTemplateFile);
//	String requerimiento = "Requerimiento1";
//	String listaElemento = "<li>Hola2</li>";
//	String scriptTag = "var myLatLng = {lat: 41.8500300, lng: -87.6500500};" + 
//			"var marker = new google.maps.Marker({" + 
//			"    position: myLatLng," + 
//			"    map: map," + 
//			"    title: 'Hello World!'" + 
//			"  });"
//			+ "var cityCircle = new google.maps.Circle({\r\n" + 
//			"      strokeColor: '#FF0000',\r\n" + 
//			"      strokeOpacity: 0.8,\r\n" + 
//			"      strokeWeight: 2,\r\n" + 
//			"      fillColor: '#FF0000',\r\n" + 
//			"      fillOpacity: 0.35,\r\n" + 
//			"      map: map,\r\n" + 
//			"      center: {lat: 41.8500300, lng: -87.6500500},\r\n" + 
//			"      radius: 100\r\n" + 
//			"    });\r\n"
//			+ "var cityCircle2 = new google.maps.Circle({\r\n" + 
//			"      strokeColor: '#FFF000',\r\n" + 
//			"      strokeOpacity: 0.8,\r\n" + 
//			"      strokeWeight: 2,\r\n" + 
//			"      fillColor: '#FFf000',\r\n" + 
//			"      fillOpacity: 0.35,\r\n" + 
//			"      map: map,\r\n" + 
//			"      center: {lat: 41.8200300, lng: -87.6200500},\r\n" + 
//			"      radius: 100\r\n" + 
//			"    });\r\n"
//			+"var flightPath = new google.maps.Polyline({\r\n" + 
//			"    path: [{lat: 41.8500300, lng: -87.6500500}, {lat: 41.8200300, lng: -87.6200500}],\r\n" + 
//			"    geodesic: true,\r\n" + 
//			"    strokeColor: '#00FF00',\r\n" + 
//			"    strokeOpacity: 1.0,\r\n" + 
//			"    strokeWeight: 1\r\n" + 
//			"  });flightPath.setMap(map);"
//			
//			
//			;
//			
//	htmlString = htmlString.replace("$Requerimiento", requerimiento);
//	htmlString = htmlString.replace("<!--$ListaElemento-->", listaElemento);
//	htmlString = htmlString.replace("//$VARIABLESCRIPT", scriptTag);
//	File newHtmlFile = new File("viewMap/"+requerimiento+".html");
//	FileUtils.writeStringToFile(newHtmlFile, htmlString);		
	
}
