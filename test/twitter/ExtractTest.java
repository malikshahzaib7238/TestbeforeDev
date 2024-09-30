/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Set;

import org.junit.Test;

public class ExtractTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment looks like.
     * Make sure you have partitions.
     */
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testGetTimespanTwoTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2));
        
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }
    
    @Test
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1));
        
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }
    
    @Test
    public void testGetTimespanSingleTweet() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1));
        
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d1, timespan.getEnd());
    }

    @Test
    public void testGetTimespanEmptyList() {
        Timespan timespan = Extract.getTimespan(Arrays.asList());
        
        // For simplicity, we expect the start and end to be the current time
        Instant now = Instant.now();
        assertEquals("expected start", now.truncatedTo(java.time.temporal.ChronoUnit.SECONDS), 
                     timespan.getStart().truncatedTo(java.time.temporal.ChronoUnit.SECONDS));
        assertEquals("expected end", now.truncatedTo(java.time.temporal.ChronoUnit.SECONDS), 
                     timespan.getEnd().truncatedTo(java.time.temporal.ChronoUnit.SECONDS));
    }

    @Test
    public void testGetMentionedUsersSingleMention() {
        Tweet tweetWithMention = new Tweet(3, "user1", "Hello @user2, how are you?", d1);
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweetWithMention));
        
        assertTrue("expected mentioned users to contain user2", mentionedUsers.contains("user2"));
    }

    @Test
    public void testGetMentionedUsersMultipleMentions() {
        Tweet tweetWithMultipleMentions = new Tweet(4, "user3", "Hey @user4 and @User2!", d2);
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweetWithMultipleMentions));
        
        assertTrue("expected mentioned users to contain user2", mentionedUsers.contains("user2"));
        assertTrue("expected mentioned users to contain user4", mentionedUsers.contains("user4"));
        assertEquals("expected size to be 2", 2, mentionedUsers.size());
    }

    @Test
    public void testGetMentionedUsersInvalidMention() {
        Tweet tweetWithInvalidMention = new Tweet(5, "user5", "Contact me at @invalid-email@example.com", d1);
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweetWithInvalidMention));
        
        assertTrue("expected empty set for invalid mentions", mentionedUsers.isEmpty());
    }

    @Test
    public void testGetMentionedUsersCaseInsensitivity() {
        Tweet tweetWithMention = new Tweet(6, "user6", "Hey @User7!", d1);
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweetWithMention));
        
        assertTrue("expected mentioned users to contain user7", mentionedUsers.contains("user7"));
    }



    /*
     * Warning: all the tests you write here must be runnable against any
     * Extract class that follows the spec. It will be run against several staff
     * implementations of Extract, which will be done by overwriting
     * (temporarily) your version of Extract with the staff's version.
     * DO NOT strengthen the spec of Extract or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Extract, because that means you're testing a
     * stronger spec than Extract says. If you need such helper methods, define
     * them in a different class. If you only need them in this test class, then
     * keep them in this test class.
     */

}
