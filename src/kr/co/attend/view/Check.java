package kr.co.attend.view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Check extends CommonMethod
{
	Connection conn = dbCon();
	String input;

	// DB 존재 및 넘어온 전화번호로 조회하여 입퇴실구분
	public void checkTable(String tableName, String phoneNum)
	{
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
				// 순서 맞춰서
				if (rs2 != null)
					rs2.close();

				if (rs != null)
					rs.close();

				if (pstmt != null)
					pstmt.close();				
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public void initialData(String tableName, String phoneNum)
	{
		PreparedStatement pstmt = null;
		Statement stmt = null;
		// ResultSet (DB 결과값 저장)를 선언하여 가져오기
		ResultSet rs = null;

		// phoneNum 전번, inTime 입실, goHome 조퇴, absence 결석, outTime 퇴실
		String sql = "SELECT phoneNum FROM " + tableName;
		String sql1 = "INSERT INTO " + tableName;
		String sql2 = "(phoneNum, inTime, goHome, absence, outTime) VALUES(?,?,?,?,?)";

		try
		{
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (!rs.next())
			{
				pstmt = conn.prepareStatement(sql1 + sql2);
				pstmt.setString(1, phoneNum);
				pstmt.setDate(2, null);
				pstmt.setDate(3, null);
				pstmt.setString(4, null);
				pstmt.setDate(5, null);
				pstmt.executeUpdate();
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
				// 순서 맞춰서
				if (rs != null)
				{
					rs.close();
				}
				if (pstmt != null)
				{
					pstmt.close();
				}
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}

	}

	// 입실시간, 조퇴시간, 퇴실시간을 보여줌
	public int checkStat(String tableName, String phoneNum)
	{

		Statement stmt = null;
		// ResultSet (DB 결과값 저장)를 선언하여 가져오기
		ResultSet rs = null;
		String sql = "SELECT inTime, goHome, outTime FROM " + tableName + " WHERE phoneNum ="
				+ phoneNum;
		try
		{
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next())
			{				
				if (rs.getDate(1) == null && rs.getDate(2) == null && rs.getDate(3) == null)
				{
					return 0;
				}
				else if (rs.getDate(1) != null && rs.getDate(2) == null && rs.getDate(3) == null)
				{
					System.out.println("입실시간: " + rs.getDate(1));
					try
					{
						// 순서 맞춰서
						if (rs != null)
							rs.close();

						if (stmt != null)
							stmt.close();

						if (conn != null)
							conn.close();
					}
					catch (SQLException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return 1;
				}
				else if (rs.getDate(1) != null && rs.getDate(3) != null)
				{
					System.out.println("입실시간: " + rs.getDate(1) + " 퇴실시간: "+ rs.getDate(3));
					try
					{
						// 순서 맞춰서
						if (rs != null)
							rs.close();

						if (stmt != null)
							stmt.close();					
					}
					catch (SQLException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return 2;
				}

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
				// 순서 맞춰서
				if (rs != null)
				{
					rs.close();
				}

				if (stmt != null)
				{
					stmt.close();
				}				
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return -2;
	}

	public void checkIn(String tableName, String inTime, String phoneNum)
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
			String sql = "UPDATE " + tableName;
			String sql2 = " SET inTime = TO_DATE(?,'MM/DD/HH24:MI') WHERE phoneNum = ?";
			try
			{
				pstmt = conn.prepareStatement(sql + sql2);
				pstmt.setString(1, inTime);
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
		}
		else
		{
			System.out.println("취소하셨습니다.");
		}
	} // checkIn END

	public void checkOut(String tableName, String outTime, String phoneNum)
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
			String sql = "UPDATE ? SET outTime = TO_DATE(?,'MM/DD/HH24:MI') WHERE phoneNum = ?";

			try
			{
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, tableName);
				pstmt.setString(2, outTime);
				pstmt.setString(3, phoneNum);
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

		}
		else
		{
			System.out.println("취소하셨습니다.");
		}
	} // checkOut END

	public void checkGoHome(String tableName, String goHome, String phoneNum)
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
			String sql = "UPDATE ? SET goHome = TO_DATE(?,'MM/DD/HH24:MI') WHERE phoneNum = ?";
			try
			{
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, tableName);
				pstmt.setString(2, goHome);
				pstmt.setString(3, phoneNum);
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
		}
		else
		{
			System.out.println("취소하셨습니다.");
		}
	} // checkGoHome END

}
