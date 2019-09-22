package kr.co.attend.domain;

public class dateBean {
private String phoneNum; //전화번호 PK
private String inTime;   //입실
private String goHome;	 //조퇴
private String absence;	 //결석여부
private String outTime;  //퇴실

public String getPhoneNum() {
	return phoneNum;
}
public void setPhoneNum(String phoneNum) {
	this.phoneNum = phoneNum;
}
public String getInTime() {
	return inTime;
}
public void setInTime(String inTime) {
	this.inTime = inTime;
}
public String getGoHome() {
	return goHome;
}
public void setGoHome(String goHome) {
	this.goHome = goHome;
}
public String getAbsence() {
	return absence;
}
public void setAbsence(String absence) {
	this.absence = absence;
}
public String getOutTime() {
	return outTime;
}
public void setOutTime(String outTime) {
	this.outTime = outTime;
}


}
