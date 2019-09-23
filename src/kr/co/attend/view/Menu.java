package kr.co.attend.view;

public class Menu extends CommonMethod
{
	public void menuView()
	{
		String input;
		CheckView cv = new CheckView();
		String phoneNum;
		// TODO Auto-generated method stub
		while (true)
		{
			System.out.println("1.출결체크");
			System.out.println("2.종료");
			input = input_msg();
			
			if (input.equals("1"))
			{
				System.out.println("전번입력");
				phoneNum = input_msg();
				
				cv.checkMenu(phoneNum);	
				break;
			}
			else if(input.equals("2")) {
				System.out.println("종료");
				break;
			}
		}
	}
}
