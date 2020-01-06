package sg.edu.iss.leave.javabean;

import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

public class LeaveForm {

	@NotNull
	private int leaveTypeId;
	@NotEmpty
	private String reason;
	@NotEmpty
	private String handover;
	private String contactNo;
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate endDate;
	
	public LeaveForm() {
		super();
	}

	public LeaveForm(@NotNull int leaveTypeId, @NotEmpty String reason, @NotEmpty String handover,
			String contactNo, @NotNull LocalDate startDate, @NotNull LocalDate endDate) {
		super();
		this.leaveTypeId = leaveTypeId;
		this.reason = reason;
		this.handover = handover;
		this.contactNo = contactNo;
		this.startDate = startDate;
		this.endDate = endDate;
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

	public String getHandover() {
		return handover;
	}

	public void setHandover(String handover) {
		this.handover = handover;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contactNo == null) ? 0 : contactNo.hashCode());
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((handover == null) ? 0 : handover.hashCode());
		result = prime * result + leaveTypeId;
		result = prime * result + ((reason == null) ? 0 : reason.hashCode());
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
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
		LeaveForm other = (LeaveForm) obj;
		if (contactNo == null) {
			if (other.contactNo != null)
				return false;
		} else if (!contactNo.equals(other.contactNo))
			return false;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (handover == null) {
			if (other.handover != null)
				return false;
		} else if (!handover.equals(other.handover))
			return false;
		if (leaveTypeId != other.leaveTypeId)
			return false;
		if (reason == null) {
			if (other.reason != null)
				return false;
		} else if (!reason.equals(other.reason))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LeaveApplyForm [leaveTypeId=" + leaveTypeId + ", reason=" + reason + ", handover=" + handover
				+ ", contactNo=" + contactNo + ", startDate=" + startDate + ", endDate=" + endDate + "]";
	}
	
}
