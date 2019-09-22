package kr.co.attend.view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import kr.co.attend.domain.dateBean;

public class Check extends CommonMethod
{
	Connection conn = dbCon();
	
	String input;

	// 현 시간 생성
	Date currentTime = new Date();
	// date DB 생성을 위헤 월일 나오게 포맷
	SimpleDateFormat monthDay = new SimpleDateFormat("MMdd");
	// 입실퇴실조퇴에 쓰일 월일시분 나오게 포맷
	SimpleDateFormat monthDayHourMinute = new SimpleDateFormat("MM/dd/HH:mm");

	public void checkMenu(String phoneNum)
	{

		// 현재 날짜를 월일 나오는 String으로 변환
		String s_currentTime = monthDay.format(currentTime);
		// date DB 이름
		String dbname = "date" + s_currentTime;		

		// 현재 날짜를 월일시분 나오는 String으로 변환
		String inputTime = monthDayHourMinute.format(currentTime);
		// 포맷에 맞춰 입력될 시간 변수, 포맷과 다를경우 ParseException 발생
		
		Date d_currentTime = null;
		try
		{
			d_currentTime = monthDayHourMinute.parse(inputTime);
		} catch (ParseException e)
		{
			e.printStackTrace();
		}
		System.out.println("출석체크메뉴입니다.");
		checkStat(dbname, phoneNum);
		// 현재 출석 상태 체크 변수
		int currentStat;

		// 현재 출석 상태 체크
		currentStat = checkDB(dbname, phoneNum);

		// 0 == 입실 전
		if (currentStat == 0)
		{
			System.out.println("상태: 입실 전");
			checkIn(dbname, d_currentTime);
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
				checkOut(dbname, d_currentTime);
			}
			else if (input.equals("2"))
			{
				checkGoHome(dbname, d_currentTime);
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

	private void checkStat(String dbname, String phoneNum)
	{		
		// Query문을 실행시킬 수 있도록 구조 만들기		
		Statement stmt = null;

		// ResultSet (DB 결과값 저장)를 선언하여 가져오기
		ResultSet rs = null;
		String sql = "SELECT inTime, goHome, outTime from " + dbname + " WHERE phoneNum ="
				+ phoneNum;
		try
		{
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next())
			{
				System.out.println("입실시간: "+rs.getDate(1)+" 조퇴시간: "+rs.getDate(2)+" 퇴실시간: "+rs.getDate(3));			
			}
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// DB 존재 및 넘어온 전화번호로 조회하여 입퇴실구분
	private int checkDB(String dbname, String phoneNum)
	{
		// Query문을 실행시킬 수 있도록 구조 만들기		
		Statement stmt = null;

		// ResultSet (DB 결과값 저장)를 선언하여 가져오기
		ResultSet rs = null;
		String sql1 = "SELECT COUNT(*) from all_tables WHERE table_name = " + dbname;
		try
		{
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql1);
			if (rs.next())
			{ // DB 존재
				String sql2 = "SELECT inTime, outTime FROM " + dbname + " WHERE phoneNum =="
						+ phoneNum;
				rs = stmt.executeQuery(sql2);
				
				// 데이터가 있다면
				if (rs.next())
				{
					if ((rs.getDate(1) != null && rs.getDate(2) == null))
					{
						return 1; // 입실정보가 있다면
					}
					else if ((rs.getDate(1) != null && rs.getDate(2) != null))
					{
						return 2; // 퇴실정보가 있다면
					}
				}
				// DB는 있는데 입실 정보가 없다면
				else
				{
					return 0;
				}
				
			}
			else
			{
				sql1 = "CREATE TABLE " + dbname + "{ " + "phoneNum VARCHAR2(20)," + "inTime DATE"
						+ "goHome DATE" + "absence VARCHAR2(20)" + "outTime DATE"
						+ "CONSTRAINT pn_pk_" + dbname + " PRIMARY KEY(phoneNum)" + "};";

				return 0; // DB가 미존재
			}
		} catch (SQLException e1)
		{
			e1.printStackTrace();
		} finally
		{
			try
			{
				// 순서 맞춰서
				if (rs != null)
				{
					rs.close();
				}
				if (stmt != null)
				{
					stmt.close();
				}
				if (conn != null)
				{
					conn.close();
				}
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		return -2; // DB 오류
	}

	private void checkIn(String dbname, Date inTime)
	{
		// Query문을 실행시킬 수 있도록 구조 만들기
		PreparedStatement pstmt = null;		

		// ResultSet (DB 결과값 저장)를 선언하여 가져오기
		ResultSet rs = null;
		
		System.out.println("입실하시겠습니까? (Y,N)");
		input = input_msg();

		if (input.toUpperCase().equals("Y"))
		{
			// phoneNum 전번, inTime 입실, goHome 조퇴, absence 결석, outTime 퇴실
			String sql = "INSERT INTO ? (inTime) VALUES(?)";

			try
			{
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, dbname);
				pstmt.setDate(2, (java.sql.Date) inTime);
				pstmt.executeUpdate(sql);
			} catch (SQLException e)
			{
				e.printStackTrace();
			} finally
			{
				try
				{
					// 순서 맞춰서
					if (rs != null)
					{
						rs.close();
					}
					if (pstmt != null)
					{
						pstmt.close();
					}
					if (conn != null)
					{
						conn.close();
					}
				} catch (SQLException e)
				{
					e.printStackTrace();
				}
			}
		}
		else
		{
			System.out.println("취소하셨습니다.");
		}
	} // checkIn END

	private void checkOut(String dbname, Date outTime)
	{
		// Query문을 실행시킬 수 있도록 구조 만들기
		PreparedStatement pstmt = null;
	
		// ResultSet (DB 결과값 저장)를 선언하여 가져오기
		ResultSet rs = null;
		
		System.out.println("퇴실시 재입실이 불가합니다.");
		System.out.println("정말로 퇴실 하시겠습니까? (Y,N)");
		input = input_msg();

		if (input.toUpperCase().equals("Y"))
		{
			// phoneNum 전번, inTime 입실, goHome 조퇴, absence 결석, outTime 퇴실
			String sql = "INSERT INTO ? (outTime) VALUES(?)";

			try
			{
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, dbname);
				pstmt.setDate(2, (java.sql.Date) outTime);
				pstmt.executeUpdate(sql);

			} catch (SQLException e)
			{
				e.printStackTrace();
			} finally
			{
				try
				{
					// 순서 맞춰서
					if (rs != null)
					{
						rs.close();
					}
					if (pstmt != null)
					{
						pstmt.close();
					}
					if (conn != null)
					{
						conn.close();
					}
				} catch (SQLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		else
		{
			System.out.println("취소하셨습니다.");
		}
	} // checkOut END

	private void checkGoHome(String dbname, Date goHome)
	{
		// Query문을 실행시킬 수 있도록 구조 만들기
		PreparedStatement pstmt = null;		
		
		// ResultSet (DB 결과값 저장)를 선언하여 가져오기
		ResultSet rs = null;
		
		System.out.println("조퇴시 재입실이 불가합니다.");
		System.out.println("정말로 조퇴 하시겠습니까? (Y,N)");
		input = input_msg();

		if (input.toUpperCase().equals("Y"))
		{
			// phoneNum 전번, inTime 입실, goHome 조퇴, absence 결석, outTime 퇴실
			String sql = "INSERT INTO ? (goHome) VALUES(?)";
			try
			{
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, dbname);
				pstmt.setDate(2, (java.sql.Date) goHome);

			} catch (SQLException e)
			{
				e.printStackTrace();
			} finally
			{
				try
				{
					// 순서 맞춰서
					if (rs != null)
					{
						rs.close();
					}
					if (pstmt != null)
					{
						pstmt.close();
					}
					if (conn != null)
					{
						conn.close();
					}
				} catch (SQLException e)
				{
					e.printStackTrace();
				}
			}
		}
		else
		{
			System.out.println("취소하셨습니다.");
		}
	} // checkGoHome END

}
