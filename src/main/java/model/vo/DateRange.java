package model.vo;

import model.data_structures.LinkedList;
import org.joda.time.DateTime;
import org.joda.time.Seconds;


/**
 * Modela una rango de fechas y horas (iniciales y finales)
 *
 */
public class DateRange implements Comparable<DateRange>
{
	//ATRIBUTOS

    /**
     * Modela la fecha inicial del rango
     */
	private DateTime fechaInicial;

	/**
	 * Modela la fecha final del rango
	 */
	private DateTime fechaFinal;

	/**
	 * Optional service list
	 */
	private LinkedList<Service> services;

	//CONSTRUCTOR
	/**
	 * @param pFechaInicial, fecha inicial del rango
	 * @param pFechaFinal, fecha final del rango
	 */
	public DateRange(String pFechaInicial, String pFechaFinal, String horaInicial, String horaFinal ,LinkedList<Service> services)
	{
		this.fechaFinal = generateDate(pFechaFinal, horaFinal);
		this.fechaInicial = generateDate(pFechaInicial, horaInicial);
		this.services = services;
	}

	public DateRange(String pFechaInicial, String pFechaFinal) {
		this.fechaFinal = generateDate(pFechaFinal);
		this.fechaInicial = generateDate(pFechaInicial);
		this.services = null;
	}

	public DateRange(DateTime fechaInicial, DateTime fechaFinal){
		this.fechaFinal = fechaFinal;
		this.fechaInicial = fechaInicial;
		this.services = null;
	}

	public DateRange(String pFechaInicial, String pFechaFinal, LinkedList services) {
		this.fechaFinal = generateDate(pFechaFinal);
		this.fechaInicial = generateDate(pFechaInicial);
		this.services = services;
	}


	//M�TODOS
	
	
	public static DateTime generateDate(String f) {
		
		int y = 0;
		int m = 0;
		int d = 0;
		int h = 0;
		int min = 0;
		int sec = 0;
		int mil = 0;

		String[] s = f.split("-");
		if(s.length != 3){
			throw new IllegalArgumentException();
		}
		y = Integer.parseInt(s[0]);
		m = Integer.parseInt(s[1]);
		
		String[] dayInfo = s[2].split("T");
		String[] hourAndMin = dayInfo[1].split(":");
		String[] secondsAndMil = hourAndMin[2].split("\\.");
		
		d = Integer.parseInt(dayInfo[0]);
		h = Integer.parseInt(hourAndMin[0]);
		min = Integer.parseInt(hourAndMin[1]);
		sec = Integer.parseInt(secondsAndMil[0]);
		mil = Integer.parseInt(secondsAndMil[1]);
		
		return new DateTime(y, m, d, h, min, sec, mil);
	}
	
	public static DateTime generateDate(String date, String hour) {
		
		int y = 0;
		int m = 0;
		int d = 0;
		int h = 0;
		int min = 0;
		int sec = 0;
		int mil = 0;
		
		String[] s = date.split("-");
		y = Integer.parseInt(s[0]);
		m = Integer.parseInt(s[1]);
		d = Integer.parseInt(s[2]);
		
		String[] hourInfo = hour.split("\\.");
		String[] hourMinSec = hourInfo[0].split(":");		
		h = Integer.parseInt(hourMinSec[0]);
		min = Integer.parseInt(hourMinSec[1]);
		sec = Integer.parseInt(hourMinSec[2]);
		mil = Integer.parseInt(hourInfo[1]);
		
		return new DateTime(y, m, d, h, min, sec, mil);
	}
	
	/**
	 * @return the fechaInicial
	 */
	public DateTime getFechaInicial() 
	{
		return fechaInicial;
	}

	/**
	 * @param fechaInicial the fechaInicial to set
	 */
	public void setFechaInicial(String fechaInicial)
	{
		this.fechaInicial = generateDate(fechaInicial);
	}

	/**
	 * @return the fechaFinal
	 */
	public DateTime getFechaFinal() 
	{
		return fechaFinal;
	}

	/**
	 * @param fechaFinal the fechaFinal to set
	 */
	public void setFechaFinal(String fechaFinal) 
	{
		this.fechaFinal = generateDate(fechaFinal);
	}

	public boolean isInRange(DateRange r) {
		int ini = fechaInicial.compareTo(r.getFechaInicial());
		int end = fechaFinal.compareTo(r.getFechaFinal());
		return (ini >= 0) && (end <= 0);
		
	}

	@Override
	public int compareTo(DateRange o) {
		return fechaInicial.compareTo(o.getFechaInicial());
	}
	
	public static int compareDates(DateTime t1, DateTime t2) {
		return t1.compareTo(t2);
	}

	public int getDiferenceInSeconds() {
		return Seconds.secondsBetween(fechaInicial, fechaFinal).getSeconds();
	}
	
	public static int getDiferenceInSeconds(DateTime ini, DateTime end) {
		return Seconds.secondsBetween(end, ini).getSeconds();
	}

	public static int getGroup(int time, int range){
		return ((Math.abs(time) - 1)/range) + 1;
	}

	public void addService(Service s){
		services.add(s);
	}

	public LinkedList<Service> getServices(){
		return services;
	}

	public void setServices(LinkedList<Service> services){
		this.services = services;
	}

	
}
