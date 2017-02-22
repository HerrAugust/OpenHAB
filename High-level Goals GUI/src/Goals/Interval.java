package Goals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Interval {

	private Date date_beg, date_end;
	
	/**
	 * Creates an interval
	 * @param inter format: "16:43-16:52"
	 * @throws ParseException if format is not the indicated one 
	 * @throws IntervalException if ending hour is less than beginning hour
	 */
	public Interval(String inter) throws ParseException, Exception {
		String time1 = inter.split("-")[0]; //"16:00";
		String time2 = inter.split("-")[1]; //"19:00";

		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		this.date_beg = format.parse(time1);
		this.date_end = format.parse(time2);
		
		if(date_end.before(date_beg))
			throw new Exception("Hours not in correct order");
	}
	
	public String date_beg() {return new SimpleDateFormat("HH:mm").format(this.date_beg);}
	
	public String date_end() {return new SimpleDateFormat("HH:mm").format(this.date_end);}
	
	/**
	 * Gets interval duration as number of seconds
	 * @return interval duration as number of seconds
	 */
	public int duration() {
		long difference = date_end.getTime() - date_beg.getTime(); 
		return (int) difference / 1000;
	}
	
	@Override
	public String toString() {
		return new  SimpleDateFormat("HH:mm").format(date_beg) + "-" + new SimpleDateFormat("HH:mm").format(date_end);
	}
	
}