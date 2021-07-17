/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/7.0.47
 * Generated at: 2021-07-17 10:09:52 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp.jsp.user.templates;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.blogstagram.models.User;
import org.blogstagram.followSystem.api.StatusCodes;

public final class followButtonTemplate_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write('\r');
      out.write('\n');
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");

    User user = (User)request.getAttribute("User");
    String currentUserNickname = (String)request.getSession().getAttribute("currentUserNickname");

    Integer followStatus = (Integer)request.getAttribute("FollowStatus");

    boolean isCurrentUser = currentUserNickname != null && (currentUserNickname.equals(user.getNickname()));
    boolean isUserLoggedIn = (currentUserNickname != null);


      out.write("\r\n");
      out.write("\r\n");
 if(!isCurrentUser && isUserLoggedIn) { 
      out.write("\r\n");
      out.write("\r\n");
      out.write("    <form id=\"follow-form\" class=\"m-0 p-0\" toID=\"");
      out.print( user.getId() );
      out.write("\">\r\n");
      out.write("        ");
 if(followStatus.equals(StatusCodes.notFollowed)) { 
      out.write("\r\n");
      out.write("                <button id=\"follow-form-submission-button\" type=\"submit\" class=\"btn btn-primary\" >Follow</button>\r\n");
      out.write("        ");
 } else if(followStatus.equals(StatusCodes.requestSent)) { 
      out.write("\r\n");
      out.write("                <button id=\"follow-form-submission-button\" type=\"submit\" class=\"btn btn-outline-dark\" >Requested</button>\r\n");
      out.write("        ");
 } else { 
      out.write("\r\n");
      out.write("                <button id=\"follow-form-submission-button\" type=\"submit\" class=\"btn btn-outline-dark\" >Following</button>\r\n");
      out.write("        ");
 } 
      out.write("\r\n");
      out.write("    </form>\r\n");
 } else if(isCurrentUser && isUserLoggedIn){ 
      out.write("\r\n");
      out.write("    <a href=\"/edit/profile\">\r\n");
      out.write("        <button type=\"button\" class=\"btn btn-outline-primary\">Edit profile</button>\r\n");
      out.write("    </a>\r\n");
 } 
      out.write("\r\n");
      out.write("\r\n");
      out.write("<script>\r\n");
      out.write("    $(document).ready(function(){\r\n");
      out.write("\r\n");
      out.write("        const followButton = document.getElementById(\"follow-form-submission-button\");\r\n");
      out.write("        const followersTag = document.getElementById(\"followers-count\");\r\n");
      out.write("        $(\"#follow-form\").submit(function(e){\r\n");
      out.write("            e.preventDefault();\r\n");
      out.write("            $.post(\"/current/user/id\").then(rawCurrentUserResponse => {\r\n");
      out.write("                const currentUserID = JSON.parse(rawCurrentUserResponse);\r\n");
      out.write("                if(currentUserID == null)\r\n");
      out.write("                    return;\r\n");
      out.write("\r\n");
      out.write("                const to_id = String(document.getElementById(\"follow-form\").getAttribute(\"toID\"));\r\n");
      out.write("                $.post(\"/followSystem\",\r\n");
      out.write("                    {to_id}\r\n");
      out.write("                ).then(rawFollowResponse => {\r\n");
      out.write("                    const followResponse = JSON.parse(rawFollowResponse);\r\n");
      out.write("                    console.log(followResponse);\r\n");
      out.write("\r\n");
      out.write("                    switch(followResponse.status[0]){\r\n");
      out.write("                        case 1:\r\n");
      out.write("                            followButton.className=\"btn btn-outline-dark\";\r\n");
      out.write("                            followButton.innerText = \"Following\";\r\n");
      out.write("                            followersTag.innerText = (Number(followersTag.innerText)+1);\r\n");
      out.write("                            break;\r\n");
      out.write("                        case 2:\r\n");
      out.write("                            followButton.className=\"btn btn-primary\";\r\n");
      out.write("                            followButton.innerText = \"Follow\"\r\n");
      out.write("                            followersTag.innerText = (Number(followersTag.innerText)-1);\r\n");
      out.write("                            break;\r\n");
      out.write("                        case 3:\r\n");
      out.write("                            followButton.className = \"btn btn-outline-dark\";\r\n");
      out.write("                            followButton.innerText = \"Request sent\";\r\n");
      out.write("                            break;\r\n");
      out.write("                        default:\r\n");
      out.write("                            console.log(\"Unknown status\");\r\n");
      out.write("                    }\r\n");
      out.write("\r\n");
      out.write("                })\r\n");
      out.write("            })\r\n");
      out.write("        })\r\n");
      out.write("    })\r\n");
      out.write("</script>\r\n");
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
