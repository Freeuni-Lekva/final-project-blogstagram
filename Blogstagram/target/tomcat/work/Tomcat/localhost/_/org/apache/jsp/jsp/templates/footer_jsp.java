/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/7.0.47
 * Generated at: 2021-07-11 09:43:17 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp.jsp.templates;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class footer_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write("\n");
      out.write("\n");
      out.write("<footer class=\"bg-dark text-white text-center text-lg-start\">\n");
      out.write("    <div class=\"container p-4\">\n");
      out.write("        <div class=\"row\">\n");
      out.write("            <div class=\"col-lg-6 col-md-12 mb-4 mb-md-0\">\n");
      out.write("                <h5 class=\" font-weight-bold\">How Blogstagram Works</h5>\n");
      out.write("                <p class=\"font-weight-lighter\">\n");
      out.write("                    Lorem ipsum dolor sit amet consectetur, adipisicing elit. Iste atque ea quis\n");
      out.write("                    molestias. Fugiat pariatur maxime quis culpa corporis vitae repudiandae aliquam\n");
      out.write("                    voluptatem veniam, est atque cumque eum delectus sint!\n");
      out.write("                </p>\n");
      out.write("            </div>\n");
      out.write("            <div class=\"col-lg-3 col-md-6 mb-4 mb-md-0\">\n");
      out.write("                <h5 class=\"font-weight-bold\">About Us</h5>\n");
      out.write("                <ul class=\"list-unstyled mb-0\">\n");
      out.write("                    <li>\n");
      out.write("                        <a href=\"#!\" class=\"text-white\">Link 1</a>\n");
      out.write("                    </li>\n");
      out.write("                    <li>\n");
      out.write("                        <a href=\"#!\" class=\"text-white\">Link 2</a>\n");
      out.write("                    </li>\n");
      out.write("                    <li>\n");
      out.write("                        <a href=\"#!\" class=\"text-white\">Link 3</a>\n");
      out.write("                    </li>\n");
      out.write("                </ul>\n");
      out.write("            </div>\n");
      out.write("            <div class=\"col-lg-3 col-md-6 mb-4 mb-md-0\">\n");
      out.write("                <h5 class=\"font-weight-bold\">Contact Us</h5>\n");
      out.write("                <ul class=\"list-unstyled\">\n");
      out.write("                    <li>\n");
      out.write("                        <a href=\"#!\" class=\"text-white\">Facebook</a>\n");
      out.write("                    </li>\n");
      out.write("                    <li>\n");
      out.write("                        <a href=\"#!\" class=\"text-white\">Instagram</a>\n");
      out.write("                    </li>\n");
      out.write("                </ul>\n");
      out.write("            </div>\n");
      out.write("        </div>\n");
      out.write("    </div>\n");
      out.write("\n");
      out.write("    <!-- Copyright -->\n");
      out.write("    <div class=\"text-center p-3\" style=\"background-color: rgba(0, 0, 0, 0.2)\">\n");
      out.write("        © 2021 Copyright:\n");
      out.write("        <a class=\"text-white\" href=\"https://mdbootstrap.com/\">blogstagram.ge</a>\n");
      out.write("    </div>\n");
      out.write("    <!-- Copyright -->\n");
      out.write("</footer>");
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
