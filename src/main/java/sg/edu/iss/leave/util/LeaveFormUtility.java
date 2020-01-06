package sg.edu.iss.leave.util;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class LeaveFormUtility {
	
	public static boolean isAWeekend(LocalDate date) {
		if(date.getDayOfWeek() == DayOfWeek.SUNDAY || date.getDayOfWeek() == DayOfWeek.SATURDAY) {
			return true;
		}
		return false;
	}
	
	public static boolean isInThisYear(LocalDate date) {
			
		LocalDate systemDate = LocalDate.now();
		
		if(date.getYear() == systemDate.getYear()) {
			return true;
		}	
		return false;
	}
	
}
