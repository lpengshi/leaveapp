package sg.edu.iss.leave.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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
import sg.edu.iss.leave.service.LeaveRecordService;
import sg.edu.iss.leave.service.StaffService;

@Controller
public class LoginController {
	
	@Autowired
	private StaffService ss;
	
	@Autowired
	private LeaveRecordService lrs;

	
	@GetMapping("/")
	public RedirectView index() {
		return new RedirectView("/stafflogin");
	}
	
	//For staff to login 
	@GetMapping("/stafflogin")
	public String staffLogin(Model model, @ModelAttribute("newMsg") String newMsg) {
		String msg = "";
		if (!newMsg.isEmpty()) {
			msg = newMsg;
		}
		model.addAttribute("action", "/stafflogin");
		model.addAttribute("msg", msg);
		model.addAttribute("page", "isSL");
		return "stafflogin";
	}
	
	//Redirect to View Application for Approval if staff is a manager and login successful 
	@PostMapping("/stafflogin")
	public RedirectView redirectStaffLogin(HttpServletRequest request, RedirectAttributes attributes, String username, String password) {
		
		if (!username.isEmpty()) {
			Staff staff = ss.findStaffByUsername(username);

			if(staff != null) {

				if (ss.verifyStaffPassword(staff, password) ) {
					
					request.getSession().setAttribute("staff",staff);

					if(ss.isManager(staff)) 
					{
						return new RedirectView("/vafa/list");
					} 
					else 
					{
						return new RedirectView("/vplh/list");
					}
				}
			}
		}
		attributes.addFlashAttribute("newMsg", "Incorrect User ID and Password. Please try again.");
		return new RedirectView("/stafflogin");
	}
	
	//For staff to login 
	@GetMapping("/stafflogin/vplh/{leaveId}/view")
	public String staffLoginViewLeave(Model model, @ModelAttribute("newMsg") String newMsg,  @PathVariable("leaveId") int leaveId) {
		String msg = "";
		String url = "/stafflogin/vplh/" + leaveId + "/view";
		if (!newMsg.isEmpty()) {
			msg = newMsg;
		}
		model.addAttribute("action", url);
		model.addAttribute("msg", msg);
		model.addAttribute("page", "isSL");
		return "stafflogin";
	}
	
	//Redirect to View Application for Approval if staff is a manager and login successful 
	@PostMapping("/stafflogin/vplh/{leaveId}/view")
	public RedirectView redirectStaffLoginViewLeave(HttpServletRequest request, @PathVariable("leaveId") int leaveId, RedirectAttributes attributes, String username, String password) {
		
		if (!username.isEmpty()) {
			Staff staff = ss.findStaffByUsername(username);

			if(staff != null) {

				if (ss.verifyStaffPassword(staff, password) ) {
					
					request.getSession().setAttribute("staff",staff);
				}
				
				LeaveRecord leave = lrs.getLeaveById(leaveId);
				
				if (leave.getStaffId() != (staff.getStaffId())) {
					return new RedirectView("/vplh/list");
				} else {
					return new RedirectView("/vplh/" + leaveId + "/view");
				}
			}
		}
		
		attributes.addFlashAttribute("newMsg", "Incorrect User ID and Password. Please try again.");
		return new RedirectView("/stafflogin/vplh/" + leaveId + "/view");
	}
	
	//For admin to login
	@GetMapping("/adminlogin")
	public String adminLogin(Model model, @ModelAttribute("newMsg") String newMsg) {
		String msg = "";
		if (!newMsg.isEmpty()) {
			msg = newMsg;
		}
		model.addAttribute("msg", msg);
		model.addAttribute("page", "isAL");
		return "adminlogin";
	}
	
	//Redirect to admin manage staff page if staff is admin and login successful
	@PostMapping("/adminlogin")
	public RedirectView redirectAdminLogin(HttpServletRequest request, RedirectAttributes attributes, String username, String password) {
		
		if (username != "") {
			Staff staff = ss.findStaffByUsername(username);

			if(staff != null) {

				if (ss.verifyStaffPassword(staff, password) && ss.isAdmin(staff)) {
					 request.getSession().setAttribute("admin", staff);

					return new RedirectView("/ms/list");
				}
			}
		}
		attributes.addFlashAttribute("newMsg", "Incorrect User ID and Password. Please try again.");
		
		return new RedirectView("/adminlogin");
	}

}
