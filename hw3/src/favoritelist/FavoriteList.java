package favoritelist;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.RollbackException;

import dao.FavoriteDAO;
import dao.UserDAO;
import databean.Favorite;
import databean.User;
import formbean.IdForm;
import formbean.FavoriteForm;
import formbean.LoginForm;
import formbean.RegisterForm;

public class FavoriteList extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private FavoriteDAO favoriteDAO;
	private UserDAO userDAO;

	public void init() throws ServletException {
		String jdbcDriverName = getInitParameter("jdbcDriver");
		String jdbcURL = getInitParameter("jdbcURL");

		try {
			ConnectionPool cp = new ConnectionPool(jdbcDriverName, jdbcURL);
/*	
			cp.setDebugOutput(System.out);  // Print out the generated SQL
*/
			userDAO = new UserDAO(cp);
			favoriteDAO = new FavoriteDAO(cp);
		} catch (DAOException e) {
			throw new ServletException(e);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		Favorite item = (Favorite) session.getAttribute("favorite");
		session.setAttribute("user", user);
		System.out.println("I am in get");
		//System.out.println(user.getEmailAddress());
		if ( user == null) {
			outputLoginPage(response, null, null);
		} else {
			try {
				favoriteDAO.increment(item);
			} catch (RollbackException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			outputFavoriteList(response, request);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		String regstr = (String) session.getAttribute("register");
		session.setAttribute("user", user);
		if (regstr == null || !regstr.equals("true")) {
			if (user == null) {
				login(request, response);
				System.out.println("I am in 9");
			}
			else{
				manageList(request, response);
			}
		}
		else if(regstr.equals("true")) {
				register(request, response);
			} 
	}

	private void login(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<String> errors = new ArrayList<String>();

		LoginForm form = new LoginForm(request);

		errors.addAll(form.getValidationErrors());
		if (errors.size() != 0) {
			outputLoginPage(response, form, errors);
			return;
		}

		try {
			User user = new User();

			if (form.getButton().equals("Register")) {
				register(request, response);
			} else {
				System.out.println("I am in 8");
				user = userDAO.read(form.getEmailAddress());
				if (user == null) {
					errors.add("No such user");
					outputLoginPage(response, form, errors);
					return;
				}
				//System.out.println(form.getPassword());
				//System.out.println(user.getPassword());

				if (!user.getPassword().equals(form.getPassword())) {
					errors.add("Incorrect password");
					outputLoginPage(response, form, errors);
					return;
				}
				HttpSession session = request.getSession();
				session.setAttribute("user", user);
				outputFavoriteList(response, request);
			}

			
		} catch (RollbackException e) {
			errors.add(e.getMessage());
			outputLoginPage(response, form, errors);
		}
	}
	
	private void register(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RegisterForm rstForm = new RegisterForm(request);
		List<String> rstErrors = new ArrayList<String>();
		//System.out.println(rstForm.getEmailAddress() + rstForm.getFirstName() + rstForm.getLastName());
		
		if (rstForm.getButton().equals("Register now")) {
			rstErrors.addAll(rstForm.getValidationErrors());
			if (rstErrors.size() != 0) {
				outputRegisterPage(response, rstForm, rstErrors);
				System.out.println("I am in 1");
				return;
			}
			
			try {
				System.out.println("I am in 2");
				User user;
					user = userDAO.read(rstForm.getEmailAddress());
					if (user != null) {
						rstErrors.add("This email address has already been registered!");
						outputRegisterPage(response, rstForm, rstErrors);
						return;
					}
						user = new User(rstForm.getEmailAddress(), rstForm.getFirstName(), rstForm.getLastName(), rstForm.getPassword());					

				HttpSession session = request.getSession();
				session.setAttribute("user", user);
				userDAO.create(user);
				session.setAttribute("register", "false");
				outputFavoriteList(response, request);
			} catch (RollbackException e) {
				rstErrors.add(e.getMessage());
				outputRegisterPage(response, rstForm, rstErrors);
			}
		} else {
			HttpSession session = request.getSession();
			session.setAttribute("register", "true");
			outputRegisterPage(response, rstForm, rstErrors);
			System.out.println("I am in 3");
		}
		
	}

	private void manageList(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Look at the action parameter to see what we're doing to the list
		String action = request.getParameter("action");
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		session.setAttribute("user", user);

		if (action == null) {
			outputFavoriteList(response, request);
			return;
		}

		if (action.equals("Add Favorite")) {
			processAdd(request, response);
			return;
		}

		if (action.equals("Log Out")) {
			processLogOut(request, response);
			return;
		}

		//outputFavoriteList(response, "Invalid action: " + action);
	}

	private void processAdd(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<String> errors = new ArrayList<String>();

		FavoriteForm form = new FavoriteForm(request);
		HttpSession session = request.getSession();
		User u = (User) session.getAttribute("user");
		session.setAttribute("user", u);

		errors.addAll(form.getValidationErrors());
		if (errors.size() > 0) {
			outputFavoriteList(response, request, errors);
			return;
		}

		try {
			Favorite bean = new Favorite();
			bean.setUrl(form.getUrl());
			bean.setComment(form.getUrl());
			bean.setClickCount(0);
			bean.setUserId(u.getUserId());
			favoriteDAO.create(bean);
			
			outputFavoriteList(response, request, "Item Added");
		} catch (RollbackException e) {
			errors.add(e.getMessage());
			outputFavoriteList(response, request, errors);
		}
	}
	
	private void processLogOut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User user = new User();
		HttpSession session = request.getSession();
        session.setAttribute("user", null);
        System.out.println("You have signed out!");
        //login(request, response);
        outputLoginPage(response, null, null);
		
	}
	

	// Methods that generate & output HTML
	
	private final String servletName = this.getClass().getSimpleName();

	private void generateHead(PrintWriter out) {
		out.println("  <head>");
		out.println("    <meta charset=\"utf-8\"/>");
		out.println("    <title>" + servletName + "</title>");
		out.println("  </head>");
	}

	private void outputLoginPage(HttpServletResponse response, LoginForm form, List<String> errors) throws IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();

		out.println("<!DOCTYPE html>");
		out.println("<html>");

		generateHead(out);

		out.println("<body>");
		out.println("<h2>" + servletName + " Login</h2>");

		if (errors != null && errors.size() > 0) {
			for (String error : errors) {
				out.println("<p style=\"font-size: large; color: red\">");
				out.println(error);
				out.println("</p>");
			}
		}

		// Generate an HTML <form> to get data from the user
		out.println("<form method=\"POST\">");
		out.println("    <table>");
		out.println("        <tr>");
		out.println(
				"            <td style=\"font-size: x-large\">User Email:</td>");
		out.println("            <td>");
		out.println("                <input type=\"text\" name=\"emailAddress\"");
		if (form != null && form.getEmailAddress() != null) {
			out.println(
					"                    value=\"" + form.getEmailAddress() + "\"");
		}
		out.println("                autofocus />");
		out.println("            </td>");
		out.println("        </tr>");
		out.println("        <tr>");
		out.println(
				"            <td style=\"font-size: x-large\">Password:</td>");
		out.println(
				"            <td><input type=\"password\" name=\"password\" /></td>");
		out.println("        </tr>");
		out.println("        <tr>");
		out.println(
				"            <td colspan=\"2\" style=\"text-align: center;\">");
		out.println(
				"                <input type=\"submit\" name=\"button\" value=\"Login\" />");
		out.println(
				"                <input type=\"submit\" name=\"button\" value=\"Register\" />");
		out.println("            </td>");
		out.println("        </tr>");
		out.println("    </table>");
		out.println("</form>");
		out.println("</body>");
		out.println("</html>");
	}
	
	private void outputRegisterPage(HttpServletResponse response, RegisterForm form, List<String> errors) throws IOException {
				
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();

		out.println("<!DOCTYPE html>");
		out.println("<html>");

		generateHead(out);

		out.println("<body>");
		out.println("<h2>" + servletName + " Register</h2>");

		if (errors != null && errors.size() > 0) {
			for (String error : errors) {
				out.println("<p style=\"font-size: large; color: red\">");
				out.println(error);
				out.println("</p>");
			}
		}

		// Generate an HTML <form> to get data from the user
		out.println("<form method=\"POST\">");
		out.println("    <table>");
		out.println("        <tr>");
		out.println(
				"            <td style=\"font-size: x-large\">User Email:</td>");
		out.println("            <td>");
		out.println("                <input type=\"text\" name=\"emailAddress\"");
		if (form != null && form.getEmailAddress() != null) {
			out.println(
					"                    value=\"" + form.getEmailAddress() + "\"");
		}
		out.println("                autofocus />");
		out.println("            </td>");
		out.println("        </tr>");
		out.println("        <tr>");
		out.println(
				"            <td style=\"font-size: x-large\">First Name:</td>");
		out.println(
				"            <td><input name=\"firstName\" /></td>");
		out.println("        </tr>");
		out.println("        <tr>");
		out.println(
				"            <td style=\"font-size: x-large\">Last Name:</td>");
		out.println(
				"            <td><input name=\"lastName\" /></td>");
		out.println("        </tr>");
		out.println("        <tr>");
		out.println(
				"            <td style=\"font-size: x-large\">Password:</td>");
		out.println(
				"            <td><input type=\"password\" name=\"password\" /></td>");
		out.println("        </tr>");
		out.println("        <tr>");
		out.println(
				"            <td colspan=\"2\" style=\"text-align: center;\">");
		out.println(
				"                <input type=\"submit\" name=\"button\" value=\"Register now\" />");
		out.println("            </td>");
		out.println("        </tr>");
		out.println("    </table>");
		out.println("</form>");
		out.println("</body>");
		out.println("</html>");
	}

	private void outputFavoriteList(HttpServletResponse response, HttpServletRequest request)
			throws IOException {
		// Just call the version that takes a List passing an empty List
		List<String> list = new ArrayList<String>();
		outputFavoriteList(response, request, list);
	}

	private void outputFavoriteList(HttpServletResponse response, HttpServletRequest request, String message)
			throws IOException {
		// Put the message into a List and call the version that takes a List
		List<String> list = new ArrayList<String>();
		list.add(message);
		outputFavoriteList(response, request, list);
	}

	private void outputFavoriteList(HttpServletResponse response, HttpServletRequest request, 
			List<String> messages) throws IOException {
		// Get the list of items to display at the end
		Favorite[] beans;
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		//System.out.println(user.getFirstName());
		try {
			beans = favoriteDAO.getFavoritesList(user.getUserId());
		} catch (RollbackException e) {
			// If there's an access error, add the message to our list of
			// messages
			messages.add(e.getMessage());
			beans = new Favorite[0];
		}

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();

		out.println("<!DOCTYPE html>");
		out.println("<html>");

		generateHead(out);

		out.println("<body>");
		out.println("<h2>Favorites for ");
		if (user != null) {
			out.println(user.getFirstName() + " ");
			out.println(user.getLastName());
		}
				
		out.println("</h2>");

		// Generate an HTML <form> to get data from the user
		out.println("<form method=\"POST\">");
		out.println("    <table>");
		out.println("        <tr><td colspan=\"3\"><hr/></td></tr>");
		out.println("        <tr>");
		out.println(
				"            <td style=\"font-size: large\">URL:</td>");
		out.println(
				"            <td colspan=\"2\"><input type=\"text\" size=\"40\" name=\"url\" autofocus/></td>");
		out.println("        </tr>");
		out.println("        <tr>");
		out.println("        <tr>");
		out.println(
				"            <td style=\"font-size: large\">Comment:</td>");
		out.println(
				"            <td colspan=\"2\"><input type=\"text\" size=\"40\" name=\"comment\" /></td>");
		out.println("        </tr>");
		out.println("        <tr>");
		out.println("            <td></td>");
		out.println(
				"            <td><input type=\"submit\" name=\"action\" value=\"Add Favorite\"/></td>");
		out.println(
				"            <td><input type=\"submit\" name=\"action\" value=\"Log out\"/></td>");
		out.println("        </tr>");
		out.println("        <tr><td colspan=\"3\"><hr/></td></tr>");
		out.println("    </table>");
		out.println("</form>");

		for (String message : messages) {
			out.println("<p style=\"font-size: large; color: red\">");
			out.println(message);
			out.println("</p>");
		}

		out.println("<table>");
		for (int i = 0; i < beans.length; i++) {
			out.println("    <tr>");
			out.println("        <td>");
			out.println(
					"                <input type=\"hidden\" name=\"id\" value=\""
							+ beans[i].getId() + "\" />");
			out.println("        </td>");
			out.println("    </tr>");
			out.println("    <tr>");
			out.println("<form method=\"GET\">");
			out.println("        <td><a href= FavoriteList>"
					+ beans[i].getUrl() + "</td>");
			HttpSession sen = request.getSession();
			sen.setAttribute("favorite", beans[i]);
			out.println("</form>");
			out.println("    </tr>");
			out.println("    <tr>");
			out.println("        <td><span style=\"font-size: x-large\">"
					+ beans[i].getComment() + "</td>");
			out.println("        <td><span style=\"font-size: x-large\">"
					+ beans[i].getClickCount() + "</td>");
			out.println("    </tr>");
			out.println("    <tr>");
			out.println("    </tr>");
		}
		out.println("</table>");

		out.println("</body>");
		out.println("</html>");
	}
}

