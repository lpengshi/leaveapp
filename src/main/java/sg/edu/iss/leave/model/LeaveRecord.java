package sg.edu.iss.leave.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class LeaveRecord {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int leaveId;
	@Column(name="staff_id")
	private int staffId;
	@Column(name="leave_type_id")
	private int leaveTypeId;
	private String reason;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate endDate;
	private int duration;
	private String contactNo;
	private String handover;
	private String remark;
	private String status;
	
	@ManyToOne
	@JoinColumn(name="staff_id", insertable=false, updatable=false)
	private Staff staff;
	@ManyToOne
	@JoinColumn(name="leave_type_id", insertable=false, updatable=false)
	private LeaveType leaveType;
	
	public LeaveRecord() {
		super();

	}

	public LeaveRecord(int staffId, int leaveTypeId, String reason, LocalDate startDate, LocalDate endDate,
			int duration, String contactNo, String handover, String remark, String status) {
		super();
		this.staffId = staffId;
		this.leaveTypeId = leaveTypeId;
		this.reason = reason;
		this.startDate = startDate;
		this.endDate = endDate;
		this.duration = duration;
		this.contactNo = contactNo;
		this.handover = handover;
		this.remark = remark;
		this.status = status;
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

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getHandover() {
		return handover;
	}

	public void setHandover(String handover) {
		this.handover = handover;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getLeaveId() {
		return leaveId;
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
		result = prime * result + leaveId;
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
		LeaveRecord other = (LeaveRecord) obj;
		if (leaveId != other.leaveId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LeaveRecord [leaveId=" + leaveId + ", staffId=" + staffId + ", leaveTypeId=" + leaveTypeId + ", reason="
				+ reason + ", startDate=" + startDate + ", endDate=" + endDate + ", duration=" + duration
				+ ", contactNo=" + contactNo + ", handover=" + handover + ", remark=" + remark + ", status=" + status
				+ "]";
	}
	
}
