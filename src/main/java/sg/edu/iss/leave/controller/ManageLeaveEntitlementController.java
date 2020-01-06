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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import sg.edu.iss.leave.javabean.LeaveEntitlement;
import sg.edu.iss.leave.model.Entitlement;
import sg.edu.iss.leave.model.LeaveType;
import sg.edu.iss.leave.model.Staff;
import sg.edu.iss.leave.service.EntitlementService;
import sg.edu.iss.leave.service.LeaveTypeService;

@Controller
public class ManageLeaveEntitlementController {

	@Autowired
	private EntitlementService es;
	@Autowired
	private LeaveTypeService lts;
	
	Logger LOG = LoggerFactory.getLogger(ManageLeaveEntitlementController.class);
	
	//load all current staff entitlement
	@RequestMapping("/mle/list")
	public String manageLeaveEntitlement(Model model, HttpSession session) {
		Staff staff = (Staff)session.getAttribute("admin");
		model.addAttribute("staff", staff);
		
		List<Entitlement> entList = es.findAllEntitlement();
		List<LeaveType> ltList = lts.findAllLeaveType();
		List<LeaveEntitlement> lEntList = es.ConvertToLeaveEntitlement(entList, ltList);
		
		model.addAttribute("page", "mle");
		model.addAttribute("lEntlist", lEntList);
		return "manageleaveentitlement";
	}
	
	//load form to create new staff entitlement
	@GetMapping("/mle/createleaveentitlement")
	public String createLeaveEntitlementForm(Model model, HttpSession session) {
		Staff staff = (Staff)session.getAttribute("admin");
		model.addAttribute("staff", staff);
		
		List<LeaveType> ltList = lts.findAllLeaveType();
		
		model.addAttribute("page", "mlecle");
		model.addAttribute("ltList", ltList);
		model.addAttribute("ent", new Entitlement());
		
		return "createleaveentitlement";
	}
	//process form and save in database
	@PostMapping("/mle/createleaveentitlement")
	public String createLeaveEntitlement(Entitlement ent, Model model, HttpSession session, RedirectAttributes attributes, String process) {
		Staff staff = (Staff)session.getAttribute("admin");
		model.addAttribute("staff", staff);
		String msg = "";

		if (process.equals("Cancel Changes")) {
			LOG.info("No changes were made");
			return "redirect:/mle/list";
		}  
		
			Entitlement newEntitlement = es.createEntitlement(ent);
			msg = "Entitlement#" + newEntitlement.getId() + " was created successfully";
			LOG.info(msg);
		
		attributes.addFlashAttribute("msg", msg);
		return "redirect:/mle/list";
	}
	//amend existing entitlement
	@GetMapping("/mle/{id}/updateleaveentitlement")
	public String updateLeaveEntitlementForm(Model model, HttpSession session,  @PathVariable("id") String id, RedirectAttributes attributes) {
		Staff staff = (Staff)session.getAttribute("admin");
		model.addAttribute("staff", staff);

		int entId = Integer.parseInt(id);
		Entitlement ent = es.findEntitlementById(entId);
		List<LeaveType> ltList = lts.findAllLeaveType();
		
		model.addAttribute("page", "mlt");
		model.addAttribute("ent", ent);
		model.addAttribute("ltList", ltList);
		
		return "updateleaveentitlement";
	}
	//save changes
	@PostMapping("/mle/{id}/updateleaveentitlement")
	public RedirectView updateLeaveType(Model model, HttpSession session,  @PathVariable("id") String id, RedirectAttributes attributes, int leaveTypeId, String role, int entitledDays, String process) {
		Staff staff = (Staff)session.getAttribute("admin");
		model.addAttribute("staff", staff);
		String msg = "";

		int entId = Integer.parseInt(id);
		Entitlement entitlement = es.findEntitlementById(entId);

		if (process.equals("Cancel Changes")) {
			LOG.info("No changes were made to Entitlement#" + entitlement.getId());
		} else if (process.equals("Delete Leave Entitlement")) {
			es.deleteEntitlement(entitlement);
			msg = "Entitlement #" + entitlement.getId() + " was deleted successfully";
			LOG.info(msg);
		}	else if (process.equals("Save Changes")) {
			entitlement.setLeaveTypeId(leaveTypeId);
			entitlement.setRole(role);
			entitlement.setEntitledDays(entitledDays);
			Entitlement updateEnt = es.updateEntitlement(entitlement);
			msg = "Entitlement #" + updateEnt.getId() + " was updated successfully";
			LOG.info(msg);
		}

		attributes.addFlashAttribute("msg", msg);
		return new RedirectView("/mle/list");
	}
}

