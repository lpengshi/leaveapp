package sg.edu.iss.leave.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.iss.leave.model.Holiday;
import sg.edu.iss.leave.repo.HolidayRepository;
import sg.edu.iss.leave.util.LeaveFormUtility;
import sg.edu.iss.leave.validator.LeaveApplyFormValidator;

@Service
public class HolidayService {

	@Autowired
	private HolidayRepository hRepo;
	
	public Holiday findHolidayById(int id) {
		Holiday holiday = hRepo.findById(id).orElse(null);
		return holiday;
	}
	
	public List<Holiday> findAllHoliday() {
		List<Holiday> hList = hRepo.findAll();
		return hList;
	}
	
	public Holiday findHolidayByDate(LocalDate date) {
		Holiday holiday = hRepo.findByHolidayDate(date);
		return holiday;
	}

	
	public Holiday updateHoliday(Holiday hol) {
		Holiday holiday = hRepo.save(hol);
		return holiday;
	}

	public void deleteHoliday(Holiday hol) {
		hRepo.delete(hol);
		return;
	}
	
	//Check if selected date is form is a 
	public boolean isPublicHoliday(LocalDate date) {

		List<Holiday> holidayList = this.findAllHoliday();
		for (Holiday holiday : holidayList) {
			if(date.isEqual(holiday.getHolidayDate()) ) {
				return true;
			}
		}
		return false;
	}
	
	public long caluateNetDuration(LocalDate startDate, LocalDate endDate, int cutOffDuration) {

		Logger LOG = LoggerFactory.getLogger(LeaveApplyFormValidator.class);	

		long duration = ChronoUnit.DAYS.between(startDate, endDate) + 1;
		LOG.info( "*** INITIAL DURATION CALCULATED AS "+duration+" ***");

		//Remove weekends and public holidays from application period if period < 14
		if(duration <= cutOffDuration) {
			for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1))
			{
				if(isPublicHoliday(date) || LeaveFormUtility.isAWeekend(date)) {						
					LOG.info( "*** REMOVING "+date.toString()+" ***");						
					duration--;
				}
			}
		}
		return duration;
	}


}
