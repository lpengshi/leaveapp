package sg.edu.iss.leave.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

import sg.edu.iss.leave.model.*;
import sg.edu.iss.leave.service.LeaveRecordService;
import sg.edu.iss.leave.service.LeaveTypeService;
import sg.edu.iss.leave.javabean.LeaveRecordPrinter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class ReportViewController {

	@Autowired
	private LeaveRecordService lrs;
	@Autowired
	private LeaveTypeService lts;

	Logger LOG = LoggerFactory.getLogger(ViewApplicationForApprovalController.class);

	@PostMapping("/rpv/fetch")
	public String EmployeeLeaveList(HttpSession session, @RequestParam String exportOrDisplay, String choice,
			String startDate, String endDate, RedirectAttributes attributes) throws Exception {

		LocalDate sDate = LocalDate.now();
		LocalDate eDate = LocalDate.now();

		boolean parsetime = false;
		if (!startDate.isEmpty() && !endDate.isEmpty()) {
			sDate = LocalDate.parse(startDate);
			eDate = LocalDate.parse(endDate);

			if (sDate.isBefore(eDate) || sDate.equals(eDate)) {
				parsetime = true;
			}
		}

		Staff stf = (Staff) session.getAttribute("staff");
		List<LeaveRecord> leaRecList = lrs.findByMgrApproved(stf);
		ArrayList<LeaveRecord> list = new ArrayList<LeaveRecord>();

		// search have time, choice is medical or annual
		if (!choice.equalsIgnoreCase("all") && parsetime) {

				for (LeaveRecord lr : leaRecList) {
					if (lr.getLeaveType().getLeaveTypeName().equalsIgnoreCase(choice)&&
							((lr.getEndDate().compareTo(sDate) >= 0) && (lr.getStartDate().compareTo(eDate) <= 0))) {
						list.add(lr);
					}
				}
		} // search have time but display all
		else if (choice.equalsIgnoreCase("all") && parsetime) {
			
				for (LeaveRecord lr : leaRecList) {
					if ((lr.getEndDate().compareTo(sDate) >= 0) && (lr.getStartDate().compareTo(eDate) <= 0)) {
						list.add(lr);
					}
				}
			
		}
		// no time but show all
		else if (!parsetime && choice.equalsIgnoreCase("all")) {
			list = (ArrayList<LeaveRecord>) leaRecList;
			String msg = "you hit show all without time";
			LOG.info(msg);
		}
		// search without time, not all
		else if (!parsetime && !choice.equalsIgnoreCase("all")) {
			for (LeaveRecord lr : leaRecList) {
				if (lr.getLeaveType().getLeaveTypeName().equalsIgnoreCase(choice)) {
					list.add(lr);
				}
			}
		}

		if (list.isEmpty()) {
			attributes.addFlashAttribute("errMsg", "No records found for selection.");
			String msg = "error code was reached";
			LOG.info(msg);
		}
		if (exportOrDisplay.equalsIgnoreCase("export") && !list.isEmpty()) {
			attributes.addFlashAttribute("list", list);
			return ("redirect:/rpv/csv");
		}

		attributes.addFlashAttribute("list", list);
		attributes.addFlashAttribute("choice", choice);
		attributes.addFlashAttribute("startDate", startDate);
		attributes.addFlashAttribute("endDate", endDate);
		return ("redirect:/rpv");
	}

	@GetMapping("/rpv")
	public String EmployeeLeaveList(Model model, HttpSession session,
			@ModelAttribute("list") ArrayList<LeaveRecord> leaRec, @ModelAttribute("errMsg") String errMsg,
			@ModelAttribute("choice") String choice, @ModelAttribute("startDate") String startDate,
			@ModelAttribute("endDate") String endDate) {

		Staff stf = (Staff) session.getAttribute("staff");
		Collection<LeaveRecord> leaRecList = lrs.findByMgrApproved(stf);
		
		Collection<LeaveType> lt= lts.findAllLeaveType();
		List<String> choicelist =new ArrayList<String>();
		choicelist.add("All");
		for(LeaveType ltype :lt) {
			choicelist.add(ltype.getLeaveTypeName());
		}

		if (!leaRec.isEmpty() && errMsg.isEmpty()) {
			model.addAttribute("list", leaRec);
		} else if (!errMsg.isEmpty()) {
			model.addAttribute("errMsg", errMsg);
		} else {
			model.addAttribute("list", leaRecList);
			model.addAttribute("choice", choice);
			model.addAttribute("startDate", startDate);
			model.addAttribute("endDate", endDate);
		}

		model.addAttribute("staff", stf);
		model.addAttribute("page", "rpv");
		
		model.addAttribute("choicelist", choicelist);
		
		return "reportlistview";
	}

	@GetMapping("/rpv/csv")
	public void LrCsvGen(Model model, @ModelAttribute("list") ArrayList<LeaveRecord> list, HttpServletResponse response) throws Exception {
		// export to csv and trigger dl.

		ArrayList<LeaveRecordPrinter> convertedList = new ArrayList<>();
		for (LeaveRecord lr : list) {

			convertedList.add(new LeaveRecordPrinter(lr));
		}

		// set file name and content type
		String filename = "report.csv";

		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

		// create a csv writer
		StatefulBeanToCsv<LeaveRecordPrinter> writer = new StatefulBeanToCsvBuilder<LeaveRecordPrinter>(
				response.getWriter()).withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
						.withSeparator(CSVWriter.DEFAULT_SEPARATOR).withOrderedResults(true).build();

		writer.write(convertedList);

	}
}
