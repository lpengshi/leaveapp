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

import sg.edu.iss.leave.model.LeaveType;
import sg.edu.iss.leave.model.Staff;
import sg.edu.iss.leave.service.LeaveTypeService;

@Controller
public class ManageLeaveTypeController {

	@Autowired
	private LeaveTypeService lts;
	
	Logger LOG = LoggerFactory.getLogger(ManageLeaveTypeController.class);
	
	//load existing leave type
	@RequestMapping("/mlt/list")
	public String manageLeaveType(Model model, HttpSession session) {
		Staff staff = (Staff)session.getAttribute("admin");
		model.addAttribute("staff", staff);
		
		List<LeaveType> typeList = lts.findAllLeaveType();
		
		model.addAttribute("page", "mlt");
		model.addAttribute("typelist", typeList);
		return "manageleavetype";
	}
	//load form to create new leave type
	@GetMapping("/mlt/createleavetype")
	public String createLeaveTypeForm(Model model, HttpSession session) {
		Staff staff = (Staff)session.getAttribute("admin");
		model.addAttribute("staff", staff);
		
		model.addAttribute("page", "mltclt");
		model.addAttribute("lt", new LeaveType());
		
		return "createleavetype";
	}
	//process form and save in database
	@PostMapping("/mlt/createleavetype")
	public String createLeaveType(LeaveType lt, Model model, HttpSession session, RedirectAttributes attributes, String process) {
		Staff staff = (Staff)session.getAttribute("admin");
		model.addAttribute("staff", staff);
		String msg = "";

		if (process.equals("Cancel Changes")) {
			LOG.info("No changes were made");
			return "redirect:/mlt/list";
		}  
		
			LeaveType newLeaveType = lts.updateLeaveType(lt);
			msg = newLeaveType.getLeaveTypeName() + " was created successfully";
			LOG.info(msg);
		
		attributes.addFlashAttribute("msg", msg);
		return "redirect:/mlt/list";
	}
	//amend existing leave type
	@GetMapping("/mlt/{id}/updateleavetype")
	public String updateLeaveTypeForm(Model model, HttpSession session,  @PathVariable("id") String id, RedirectAttributes attributes) {
		Staff staff = (Staff)session.getAttribute("admin");
		model.addAttribute("staff", staff);

		int ltId = Integer.parseInt(id);
		LeaveType leaType = lts.findLeaveTypeById(ltId);
		
		model.addAttribute("page", "mlt");
		model.addAttribute("leaType", leaType);
		
		return "updateleavetype";
	}
	//save changes
	@PostMapping("/mlt/{id}/updateleavetype")
	public RedirectView updateLeaveType(Model model, HttpSession session,  @PathVariable("id") String id, RedirectAttributes attributes, String leaveTypeName, String process) {
		Staff staff = (Staff)session.getAttribute("admin");
		model.addAttribute("staff", staff);
		String msg = "";

		int ltId = Integer.parseInt(id);
		LeaveType leaType = lts.findLeaveTypeById(ltId);

		if (process.equals("Cancel Changes")) {
			LOG.info("No changes were made to " + leaType.getLeaveTypeName());
		} else if (process.equals("Delete Leave Type")) {
			lts.deleteLeaveType(leaType);
			msg = leaType.getLeaveTypeName() + " was deleted successfully";
			LOG.info(msg);
		}	else if (process.equals("Save Changes")) {
			leaType.setLeaveTypeName(leaveTypeName);
			LeaveType updateLT = lts.updateLeaveType(leaType);
			msg = updateLT.getLeaveTypeName() + " was updated successfully";
			LOG.info(msg);
		}

		attributes.addFlashAttribute("msg", msg);
		return new RedirectView("/mlt/list");
	}
}
