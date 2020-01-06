package sg.edu.iss.leave.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import sg.edu.iss.leave.model.LeaveRecord;
import sg.edu.iss.leave.model.Staff;
import sg.edu.iss.leave.service.LeaveRecordService;
import sg.edu.iss.leave.service.StaffService;

@Controller
public class ViewEmployeeLeaveHistoryController {
	
	@Autowired
	private StaffService ss;
	@Autowired
	private LeaveRecordService lrs;

	//view list of employee under the manager
	@GetMapping("/velh/list")
	public String viewAllEmployee(Model model, HttpSession session) {
		Staff staff = (Staff)session.getAttribute("staff");
		model.addAttribute("staff", staff);
		
		List<Staff> myStaffList = ss.findMyStaff(staff);
		model.addAttribute("myStaffList", myStaffList);
		model.addAttribute("page", "velh");
		return "viewemployeelist";
	}
	
	//retrieve leave history for selected employee
	@GetMapping("/velh/{staffId}/list")
    public String viewEmployeeLeaveHistory(HttpSession session, @PathVariable("staffId") int staffId, Model model) {
    	Staff staff = (Staff) session.getAttribute("staff");
        model.addAttribute("staff", staff);
        
        Staff stf = ss.findStaffById(staffId);
        
       	List<LeaveRecord> staffleaverecord = lrs.getStaffLeavesByYear(stf.getStaffId());
        model.addAttribute("staffleaverecord", staffleaverecord);
        model.addAttribute("stfname", stf.getName());
		model.addAttribute("page", "velh");
        return "viewemployeeleavehistory";
    }
	//retrieve leave history for all employee under the manager
	@GetMapping("/velh/mystaff/list")
    public String viewMyStaffLeaveHistory(HttpSession session, Model model) {
    	Staff staff = (Staff) session.getAttribute("staff");
        model.addAttribute("staff", staff);
        
        List<Staff> myStfList = ss.findMyStaff(staff);
        
        List<LeaveRecord> staffleaverecord = lrs.getAllMyStaffLeavesByYear(myStfList);
        model.addAttribute("staffleaverecord", staffleaverecord);
        model.addAttribute("stfname", "mystaff");
		model.addAttribute("page", "velh");
        return "viewemployeeleavehistory";
    }
	//view leave details for selected leave
	@GetMapping("/velh/{leaveId}/view")
    public String viewLeaveDetails(Model model, @PathVariable("leaveId") int leaveId, HttpSession session) {
    	Staff staff = (Staff) session.getAttribute("staff");
        model.addAttribute("staff", staff);
		
		LeaveRecord leave = lrs.getLeaveById(leaveId);
        model.addAttribute("leave", leave);
		model.addAttribute("page", "velh");
        return "viewemployeeleavedetails";
    }
}
