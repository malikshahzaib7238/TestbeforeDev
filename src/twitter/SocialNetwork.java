package twitter;

import java.util.*;

public class SocialNetwork {

	public static Map<String, Set<String>> guessFollowsGraph(List<Tweet> tweets) {
	    Map<String, Set<String>> followsGraph = new HashMap<>();
	    
	    for (Tweet tweet : tweets) {
	        String author = tweet.getAuthor().toLowerCase();
	        Set<String> mentionedUsers = Extract.getMentionedUsers(Collections.singletonList(tweet));

	        // Remove self-mentions and handle lowercase usernames
	        mentionedUsers.remove(author);
	        
	        // Only add the author to the graph if they mention someone
	        if (!mentionedUsers.isEmpty()) {
	            followsGraph.putIfAbsent(author, new HashSet<>());
	            followsGraph.get(author).addAll(mentionedUsers);
	        }
	    }

	    return followsGraph;
	}

    public static List<String> influencers(Map<String, Set<String>> followsGraph) {
        Map<String, Integer> followerCount = new HashMap<>();
        
        // Count how many times each user is followed
        for (Set<String> follows : followsGraph.values()) {
            for (String followedUser : follows) {
                followedUser = followedUser.toLowerCase();  // ensure lowercase
                followerCount.put(followedUser, followerCount.getOrDefault(followedUser, 0) + 1);
            }
        }

        // Sort the users by their follower count in descending order
        List<String> influencers = new ArrayList<>(followerCount.keySet());
        influencers.sort((a, b) -> followerCount.get(b) - followerCount.get(a)); // Descending
        
        return influencers;
    }
}
