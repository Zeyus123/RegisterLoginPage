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
@WebServlet("/log")
public class Login extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String email=req.getParameter("em");
		String password=req.getParameter("ps");
		Connection con=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		RequestDispatcher d=null;
		String qry="select * from student where Mail=? and Pasword=?";
		try {
			Class.forName(UserUtility.DRIVER);
			con=DriverManager.getConnection(UserUtility.URL,UserUtility.USER,UserUtility.PASSWORD);
			pst=con.prepareStatement(qry);
			PrintWriter writer=resp.getWriter();
			pst.setString(1, email);
			pst.setString(2, password);
			
			try {
			rs=pst.executeQuery();
			if(rs.next()) {
			writer.write("<html><link rel=\"stylesheet\" href=\"sucess.css\"><body><body id=\"particles-js\"></body>\r\n" + 
					"    <div class=\"animated bounceInDown\">\r\n" + 
					"      <div class=\"container\">\r\n" + 
					"        <span class=\"error animated tada\" id=\"msg\"></span>\r\n" + 
					"        <form name=\"form1\" class=\"box\" \">\r\n" + 
					"          <h1>Hellow </h1>\r\n" + 
					"          <h5> "+rs.getString(1)+"</h5>\r\n" + 
					"           \r\n" + 
					"            \r\n" + 
					"          </form></body></html>");
			}
			else {
				d=req.getRequestDispatcher("UserNotExist.html");
				d.include(req, resp);
			}
			}
			catch(SQLIntegrityConstraintViolationException e) {
				d=req.getRequestDispatcher("UserNotExist.html");
				d.include(req, resp);
			}
			
		} catch (ClassNotFoundException|SQLException e) {
			e.printStackTrace();
		}
		finally {
			if(con!=null&&rs!=null&&pst!=null) {
				try {
					con.close();
					pst.close();
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}

}
