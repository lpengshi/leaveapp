package sg.edu.iss.leave.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import sg.edu.iss.leave.javabean.LeaveForm;
import sg.edu.iss.leave.model.LeaveBalance;
import sg.edu.iss.leave.model.LeaveRecord;
import sg.edu.iss.leave.model.LeaveType;
import sg.edu.iss.leave.model.Staff;
import sg.edu.iss.leave.service.HolidayService;
import sg.edu.iss.leave.service.LeaveBalanceService;
import sg.edu.iss.leave.service.LeaveRecordService;
import sg.edu.iss.leave.service.LeaveTypeService;
import sg.edu.iss.leave.validator.LeaveApplyFormValidator;
import sg.edu.iss.leave.validator.LeaveUpdateFormValidator;

@Controller
public class ManageLeaveApplicationController {

	Logger LOG = LoggerFactory.getLogger(ManageLeaveApplicationController.class);
	
	@Autowired
	private HolidayService hs;
	@Autowired
	private LeaveRecordService lrs;
	@Autowired
	private LeaveBalanceService lbs;
	
	@Autowired
	private LeaveTypeService lts;

	@InitBinder("leaveform")
	protected void initBinder(WebDataBinder binder, HttpSession session) {
		binder.addValidators(new LeaveApplyFormValidator(hs, lrs, lbs, session));
	}
	
	@InitBinder("editleaveform")
	protected void initBinder2(WebDataBinder binder, HttpSession session, @PathVariable(value="leaveId") int leaveId) {
		binder.addValidators(new LeaveUpdateFormValidator(hs, lrs, lbs, session, leaveId));
	}
	
	//load form for staff to apply leave
	@GetMapping("/mla/leaveform")	
	public String loadLeaveFormMethod(Model model, HttpSession session) {	
	
		Staff staff = (Staff)session.getAttribute("staff");
		if(session.getAttribute("staff") == null) {
			return "redirect:/stafflogin";
		}
				
		List<LeaveType> leaveTypeList= lts.findAllLeaveType();

		model.addAttribute("staff", staff);
		model.addAttribute("leavetype",leaveTypeList);
		model.addAttribute("leaveform", new LeaveForm());
		model.addAttribute("page", "mla");
		
		return "applyleaveform";
	}

	@PostMapping("/mla/leaveform")
	public String processLeaveFormMethod(@ModelAttribute("leaveform") @Valid LeaveForm leaveForm, BindingResult bindingResult, Model model, HttpSession session)  {
	
		if(session.getAttribute("staff") == null) {
			return "redirect:/stafflogin";
		}	
		
		LOG.info("*** FORM SUBMITTED AND VALIDATED *** : ");

		Staff staff = (Staff)session.getAttribute("staff");	
		model.addAttribute("staff", staff); //put this inside if statement ?
		model.addAttribute("page", "mla"); //put this inside if statement ?
		
		if(bindingResult.hasErrors()) {		
			LOG.info("*** VALIDATED WITH ERRORS *** : "  + leaveForm.toString());
			
			List<LeaveType> leaveTypeList= lts.findAllLeaveType();		
			model.addAttribute("leavetype",leaveTypeList);
			
			return "applyleaveform";
		}
		
		//Get staff id from session
		int staffId = staff.getStaffId();	
		
		//Hard coded Cut-off duration business logic
		int cutOffDuration = 14;		
		int duration = (int) hs.caluateNetDuration(leaveForm.getStartDate(), leaveForm.getEndDate(), cutOffDuration);

		//Calculate new balance - deduct selected leave duration from staff balance 
		LeaveBalance leaveBalance = lbs.findLeaveBalanceByStaffIdAndLeaveTypeId(staffId, leaveForm.getLeaveTypeId());

		int balance = leaveBalance.getBalance();
		balance = balance - duration;
	
		LeaveRecord leaveRecord = new LeaveRecord(staffId, leaveForm.getLeaveTypeId(), leaveForm.getReason(), leaveForm.getStartDate(), leaveForm.getEndDate(), duration, leaveForm.getContactNo(), leaveForm.getHandover(), "Decision pending", "Applied");
		
		LOG.info("*** ATTEMPTING TO PERSIST LEAVE BALANCE : "  + leaveBalance.toString()+" ***");
		leaveBalance.setBalance(balance);
		lbs.updateLeaveBalance(leaveBalance);	
		LOG.info("*** PERSISTENCE METHOD EXECUTED *** : ");
		LOG.info("*** ATTEMPTING TO PERSIST LEAVE RECORD : "  + leaveRecord.toString()+" ***");
		lrs.saveLeaveRecord(leaveRecord);	
		LOG.info("*** PERSISTENCE METHOD EXECUTED *** : ");		
		
		return "redirect:/vplh/list";
	}
	
