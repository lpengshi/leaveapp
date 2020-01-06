package sg.edu.iss.leave.javabean;

import sg.edu.iss.leave.model.Entitlement;
import sg.edu.iss.leave.model.LeaveType;

public class LeaveEntitlement {

	private Entitlement entitlement;
	private LeaveType leaveType;
	
	public LeaveEntitlement() {
		super();
	}

	public Entitlement getEntitlement() {
		return entitlement;
	}

	public void setEntitlement(Entitlement entitlement) {
		this.entitlement = entitlement;
	}

	public LeaveType getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(LeaveType leaveType) {
		this.leaveType = leaveType;
	}
	
}
