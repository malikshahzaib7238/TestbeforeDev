package twitter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.Instant;

public class Extract {

    public static Timespan getTimespan(List<Tweet> tweets) {
        if (tweets.isEmpty()) {
            return new Timespan(Instant.now(), Instant.now());
        }
        
        Instant start = tweets.get(0).getTimestamp();
        Instant end = tweets.get(0).getTimestamp();
        
        for (Tweet tweet : tweets) {
            Instant tweetTime = tweet.getTimestamp();
            if (tweetTime.isBefore(start)) {
                start = tweetTime;
            }
            if (tweetTime.isAfter(end)) {
                end = tweetTime;
            }
        }
        
        return new Timespan(start, end);
    }

    public static Set<String> getMentionedUsers(List<Tweet> tweets) {
        Set<String> mentionedUsers = new HashSet<>();

        for (Tweet tweet : tweets) {
            String text = tweet.getText();
            
            String regex = "(?<![\\w.-])@([\\w_]+)(?![\\w.-])"; 
            Matcher matcher = Pattern.compile(regex).matcher(text);
            
            while (matcher.find()) {
                String username = matcher.group(1).toLowerCase(); 
                mentionedUsers.add(username);
            }
        }

        return mentionedUsers;
    }
}
