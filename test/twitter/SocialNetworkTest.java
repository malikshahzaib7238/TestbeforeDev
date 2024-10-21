package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.*;

import org.junit.Test;

public class SocialNetworkTest {

    /*
     * Testing Strategy:
     * 
     * guessFollowsGraph():
     *  - Test with an empty list of tweets.
     *  - Test with tweets that contain no mentions.
     *  - Test with a single tweet with a single mention.
     *  - Test with a tweet containing multiple mentions.
     *  - Test with multiple tweets by the same author.
     * 
     * influencers():
     *  - Test with an empty graph.
     *  - Test with a single user who follows no one.
     *  - Test with a single influencer in the graph.
     *  - Test with multiple influencers with different numbers of followers.
     *  - Test with multiple influencers with the same number of followers.
     */

    @Test
    public void testGuessFollowsGraphEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<>());
        assertTrue("expected empty graph", followsGraph.isEmpty());
    }
    
    @Test
    public void testGuessFollowsGraphNoMentions() {
        List<Tweet> tweets = Arrays.asList(
            new Tweet(1, "alice", "Hello world!", Instant.now()),
            new Tweet(2, "bob", "Good morning!", Instant.now())
        );
        
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue("expected empty graph", followsGraph.isEmpty());
    }
    
    @Test
    public void testGuessFollowsGraphSingleMention() {
        List<Tweet> tweets = Arrays.asList(
            new Tweet(1, "alice", "Hey @bob, how are you?", Instant.now())
        );
        
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        
        assertTrue("alice should follow bob", followsGraph.containsKey("alice"));
        assertTrue("alice should follow bob", followsGraph.get("alice").contains("bob"));
    }

    @Test
    public void testGuessFollowsGraphMultipleMentions() {
        List<Tweet> tweets = Arrays.asList(
            new Tweet(1, "alice", "Hey @bob and @charlie, how are you?", Instant.now())
        );
        
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        
        assertTrue("alice should follow bob", followsGraph.get("alice").contains("bob"));
        assertTrue("alice should follow charlie", followsGraph.get("alice").contains("charlie"));
    }

    @Test
    public void testGuessFollowsGraphMultipleTweetsSameAuthor() {
        List<Tweet> tweets = Arrays.asList(
            new Tweet(1, "alice", "Hey @bob", Instant.now()),
            new Tweet(2, "alice", "Hey @charlie", Instant.now())
        );
        
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        
        assertTrue("alice should follow bob", followsGraph.get("alice").contains("bob"));
        assertTrue("alice should follow charlie", followsGraph.get("alice").contains("charlie"));
    }

    @Test
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertTrue("expected empty list", influencers.isEmpty());
    }

    @Test
    public void testInfluencersSingleUserNoFollowers() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("alice", new HashSet<>());
        
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertTrue("expected empty list of influencers", influencers.isEmpty());
    }
    
    @Test
    public void testInfluencersSingleUser() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("alice", new HashSet<>(Arrays.asList("bob")));
        
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertEquals("bob should be the top influencer", Arrays.asList("bob"), influencers);
    }

    @Test
    public void testInfluencersMultipleUsers() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("alice", new HashSet<>(Arrays.asList("bob", "charlie")));
        followsGraph.put("david", new HashSet<>(Arrays.asList("bob")));
        
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertEquals("bob should be the top influencer", "bob", influencers.get(0));
        assertEquals("charlie should be the second influencer", "charlie", influencers.get(1));
    }

    @Test
    public void testInfluencersEqualFollowers() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("alice", new HashSet<>(Arrays.asList("bob", "charlie")));
        followsGraph.put("david", new HashSet<>(Arrays.asList("bob", "charlie")));
        
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        // bob and charlie both have the same number of followers, so they can appear in any order
        List<String> expected = Arrays.asList("bob", "charlie");
        assertTrue("bob and charlie should both be influencers", influencers.containsAll(expected));
    }
}
