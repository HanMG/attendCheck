package kr.co.attend.domain;

public class adminBean {
private String phoneNum; //전화번호 PK
private String adName; 	 //관리자 명
private String isFire;	 //근퇴여부

public String getPhoneNum() {
	return phoneNum;
}
public void setPhoneNum(String phoneNum) {
	this.phoneNum = phoneNum;
}
public String getAdName() {
	return adName;
}
public void setAdName(String adName) {
	this.adName = adName;
}
public String getIsFire() {
	return isFire;
}
public void setIsFire(String isFire) {
	this.isFire = isFire;
}


}
