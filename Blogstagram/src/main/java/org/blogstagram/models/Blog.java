package org.blogstagram.models;


import java.util.Date;
import java.util.List;

public class Blog {
    private int id;
    private int user_id;
    private String title;
    private String content;
    private Date createdAt;
    public static final int NO_BLOG_ID = -1;
    private List <User> blogModerators;

    public Blog(){
        id = NO_BLOG_ID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if(title == null)
            throw new NullPointerException("Blog title can't be null.");
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        if(content == null)
            throw new NullPointerException("Blog content can't be null");
        this.content = content;
    }

    public Date getCreated_at() {
        return createdAt;
    }

    public void setCreated_at(Date createdAt) {
        if(createdAt == null)
            throw new NullPointerException("Created at object can't be null.");
        this.createdAt = createdAt;
    }

    public List <User> getBlogModerators(){
        return blogModerators;
    }

    public void setBlogModerators(List <User> blogModerators){
        if (blogModerators == null)
            throw new NullPointerException("Moderators list can't be null");
        this.blogModerators = blogModerators;
    }


}
