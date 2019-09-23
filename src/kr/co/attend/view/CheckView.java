package kr.co.attend.view;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CheckView extends CommonMethod
{
	String input;
	// 현 시간 생성
	Date currentTime = new Date();
	// date DB 생성을 위헤 월일 나오게 포맷
	SimpleDateFormat monthDay = new SimpleDateFormat("MMdd");
	// 입실퇴실조퇴에 쓰일 월일시분 나오게 포맷
	SimpleDateFormat monthDayHourMinute = new SimpleDateFormat("MM/dd/HH:mm");
	
	Check ck = new Check();

	public void checkMenu(String phoneNum)
	{

		// 현재 날짜를 월일 나오는 String으로 변환
		String s_currentTime = monthDay.format(currentTime);
		// date DB 이름
		String tableName = "DATE" + s_currentTime;

		// 현재 날짜를 월일시분 나오는 String으로 변환
		String inputTime = monthDayHourMinute.format(currentTime);
		// 포맷에 맞춰 입력될 시간 변수, 포맷과 다를경우 ParseException 발생		

		System.out.println("출석체크메뉴입니다.");
		// 현재 출석 상태 체크 변수
		int currentStat = -2;
		// 현재 출석 상태 체크
		ck.checkTable(tableName, phoneNum);
		ck.initialData(tableName, phoneNum);
		currentStat = ck.checkStat(tableName, phoneNum);
		System.out.println(inputTime);
		// 0 == 입실 전
		if (currentStat == 0)
		{
			System.out.println("상태: 입실 전");
			ck.checkIn(tableName, inputTime, phoneNum);
		}
		// 1 == 입실한 후
		else if (currentStat == 1)
		{
			System.out.println("상태: 입실");
			System.out.println("1. 퇴실");
			System.out.println("2. 조퇴");
			input = input_msg();
			// 1 == 퇴실
			if (input.equals("1"))
			{
				ck.checkOut(tableName, inputTime, phoneNum);
			}
			else if (input.equals("2"))
			{
				ck.checkGoHome(tableName, inputTime, phoneNum);
			}
		}
		// 2 == 퇴실한 후
		else if (currentStat == 2)
		{
			System.out.println("상태: 퇴실");
			System.out.println("이미 퇴실 하셨습니다.");
		}
		else
		{
			System.out.println("DB 오류!");
		}
	}
}
