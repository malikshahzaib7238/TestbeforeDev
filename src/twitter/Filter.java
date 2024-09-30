package twitter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Filter {

    public static List<Tweet> writtenBy(List<Tweet> tweets, String username) {
        List<Tweet> result = new ArrayList<>();
        for (Tweet tweet : tweets) {
            if (tweet.getAuthor().equalsIgnoreCase(username)) {
                result.add(tweet);
            }
        }
        return result;
    }

    public static List<Tweet> inTimespan(List<Tweet> tweets, Timespan timespan) {
        List<Tweet> result = new ArrayList<>();
        for (Tweet tweet : tweets) {
            Instant tweetTime = tweet.getTimestamp();
            if (!tweetTime.isBefore(timespan.getStart()) && !tweetTime.isAfter(timespan.getEnd())) {
                result.add(tweet);
            }
        }
        return result;
    }

    public static List<Tweet> containing(List<Tweet> tweets, List<String> words) {
        List<Tweet> result = new ArrayList<>();
        for (Tweet tweet : tweets) {
            String text = tweet.getText();
            for (String word : words) {
                if (text.toLowerCase().contains(word.toLowerCase())) {
                    result.add(tweet);
                    break; // No need to check other words if one match is found
                }
            }
        }
        return result;
    }
}
