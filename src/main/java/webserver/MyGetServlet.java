package webserver;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MyGetServlet extends HttpServlet {
  public void doPost(HttpServletRequest request, HttpServletResponse response) {
    return;
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.getWriter().println(PageGenerator.instance().getPage("chart.html", createPageVariablesMap(request)));
  }

  private static Map<String, Object> createPageVariablesMap(HttpServletRequest request) {
    Map<String, Object> pageVariables = new HashMap<>();

    pageVariables.put("start", request.getParameter("start"));
    pageVariables.put("finish", request.getParameter("finish"));
    return pageVariables;
  }
}
