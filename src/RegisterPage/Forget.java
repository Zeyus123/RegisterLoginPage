package RegisterPage;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/forget")
public class Forget extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String email = req.getParameter("em");
		String pass1 = req.getParameter("ps1");
		String pass2 = req.getParameter("ps2");
		;
		String qry = "update student set Pasword=? where Mail=?";
		Connection con = null;
		PrintWriter writer = resp.getWriter();
		PreparedStatement pst = null;
		RequestDispatcher rd = null;
		boolean flag = pass1.equals(pass2);
		// System.out.println(flag);
		if (flag == false) {
			writer.write("<html><body><script>\r\n" + "        alert(\"Password must be same\")\r\n"
					+ "    </script></body></html>");
			rd = req.getRequestDispatcher("forget.html");
			rd.include(req, resp);
		} else {
			try {
				Class.forName(UserUtility.DRIVER);
				con = DriverManager.getConnection(UserUtility.URL, UserUtility.USER, UserUtility.PASSWORD);
				pst = con.prepareStatement(qry);
				pst.setString(1, pass1);
				pst.setString(2, email);
				

				try {
					if (mail(email).equals(email)) {

						pst.executeUpdate();

						writer.write("<html><body><script>\r\n" + "        alert(\"Sucessfull\")\r\n"
								+ "    </script></body></html>");
						rd = req.getRequestDispatcher("login.html");
						rd.forward(req, resp);
					} else {
						writer.write("<html><body><script>\r\n" + "        alert(\"Email not found\")\r\n"
								+ "    </script></body></html>");
						rd = req.getRequestDispatcher("forget.html");
						rd.include(req, resp);
					}

				} catch (SQLIntegrityConstraintViolationException e) {
					writer.write("<html><body><script>\r\n" + "        alert(\"Email not found\")\r\n"
							+ "    </script></body></html>");
					rd = req.getRequestDispatcher("forget.html");
					rd.include(req, resp);
				}
			}

			catch (SQLException | ClassNotFoundException e) {
				e.printStackTrace();
			}

			finally {
				if (con != null && pst != null) {
					try {
						con.close();
						pst.close();

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}

		}
	}
	public static String mail(String emailid) {
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String qry = "select * from student where Mail=?";
		try {
			Class.forName(UserUtility.DRIVER);
			con = DriverManager.getConnection(UserUtility.URL, UserUtility.USER, UserUtility.PASSWORD);
			pst = con.prepareStatement(qry);
			pst.setString(1, emailid);
			rs = pst.executeQuery();
			if (rs.next()) {
				return rs.getString("Mail");
			}
			else {
				return "not found";
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		finally {
			if (con != null && pst != null ) {
				try {
					con.close();
					pst.close();
					

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		return "not found";
		

	}

	
}
