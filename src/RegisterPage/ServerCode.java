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
@WebServlet("/reg")
public class ServerCode extends HttpServlet{
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String name=req.getParameter("nm");
		String email=req.getParameter("em");
		String password=req.getParameter("ps");
		Connection con=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		RequestDispatcher d=null;
		String qry="insert into student values(?,?,?)";
		try {
			Class.forName(UserUtility.DRIVER);
			con=DriverManager.getConnection(UserUtility.URL,UserUtility.USER,UserUtility.PASSWORD);
			pst=con.prepareStatement(qry);
			PrintWriter writer=resp.getWriter();
			pst.setString(1, name);
			pst.setString(2, email);
			pst.setString(3, password);
			
			try {
			pst.executeUpdate();
			d=req.getRequestDispatcher("SucessfullySaved.html");
			d.forward(req, resp);
			}
			catch(SQLIntegrityConstraintViolationException e) {
				d=req.getRequestDispatcher("Already.html");
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
