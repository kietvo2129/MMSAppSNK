package com.licheedev.MMSSNK.Actual.Staff;

public class StaffMaster {
    String Stt;
    String StaffNo;
    String StaffId;
    String StaffName;
    String TypeName;
    String StartDate;
    String EndDate;

    public StaffMaster(String stt, String staffNo, String staffId, String staffName, String typeName, String startDate, String endDate) {
        Stt = stt;
        StaffNo = staffNo;
        StaffId = staffId;
        StaffName = staffName;
        TypeName = typeName;
        StartDate = startDate;
        EndDate = endDate;
    }

    public String getStt() {
        return Stt;
    }

    public void setStt(String stt) {
        Stt = stt;
    }

    public String getStaffNo() {
        return StaffNo;
    }

    public void setStaffNo(String staffNo) {
        StaffNo = staffNo;
    }

    public String getStaffId() {
        return StaffId;
    }

    public void setStaffId(String staffId) {
        StaffId = staffId;
    }

    public String getStaffName() {
        return StaffName;
    }

    public void setStaffName(String staffName) {
        StaffName = staffName;
    }

    public String getTypeName() {
        return TypeName;
    }

    public void setTypeName(String typeName) {
        TypeName = typeName;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }
}
