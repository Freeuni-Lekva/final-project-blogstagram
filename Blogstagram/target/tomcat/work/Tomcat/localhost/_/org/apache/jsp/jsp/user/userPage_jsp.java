/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/7.0.47
 * Generated at: 2021-07-17 10:01:53 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp.jsp.user;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.blogstagram.models.User;

public final class userPage_jsp extends org.apache.jasper.runtime.HttpJspBase
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
    boolean isUserLoggedIn = (currentUserNickname != null);

      out.write("\r\n");
      out.write("<html>\r\n");
      out.write("    <head>\r\n");
      out.write("        <title>Blogstagram</title>\r\n");
      out.write("\r\n");
      out.write("        ");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/jsp/templates/bootstrap.jsp", out, false);
      out.write("\r\n");
      out.write("        <!-- Link for icons -->\r\n");
      out.write("        <link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\">\r\n");
      out.write("        <style>\r\n");
      out.write("            .user-profile-picture{\r\n");
      out.write("                width: 150px;\r\n");
      out.write("                height: 150px;\r\n");
      out.write("                border-radius:50%;\r\n");
      out.write("                border: 2px solid lightgray;\r\n");
      out.write("                padding: 5px;\r\n");
      out.write("                background-color: whitesmoke;\r\n");
      out.write("            }\r\n");
      out.write("            .modal-link:hover{\r\n");
      out.write("                text-decoration: none;\r\n");
      out.write("            }\r\n");
      out.write("        </style>\r\n");
      out.write("    </head>\r\n");
      out.write("    <body>\r\n");
      out.write("        ");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/jsp/templates/nav.jsp", out, false);
      out.write("\r\n");
      out.write("\r\n");
      out.write("        <div class=\"container my-5\">\r\n");
      out.write("            <div class=\"container border border-dark mt-2\" style=\"width:80%;\">\r\n");
      out.write("                <div class=\"row m-2\">\r\n");
      out.write("                    <div class=\"col-2\">\r\n");
      out.write("                        <img src=\"");
      out.print( user.getImage() );
      out.write("\" class=\"user-profile-picture mr-5\"/>\r\n");
      out.write("                    </div>\r\n");
      out.write("                    <div class=\"col-10\">\r\n");
      out.write("                        <div class=\"d-flex ml-5 mt-4\">\r\n");
      out.write("                            <h4 class=\"mr-2\">");
      out.print( user.getFirstname() );
      out.write(' ');
      out.print( user.getLastname() );
      out.write(' ');
      out.write('(');
      out.print( user.getNickname());
      out.write(")</h4>\r\n");
      out.write("                            <!-- Follow Button Template -->\r\n");
      out.write("                            ");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/jsp/user/templates/followButtonTemplate.jsp", out, false);
      out.write("\r\n");
      out.write("\r\n");
      out.write("                            <!-- Gear Icon Template (Including Modal) -->\r\n");
      out.write("                            ");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/jsp/user/templates/gearIconTemplate.jsp", out, false);
      out.write("\r\n");
      out.write("\r\n");
      out.write("                        </div>\r\n");
      out.write("\r\n");
      out.write("                        <!-- User General Info Template -->\r\n");
      out.write("                        ");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/jsp/user/templates/userGeneralInfoTemplate.jsp", out, false);
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("                    </div>\r\n");
      out.write("                </div>\r\n");
      out.write("            </div>\r\n");
      out.write("            <div class=\"container border-dark border-left border-bottom border-right\" style=\"width:80%;\">\r\n");
      out.write("                <div class=\"row\">\r\n");
      out.write("                    <div class=\"col-4 border text-center p-2\">\r\n");
      out.write("                        <span class=\"font-weight-bold mr-1\">100</span>blogs\r\n");
      out.write("                    </div>\r\n");
      out.write("                    <div class=\"col-4 border text-center p-2\">\r\n");
      out.write("                        <span class=\"font-weight-bold mr-1\">199</span><a href=\"#\" class=\"text-dark\">followers</a>\r\n");
      out.write("                    </div>\r\n");
      out.write("                    <div class=\"col-4 border text-center p-2\">\r\n");
      out.write("                        <span class=\"font-weight-bold mr-1\">200</span><a href=\"#\" class=\"text-dark\">following</a>\r\n");
      out.write("                    </div>\r\n");
      out.write("                </div>\r\n");
      out.write("            </div>\r\n");
      out.write("        </div>\r\n");
      out.write("\r\n");
      out.write("        <!-- Bio Template -->\r\n");
      out.write("        ");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/jsp/user/templates/userBioTemplate.jsp", out, false);
      out.write("\r\n");
      out.write("\r\n");
      out.write("        <div class=\"container my-5\">\r\n");
      out.write("            <div class=\"container border\" style=\"width:80%;\">\r\n");
      out.write("                <div class=\"container-fluid d-flex flex-wrap\">\r\n");
      out.write("                    <div class=\"card bg-light blog-card\">\r\n");
      out.write("                        <div class=\"card-body text-center\">\r\n");
      out.write("                            <p class=\"card-text\">\r\n");
      out.write("                                <span class=\"text-info font-weight-bold\">Some text inside the sixth card</span>\r\n");
      out.write("                            </p>\r\n");
      out.write("                        </div>\r\n");
      out.write("                    </div>\r\n");
      out.write("                    <div class=\"card bg-light blog-card\">\r\n");
      out.write("                        <div class=\"card-body text-center\">\r\n");
      out.write("                            <p class=\"card-text\">\r\n");
      out.write("                                <span class=\"text-info font-weight-bold\">Some text inside the sixth card</span>\r\n");
      out.write("                            </p>\r\n");
      out.write("                        </div>\r\n");
      out.write("                    </div>\r\n");
      out.write("                    <div class=\"card bg-light blog-card\">\r\n");
      out.write("                        <div class=\"card-body text-center\">\r\n");
      out.write("                            <p class=\"card-text\">\r\n");
      out.write("                                <span class=\"text-info font-weight-bold\">Some text inside the sixth card</span>\r\n");
      out.write("                            </p>\r\n");
      out.write("                        </div>\r\n");
      out.write("                    </div>\r\n");
      out.write("                    <div class=\"card bg-light blog-card\">\r\n");
      out.write("                        <div class=\"card-body text-center\">\r\n");
      out.write("                            <p class=\"card-text\">\r\n");
      out.write("                                <span class=\"text-info font-weight-bold\">Some text inside the sixth card</span>\r\n");
      out.write("                            </p>\r\n");
      out.write("                        </div>\r\n");
      out.write("                    </div>\r\n");
      out.write("                    <div class=\"card bg-light blog-card\">\r\n");
      out.write("                        <div class=\"card-body text-center\">\r\n");
      out.write("                            <p class=\"card-text\">\r\n");
      out.write("                                <span class=\"text-info font-weight-bold\">Some text inside the sixth card</span>\r\n");
      out.write("                            </p>\r\n");
      out.write("                        </div>\r\n");
      out.write("                    </div>\r\n");
      out.write("                    <div class=\"card bg-light blog-card\">\r\n");
      out.write("                        <div class=\"card-body text-center\">\r\n");
      out.write("                            <p class=\"card-text\">\r\n");
      out.write("                                <span class=\"text-info font-weight-bold\">Some text inside the sixth card</span>\r\n");
      out.write("                            </p>\r\n");
      out.write("                        </div>\r\n");
      out.write("                    </div>\r\n");
      out.write("\r\n");
      out.write("                </div>\r\n");
      out.write("            </div>\r\n");
      out.write("        </div>\r\n");
      out.write("\r\n");
      out.write("        ");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/jsp/templates/footer.jsp", out, false);
      out.write("\r\n");
      out.write("    </body>\r\n");
      out.write("</html>\r\n");
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
