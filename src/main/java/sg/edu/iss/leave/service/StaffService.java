package sg.edu.iss.leave.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.iss.leave.javabean.Employee;
import sg.edu.iss.leave.model.Staff;
import sg.edu.iss.leave.repo.StaffRepository;

@Service
public class StaffService {
	
	@Autowired
	private StaffRepository sRepo;
	
	//find staff by username
	public Staff findStaffByUsername(String username) {
		
		Staff staff = sRepo.findByUsername(username).orElse(null);
		return staff;
	}

	//check if password is correct
	public boolean verifyStaffPassword(Staff stf, String password) {
		
		boolean isPassword = false;
		if (stf.getPassword().equals(password)) {
			isPassword = true;
		}
		return isPassword;
	}

	//check if staff is a employee or manager
	public boolean isManager(Staff stf) {
		
		boolean isManager = false;
		if (stf.getRole().equals("Manager")) {
			isManager = true;
		}
		return isManager;
	}

	//check if staff is an admin
	public boolean isAdmin(Staff stf) {
		
		boolean isAdmin = false;
		if (stf.getRole().equals("Admin")) {
			isAdmin = true;
		}
		return isAdmin;
	}

	//return list of all staff
	public List<Staff> findAllStaff() {
		
		List<Staff> staffList = sRepo.findAll();
		return staffList;
	}

	public Staff findStaffById(int staffId) {
		Staff staff = sRepo.findById(staffId).orElse(null);
		return staff;
	}
	
	public List<Staff> findAllManagers() {
		List<Staff> mgrList = sRepo.findByRole("Manager");
		return mgrList;
	}

	public void deleteStaff(Staff staff) {
		sRepo.delete(staff);
		return;
	}
	
	public Staff updateStaff(Staff stf) {
		Staff updateStaff = sRepo.save(stf);
		return updateStaff;
	}

	public List<Employee> convertToEmployee(List<Staff> staffList) {
		List<Employee> empList = new ArrayList<Employee>();

		for (Staff stf : staffList) {
			Employee emp = this.convertToEmployee(stf);
			empList.add(emp);
		}
		return empList;
	}

	public Employee convertToEmployee(Staff stf) {
		Employee emp = new Employee();
		emp.setStaff(stf);
		emp.setManager(this.findStaffById(stf.getReportTo()));

		return emp;
	}

	public List<Staff> findMyStaff(Staff stf) {
		List<Staff> myStaffList = sRepo.findByReportTo(stf.getStaffId());
		return myStaffList;
	}
	
}
