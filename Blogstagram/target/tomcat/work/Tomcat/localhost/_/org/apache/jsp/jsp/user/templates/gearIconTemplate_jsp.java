/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/7.0.47
 * Generated at: 2021-07-15 14:02:25 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp.jsp.user.templates;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.blogstagram.models.User;

public final class gearIconTemplate_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
  }

  public void _jspDestroy() {
  }

  public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
        throws java.io.IOException, javax.servlet.ServletException {

    final javax.servlet.jsp.PageContext pageContext;
    javax.servlet.http.HttpSession session = null;
    final javax.servlet.ServletContext application;
    final javax.servlet.ServletConfig config;
    javax.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html;charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");

    User user = (User)request.getAttribute("User");
    String currentUserNickname = (String)request.getSession().getAttribute("currentUserNickname");
    boolean isCurrentUser = currentUserNickname != null && (currentUserNickname.equals(user.getNickname()));
    boolean isUserLoggedIn = (currentUserNickname != null);

      out.write("\r\n");
      out.write("\r\n");
 if(isUserLoggedIn) { 
      out.write("\r\n");
      out.write("<button type=\"button\" class=\"btn\" data-toggle=\"modal\" data-target=\"#settingsModal\">\r\n");
      out.write("    <i class=\"fa fa-gear fa-spin text-dark mt-2\" style=\"font-size:20px;\"></i>\r\n");
      out.write("</button>\r\n");
      out.write("<!-- The Modal -->\r\n");
      out.write("<div class=\"modal\" id=\"settingsModal\">\r\n");
      out.write("    <div class=\"modal-dialog\">\r\n");
      out.write("        <div class=\"modal-content\">\r\n");
      out.write("\r\n");
      out.write("            <!-- Modal Header -->\r\n");
      out.write("            <div class=\"modal-header\">\r\n");
      out.write("                <h4 class=\"modal-title\">");
      out.print( user.getFirstname() );
      out.write(' ');
      out.print( user.getLastname() );
      out.write(' ');
      out.write('(');
      out.print( user.getNickname());
      out.write(")</h4>\r\n");
      out.write("                <button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button>\r\n");
      out.write("            </div>\r\n");
      out.write("\r\n");
      out.write("            <!-- Modal body -->\r\n");
      out.write("            <div class=\"modal-body\">\r\n");
      out.write("                ");
 if(!isCurrentUser){ 
      out.write("\r\n");
      out.write("                <a href=\"#\" class=\"modal-link\">\r\n");
      out.write("                    <button type=\"button\" class=\"btn btn-outline-danger btn-block my-2\">Report</button>\r\n");
      out.write("                </a>\r\n");
      out.write("                ");
 } else { 
      out.write("\r\n");
      out.write("                <a href=\"/edit/profile\" class=\"modal-link\">\r\n");
      out.write("                    <button type=\"button\" class=\"btn btn-outline-info btn-block my-2\">Edit Profile</button>\r\n");
      out.write("                </a>\r\n");
      out.write("                <a href=\"/logout\" class=\"modal-link\">\r\n");
      out.write("                    <button type=\"button\" class=\"btn btn-outline-secondary btn-block my-2\">Logout</button>\r\n");
      out.write("                </a>\r\n");
      out.write("                ");
 } 
      out.write("\r\n");
      out.write("\r\n");
      out.write("            </div>\r\n");
      out.write("\r\n");
      out.write("            <!-- Modal footer -->\r\n");
      out.write("            <div class=\"modal-footer\">\r\n");
      out.write("                <button type=\"button\" class=\"btn btn-danger\" data-dismiss=\"modal\">Close</button>\r\n");
      out.write("            </div>\r\n");
      out.write("\r\n");
      out.write("        </div>\r\n");
      out.write("    </div>\r\n");
      out.write("</div>\r\n");
 } 
    } catch (java.lang.Throwable t) {
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
