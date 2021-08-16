package org.blogstagram.tests.blogTests;

import org.blogstagram.models.HashTag;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ejb.Init;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class testHashtag {

    private HashTag hashTag;

    private final String content = "hashtag";

    @Before
    public void init(){
        hashTag = new HashTag(content);
    }

    @Test
    public void testId(){
        assertEquals(HashTag.NO_ID, hashTag.getId());
        assertEquals(content, hashTag.getHashTag());
        final Integer newId = 4;
        hashTag.setId(newId);
        assertEquals(newId, hashTag.getId());
    }


    @Test
    public void testBlogId(){
        final Integer blogId = 2;
        hashTag.setBlogId(blogId);
        assertEquals(blogId, hashTag.getBlogId());
    }

}
