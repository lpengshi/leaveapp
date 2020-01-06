package sg.edu.iss.leave.javabean;

	import java.time.LocalDate;

	import sg.edu.iss.leave.model.LeaveRecord;

	//import com.opencsv.bean.CsvBindByName;
	import com.opencsv.bean.CsvBindByPosition;


	public class LeaveRecordPrinter {
		

		@CsvBindByPosition(position = 0)
		private int leaveId;
		@CsvBindByPosition(position = 1)
		private String staffName;	
		@CsvBindByPosition(position = 2)
		private String leaveType;
		@CsvBindByPosition(position = 3)
		private LocalDate startDate;
		@CsvBindByPosition(position = 4)
		private LocalDate endDate;
		@CsvBindByPosition(position = 5)
		private String reason;
		@CsvBindByPosition(position = 6)
		private String remark;
		@CsvBindByPosition(position = 7)
		private String contactNo;
		@CsvBindByPosition(position = 8)
		private String handover;
		
		public LeaveRecordPrinter(LeaveRecord lr) {
			
			this.leaveId = lr.getLeaveId();
			this.staffName = lr.getStaff().getName();
			this.leaveType = lr.getLeaveType().getLeaveTypeName();
			this.startDate = lr.getStartDate();
			this.endDate = lr.getEndDate();
			this.reason = lr.getReason();
			this.contactNo = lr.getContactNo();
			this.remark = lr.getRemark();
			this.handover = lr.getHandover();
		}

		public int getLeaveId() {
			return leaveId;
		}

		public String getStaffName() {
			return staffName;
		}

		public String getLeaveType() {
			return leaveType;
		}

		public LocalDate getStartDate() {
			return startDate;
		}

		public LocalDate getEndDate() {
			return endDate;
		}

		public String getReason() {
			return reason;
		}

		public String getHandover() {
			return handover;
		}

		public String getContactNo() {
			return contactNo;
		}

		public String getRemark() {
			return remark;
		}
		
		
	}
