package sg.edu.iss.leave.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Entity
public class LeaveBalance {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name="staff_id")
	private int staffId;
	@Column(name="leave_type_id")
	private int leaveTypeId;
    private int balance;
	
    @ManyToOne
	@JoinColumn(name="staff_id", insertable=false, updatable=false)
	private Staff staff;
	@ManyToOne
	@JoinColumn(name="leave_type_id", insertable=false, updatable=false)
	private LeaveType leaveType;
    
    public LeaveBalance() {
		super();
	}

	public LeaveBalance(int staffId, int leaveTypeId, int balance) {
		super();
		this.staffId = staffId;
		this.leaveTypeId = leaveTypeId;
		this.balance = balance;
	}

	public int getStaffId() {
		return staffId;
	}

	public void setStaffId(int staffId) {
		this.staffId = staffId;
	}

	public int getLeaveTypeId() {
		return leaveTypeId;
	}

	public void setLeaveTypeId(int leaveTypeId) {
		this.leaveTypeId = leaveTypeId;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public int getId() {
		return id;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public LeaveType getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(LeaveType leaveType) {
		this.leaveType = leaveType;
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
		LeaveBalance other = (LeaveBalance) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LeaveBalance [id=" + id + ", staffId=" + staffId + ", leaveTypeId=" + leaveTypeId + ", balance="
				+ balance + "]";
	}
    
}
