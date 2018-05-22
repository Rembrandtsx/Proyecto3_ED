package mapsDraw;


import java.io.File;
import java.io.IOException;
import java.util.Iterator;


import model.data_structures.*;
import org.apache.commons.io.FileUtils;

import model.vo.ArcServices;

public class MapsDrawer {
	public static final String RUTAPRINCIPAL = "viewMap/index.html";

	private boolean reqMade[] = new boolean[6];
	
	public void dibujoRequerimiento1(double lat, double lon){
		System.out.println("Se Creo Mapa de Requerimiento 1");
		
		try {
			reqMade[0]=true;
			
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
	
	public void dibujoRequerimiento2(IArrayList vertices, String color) {
		
		System.out.println("Se Creo Mapa de Requerimiento 2");
		
		try {
			reqMade[1]=true;
			
			File htmlTemplateFile = new File("viewMap/templates/templateMap.html");
			String htmlString;
			htmlString = FileUtils.readFileToString(htmlTemplateFile);
			String requerimiento = "Requerimiento2";
			String listaElemento = "<li>La componente fuertemente conexa con mayor cantidad de vertices</li>";
			
			String scriptTag = "var latlons=[";
			for(int i = 0; i <vertices.size();i++) {
				
				String[] latlons= vertices.get(i).toString().split("[|]");
				
				scriptTag += "{lat: "+latlons[0]+", lng:"+latlons[1]+" }";
						if(i < vertices.size()-1)
							scriptTag +=",";
			}
			scriptTag +="];"
					+ "var arcos = new google.maps.Polyline({\n" + 
					"          path: latlons,\n" + 
					"          geodesic: true,\n" + 
					"          strokeColor: '"+color+"',\n" + 
					"          strokeOpacity: 1.0,\n" + 
					"          strokeWeight: 2,\n"+
					"		   map:map\n" + 
					"        });"+ 
					"latlons.forEach(function(elemento) {"
					+ "var circulillo = new google.maps.Circle({\n" + 
					"            strokeColor: '"+color+"',\n" + 
					"            strokeOpacity: 0.8,\n" + 
					"            strokeWeight: 2,\n" + 
					"            fillColor: '"+color+"',\n" + 
					"            fillOpacity: 0.35,\n" + 
					"            map: map,\n" + 
					"            center: elemento,\n" + 
					"            radius: 100\n" + 
					"          });"
					+ "});";
			
			
			
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
	
	
	public void dibujoRequerimiento3(IArrayList<IHashMap<String, Double>> arrayParaMapa, IArrayList<String> colores) {
		System.out.println("Se Creo Mapa de Requerimiento 3");
		
		try {
			reqMade[2]=true;
			
			File htmlTemplateFile = new File("viewMap/templates/templateMap.html");
			String htmlString;
			htmlString = FileUtils.readFileToString(htmlTemplateFile);
			String requerimiento = "Requerimiento3";
			String listaElemento = "<li>Todas las Componentes fuertemente conexas en el grafo</li>"
					+ "<li>Los vertices que pertenecen a las componentes con tamaño dados por su densidad</li>";
					
			
			
				//[[{"{lat, lon}":radio},{"{lat, lon}":radio},"{lat, lon}":radio{},"{lat, lon}":radio}],[....]]
			
			String scriptTag = "var latlons = new Array();\n"
					+ "var radius = new Array(); \n";
			scriptTag+="var colores=new Array();\n";
			try {
			for(int i=0; i<arrayParaMapa.size();i++) {
				scriptTag+="colores.push('"+colores.get(i)+"');\n";
				scriptTag+="var arreglo"+i+" = new Array();\n";
				scriptTag+="var arregloR"+i+" = new Array();\n";
				IHashMap<String, Double> hash= arrayParaMapa.get(i);
				Iterator<String> keys = hash.keys();
				
				while(keys.hasNext()) {
						String f = keys.next();
						String[] latlons = f.split("[|]");
						scriptTag+="arreglo"+i+".push({lat: "+latlons[0]+", lng: "+latlons[1]+"}) ;\n";
						scriptTag+="arregloR"+i+".push("+hash.get(f)+");\n";
					
					
				}
				scriptTag+="latlons.push(arreglo"+i+");\n";
				scriptTag+="radius.push(arregloR"+i+");\n";
				
			}
			
			}catch(Exception e) {
				e.printStackTrace();
			}
			scriptTag = scriptTag.substring(0, scriptTag.length() - 1);
			scriptTag += "\n"
					+ "console.log(latlons);\n"
					+ "console.log(radius);\n"
					+ "console.log(colores)\n"
					+ "var i = 0;\n" + 
					"for(ubc in latlons){\n" + 
					"console.log(ubc);\n" + 
					"var polyline = new google.maps.Polyline({\n" + 
					"          path: latlons[ubc],\n" + 
					"          geodesic: true,\n" + 
					"          strokeColor: colores[i],\n" + 
					"          strokeOpacity: 1.0,\n" + 
					"          strokeWeight: 2,\n" + 
					"          map:map\n" + 
					"        });\n" + 
					
					"\n" + 
					"\n" + 
					"i++;\n" + 
					"}\n"					
					+ "i=0;\n" + 
					"latlons.forEach((e)=>{\n" + 
					"\n" + 
					"  j=0;\n" + 
					"  e.forEach((x)=>{\n" + 
					"\n" + 
					"    console.log(radius[i][j]);\n" + 
					"\n" + 
					"    var compCircle = new google.maps.Circle({\n" + 
					"                strokeColor: colores[i],\n" + 
					"                strokeOpacity: 0.8,\n" + 
					"                strokeWeight: 2,\n" + 
					"                fillColor: colores[i],\n" + 
					"                fillOpacity: 0.35,\n" + 
					"                map: map,\n" + 
					"                center: x,\n" + 
					"                radius: radius[i][j]* 1000\n" + 
					"              });\n" + 
					"j++;\n" + 
					"  })\n" + 
					"  i++;\n" + 
					"})";
				
			
			
			htmlString = htmlString.replace("$Requerimiento", requerimiento);
			htmlString = htmlString.replace("<!--$ListaElemento-->", listaElemento);
			htmlString = htmlString.replace("//$VARIABLESCRIPT", scriptTag);
			File newHtmlFile = new File("viewMap/"+requerimiento+".html");
			FileUtils.writeStringToFile(newHtmlFile, htmlString);		
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("NO SE PUDO PINTAR EL REQUERIMIENTO 3");
			e.printStackTrace();
		}
		
		
		
		
		
		
		
	}
	
	
	public void dibujoRequerimiento4(LinkedList<ArcServices> lista, String[] callesIni, String[] callesFin) {
		System.out.println("Se Creo Mapa de Requerimiento 4");
		
		try {
			reqMade[3]=true;
			
			File htmlTemplateFile = new File("viewMap/templates/templateMap.html");
			String htmlString;
			htmlString = FileUtils.readFileToString(htmlTemplateFile);
			String requerimiento = "Requerimiento4";
			String listaElemento = "<li>El camino mas corto entre 2 puntos generados aleatoreamente del archivo Streets.csv </li>\n"
					+ "<li>Los puntos Aleatoreamente generados del Streets.csv fueron:</li><ul>"
						+ "<li>INICIO</li> <ul>"
						+ "<li>lat:"+callesIni[0]+"</li>"
						+ "<li>lng:"+callesIni[1]+"</li> "
						+ "</ul>"
						+ "<li>FIN:</li><ul>"
						+ "<li>lat:"+callesFin[0]+"</li>"
						+ "<li>lng:"+callesFin[1]+"</li>"
						+ "</ul>"
						+ "</ul> ";
			String scriptTag = "";
			if(!lista.isEmpty()) {
				scriptTag += "var arregloDirecciones =[ ";
				lista.listing();
				for(int i =0; i <lista.size(); i++) {
					String[] valor1 = lista.getCurrent().getIniVertex().split("[|]");
					String[] valor2 = lista.getCurrent().getEndVertex().split("[|]");
					scriptTag += "{lat:"+valor1[0]+",lng: "+valor1[1]+"},{lat:"+valor2[0]+",lng: "+valor2[1]+"},";	
					lista.next();
				}
				scriptTag = scriptTag.substring(0, scriptTag.length()-1);
				scriptTag += "];\n";
				scriptTag += "var color = '#'+Math.floor(Math.random()*16777215).toString(16);\n";
				scriptTag += "var flightPath = new google.maps.Polyline({\n" + 
						"          path: arregloDirecciones,\n" + 
						"          geodesic: true,\n" + 
						"          strokeColor: color,\n" + 
						"          strokeOpacity: 1.0,\n" + 
						"          strokeWeight: 2,\n"+ 
						"		   map:map" + 
						"        });\n"
						+ "arregloDirecciones.forEach((e)=>{"
						+ "var cityCircle = new google.maps.Circle({\n" + 
						"            strokeColor: color,\n" + 
						"            strokeOpacity: 0.8,\n" + 
						"            strokeWeight: 2,\n" + 
						"            fillColor: color,\n" + 
						"            fillOpacity: 0.35,\n" + 
						"            map: map,\n" + 
						"            center: e,\n" + 
						"            radius: 100\n" + 
						"          });"
						+ ""
						+ "});\n"
						+ "var image = 'https://developers.google.com/maps/documentation/javascript/examples/full/images/beachflag.png';\n" + 
						"        var marcadorCalle1 = new google.maps.Marker({\n" + 
						"          position: {lat: "+callesIni[0]+", lng: "+callesIni[1]+"},\n" + 
						"          map: map,\n" + 
						"          icon: image,\n"
						+ "			label:'INI'\n" + 
						"        });\n"
						+ "var marcadorCalle2 = new google.maps.Marker({\n" + 
						"          position: {lat: "+callesFin[0]+", lng: "+callesFin[1]+"},\n" + 
						"          map: map,\n" + 
						"          icon: image,\n"
						+ "			label: 'FIN'\n" + 
						"        });\n"
						+ "var color2= '#'+Math.floor(Math.random()*16777215).toString(16);"
						+ "var coneccionCalles = new google.maps.Polyline({\n" + 
						"          path: [{lat: "+callesIni[0]+", lng: "+callesIni[1]+"},{lat: "+callesFin[0]+", lng: "+callesFin[1]+"}],\n" + 
						"          geodesic: true,\n" + 
						"          strokeColor: color2,\n" + 
						"          strokeOpacity: 1.0,\n" + 
						"          strokeWeight: 2\n" + 
						"        });"
						+ "var directionsService = new google.maps.DirectionsService;\n"
						+ "var directionsDisplay = new google.maps.DirectionsRenderer({suppressMarkers: true});\n"
						+ "directionsDisplay.setMap(map);\n"
						+ "directionsService.route({\n" + 
						"          origin: {lat:"+callesIni[0]+",lng:"+callesIni[1]+"},\n" + 
						"          destination: {lat:"+callesFin[0]+",lng:"+callesFin[1]+"},\n" + 
						"          travelMode: 'DRIVING'\n" + 
						"        }, function(response, status) {\n" + 
						"          if (status === 'OK') {\n" + 
						"            directionsDisplay.setDirections(response);\n" + 
						"          } else {\n" + 
						"            window.alert('No Se Pudo Desplegar la direccion por: ' + status);\n" + 
						"          }\n" + 
						"        });"
						+ "var markerVertex1 = new google.maps.Marker({\n" + 
						"          position: arregloDirecciones[0],\n" + 
						"          map: map,\n" + 
						"          title: 'Vertice Inicial',\n"
						+ "		   label: 'INI'" + 
						"        });\n"
						+ "var markerVertex2 = new google.maps.Marker({\n" + 
						"          position: arregloDirecciones[arregloDirecciones.length -1],\n" + 
						"          map: map,\n" + 
						"          title: 'Vertice Final',\n"
						+ "			label:'FIN'" + 
						"        });\n";
			}else {
				scriptTag += "var image = 'https://developers.google.com/maps/documentation/javascript/examples/full/images/beachflag.png';\n" + 
						"        var marcadorCalle1 = new google.maps.Marker({\n" + 
						"          position: {lat: "+callesIni[0]+", lng: "+callesIni[1]+"},\n" + 
						"          map: map,\n" + 
						"          icon: image,\n"
						+ "			label:'INI'\n" + 
						"        });\n"
						+ "var marcadorCalle2 = new google.maps.Marker({\n" + 
						"          position: {lat: "+callesFin[0]+", lng: "+callesFin[1]+"},\n" + 
						"          map: map,\n" + 
						"          icon: image,\n"
						+ "			label: 'FIN'\n" + 
						"        });\n"
						+ "var color2= '#'+Math.floor(Math.random()*16777215).toString(16);"
						+ "var coneccionCalles = new google.maps.Polyline({\n" + 
						"          path: [{lat: "+callesIni[0]+", lng: "+callesIni[1]+"},{lat: "+callesFin[0]+", lng: "+callesFin[1]+"}],\n" + 
						"          geodesic: true,\n" + 
						"          strokeColor: color2,\n" + 
						"          strokeOpacity: 1.0,\n" + 
						"          strokeWeight: 2\n" + 
						"        });"
						+ "var directionsService = new google.maps.DirectionsService;\n"
						+ "var directionsDisplay = new google.maps.DirectionsRenderer({suppressMarkers: true});\n"
						+ "directionsDisplay.setMap(map);\n"
						+ "directionsService.route({\n" + 
						"          origin: {lat:"+callesIni[0]+",lng:"+callesIni[1]+"},\n" + 
						"          destination: {lat:"+callesFin[0]+",lng:"+callesFin[1]+"},\n" + 
						"          travelMode: 'DRIVING'\n" + 
						"        }, function(response, status) {\n" + 
						"          if (status === 'OK') {\n" + 
						"            directionsDisplay.setDirections(response);\n" + 
						"          } else {\n" + 
						"            window.alert('No Se Pudo Desplegar la direccion por: ' + status);\n" + 
						"          }\n" + 
						"        });";
				listaElemento = "<li>Debido a que no se encontraron caminos entre el grafo entre los puntos dados se imprime la ruta optima generada por google maps.</li>";
			}
		
			
			
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
	public void dibujoRequerimiento5(ArrayList<LinkedList<ArcServices>> lista, String[] callesIni, String[] callesFin) {
		System.out.println("Se Creo Mapa de Requerimiento 5");
		
		try {
			reqMade[4]=true;
			
			File htmlTemplateFile = new File("viewMap/templates/templateMap.html");
			String htmlString;
			htmlString = FileUtils.readFileToString(htmlTemplateFile);
			String requerimiento = "Requerimiento5";
			String listaElemento = "<li>El camino mas corto entre 2 puntos generados aleatoreamente del archivo Streets.csv </li>\n"
					+ "<li>Los puntos Aleatoreamente generados del Streets.csv fueron:</li><ul>"
						+ "<li>INICIO</li> <ul>"
						+ "<li>lat:"+callesIni[0]+"</li>"
						+ "<li>lng:"+callesIni[1]+"</li> "
						+ "</ul>"
						+ "<li>FIN:</li><ul>"
						+ "<li>lat:"+callesFin[0]+"</li>"
						+ "<li>lng:"+callesFin[1]+"</li>"
						+ "</ul>"
						+ "</ul> ";
			String scriptTag = "";
			if(!lista.get(0).isEmpty()) {
				scriptTag += "var arregloDirecciones =[ ";
				lista.get(0).listing();
				for(int i =0; i <lista.get(0).size(); i++) {
					String[] valor1 = lista.get(0).getCurrent().getIniVertex().split("[|]");
					String[] valor2 = lista.get(0).getCurrent().getEndVertex().split("[|]");
					scriptTag += "{lat:"+valor1[0]+",lng: "+valor1[1]+"},{lat:"+valor2[0]+",lng: "+valor2[1]+"},";	
					lista.get(0).next();
				}
				scriptTag = scriptTag.substring(0, scriptTag.length()-1);
				scriptTag += "];\n";
				scriptTag += "var color = '#'+Math.floor(Math.random()*16777215).toString(16);\n";
				scriptTag += "var flightPath = new google.maps.Polyline({\n" + 
						"          path: arregloDirecciones,\n" + 
						"          geodesic: true,\n" + 
						"          strokeColor: color,\n" + 
						"          strokeOpacity: 1.0,\n" + 
						"          strokeWeight: 2,\n"+ 
						"		   map:map" + 
						"        });\n"
						+ "arregloDirecciones.forEach((e)=>{"
						+ "var cityCircle = new google.maps.Circle({\n" + 
						"            strokeColor: color,\n" + 
						"            strokeOpacity: 0.8,\n" + 
						"            strokeWeight: 2,\n" + 
						"            fillColor: color,\n" + 
						"            fillOpacity: 0.35,\n" + 
						"            map: map,\n" + 
						"            center: e,\n" + 
						"            radius: 100\n" + 
						"          });"
						+ ""
						+ "});\n"
						+ "var image = 'https://developers.google.com/maps/documentation/javascript/examples/full/images/beachflag.png';\n" + 
						"        var marcadorCalle1 = new google.maps.Marker({\n" + 
						"          position: {lat: "+callesIni[0]+", lng: "+callesIni[1]+"},\n" + 
						"          map: map,\n" + 
						"          icon: image,\n"
						+ "			label:'INI'\n" + 
						"        });\n"
						+ "var marcadorCalle2 = new google.maps.Marker({\n" + 
						"          position: {lat: "+callesFin[0]+", lng: "+callesFin[1]+"},\n" + 
						"          map: map,\n" + 
						"          icon: image,\n"
						+ "			label: 'FIN'\n" + 
						"        });\n"
						+ "var color2= '#'+Math.floor(Math.random()*16777215).toString(16);"
						+ "var coneccionCalles = new google.maps.Polyline({\n" + 
						"          path: [{lat: "+callesIni[0]+", lng: "+callesIni[1]+"},{lat: "+callesFin[0]+", lng: "+callesFin[1]+"}],\n" + 
						"          geodesic: true,\n" + 
						"          strokeColor: color2,\n" + 
						"          strokeOpacity: 1.0,\n" + 
						"          strokeWeight: 2\n" + 
						"        });"
						+ "var directionsService = new google.maps.DirectionsService;\n"
						+ "var directionsDisplay = new google.maps.DirectionsRenderer({suppressMarkers: true});\n"
						+ "directionsDisplay.setMap(map);\n"
						+ "directionsService.route({\n" + 
						"          origin: {lat:"+callesIni[0]+",lng:"+callesIni[1]+"},\n" + 
						"          destination: {lat:"+callesFin[0]+",lng:"+callesFin[1]+"},\n" + 
						"          travelMode: 'DRIVING'\n" + 
						"        }, function(response, status) {\n" + 
						"          if (status === 'OK') {\n" + 
						"            directionsDisplay.setDirections(response);\n" + 
						"          } else {\n" + 
						"            window.alert('No Se Pudo Desplegar la direccion por: ' + status);\n" + 
						"          }\n" + 
						"        });"
						+ "var markerVertex1 = new google.maps.Marker({\n" + 
						"          position: arregloDirecciones[0],\n" + 
						"          map: map,\n" + 
						"          title: 'Vertice Inicial',\n"
						+ "		   label: 'INI'" + 
						"        });\n"
						+ "var markerVertex2 = new google.maps.Marker({\n" + 
						"          position: arregloDirecciones[arregloDirecciones.length -1],\n" + 
						"          map: map,\n" + 
						"          title: 'Vertice Final',\n"
						+ "			label:'FIN'" + 
						"        });\n";
			}else {
				scriptTag += "var image = 'https://developers.google.com/maps/documentation/javascript/examples/full/images/beachflag.png';\n" + 
						"        var marcadorCalle1 = new google.maps.Marker({\n" + 
						"          position: {lat: "+callesIni[0]+", lng: "+callesIni[1]+"},\n" + 
						"          map: map,\n" + 
						"          icon: image,\n"
						+ "			label:'INI'\n" + 
						"        });\n"
						+ "var marcadorCalle2 = new google.maps.Marker({\n" + 
						"          position: {lat: "+callesFin[0]+", lng: "+callesFin[1]+"},\n" + 
						"          map: map,\n" + 
						"          icon: image,\n"
						+ "			label: 'FIN'\n" + 
						"        });\n"
						+ "var color2= '#'+Math.floor(Math.random()*16777215).toString(16);"
						+ "var coneccionCalles = new google.maps.Polyline({\n" + 
						"          path: [{lat: "+callesIni[0]+", lng: "+callesIni[1]+"},{lat: "+callesFin[0]+", lng: "+callesFin[1]+"}],\n" + 
						"          geodesic: true,\n" + 
						"          strokeColor: color2,\n" + 
						"          strokeOpacity: 1.0,\n" + 
						"          strokeWeight: 2\n" + 
						"        });"
						+ "var directionsService = new google.maps.DirectionsService;\n"
						+ "var directionsDisplay = new google.maps.DirectionsRenderer({suppressMarkers: true});\n"
						+ "directionsDisplay.setMap(map);\n"
						+ "directionsService.route({\n" + 
						"          origin: {lat:"+callesIni[0]+",lng:"+callesIni[1]+"},\n" + 
						"          destination: {lat:"+callesFin[0]+",lng:"+callesFin[1]+"},\n" + 
						"          travelMode: 'DRIVING'\n" + 
						"        }, function(response, status) {\n" + 
						"          if (status === 'OK') {\n" + 
						"            directionsDisplay.setDirections(response);\n" + 
						"          } else {\n" + 
						"            window.alert('No Se Pudo Desplegar la direccion por: ' + status);\n" + 
						"          }\n" + 
						"        });";
				
			}
			if(!lista.get(1).isEmpty()) {
				scriptTag += "var arregloDirecciones1 =[ ";
				lista.get(1).listing();
				for(int i =0; i <lista.get(1).size(); i++) {
					String[] valor1 = lista.get(1).getCurrent().getIniVertex().split("[|]");
					String[] valor2 = lista.get(1).getCurrent().getEndVertex().split("[|]");
					scriptTag += "{lat:"+valor1[0]+",lng: "+valor1[1]+"},{lat:"+valor2[0]+",lng: "+valor2[1]+"},";	
					lista.get(1).next();
				}
				scriptTag = scriptTag.substring(0, scriptTag.length()-1);
				scriptTag += "];\n";
				scriptTag += "var color1 = '#'+Math.floor(Math.random()*16777215).toString(16);\n";
				scriptTag += "var flightPath1 = new google.maps.Polyline({\n" + 
						"          path: arregloDirecciones1,\n" + 
						"          geodesic: true,\n" + 
						"          strokeColor: color1,\n" + 
						"          strokeOpacity: 1.0,\n" + 
						"          strokeWeight: 2,\n"+ 
						"		   map:map" + 
						"        });\n"
						+ "arregloDirecciones1.forEach((e)=>{"
						+ "var cityCircle1 = new google.maps.Circle({\n" + 
						"            strokeColor: color1,\n" + 
						"            strokeOpacity: 0.8,\n" + 
						"            strokeWeight: 2,\n" + 
						"            fillColor: color1,\n" + 
						"            fillOpacity: 0.35,\n" + 
						"            map: map,\n" + 
						"            center: e,\n" + 
						"            radius: 100\n" + 
						"          });"
						+ ""
						+ "});\n"
						+ "var image1 = 'https://developers.google.com/maps/documentation/javascript/examples/full/images/beachflag.png';\n" + 
						"        var marcadorCalle1 = new google.maps.Marker({\n" + 
						"          position: {lat: "+callesIni[0]+", lng: "+callesIni[1]+"},\n" + 
						"          map: map,\n" + 
						"          icon: image1,\n"
						+ "			label:'INI'\n" + 
						"        });\n"
						+ "var marcadorCalle21 = new google.maps.Marker({\n" + 
						"          position: {lat: "+callesFin[0]+", lng: "+callesFin[1]+"},\n" + 
						"          map: map,\n" + 
						"          icon: image1,\n"
						+ "			label: 'FIN'\n" + 
						"        });\n"
						+ "var color21= '#'+Math.floor(Math.random()*16777215).toString(16);"
						+ "var coneccionCalles1 = new google.maps.Polyline({\n" + 
						"          path: [{lat: "+callesIni[0]+", lng: "+callesIni[1]+"},{lat: "+callesFin[0]+", lng: "+callesFin[1]+"}],\n" + 
						"          geodesic: true,\n" + 
						"          strokeColor: color21,\n" + 
						"          strokeOpacity: 1.0,\n" + 
						"          strokeWeight: 2\n" + 
						"        });"
						+ "var directionsService1 = new google.maps.DirectionsService;\n"
						+ "var directionsDisplay1 = new google.maps.DirectionsRenderer({suppressMarkers: true});\n"
						+ "directionsDisplay1.setMap(map);\n"
						+ "directionsService1.route({\n" + 
						"          origin: {lat:"+callesFin[0]+",lng:"+callesFin[1]+"},\n" + 
						"          destination: {lat:"+callesIni[0]+",lng:"+callesIni[1]+"},\n" + 
						"          travelMode: 'DRIVING'\n" + 
						"        }, function(response, status) {\n" + 
						"          if (status === 'OK') {\n" + 
						"            directionsDisplay.setDirections(response);\n" + 
						"          } else {\n" + 
						"            window.alert('No Se Pudo Desplegar la direccion por: ' + status);\n" + 
						"          }\n" + 
						"        });"
						+ "var markerVertex11 = new google.maps.Marker({\n" + 
						"          position: arregloDirecciones1[arregloDirecciones1.length -1],\n" + 
						"          map: map,\n" + 
						"          title: 'Vertice Inicial',\n"
						+ "		   label: 'INI'" + 
						"        });\n"
						+ "var markerVertex21 = new google.maps.Marker({\n" + 
						"          position: arregloDirecciones1[0],\n" + 
						"          map: map,\n" + 
						"          title: 'Vertice Final',\n"
						+ "			label:'FIN'" + 
						"        });\n";
			}else {
				scriptTag += "var image1 = 'https://developers.google.com/maps/documentation/javascript/examples/full/images/beachflag.png';\n" + 
						"        var marcadorCalle1 = new google.maps.Marker({\n" + 
						"          position: {lat: "+callesIni[0]+", lng: "+callesIni[1]+"},\n" + 
						"          map: map,\n" + 
						"          icon: image,\n"
						+ "			label:'FIN'\n" + 
						"        });\n"
						+ "var marcadorCalle21 = new google.maps.Marker({\n" + 
						"          position: {lat: "+callesFin[0]+", lng: "+callesFin[1]+"},\n" + 
						"          map: map,\n" + 
						"          icon: image,\n"
						+ "			label: 'INI'\n" + 
						"        });\n"
						+ "var color21= '#'+Math.floor(Math.random()*16777215).toString(16);"
						+ "var coneccionCalles1 = new google.maps.Polyline({\n" + 
						"          path: [{lat: "+callesIni[0]+", lng: "+callesIni[1]+"},{lat: "+callesFin[0]+", lng: "+callesFin[1]+"}],\n" + 
						"          geodesic: true,\n" + 
						"          strokeColor: color2,\n" + 
						"          strokeOpacity: 1.0,\n" + 
						"          strokeWeight: 2\n" + 
						"        });"
						+ "var directionsService1 = new google.maps.DirectionsService;\n"
						+ "var directionsDisplay1 = new google.maps.DirectionsRenderer({suppressMarkers: true});\n"
						+ "directionsDisplay1.setMap(map);\n"
						+ "directionsService1.route({\n" + 
						"          origin: {lat:"+callesFin[0]+",lng:"+callesFin[1]+"},\n" + 
						"          destination: {lat:"+callesIni[0]+",lng:"+callesIni[1]+"},\n" + 
						"          travelMode: 'DRIVING'\n" + 
						"        }, function(response, status) {\n" + 
						"          if (status === 'OK') {\n" + 
						"            directionsDisplay.setDirections(response);\n" + 
						"          } else {\n" + 
						"            window.alert('No Se Pudo Desplegar la direccion por: ' + status);\n" + 
						"          }\n" + 
						"        });";
				
			}
		
			
			
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
	public void dibujoRequerimiento6() {
		
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
