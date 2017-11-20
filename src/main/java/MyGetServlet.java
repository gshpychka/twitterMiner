
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MyGetServlet extends HttpServlet {
  private String chartData;
  public void doPost(HttpServletRequest request, HttpServletResponse response) {
    return;
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.getWriter().println(PageGenerator.instance().getPage("chart.html", createPageVariablesMap(request)));
  }

  private Map<String, Object> createPageVariablesMap(HttpServletRequest request) {
    Map<String, Object> pageVariables = new HashMap<>();
    pageVariables.put("chartData", chartData);
    return pageVariables;
  }

  MyGetServlet() {
    super();
    long start;
    long finish;
    this.chartData = ChartDataProvider.getChartData(1498780800, Long.MAX_VALUE);
  }
}
