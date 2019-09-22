package kr.co.attend.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CommonMethod {
	// Create Connection
	private static final String URL = "jdbc:oracle:thin:@192.168.14.163:1521:XE"; // direct conn
	private static final String USER = "scott";
	private static final String PASSWORD = "tiger";

	// 공용적으로 사용할 메소드 선언 객체
	// 입력값 받을 수 있도록 구성하는 메소드
	public String input_msg() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String menu = null;
		try {
			menu = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return menu;
	}

	// Connection 생성 메소드 구성
	protected Connection dbCon() {
		Connection con = null;

		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return con;
	}

}
