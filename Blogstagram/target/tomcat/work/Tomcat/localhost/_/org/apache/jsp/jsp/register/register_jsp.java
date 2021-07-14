/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/7.0.47
 * Generated at: 2021-07-14 19:33:19 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp.jsp.register;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.blogstagram.errors.VariableError;
import java.util.List;
import java.util.ArrayList;

public final class register_jsp extends org.apache.jasper.runtime.HttpJspBase
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
      out.write("<html>\r\n");
      out.write("\r\n");
      out.write("    <head>\r\n");
      out.write("        <title>Blogstagram</title>\r\n");
      out.write("        ");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/jsp/templates/bootstrap.jsp", out, false);
      out.write("\r\n");
      out.write("    </head>\r\n");
      out.write("    <body>\r\n");
      out.write("        ");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/jsp/templates/nav.jsp", out, false);
      out.write("\r\n");
      out.write("        <div class=\"container my-4\">\r\n");
      out.write("            <div class=\"row justify-content-center\">\r\n");
      out.write("                <div class=\"col-md-8\">\r\n");
      out.write("                    <div class=\"card\">\r\n");
      out.write("                        <div class=\"card-header\">Register</div>\r\n");
      out.write("\r\n");
      out.write("                        <div class=\"card-body\">\r\n");
      out.write("                            <form id=\"register-form\" method=\"POST\" action=\"/register\">\r\n");
      out.write("\r\n");
      out.write("                                <div class=\"form-group row\">\r\n");
      out.write("                                    <label for=\"firstname\" class=\"col-md-4 col-form-label text-md-right\">Firstname</label>\r\n");
      out.write("\r\n");
      out.write("                                    <div class=\"col-md-6\">\r\n");
      out.write("                                        <input id=\"firstname\" type=\"text\" class=\"form-control \" name=\"firstname\" required />\r\n");
      out.write("                                        <div id=\"error-firstname\" class=\"text-danger\">\r\n");
      out.write("\r\n");
      out.write("                                        </div>\r\n");
      out.write("                                    </div>\r\n");
      out.write("                                </div>\r\n");
      out.write("                                <div class=\"form-group row\">\r\n");
      out.write("                                    <label for=\"lastname\" class=\"col-md-4 col-form-label text-md-right\">Lastname</label>\r\n");
      out.write("\r\n");
      out.write("                                    <div class=\"col-md-6\">\r\n");
      out.write("                                        <input id=\"lastname\" type=\"text\" class=\"form-control\" name=\"lastname\" required />\r\n");
      out.write("                                        <div id=\"error-lastname\" class=\"text-danger\">\r\n");
      out.write("\r\n");
      out.write("                                        </div>\r\n");
      out.write("                                    </div>\r\n");
      out.write("                                </div>\r\n");
      out.write("                                <div class=\"form-group row\">\r\n");
      out.write("                                    <label for=\"nickname\" class=\"col-md-4 col-form-label text-md-right\">Nickname</label>\r\n");
      out.write("                                    <div class=\"col-md-6\">\r\n");
      out.write("                                        <input id=\"nickname\" type=\"text\" class=\"form-control\" name=\"lastname\" required  />\r\n");
      out.write("                                        <div id=\"error-nickname\" class=\"text-danger\">\r\n");
      out.write("\r\n");
      out.write("                                        </div>\r\n");
      out.write("                                    </div>\r\n");
      out.write("                                </div>\r\n");
      out.write("\r\n");
      out.write("                                <div class=\"form-group row\">\r\n");
      out.write("                                    <label for=\"email\" class=\"col-md-4 col-form-label text-md-right\">E-Mail Address</label>\r\n");
      out.write("\r\n");
      out.write("                                    <div class=\"col-md-6\">\r\n");
      out.write("                                        <input id=\"email\" type=\"email\" class=\"form-control\" name=\"email\" required />\r\n");
      out.write("                                        <div id=\"error-email\" class=\"text-danger\">\r\n");
      out.write("\r\n");
      out.write("                                        </div>\r\n");
      out.write("                                    </div>\r\n");
      out.write("                                </div>\r\n");
      out.write("                                <div class=\"form-group row\">\r\n");
      out.write("                                    <label for=\"birthday\" class=\"col-md-4 col-form-label text-md-right\">Birthday</label>\r\n");
      out.write("                                    <div class=\"col-md-6\">\r\n");
      out.write("                                        <input id=\"birthday\" type=\"date\" class=\"form-control\" name=\"birthday\" required />\r\n");
      out.write("                                        <div id=\"error-birthday\" class=\"text-danger\">\r\n");
      out.write("\r\n");
      out.write("                                        </div>\r\n");
      out.write("                                    </div>\r\n");
      out.write("\r\n");
      out.write("                                </div>\r\n");
      out.write("\r\n");
      out.write("                                <div class=\"form-group row\">\r\n");
      out.write("                                    <label for=\"gender\" class=\"col-md-4 col-form-label text-md-right\">Gender</label>\r\n");
      out.write("                                    <div class=\"col-md-6\">\r\n");
      out.write("                                        <select id=\"gender\" name=\"gender\" class=\"form-control\" required>\r\n");
      out.write("                                            <option value=\"\" selected>Select</option>\r\n");
      out.write("                                            <option value=\"0\">Male</option>\r\n");
      out.write("                                            <option value=\"1\">Female</option>\r\n");
      out.write("                                        </select>\r\n");
      out.write("                                        <div id=\"error-gender\" class=\"text-danger\">\r\n");
      out.write("\r\n");
      out.write("                                        </div>\r\n");
      out.write("                                    </div>\r\n");
      out.write("\r\n");
      out.write("                                </div>\r\n");
      out.write("\r\n");
      out.write("                                <div class=\"form-group row\">\r\n");
      out.write("                                    <label for=\"privacy\" class=\"col-md-4 col-form-label text-md-right\">Privacy</label>\r\n");
      out.write("                                    <div class=\"col-md-6\">\r\n");
      out.write("                                        <select  id=\"privacy\" name=\"privacy\" class=\"form-control\">\r\n");
      out.write("                                            <option value=\"0\" selected>Public</option>\r\n");
      out.write("                                            <option value=\"1\">Private</option>\r\n");
      out.write("                                        </select>\r\n");
      out.write("                                        <div id=\"error-privacy\" class=\"text-danger\">\r\n");
      out.write("\r\n");
      out.write("                                        </div>\r\n");
      out.write("                                    </div>\r\n");
      out.write("\r\n");
      out.write("                                </div>\r\n");
      out.write("\r\n");
      out.write("                                <div class=\"form-group row\">\r\n");
      out.write("                                    <label for=\"password\" class=\"col-md-4 col-form-label text-md-right\">Password</label>\r\n");
      out.write("\r\n");
      out.write("                                    <div class=\"col-md-6\">\r\n");
      out.write("                                        <input id=\"password\" type=\"password\" class=\"form-control\" name=\"password\" required=\"\"  />\r\n");
      out.write("                                        <div id=\"error-password\" class=\"text-danger\">\r\n");
      out.write("\r\n");
      out.write("                                        </div>\r\n");
      out.write("                                    </div>\r\n");
      out.write("                                </div>\r\n");
      out.write("\r\n");
      out.write("                                <div class=\"form-group row\">\r\n");
      out.write("                                    <label for=\"password_confirmation\" class=\"col-md-4 col-form-label text-md-right\">Confirm Password</label>\r\n");
      out.write("\r\n");
      out.write("                                    <div class=\"col-md-6\">\r\n");
      out.write("                                        <input id=\"password_confirmation\" type=\"password\" class=\"form-control\" name=\"password_confirmation\" required />\r\n");
      out.write("                                        <div id=\"error-password_confirmation\" class=\"text-danger\">\r\n");
      out.write("\r\n");
      out.write("                                        </div>\r\n");
      out.write("                                    </div>\r\n");
      out.write("                                </div>\r\n");
      out.write("\r\n");
      out.write("                                <div class=\"form-group row my-2\">\r\n");
      out.write("                                    <div class=\"col-md-4\"></div>\r\n");
      out.write("                                    <div class=\"col-md-6\">\r\n");
      out.write("                                        <button type=\"submit\" class=\"btn btn-primary w-100\">\r\n");
      out.write("                                            Register\r\n");
      out.write("                                        </button>\r\n");
      out.write("                                    </div>\r\n");
      out.write("                                </div>\r\n");
      out.write("                                <div class=\"form-group row\">\r\n");
      out.write("                                    <div class=\"col-md-4\"></div>\r\n");
      out.write("                                    <div class=\"col-md-6\">\r\n");
      out.write("                                        <button class=\"btn btn-secondary w-100\">\r\n");
      out.write("                                            <a class=\"text-white\" href=\"/login\">Already Signed Up? Log In!</a>\r\n");
      out.write("                                        </button>\r\n");
      out.write("                                    </div>\r\n");
      out.write("                                </div>\r\n");
      out.write("                                <div class=\"form-group row\">\r\n");
      out.write("                                    <div class=\"col-md-4\"></div>\r\n");
      out.write("                                    <div class=\"col-md-6\">\r\n");
      out.write("                                        <div id=\"success\" class=\"text-success\">\r\n");
      out.write("\r\n");
      out.write("                                        </div>\r\n");
      out.write("                                    </div>\r\n");
      out.write("                                </div>\r\n");
      out.write("                            </form>\r\n");
      out.write("                        </div>\r\n");
      out.write("                    </div>\r\n");
      out.write("                </div>\r\n");
      out.write("            </div>\r\n");
      out.write("        </div>\r\n");
      out.write("        ");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/jsp/templates/footer.jsp", out, false);
      out.write("\r\n");
      out.write("        <script>\r\n");
      out.write("            const fields = [\"firstname\",\"lastname\",\"nickname\",\"birthday\",\"email\",\"gender\",\"privacy\",\"password\",\"password_confirmation\"];\r\n");
      out.write("\r\n");
      out.write("            function addFieldChangeListeners(){\r\n");
      out.write("                for(let field of fields){\r\n");
      out.write("                    document.getElementById(field).addEventListener('input',(e) => {\r\n");
      out.write("                        document.getElementById(`error-${field}`).textContent = '';\r\n");
      out.write("                        document.getElementById(field).classList.remove('is-invalid');\r\n");
      out.write("                    });\r\n");
      out.write("                }\r\n");
      out.write("            }\r\n");
      out.write("            addFieldChangeListeners();\r\n");
      out.write("\r\n");
      out.write("            function refreshFieldMessages(){\r\n");
      out.write("                for(let field of fields){\r\n");
      out.write("                    document.getElementById(`error-${field}`).textContent = '';\r\n");
      out.write("                    document.getElementById(field).classList.remove('is-invalid');\r\n");
      out.write("                }\r\n");
      out.write("            }\r\n");
      out.write("            let formInput = document.getElementById(\"register-form\");\r\n");
      out.write("            formInput.addEventListener('submit',(e) => {\r\n");
      out.write("                e.preventDefault();\r\n");
      out.write("\r\n");
      out.write("                let firstname = document.getElementById(\"firstname\").value;\r\n");
      out.write("                let lastname = document.getElementById(\"lastname\").value;\r\n");
      out.write("                let nickname = document.getElementById(\"nickname\").value;\r\n");
      out.write("                let email = document.getElementById(\"email\").value;\r\n");
      out.write("                let birthday = document.getElementById(\"birthday\").value;\r\n");
      out.write("                let gender = document.getElementById(\"gender\").value;\r\n");
      out.write("                let privacy = document.getElementById(\"privacy\").value;\r\n");
      out.write("                let password = document.getElementById(\"password\").value;\r\n");
      out.write("                let password_confirmation = document.getElementById(\"password_confirmation\").value;\r\n");
      out.write("                $.post(\"/register\",{\r\n");
      out.write("                    firstname,lastname,nickname,email,birthday,gender,privacy,password,password_confirmation\r\n");
      out.write("                }).then(rawResponse => {\r\n");
      out.write("                    refreshFieldMessages();\r\n");
      out.write("\r\n");
      out.write("                    if(rawResponse.length === 0){\r\n");
      out.write("                        refreshFieldMessages();\r\n");
      out.write("                        document.getElementById(\"success\").innerText = \"You have been registered successfully! You will be redirected to news feed in 5 seconds...\";\r\n");
      out.write("                        setTimeout(() => {\r\n");
      out.write("                            window.location.href = \"/\";\r\n");
      out.write("                        },5000);\r\n");
      out.write("                    } else {\r\n");
      out.write("                        let errors = JSON.parse(rawResponse);\r\n");
      out.write("                        for(let error of errors){\r\n");
      out.write("                            let {variableName, errorMessage} = error;\r\n");
      out.write("                            let errorID = \"error-\"+variableName;\r\n");
      out.write("                            document.getElementById(errorID).innerText += errorMessage+\"\\n\";\r\n");
      out.write("                            document.getElementById(variableName).classList.add('is-invalid');\r\n");
      out.write("                        }\r\n");
      out.write("                    }\r\n");
      out.write("\r\n");
      out.write("                }).catch(error => {\r\n");
      out.write("                    console.log(error);\r\n");
      out.write("                });\r\n");
      out.write("            });\r\n");
      out.write("        </script>\r\n");
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
