package sg.edu.iss.leave.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import sg.edu.iss.leave.model.LeaveRecord;
import sg.edu.iss.leave.model.Staff;
import sg.edu.iss.leave.service.LeaveBalanceService;
import sg.edu.iss.leave.service.LeaveRecordService;
import sg.edu.iss.leave.service.StaffService;

@Controller
public class ViewApplicationForApprovalController {
	@Autowired
	private StaffService ss;
	@Autowired
	private LeaveBalanceService lbs;
	@Autowired
	private LeaveRecordService lrs;
	@Autowired
	private JavaMailSender javaMailSender;
	
	Logger LOG = LoggerFactory.getLogger(ViewApplicationForApprovalController.class);
	//get list of all employee applied/updated leave
	@GetMapping("/vafa/list")
	public String ViewApplication(Model model, HttpSession session) {
		Staff staff = (Staff)session.getAttribute("staff");
		model.addAttribute("staff", staff);
		
		List<Staff> myStfList = ss.findMyStaff(staff);
		List<LeaveRecord> leaRecList = lrs.findListStaffLeaveAppRecords(myStfList);
		model.addAttribute("leaRecList", leaRecList);
		model.addAttribute("page", "vafa");
		return "viewleaveapplicationlist";
	}
	//see detailed view for each leave
	@GetMapping("/vafa/{leaveId}/view")
    public String viewLeaveDetails(Model model, @PathVariable("leaveId") int leaveId, HttpSession session, @ModelAttribute("errMsg") String errMsg) {
    	Staff staff = (Staff) session.getAttribute("staff");
        model.addAttribute("staff", staff);
		
    	LeaveRecord leave = lrs.getLeaveById(leaveId);
        List<Staff> staffList = ss.findAllStaff();
		List<LeaveRecord> overlapList = lrs.findByOverlapping(leave, staff, staffList);
		
		if (!overlapList.isEmpty()) {
			model.addAttribute("overlapPresent", "true");
			model.addAttribute("overlaplist", overlapList);
		} else {
			model.addAttribute("overlapPresent", "false");
		}
       
        model.addAttribute("leave", leave);
		model.addAttribute("page", "vafa");
		model.addAttribute("errMsg", errMsg);
        return "viewleaveapplicationdetails";
    }
	//process leave application and update remarks and status
	@PostMapping("/vafa/{leaveId}/process")
    public RedirectView processLeaveApplication(Model model, @PathVariable("leaveId") int leaveId, HttpSession session, RedirectAttributes attributes, String remark, String decision) {
    	Staff staff = (Staff) session.getAttribute("staff");
        model.addAttribute("staff", staff);
		String msg="";
        
		LeaveRecord leave = lrs.getLeaveById(leaveId);
		
		//setup for email
		SimpleMailMessage emailMsg = new SimpleMailMessage();
		Staff stf = ss.findStaffById(leave.getStaffId());
		emailMsg.setTo(stf.getEmail());
		
		if (remark.isEmpty()) {
			attributes.addFlashAttribute("errMsg", "Remark cannot be empty");
			 return new RedirectView("/vafa/{leaveId}/view");
		} else if (decision.equals("Approve")) {
			leave.setRemark(remark);
			LeaveRecord updateLeave = lrs.approveLeave(leave);
			msg = "Leave#" + updateLeave.getLeaveId() + " was approved successfully";
			LOG.info(msg);
			
			String link = "http://localhost:8080/stafflogin/vplh/" + leave.getLeaveId() + "/view";
			emailMsg.setSubject("LAPS Notification: Leave Application Approved");
			emailMsg.setText(
					"Your leave application has been approved by " + staff.getName() +
					". \n \n You can view more details at: " + link);
			
			javaMailSender.send(emailMsg);
			
		} else if (decision.equals ("Reject")) {
			leave.setRemark(remark);
			LeaveRecord updateLeave = lrs.rejectLeave(leave);
			lbs.returnLeaveBalance(leave);
			msg = "Leave#" + updateLeave.getLeaveId() + " was rejected successfully";
			LOG.info(msg);
			
			String link = "http://localhost:8080/stafflogin/vplh/" + leave.getLeaveId() + "/view";
			emailMsg.setSubject("LAPS Notification: Leave Application Rejected");
			emailMsg.setText(
					"Your leave application has been rejected by " + staff.getName() +
					". \n \n You can view more details at: " + link);
		
			javaMailSender.send(emailMsg);
			
		}
	
		attributes.addFlashAttribute("msg", msg);
        return new RedirectView("/vafa/list");
    }
	
}
