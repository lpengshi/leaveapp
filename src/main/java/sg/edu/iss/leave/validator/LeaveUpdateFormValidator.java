package sg.edu.iss.leave.validator;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import sg.edu.iss.leave.javabean.LeaveForm;
import sg.edu.iss.leave.model.LeaveRecord;
import sg.edu.iss.leave.model.LeaveType;
import sg.edu.iss.leave.model.Staff;
import sg.edu.iss.leave.service.HolidayService;
import sg.edu.iss.leave.service.LeaveBalanceService;
import sg.edu.iss.leave.service.LeaveRecordService;
import sg.edu.iss.leave.util.LeaveFormUtility;

public class LeaveUpdateFormValidator implements Validator{

	@Autowired
	private LeaveRecordService lrs;
	@Autowired
	private HolidayService hs;
	@Autowired
	private LeaveBalanceService lbs;
	
	private HttpSession session;
	private int leaveId;
	
	Logger LOG = LoggerFactory.getLogger(LeaveUpdateFormValidator.class);
	
	public LeaveUpdateFormValidator (HolidayService hs, LeaveRecordService ls, LeaveBalanceService lbs,  HttpSession session, int leaveId) {
		super();
		this.hs = hs;
		this.lrs = ls;
		this.lbs = lbs;
		this.session = session;
		this.leaveId = leaveId;
	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		return LeaveForm.class.equals(clazz) || Staff.class.equals(clazz) || LeaveType.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		Logger LOG = LoggerFactory.getLogger(LeaveUpdateFormValidator.class);
		
		LeaveForm leaveForm = (LeaveForm) target;
		
		//Get staff id from session
		Staff sessionStaff = (Staff) session.getAttribute("staff");
		int sessionStaffId = sessionStaff.getStaffId();
		
		long duration = 0;
		
		LOG.info("*** BUSINESS LOGIC VALIDATION START *** : "  + leaveForm.toString());
		
		//Check Start Date is in this year
		if(leaveForm.getStartDate() != null) {
			if(!LeaveFormUtility.isInThisYear(leaveForm.getStartDate())) {
				errors.rejectValue("startDate","startDateYear");
				LOG.info("*** VALIDATED - START DATE YEAR ***");
			}
		}
		
		//Check End Date is in this year	
		if(leaveForm.getEndDate() != null) {
			if(!LeaveFormUtility.isInThisYear(leaveForm.getEndDate())) {
				errors.rejectValue("endDate","startDateYear");
				LOG.info("*** VALIDATED - END DATE YEAR ***");
			}
		}
		
		//Check startDate is not a weekend or public holiday
		if(leaveForm.getStartDate() != null) {
			if(LeaveFormUtility.isAWeekend(leaveForm.getStartDate()) || hs.isPublicHoliday(leaveForm.getStartDate())) {
				errors.rejectValue("startDate","startDateHoliday");		
				LOG.info("*** VALIDATED - START DATE HOLIDAY ***");
			}
		}

		//Check endDate is not weekend or public holiday
		if(leaveForm.getEndDate() != null) {
			if(LeaveFormUtility.isAWeekend(leaveForm.getEndDate()) || hs.isPublicHoliday(leaveForm.getEndDate())) {
				errors.rejectValue("endDate","endDateHoliday");		
				LOG.info("*** VALIDATED - END DATE HOLIDAY ***");
			}	
		}
	
		if(leaveForm.getStartDate() != null && leaveForm.getEndDate() != null) {
						
			//Check Start Date is less than End Date
			if(leaveForm.getEndDate().isBefore(leaveForm.getStartDate()) ) {
				errors.rejectValue("endDate","endDateBeforeStartDate");		
				LOG.info("*** VALIDATED - START AND END DATE ORDER ***");
			}		
			LOG.info("*** LEAVE ID : "+leaveId+"***");
			
			//Check if selected duration falls in the duration of a leave already applied
			List<LeaveRecord> retreivedLeaveRecord = lrs.getStaffLeavesByYear(sessionStaffId);
			for (LeaveRecord leaveRecordItem : retreivedLeaveRecord) {
				if(leaveRecordItem.getStaffId() == sessionStaffId 
						&& !leaveRecordItem.getStatus().toLowerCase().equalsIgnoreCase("cancelled")
						&& !leaveRecordItem.getStatus().toLowerCase().equalsIgnoreCase("rejected")
						&& !leaveRecordItem.getStatus().toLowerCase().equalsIgnoreCase("deleted")
						&& leaveRecordItem.getLeaveId() != leaveId) { 
					//	&& !leaveForm.getStartDate().equals(leaveRecordItem.getStartDate())
					//	&& !leaveForm.getEndDate().equals(leaveRecordItem.getEndDate())
					//	&& !leaveForm.getStartDate().equals(leaveRecordItem.getEndDate())) {
					
					//Start Date already exists
					if (leaveForm.getStartDate().equals(leaveRecordItem.getStartDate())) {
						errors.rejectValue("startDate","existingStartDate");
					}
					
					//End Date already exists
					if (leaveForm.getEndDate().equals(leaveRecordItem.getEndDate())) {	
						errors.rejectValue("endDate","existingEndDate");	
					}
					
					//Start date matches with existing end date
					if (leaveForm.getStartDate().equals(leaveRecordItem.getEndDate())) {	
						errors.rejectValue("startDate","sameStartEndDate");	
					}
					
					//End date matches with existing start date
					if (leaveForm.getEndDate().equals(leaveRecordItem.getStartDate())) {	
						errors.rejectValue("endDate","sameStartEndDate");	
					}
										
					//Start Date falls in the existing duration
					if(!errors.hasFieldErrors("startDate")) {
					  if (leaveForm.getStartDate().isAfter(leaveRecordItem.getStartDate()) && leaveForm.getStartDate().isBefore(leaveRecordItem.getEndDate()) ) {
					    errors.rejectValue("startDate", "startDateFallsInside");
					  }
					}

					//End Date falls in the existing duration
					if(!errors.hasFieldErrors("endDate")) {
					  if (leaveForm.getEndDate().isAfter(leaveRecordItem.getStartDate()) && leaveForm.getEndDate().isBefore(leaveRecordItem.getEndDate()) ) {
					    errors.rejectValue("endDate", "endDateFallsInside");
					  }
					}

					//Existing Start Date falls in the applying duration
					if(!errors.hasFieldErrors("startDate")) {
					  if (leaveRecordItem.getStartDate().isAfter(leaveForm.getStartDate()) && leaveRecordItem.getStartDate().isBefore(leaveForm.getEndDate()) ) {
					    errors.rejectValue("startDate", "existingStartDateFallsInside");
					  }
					}

					//Existing End Date falls in the duration
					if(!errors.hasFieldErrors("endDate")) {
					  if (leaveRecordItem.getEndDate().isAfter(leaveForm.getStartDate()) && leaveRecordItem.getEndDate().isBefore(leaveForm.getEndDate()) ) {
					    errors.rejectValue("endDate", "existingEndDateFallsInside");
					  }
					}
				}
			}
			LOG.info("*** FINISHED ITERATING THROUGH LEAVE RECORDS ***");	
			
			//Calculate duration and remove weekends and public holidays from application period if period <= 14
			duration = hs.caluateNetDuration(leaveForm.getStartDate(), leaveForm.getEndDate(), 14);			
			LOG.info( "*** FINAL DURATION CALCULATED AS "+duration+" ***");
		
			//Check duration is less than the balance leave the staff has
			int balance = lbs.getLeaveBalance(sessionStaffId, leaveForm.getLeaveTypeId());
		
			//Add balance back duration of leave being edited for checking
			LeaveRecord leaveRecord = lrs.getLeaveById(leaveId);
			balance = balance + leaveRecord.getDuration();;

				if(duration > balance) {
					errors.rejectValue("endDate","DurationError");	
					LOG.info("*** BALANCE VALIDATED *** ");		
				}
				LOG.info("*** BALANCE RETREIVED FOR LEAVE TYPE ID "+leaveForm.getLeaveTypeId()+" AS : "+balance+" ***");		

		}		
		
		LOG.info("*** EXITING VALIDATOR FUNCTION ***");	
	}

}