	@GetMapping("/vplh/{leaveId}/update")	
	public String loadUpdateLeaveFormMethod(Model model, @PathVariable(value="leaveId") int leaveId, HttpSession session) {	
				
		Staff staff = (Staff)session.getAttribute("staff");
		if(session.getAttribute("staff") == null) {
			return "redirect:/stafflogin";
		}
		
		LeaveRecord leaveRecord = lrs.getLeaveById(leaveId);
		if(leaveRecord == null) {
			return "redirect:/vplh/list";
		}
		
		model.addAttribute("staff", staff);
		model.addAttribute("page", "mla");
		model.addAttribute("leavetype",leaveRecord.getLeaveType());		
		model.addAttribute("editleaveform", new LeaveForm(leaveRecord.getLeaveType().getId(), leaveRecord.getReason(), leaveRecord.getHandover(), leaveRecord.getContactNo(), leaveRecord.getStartDate(), leaveRecord.getEndDate()));
		model.addAttribute(leaveId);
		
		return "updateleaveform";
	}

	@PostMapping("/vplh/{leaveId}/update")
	public String processUpdateLeaveFormMethod(@ModelAttribute("editleaveform") @Valid LeaveForm editLeaveForm, BindingResult bindingResult, @PathVariable(value="leaveId") int leaveId, Model model, HttpSession session)  {
		
		if(session.getAttribute("staff") == null) {
			return "redirect:/stafflogin";
		}
		
		//Get staff id from session
		Staff staff = (Staff) session.getAttribute("staff");

		int staffId = staff.getStaffId();
		int duration = 0;
		
		LeaveRecord leaveRecord = lrs.getLeaveById(leaveId);
		
		if(bindingResult.hasErrors()) {		
			LOG.info("*** VALIDATED WITH ERRORS *** : "  + editLeaveForm.toString());
			
			model.addAttribute("staff", staff);
			model.addAttribute("page", "mla");
			model.addAttribute("leavetype",leaveRecord.getLeaveType());		
			model.addAttribute(leaveId);
			
			return "updateleaveform";
		}
		
		//Hard coded Cut-off duration business logic
		int cutOffDuration = 14;		
		duration = (int) hs.caluateNetDuration(editLeaveForm.getStartDate(), editLeaveForm.getEndDate(), cutOffDuration);
		
		//Add back balance and deduct new duration
		LeaveBalance leaveBalance = lbs.findLeaveBalanceByStaffIdAndLeaveTypeId(staffId, editLeaveForm.getLeaveTypeId());
		int balance = leaveBalance.getBalance();
		try {
			balance = balance + leaveRecord.getDuration() - duration;
			if (balance<0) {
				throw new Exception("Balance is negative");
			}
		}
		catch( Exception e ) {
			LOG.info("*** NEGATIVE BALANCE CALCULTED. EXCEPTION HANDLED. ***"+e.getMessage());
		}
		
		LOG.info("*** ATTEMPTING TO UPDATE LEAVE BALANCE : " + leaveBalance.toString()+" ***");
		leaveBalance.setBalance(balance);
		lbs.updateLeaveBalance(leaveBalance);	
		LOG.info("*** PERSISTENCE METHOD EXECUTED *** : ");
		LOG.info("*** ATTEMPTING TO UPDATE LEAVE RECORD : " + leaveRecord.toString()+" ***");
		leaveRecord.setReason(editLeaveForm.getReason()); 
		leaveRecord.setContactNo(editLeaveForm.getContactNo());
		leaveRecord.setStartDate(editLeaveForm.getStartDate());
		leaveRecord.setEndDate(editLeaveForm.getEndDate());
		leaveRecord.setDuration(duration); 
		leaveRecord.setHandover(editLeaveForm.getHandover());
		leaveRecord.setRemark("Decision pending");
		leaveRecord.setStatus("Updated");
		lrs.saveLeaveRecord(leaveRecord);	
		LOG.info("*** PERSISTENCE METHOD EXECUTED *** : ");
		
		return "redirect:/vplh/list";
				
	}
	
}
