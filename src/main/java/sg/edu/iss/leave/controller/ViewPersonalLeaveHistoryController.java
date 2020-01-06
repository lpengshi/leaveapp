package sg.edu.iss.leave.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sg.edu.iss.leave.model.LeaveRecord;
import sg.edu.iss.leave.model.Staff;
import sg.edu.iss.leave.service.LeaveBalanceService;
import sg.edu.iss.leave.service.LeaveRecordService;

@Controller
public class ViewPersonalLeaveHistoryController {

	@Autowired
	private LeaveRecordService lrs;
	@Autowired
	private LeaveBalanceService lbs;
	
	//view personal leave history
	@GetMapping("/vplh/list")
    public String viewStaffLeaveRecords(HttpSession session, Model model) {
    	Staff staff = (Staff) session.getAttribute("staff");
        model.addAttribute("staff", staff);
        
       	List<LeaveRecord> staffleaverecord = lrs.getStaffLeavesByYear(staff.getStaffId());
        model.addAttribute("staffleaverecord", staffleaverecord);
        
		model.addAttribute("page", "vplh");
        return "viewleavehistory";
    }
	//view details of each leave application
	@GetMapping("/vplh/{leaveId}/view")
    public String viewLeaveDetails(Model model, @PathVariable("leaveId") int leaveId, HttpSession session) {
    	Staff staff = (Staff) session.getAttribute("staff");
        model.addAttribute("staff", staff);
		
		LeaveRecord leave = lrs.getLeaveById(leaveId);
        model.addAttribute("leave", leave);
		model.addAttribute("page", "vplh");
        return "viewleavedetails";
    }
    //cancel leave application when the status is approved
    @GetMapping("/vplh/{leaveId}/cancel")
    public String cancelLeave(@PathVariable("leaveId") int leaveId, RedirectAttributes redirectAttributes) {
    	LeaveRecord leave = lrs.getLeaveById(leaveId);
    	lrs.cancelLeave(leave);
    	lbs.returnLeaveBalance(leave);
        redirectAttributes.addAttribute("leaveId", leaveId);
    	return "redirect:/vplh/{leaveId}/view";
    }
    //delete leave application when the status is still applied / updated
    @GetMapping("/vplh/{leaveId}/delete")
    public String deleteLeave(@PathVariable("leaveId") int leaveId, RedirectAttributes redirectAttributes) {
    	LeaveRecord leave = lrs.getLeaveById(leaveId);
    	lrs.deleteLeave(leave);
    	lbs.returnLeaveBalance(leave);
        redirectAttributes.addAttribute("leaveId", leaveId);
    	return "redirect:/vplh/{leaveId}/view";
    }
}
