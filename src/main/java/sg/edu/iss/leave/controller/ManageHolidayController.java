package sg.edu.iss.leave.controller;

import java.time.LocalDate;
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

import sg.edu.iss.leave.model.Holiday;
import sg.edu.iss.leave.model.Staff;
import sg.edu.iss.leave.service.HolidayService;

@Controller
public class ManageHolidayController {

	@Autowired
	private HolidayService hs;
	
	Logger LOG = LoggerFactory.getLogger(ManageHolidayController.class);
	
	//Retrieve existing list of holiday
	@RequestMapping("/mh/list")
	public String manageHoliday(Model model, HttpSession session) {
		Staff staff = (Staff)session.getAttribute("admin");
		model.addAttribute("staff", staff);
		
		List<Holiday> hList = hs.findAllHoliday();
		
		model.addAttribute("page", "mh");
		model.addAttribute("hlist", hList);
		return "manageholiday";
	}
	
	//load form to create holiday
	@GetMapping("/mh/createholiday")
	public String createHolidayForm(Model model, HttpSession session) {
		Staff staff = (Staff)session.getAttribute("admin");
		model.addAttribute("staff", staff);
		
		model.addAttribute("page", "mhch");
		model.addAttribute("hol", new Holiday());
		
		return "createholiday";
	}
	
	//create holiday and save in database
	@PostMapping("/mh/createholiday")
	public String createHoliday(Holiday hol, Model model, HttpSession session, RedirectAttributes attributes, String process) {
		Staff staff = (Staff)session.getAttribute("admin");
		model.addAttribute("staff", staff);
		String msg = "";

		if (process.equals("Cancel Changes")) {
			LOG.info("No changes were made");
			return "redirect:/mh/list";
		}  
		
		Holiday existHoliday = hs.findHolidayByDate(hol.getHolidayDate());
		
		msg = "The selected date is already a public holiday.";
		
		if (existHoliday == null) {
			Holiday newHoliday = hs.updateHoliday(hol);
			msg = newHoliday.getDescription() + " was created successfully";
			LOG.info(msg);
		}
		
		attributes.addFlashAttribute("msg", msg);
		return "redirect:/mh/list";
	}
	
	//load form to amend existing holiday
	@GetMapping("/mh/{id}/updateholiday")
	public String updateHolidayForm(Model model, HttpSession session,  @PathVariable("id") String id, RedirectAttributes attributes) {
		Staff staff = (Staff)session.getAttribute("admin");
		model.addAttribute("staff", staff);

		int holId = Integer.parseInt(id);
		Holiday hol = hs.findHolidayById(holId);
		
		model.addAttribute("page", "mh");
		model.addAttribute("hol", hol);
		
		return "updateholiday";
	}
	
	//save changes to database
	@PostMapping("/mh/{id}/updateholiday")
	public RedirectView updateHoliday(Model model, HttpSession session,  @PathVariable("id") String id, RedirectAttributes attributes, String description, String holidayDate,String process) {
		Staff staff = (Staff)session.getAttribute("admin");
		model.addAttribute("staff", staff);
		String msg = "";

		int holId = Integer.parseInt(id);
		Holiday holiday = hs.findHolidayById(holId);

		if (process.equals("Cancel Changes")) {
			LOG.info("No changes were made to " + holiday.getDescription());
		} else if (process.equals("Delete Holiday")) {
			hs.deleteHoliday(holiday);
			msg = holiday.getDescription() + " was deleted successfully";
			LOG.info(msg);
		}	else if (process.equals("Save Changes")) {
			holiday.setDescription(description);
			LocalDate holDate = LocalDate.parse(holidayDate);
			holiday.setHolidayDate(holDate);
			Holiday updateHol = hs.updateHoliday(holiday);
			msg = updateHol.getDescription() + " was updated successfully";
			LOG.info(msg);
		}

		attributes.addFlashAttribute("msg", msg);
		return new RedirectView("/mh/list");
	}
}

