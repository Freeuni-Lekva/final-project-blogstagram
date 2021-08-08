package org.blogstagram.tests.blogTests;

import org.blogstagram.models.Blog;
import org.blogstagram.models.Comment;
import org.blogstagram.models.HashTag;
import org.blogstagram.models.User;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class testBlog {
    @Spy
    private Blog blog;

    @Test
    public void testId(){
        assertEquals(blog.getId(), -1);
        blog.setId(5);
        assertEquals(blog.getId(), 5);
        blog.setId(7);
        assertEquals(7, blog.getId());
    }

    @Test
    public void testInvalidId(){
        final int id = -3;
        assertThrows(IllegalArgumentException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                blog.setId(id);
            }
        });
    }

    @Test
    public void testTestSet_GetUserId(){
        final int userId = 2;
        blog.setUser_id(userId);
        assertEquals(userId, blog.getUser_id());
        blog.setUser_id(3);
        assertNotEquals(userId, blog.getUser_id());
        assertEquals(3, blog.getUser_id());
    }

    @Test
    public void testSet_GetTitle(){
        String title = "new title";
        blog.setTitle(title);
        assertEquals(title, blog.getTitle());
        title = title + "1";
        blog.setTitle(title);
        assertEquals(title, blog.getTitle());
    }

    @Test
    public void testSet_GetContent(){
        String content = "content";
        blog.setContent(content);
        assertEquals(blog.getContent(), content);
        content += "1";
        blog.setContent(content);
        assertEquals(blog.getContent(), content);
    }

    @Test
    public void testSet_GetCreatedAt(){
        Date createdAt = new Date();
        blog.setCreated_at(createdAt);
        assertEquals(createdAt, blog.getCreated_at());
        Date newDate = new Date();
        blog.setCreated_at(newDate);
        assertEquals(newDate, blog.getCreated_at());
    }

    @Test
    public void testSet_GetModerators(){
        List<User> moderators = new ArrayList<>();
        blog.setBlogModerators(moderators);
        assertEquals(moderators, blog.getBlogModerators());
        moderators.add(new User(1, "firstname", "lastname", "nickname", User.DEFAULT_ROLE, "email@gmail.com", User.MALE, null, null, null,null, null, null, null, null));
        moderators.add(new User(1, "firstname1", "lastname1", "nickname1", User.DEFAULT_ROLE, "email1@gmail.com", User.MALE, null, null, null,null, null, null, null, null));
        moderators.add(new User(1, "firstname2", "lastname2", "nickname2", User.DEFAULT_ROLE, "email2@gmail.com", User.MALE, null, null, null,null, null, null, null, null));
        blog.setBlogModerators(moderators);
        assertEquals(moderators, blog.getBlogModerators());
    }


    @Test
    public void testSet_GetHashTags(){
        List <HashTag> hashTags = new ArrayList<>();
        blog.setHashTagList(hashTags);
        assertEquals(hashTags, blog.getHashTagList());
        hashTags.add(new HashTag("good"));
        hashTags.add(new HashTag("better"));
        hashTags.add(new HashTag("the best"));
        blog.setHashTagList(hashTags);
        assertEquals(hashTags, blog.getHashTagList());
    }

    @Test
    public void testSet_GetComment(){
        List <Comment> comments = new ArrayList<>();
        blog.setComments(comments);
        assertEquals(comments, blog.getComments());
        comments.add(new Comment(1, 1, "comment1", new java.sql.Date(System.currentTimeMillis())));
        comments.add(new Comment(2, 3, "comment1", new java.sql.Date(System.currentTimeMillis())));
        comments.add(new Comment(1, 5, "comment2", new java.sql.Date(System.currentTimeMillis())));
        blog.setComments(comments);
        assertEquals(comments, blog.getComments());
    }

    @Test
    public void testLikes() {
        int numLikes = 10;
        blog.setNumLikes(numLikes);
        assertEquals(numLikes, blog.getNumLikes());
    }

}


