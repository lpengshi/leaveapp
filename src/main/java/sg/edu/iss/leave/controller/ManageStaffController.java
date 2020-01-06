package sg.edu.iss.leave.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import sg.edu.iss.leave.exception.DeleteAdminException;
import sg.edu.iss.leave.javabean.Employee;
import sg.edu.iss.leave.model.Staff;
import sg.edu.iss.leave.service.StaffService;

@Controller
public class ManageStaffController {
	
	Logger LOG = LoggerFactory.getLogger(ManageStaffController.class);
	
	@Autowired
	private StaffService ss;
	
	//View all staff page
	@RequestMapping("/ms/list")
	public String manageStaff(Model model, HttpSession session) {
		Staff staff = (Staff)session.getAttribute("admin");
		model.addAttribute("staff", staff);
		
		List<Staff> staffList = ss.findAllStaff();
		List<Employee> empList = ss.convertToEmployee(staffList);
		model.addAttribute("page", "ms");
		model.addAttribute("emplist", empList);
		
		return "managestaff";
	}
	
	//load form to create staff
	@GetMapping("/ms/createstaff")
	public String createStaffForm(Model model, HttpSession session) {
		Staff staff = (Staff)session.getAttribute("admin");
		model.addAttribute("staff", staff);
		
		List<Staff> mgrList = ss.findAllManagers();
		model.addAttribute("page", "mscs");
		model.addAttribute("mgrList", mgrList);
		model.addAttribute("stf", new Staff());
		
		return "createstaff";
	}
	//save staff into database
	@PostMapping(value="/ms/createstaff")
	public String createStaff(Staff stf, BindingResult result, Model model, HttpSession session, RedirectAttributes attributes, String process) {
		Staff staff = (Staff)session.getAttribute("admin");
		model.addAttribute("staff", staff);
		String msg = "";

		if (process.equals("Cancel Changes")) {
			LOG.info("No changes were made");
			return "redirect:/ms/list";
		}  
		
		if (result.hasErrors()) {
			model.addAttribute("page", "mscs");
			return "createstaff";
		}
		
			Staff newStaff = ss.updateStaff(stf);
			msg = newStaff.getName() + "'s profile was created successfully";
			LOG.info(msg);
		
		attributes.addFlashAttribute("msg", msg);
		return "redirect:/ms/list";
	}
	//load form to amend existing staff details
	@GetMapping("/ms/{staffId}/updatestaff")
	public String updateStaffForm(Model model, HttpSession session,  @PathVariable("staffId") String staffId, RedirectAttributes attributes) {
		Staff staff = (Staff)session.getAttribute("admin");
		model.addAttribute("staff", staff);

		int stfId = Integer.parseInt(staffId);
		Staff stf = ss.findStaffById(stfId);
		List<Staff> mgrList = ss.findAllManagers();
		
		model.addAttribute("page", "mscs");
		model.addAttribute("mgrList", mgrList);
		model.addAttribute("stf", stf);
		
		return "updatestaff";
	}
	
	//save changes
	@PostMapping("/ms/{staffId}/updatestaff")
	public RedirectView updateStaff(Model model, HttpSession session,  @PathVariable("staffId") String staffId, RedirectAttributes attributes, String username, String password, String name, String email, String process) throws DeleteAdminException {
		Staff staff = (Staff)session.getAttribute("admin");
		model.addAttribute("staff", staff);
		String msg = "";
		int stfId = Integer.parseInt(staffId);
		Staff stf = ss.findStaffById(stfId);

		if (process.equals("Cancel Changes")) {
			LOG.info("No changes were made to " + stf.getName());
		} else if (process.equals("Delete Staff")) {
			
			if (stf.getRole().equals("Admin")) {
				throw new DeleteAdminException(stf.getName() + " is an admin. Admin cannot be deleted. Please change the role before deleting the staff");
			}
			ss.deleteStaff(stf);
			msg = stf.getName() + "'s profile was deleted successfully";
			LOG.info(msg);
		}	else if (process.equals("Save Changes")) {
			stf.setUsername(username);
			stf.setPassword(password);
			stf.setName(name);
			stf.setEmail(email);
			Staff updateStf = ss.updateStaff(stf);
			msg = updateStf.getName() + "'s profile was updated successfully";
			LOG.info(msg);
		}

		attributes.addFlashAttribute("msg", msg);
		return new RedirectView("/ms/list");
	}
	
}