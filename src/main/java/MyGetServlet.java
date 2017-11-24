
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MyGetServlet extends HttpServlet {
  private ChartDataProvider chartDataProvider;

  public void doPost(HttpServletRequest request, HttpServletResponse response) {
    return;
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.getWriter().println(PageGenerator.instance().getPage("chart.html", createPageVariablesMap()));
  }

  private Map<String, Object> createPageVariablesMap() {
    Map<String, Object> pageVariables = new HashMap<>();
    pageVariables.put("chartData", this.chartDataProvider.getChartData());
    return pageVariables;
  }

  MyGetServlet() {
    super();
    this.chartDataProvider = new ChartDataProvider("impeach", 1488404001, Long.MAX_VALUE);
  }
}
