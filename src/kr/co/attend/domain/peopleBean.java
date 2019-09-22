package kr.co.attend.domain;

public class peopleBean {
private String phoneNum;   //전화번호 PK
private String stName; 	   //사람 이름
private String isTraining; //훈련여부

public String getPhoneNum() {
	return phoneNum;
}
public void setPhoneNum(String phoneNum) {
	this.phoneNum = phoneNum;
}
public String getStName() {
	return stName;
}
public void setStName(String stName) {
	this.stName = stName;
}
public String getIsTraining() {
	return isTraining;
}
public void setIsTraining(String isTraining) {
	this.isTraining = isTraining;
}


}
