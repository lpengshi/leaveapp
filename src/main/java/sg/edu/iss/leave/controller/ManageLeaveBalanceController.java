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

import sg.edu.iss.leave.model.Entitlement;
import sg.edu.iss.leave.model.LeaveBalance;
import sg.edu.iss.leave.model.LeaveType;
import sg.edu.iss.leave.model.Staff;
import sg.edu.iss.leave.service.EntitlementService;
import sg.edu.iss.leave.service.LeaveBalanceService;
import sg.edu.iss.leave.service.LeaveTypeService;
import sg.edu.iss.leave.service.StaffService;

@Controller
public class ManageLeaveBalanceController {

	Logger LOG = LoggerFactory.getLogger(ManageLeaveTypeController.class);
	
	@Autowired
	private LeaveBalanceService lbs;
	@Autowired
	private StaffService ss;
	@Autowired
	private EntitlementService es;
	@Autowired
	private LeaveTypeService lts;
	
	//load existing leave balance
	@GetMapping("/mlb/list")
	public String manageLeaveBalance(Model model, HttpSession session) {
		Staff staff = (Staff)session.getAttribute("admin");
		model.addAttribute("staff", staff);
		
		List<LeaveBalance> leaBalList = lbs.findAllLeaveBalance();
		
		model.addAttribute("leaBalList", leaBalList);
		model.addAttribute("page", "mlb");
		return "manageleavebalance";
	}
	
	//load form to create leave balance for new staff
	@GetMapping("/mlb/createleavebalance")
	public String createLeaveBalanceForm(Model model, HttpSession session) {
		Staff staff = (Staff)session.getAttribute("admin");
		model.addAttribute("staff", staff);
		
		List<Staff> staffList = ss.findAllStaff();
		List<LeaveType> ltList = lts.findAllLeaveType();
		
		model.addAttribute("staffList", staffList);
		model.addAttribute("ltList", ltList);
		model.addAttribute("page", "mlbclb");
		model.addAttribute("lb", new LeaveBalance());
		
		return "createleavebalance";
	}
	
	//process and save new leave balance
	@PostMapping("/mlb/createleavebalance")
	public String createLeaveBalance(LeaveBalance lb, Model model, HttpSession session, RedirectAttributes attributes, String process) {
		Staff staff = (Staff)session.getAttribute("admin");
		model.addAttribute("staff", staff);
		String msg = "";

		if (process.equals("Cancel Changes")) {
			LOG.info("No changes were made");
			return "redirect:/mlb/list";
		}  
		
		LeaveBalance checklb = lbs.findLeaveBalanceByStaffIdAndLeaveTypeId(lb.getStaffId(), lb.getLeaveTypeId());
		if (checklb != null) {
			msg = "There is an existing balance for the selected staff and leave type: (Leave Balance#" + checklb.getId() + ")";
			LOG.info(msg);
		} else {
			LeaveBalance newLeaveBalance = lbs.updateLeaveBalance(lb);
			msg = "Leave Balance#" + newLeaveBalance.getId() + " was created successfully";
			LOG.info(msg);
		}
			
		attributes.addFlashAttribute("msg", msg);
		return "redirect:/mlb/list";
	}
	
	//amend existing leave balance
	@GetMapping("/mlb/{id}/updateleavebalance")
	public String updateLeaveBalanceForm(Model model, HttpSession session,  @PathVariable("id") String id, RedirectAttributes attributes) {
		Staff staff = (Staff)session.getAttribute("admin");
		model.addAttribute("staff", staff);

		int ltId = Integer.parseInt(id);
		LeaveBalance leaBal = lbs.findLeaveBalanceById(ltId);
		
		model.addAttribute("page", "mlb");
		model.addAttribute("leaBal", leaBal);
		
		return "updateleavebalance";
	}
	
	//save changes
	@PostMapping("/mlb/{id}/updateleavebalance")
	public RedirectView updateLeaveBalance(Model model, HttpSession session,  @PathVariable("id") String id, RedirectAttributes attributes, int balance, String process) {
		Staff staff = (Staff)session.getAttribute("admin");
		model.addAttribute("staff", staff);
		String msg = "";

		int ltId = Integer.parseInt(id);
		LeaveBalance leaBal = lbs.findLeaveBalanceById(ltId);

		if (process.equals("Cancel Changes")) {
			LOG.info("No changes were made to Leave Balance #" + leaBal.getId());
		}	else if (process.equals("Save Changes")) {
			leaBal.setBalance(balance);
			LeaveBalance updateLB = lbs.updateLeaveBalance(leaBal);
			msg = "Leave Balance #" + updateLB.getId() + " was updated successfully";
			LOG.info(msg);
		}

		attributes.addFlashAttribute("msg", msg);
		return new RedirectView("/mlb/list");
	}
	
	@PostMapping("/mlb/refreshleavebalance")
	public RedirectView refreshLeaveBalance(Model model, HttpSession session, RedirectAttributes attributes, String password) {
		Staff staff = (Staff)session.getAttribute("admin");
		model.addAttribute("staff", staff);
		String msg = "";

		if (!staff.getPassword().equals(password)) {
			msg = "Password is wrong. Please enter again";
			LOG.info(msg);
		}	else {
			List<Staff> staffList = ss.findAllStaff();
			List<Entitlement> entList = es.findAllEntitlement();
			
			lbs.refreshLeaveBalace(staffList, entList);
			msg = "All leave balance has been refreshed as per staff entitlement.";
			LOG.info(msg);
		}

		attributes.addFlashAttribute("msg", msg);
		return new RedirectView("/mlb/list");
	}
	
	
}
