package sg.edu.iss.leave.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class LogoutController {
	
	//clear session data when staff logout
	@GetMapping("/logout/{system}")
	public RedirectView staffLogout(RedirectAttributes attributes, SessionStatus status, @PathVariable("system") String system){
		status.setComplete();
		attributes.addFlashAttribute("newMsg", "You have successfully logged out");
		if (system.equals("admin")) {
			return new RedirectView("/adminlogin");
		} 
		
		return new RedirectView("/stafflogin");
		
	}
}
