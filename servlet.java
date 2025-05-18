// Java Servlet Assignment Overview with Example Stubs

/*
 * 1. CGI vs Servlets (Theory)
 * - CGI starts a new process for each request; Servlets are multi-threaded.
 * - Servlets are more efficient, scalable, and have access to Java libraries.
 */

/*
 * 1B. Example CGI using Python (python_cgi.py):
 * #!/usr/bin/env python3
 * import cgi
 * form = cgi.FieldStorage()
 * print("Content-Type: text/html\n")
 * print(f"Hello, {form.getvalue('name')}!")
 */

/*
 * 2. Basic Servlet with doGet/doPost and Request Info
 */
@WebServlet("/WelcomeServlet")
public class WelcomeServlet extends HttpServlet {
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    out.println("<h1>Welcome!</h1>");
    out.println("Client IP: " + request.getRemoteAddr());
    out.println("User-Agent: " + request.getHeader("User-Agent"));
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doGet(request, response);
  }
}

/*
 * 3. ServletConfig and ServletContext Example
 */
@WebServlet("/ConfigServlet")
public class ConfigServlet extends HttpServlet {
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    String dbUser = config.getInitParameter("dbUser");
    getServletContext().setAttribute("dbUser", dbUser);
  }

  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
    PrintWriter out = res.getWriter();
    out.print("DB User: " + getServletContext().getAttribute("dbUser"));
  }
}

/*
 * 4. Session Management (Cookie, URL Rewriting, HttpSession)
 */
@WebServlet("/SessionServlet")
public class SessionServlet extends HttpServlet {
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
    HttpSession session = req.getSession();
    session.setAttribute("theme", "dark");
    res.getWriter().print("Session theme set to dark");
  }
}

/*
 * 5. RequestDispatcher Example
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    String user = request.getParameter("username");
    String pass = request.getParameter("password");

    if("admin".equals(user) && "admin123".equals(pass)) {
      RequestDispatcher rd = request.getRequestDispatcher("/WelcomeServlet");
      rd.forward(request, response);
    } else {
      response.sendRedirect("login.html?error=invalid");
    }
  }
}

/*
 * 6. Filters Example
 */
@WebFilter("/*")
public class LoggingFilter implements Filter {
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    System.out.println("Request from: " + req.getRemoteAddr());
    chain.doFilter(req, res);
  }
}

@WebFilter("/ProtectedServlet")
public class AuthFilter implements Filter {
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpSession session = request.getSession(false);
    if (session != null && session.getAttribute("user") != null) {
      chain.doFilter(req, res);
    } else {
      ((HttpServletResponse) res).sendRedirect("login.html");
    }
  }
}

/*
 * Advanced Topics:
 * - JDBC (with DriverManager.getConnection, SQL queries)
 * - MVC (Servlets as Controllers, JSP for Views, JavaBeans or DB as Model)
 * - AJAX endpoint: Servlet with JSON response using response.setContentType("application/json")
 */
