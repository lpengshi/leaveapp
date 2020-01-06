package sg.edu.iss.leave.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Entitlement {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name="leave_type_id")
	private int leaveTypeId;
	private String role;
	private int entitledDays;
	
	@ManyToOne
	@JoinColumn(name="leave_type_id", insertable=false, updatable=false)
	private LeaveType leaveType;
	
	public Entitlement() {
		super();
	}

	public Entitlement(int leaveTypeId, String role, int entitledDays) {
		super();
		this.leaveTypeId = leaveTypeId;
		this.role = role;
		this.entitledDays = entitledDays;
	}

	public int getLeaveTypeId() {
		return leaveTypeId;
	}

	public void setLeaveTypeId(int leaveTypeId) {
		this.leaveTypeId = leaveTypeId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public int getEntitledDays() {
		return entitledDays;
	}

	public void setEntitledDays(int entitledDays) {
		this.entitledDays = entitledDays;
	}

	public int getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Entitlement other = (Entitlement) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Entitlement [id=" + id + ", leaveTypeId=" + leaveTypeId + ", role=" + role + ", entitledDays="
				+ entitledDays + "]";
	}
	
}
