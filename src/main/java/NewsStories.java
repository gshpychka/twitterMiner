import java.util.Date;
import java.util.HashMap;
import java.util.Map;

class NewsStories {
  private Map<String, String> newsMap = new HashMap<>();

  NewsStories() {
    this.newsMap.put("2017/07/07", "'Trump meets with putin'");
  }



  String getStory(long timestamp) {
    return newsMap.getOrDefault(new Date(timestamp).toString(), "null");
  }
}
