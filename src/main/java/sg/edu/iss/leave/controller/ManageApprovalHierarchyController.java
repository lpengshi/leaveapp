package sg.edu.iss.leave.controller;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import sg.edu.iss.leave.javabean.Employee;
import sg.edu.iss.leave.model.Staff;
import sg.edu.iss.leave.service.StaffService;

@Controller
public class ManageApprovalHierarchyController {

	@Autowired
	private StaffService ss;
	
	Logger LOG = LoggerFactory.getLogger(ManageApprovalHierarchyController.class);
	
	//View all staff roles and report to
	@GetMapping("/mah/list")
	public String manageApproval(Model model, HttpSession session, String role, String reportTo) {
		Staff staff = (Staff)session.getAttribute("admin");
		model.addAttribute("staff", staff);

		List<Staff> staffList = ss.findAllStaff();
		List<Employee> empList = ss.convertToEmployee(staffList);

		model.addAttribute("page", "mah");
		model.addAttribute("emplist", empList);

		return "manageapproval";
	}
	
	//view staff details for reassignment
	@GetMapping("/mah/{staffId}/updateapproval")
	public String updateApprovalForm(Model model, HttpSession session,  @PathVariable("staffId") String staffId, RedirectAttributes attributes) {
		Staff staff = (Staff)session.getAttribute("admin");
		model.addAttribute("staff", staff);

		int stfId = Integer.parseInt(staffId);
		Staff stf = ss.findStaffById(stfId);
		Employee emp = ss.convertToEmployee(stf);
		List<Staff> mgrList = ss.findAllManagers();
		
		model.addAttribute("page", "mah");
		model.addAttribute("mgrList", mgrList);
		model.addAttribute("emp", emp);
		
		return "updateapproval";
	}
	
	//update staff roles and reportTo as per form input
	@PostMapping("/mah/{staffId}/updateapproval")
	public RedirectView updateApproval(Model model, HttpSession session, @PathVariable("staffId") String staffId, RedirectAttributes attributes, String role, String reportTo, String process) {
		Staff staff = (Staff)session.getAttribute("admin");
		model.addAttribute("staff", staff);
		String msg = "";
		int stfId = Integer.parseInt(staffId);
		Staff stf = ss.findStaffById(stfId);
		int reportMgr = Integer.parseInt(reportTo);
		
		if (process.equals("Cancel Changes")) {
			LOG.info("No changes were made to " + stf.getName());
		} else {
			
			if (staff.getStaffId() == stf.getStaffId() && !role.equals("Admin")) {
				msg = "Update failed. Please get other admins to update your role.";
				attributes.addFlashAttribute("msg", msg);
				return new RedirectView("/mah/list");
			}
			stf.setRole(role);
			stf.setReportTo(reportMgr);
			Staff updateStf = ss.updateStaff(stf);
			msg = updateStf.getName() + "'s approval was updated successfully";
			LOG.info(msg);
		}

		attributes.addFlashAttribute("msg", msg);
		return new RedirectView("/mah/list");
	}

}
