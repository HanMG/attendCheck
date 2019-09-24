package kr.co.attend.view;

public class CheckView extends CommonMethod
{
	public void checkMenu(String phoneNum)
	{
		// 입력받을 String 변수
		String input;
		
		// Check 객체 생성
		Check ck = new Check();	

		System.out.println("*****출석체크메뉴*****");
		// 현재 출석 상태 체크 변수
		int currentStat = -2;
		// 현재 출석 상태 체크
		ck.checkTable();		
		// 현재 상태를 체크해서 값을 반환
		for (;;)
		{
			currentStat = ck.checkStat(phoneNum);

			// 0 == 입실 전
			if (currentStat == 0)
			{				
				System.out.println("- 상태: 입실 전");
				System.out.println("1. 입실");
				System.out.println("2. 메뉴 나가기");
				input = input_msg();
				if(input.equals("1")) {
					ck.checkIn(phoneNum);					
				}
				else if(input.equals("2")) {
					break;
				}		
				else {
					System.out.println("Error - 1 또는 2만 입력가능합니다.");
				}
			}
			// 1 == 입실한 후
			else if (currentStat == 1)
			{
				System.out.println("- 상태: 입실");
				System.out.println("1. 퇴실");
				System.out.println("2. 조퇴");
				System.out.println("3. 메뉴 나가기");
				input = input_msg();
				
				if(input.equals("1")) {
					ck.checkOut(phoneNum);					
				}
				else if(input.equals("2")) {
					ck.checkGoHome(phoneNum);					
				}
				else if(input.equals("3")) {					
					break;
				}
				else {
					System.out.println("Error - 1, 2, 3만 입력가능합니다.");
				}			
			}
			// 2 == 퇴실한 후
			else if (currentStat == 2)
			{
				System.out.println("- 상태: 퇴실");
				System.out.println("퇴실 하셨습니다.\n");
				break;
			}
			// 3 == 조퇴한 후
			else if (currentStat == 3)
			{
				System.out.println("- 상태: 조퇴");
				System.out.println("조퇴 하셨습니다.\n");
				break;
			}
			else
			{
				System.out.println("ERROR!\n");
			}
		}
	}
}
