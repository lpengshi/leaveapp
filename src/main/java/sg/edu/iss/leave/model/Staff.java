package sg.edu.iss.leave.model;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class Staff {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int staffId;
	private String username;
	private String password;
	private String role;
	@NotNull
	private int reportTo;
	private String email;
	@NotEmpty
	private String name;
	
	@OneToMany(targetEntity=LeaveRecord.class, mappedBy="staff")
	private Collection<LeaveRecord> leaveRecords;
	@OneToMany(targetEntity=LeaveBalance.class, mappedBy="staff")
	private Collection<LeaveBalance> leaveBalances;
	
	public Staff() {
		super();
	}

	public Staff(String username, String password, String role, int reportTo, String email, String name,
			Collection<LeaveRecord> leaveRecords, Collection<LeaveBalance> leaveBalances) {
		super();
		this.username = username;
		this.password = password;
		this.role = role;
		this.reportTo = reportTo;
		this.email = email;
		this.name = name;
		this.leaveRecords = leaveRecords;
		this.leaveBalances = leaveBalances;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + staffId;
		return result;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public int getReportTo() {
		return reportTo;
	}

	public void setReportTo(int reportTo) {
		this.reportTo = reportTo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<LeaveRecord> getLeaveRecords() {
		return leaveRecords;
	}

	public void setLeaveRecords(Collection<LeaveRecord> leaveRecords) {
		this.leaveRecords = leaveRecords;
	}

	public Collection<LeaveBalance> getLeaveBalances() {
		return leaveBalances;
	}

	public void setLeaveBalances(Collection<LeaveBalance> leaveBalances) {
		this.leaveBalances = leaveBalances;
	}

	public int getStaffId() {
		return staffId;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Staff other = (Staff) obj;
		if (staffId != other.staffId)
			return false;
		return true;
		
		
	}

	@Override
	public String toString() {
		return "Staff [name=" + name + "]";
	} 
	
	

}
