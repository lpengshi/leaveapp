package sg.edu.iss.leave.javabean;

import sg.edu.iss.leave.model.Staff;

public class Employee {
	
	private Staff staff;
	private Staff manager;
	
	public Employee() {
		super();
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public Staff getManager() {
		return manager;
	}

	public void setManager(Staff manager) {
		this.manager = manager;
	}

}
