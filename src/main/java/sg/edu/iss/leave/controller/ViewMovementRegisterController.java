package sg.edu.iss.leave.controller;

import java.time.LocalDate;
import java.time.Year;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sg.edu.iss.leave.model.LeaveRecord;
import sg.edu.iss.leave.model.Staff;
import sg.edu.iss.leave.service.LeaveRecordService;

@Controller
public class ViewMovementRegisterController {

	@Autowired
	private LeaveRecordService lrs;
	
	@RequestMapping(path = "/movement/current")
	public String getCurrentMonth(RedirectAttributes redirectAttributes) {
		//get current month
		int currentMonth = LocalDate.now().getMonthValue();
		redirectAttributes.addAttribute("month", currentMonth);
		return "redirect:/movement/{month}";
	}
	
	@RequestMapping(path = "/movement/{month}", method = RequestMethod.GET)
    public String viewStaffLeaveRecords(HttpSession session, Model model, @PathVariable(name="month") Integer month) {
    	//retrieve staff
		Staff staff = (Staff) session.getAttribute("staff");
        model.addAttribute("staff", staff);
        
        //to create dropdown list of months
        int bMonth = LocalDate.now().getMonthValue() - 1; //before current month
        int cMonth = LocalDate.now().getMonthValue(); //current month
        int aMonth = LocalDate.now().getMonthValue() + 1; //after current month
       	
        List<Integer> monthList = Arrays.asList(bMonth, cMonth, aMonth);
       	model.addAttribute("monthList", monthList);
       	
       	//pass month selected to view
       	model.addAttribute("month", month);
 
       	//get list of leave of all staff during selected month in the current year
       	List<LeaveRecord> leaveRecord = lrs.getMovementRegister(month, Year.now().getValue());
       	model.addAttribute("leaveRecord", leaveRecord);
       	model.addAttribute("page", "mr");
        
        return "movementregister";
    }
}
