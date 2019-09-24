package kr.co.attend.view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Check extends CommonMethod
{
	// 입력받을 변수
	String input;
	// date table 생성을 위헤 월일 나오게 포맷
	SimpleDateFormat monthDay = new SimpleDateFormat("MMdd");	 
	// 시간 date 값을 table로 저장하기위한 포맷
	SimpleDateFormat monthDayHourMinute = new SimpleDateFormat("MM/dd/HH:mm");

	// date table 이름 변수
	String tableName = CreateTableName();

	// date table 이름 생성 
	public String CreateTableName()
	{
		// 현 시간 생성
		Date currentTime = new Date();
		// date table 생성을 위헤 월일 나오게 포맷
		String s_currentTime = monthDay.format(currentTime);
		return "DATE" + s_currentTime;
	}
	
	// 시간을 받아 월일시분 포맷에 맞춰 변경
	public String MonthDayHourMinute(Date currentTime)
	{
		// 입실퇴실조퇴에 쓰일 월일시분 나오게 포맷
		String inputTime = monthDayHourMinute.format(currentTime);
		return inputTime;
	}

	// DB 존재 및 넘어온 전화번호로 조회하여 입퇴실구분
	public void checkTable()
	{
		Connection conn = dbCon();
		PreparedStatement pstmt = null;
		Statement stmt = null;
		// ResultSet (DB 결과값 저장)를 선언하여 가져오기
		ResultSet rs = null, rs2 = null;
		String sql = "SELECT table_name FROM all_tables WHERE table_name = ?";
		try
		{
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, tableName);
			rs = pstmt.executeQuery();

			// table 미존재시 table 생성
			if (!rs.next())
			{
				System.out.println("미존재!");
				stmt = conn.createStatement();
				sql = "CREATE TABLE " + tableName + "(" + "phoneNum VARCHAR2(20)," + "inTime DATE,"
						+ "goHome DATE," + "absence VARCHAR2(20)," + "outTime DATE,"
						+ "CONSTRAINT pn_pk_" + tableName + " PRIMARY KEY(phoneNum)" + ")";
				rs2 = stmt.executeQuery(sql);
				initialData();
				System.out.println("테이블생성");
			}

		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try
			{
				// 순서 맞춰서 종료
				if (rs2 != null)
				{
					rs2.close();
				}
				if (rs != null)
				{
					rs.close();
				}
				if (stmt != null)
				{
					stmt.close();
				}
				if (pstmt != null)
				{
					pstmt.close();
				}
				if (conn != null)
				{
					conn.close();
				}
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	} // checkTable END

	// 초기값 생성 - people 테이블로 부터 훈련중인 사람만 찾아 테이블에 입력 
	public void initialData()
	{
		Connection conn = dbCon();
		PreparedStatement pstmt = null;
		Statement stmt = null;
		// ResultSet (DB 결과값 저장)를 선언하여 가져오기
		ResultSet rs = null;

		// phoneNum 전번, inTime 입실, goHome 조퇴, absence 결석, outTime 퇴실

		String sql1 = "INSERT INTO " + tableName;
		String sql2 = "(phoneNum) SELECT phoneNum FROM people WHERE isTraining = '훈련중'";

		try
		{
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql1 + sql2);

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				// 순서 맞춰서 종료
				if (rs != null)
				{
					rs.close();
				}
				if (stmt != null)
				{
					stmt.close();
				}
				if (pstmt != null)
				{
					pstmt.close();
				}
				if (conn != null)
				{
					conn.close();
				}
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	} // initialData END

	// 입실시간, 조퇴시간, 퇴실시간을 보여줌
	public int checkStat(String phoneNum)
	{
		Connection conn = dbCon();
		
		Statement stmt = null;
		// ResultSet (DB 결과값 저장)를 선언하여 가져오기
		ResultSet rs = null;
		int result = 0;
		String sql = "SELECT inTime, goHome, outTime FROM " + tableName + " WHERE phoneNum ="
				+ phoneNum;
		try
		{
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next())
			{
				if (rs.getDate(1) != null && rs.getDate(2) == null && rs.getDate(3) == null)
				{
					System.out.println("입실시간 : " + MonthDayHourMinute(rs.getTimestamp(1)));
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
					}
					catch (SQLException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					result = 1;
				}
				else if (rs.getDate(1) != null && rs.getDate(3) != null)
				{
					System.out.println("입실시간 : " + MonthDayHourMinute(rs.getTimestamp(1))
							+ " | 퇴실시간 : " + MonthDayHourMinute(rs.getTime(3)));
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
					}
					catch (SQLException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					result = 2;
				}
				else if (rs.getDate(1) != null && rs.getDate(2) != null)
				{
					System.out.println("입실시간 : " + MonthDayHourMinute(rs.getTimestamp(1))
							+ " | 조퇴시간 : " + MonthDayHourMinute(rs.getTimestamp(2)));
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
					}
					catch (SQLException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					result = 3;
				}
			}

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				// 순서 맞춰서 종료
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
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return result;
	} // checkStat END

	public void checkIn(String phoneNum)
	{
		Connection conn = dbCon();
		// Query문을 실행시킬 수 있도록 구조 만들기
		PreparedStatement pstmt = null;
		// ResultSet (DB 결과값 저장)를 선언하여 가져오기
		ResultSet rs = null;

		for (;;)
		{
			System.out.println("입실하시겠습니까? (Y,N)");
			input = input_msg();
			if (input.toUpperCase().equals("Y"))
			{
				// 현 시간 생성
				Date currentTime = new Date();
				// 현재 날짜를 월일시분 나오는 String으로 변환
				String inputTime = MonthDayHourMinute(currentTime);
				// phoneNum 전번, inTime 입실, goHome 조퇴, absence 결석, outTime 퇴실
				String sql = "UPDATE " + tableName;
				String sql2 = " SET inTime = TO_DATE(?,'MM/DD/HH24:MI') WHERE phoneNum = ?";
				try
				{
					pstmt = conn.prepareStatement(sql + sql2);
					pstmt.setString(1, inputTime);
					pstmt.setString(2, phoneNum);
					pstmt.executeUpdate();
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}
				finally
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
					}
					catch (SQLException e)
					{
						e.printStackTrace();
					}

				}
				break;
			}
			else if (input.toUpperCase().equals("N"))
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
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}
				System.out.println("취소하셨습니다.\n");
				break;
			}
			else
			{
				System.out.println("Y 또는 N만 입력해주세요.");
			}
		}
	} // checkIn END

	public void checkOut(String phoneNum)
	{
		Connection conn = dbCon();
		// Query문을 실행시킬 수 있도록 구조 만들기
		PreparedStatement pstmt = null;
		// ResultSet (DB 결과값 저장)를 선언하여 가져오기
		ResultSet rs = null;

		System.out.println("퇴실시 재입실이 불가합니다.");
		for (;;)
		{
			System.out.println("정말로 퇴실 하시겠습니까? (Y,N)");
			input = input_msg();

			if (input.toUpperCase().equals("Y"))
			{
				// 현 시간 생성
				Date currentTime = new Date();
				// 현재 날짜를 월일시분 나오는 String으로 변환
				String outTime = MonthDayHourMinute(currentTime);
				// phoneNum 전번, inTime 입실, goHome 조퇴, absence 결석, outTime 퇴실
				String sql = "UPDATE " + tableName;
				String sql2 = " SET outTime = TO_DATE(?,'MM/DD/HH24:MI') WHERE phoneNum = ?";

				try
				{
					pstmt = conn.prepareStatement(sql + sql2);
					pstmt.setString(1, outTime);
					pstmt.setString(2, phoneNum);
					pstmt.executeUpdate();

				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}
				finally
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
					}
					catch (SQLException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			}
			else if (input.toUpperCase().equals("N"))
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
				}
				catch (SQLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("취소하셨습니다.\n");
				break;
			}
			else
			{
				System.out.println("Y 또는 N만 입력해주세요.");
			}
		}
	} // checkOut END

	public void checkGoHome(String phoneNum)
	{
		Connection conn = dbCon();
		// Query문을 실행시킬 수 있도록 구조 만들기
		PreparedStatement pstmt = null;
		// ResultSet (DB 결과값 저장)를 선언하여 가져오기
		ResultSet rs = null;

		System.out.println("조퇴시 재입실이 불가합니다.");
		for (;;)
		{
			System.out.println("정말로 조퇴 하시겠습니까? (Y,N)");
			input = input_msg();

			if (input.toUpperCase().equals("Y"))
			{
				// 현 시간 생성
				Date currentTime = new Date();
				// 현재 날짜를 월일시분 나오는 String으로 변환
				String goHome = MonthDayHourMinute(currentTime);
				// phoneNum 전번, inTime 입실, goHome 조퇴, absence 결석, outTime 퇴실
				String sql = "UPDATE " + tableName;
				String sql2 = " SET goHome = TO_DATE(?,'MM/DD/HH24:MI') WHERE phoneNum = ?";
				try
				{
					pstmt = conn.prepareStatement(sql + sql2);
					pstmt.setString(1, goHome);
					pstmt.setString(2, phoneNum);
					pstmt.executeUpdate();
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}
				finally
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
					}
					catch (SQLException e)
					{
						e.printStackTrace();
					}
				}
				break;
			}
			else if (input.toUpperCase().equals("N"))
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
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}
				System.out.println("취소하셨습니다.\n");
				break;
			}
			else
			{
				System.out.println("Y 또는 N만 입력해주세요.");
			}
		}
	} // checkGoHome END

}
