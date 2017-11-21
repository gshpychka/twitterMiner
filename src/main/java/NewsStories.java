import java.util.Date;
import java.util.HashMap;
import java.util.Map;

class NewsStories {
  private Map<String, String> newsMap = new HashMap<>();

  NewsStories() {
    this.newsMap.put("07/07/2017", "'Trump meets with putin'");
  }



  String getStory(long timestamp) {
    Date date = new Date(timestamp);
    String dateFormat = date.getDay() + "/" + date.getMonth() + "/" + date.getYear();
    if (newsMap.containsKey(dateFormat)) {
      String story = newsMap.get(dateFormat);
      newsMap.remove(dateFormat); // so that each story is only printed once
      return story;
    } else {
      return "null";
    }

  }
}
