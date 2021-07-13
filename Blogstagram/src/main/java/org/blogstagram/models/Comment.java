package org.blogstagram.models;

import java.sql.Date;

public class Comment {
    // unique comment id
    private int comment_id;
    // unique user id who posted comment
    private int user_id;
    // unique blog_id
    private int blog_id;
    // comment
    private String comment;
    // comment creation date
    private Date comment_creation_date;

    public Comment(int user_id, int blog_id, String comment, Date comment_creation_date){
        this.comment_id = comment_id;
        this.user_id = user_id;
        this.blog_id = blog_id;
        this.comment = comment;
        this.comment_creation_date = comment_creation_date;
    }

    public int getComment_id(){
        return comment_id;
    }

    public int getUser_id(){
        return user_id;
    }

    public int getBlog_id(){
        return blog_id;
    }

    public Date getCommentDate(){
        return comment_creation_date;
    }

    public String getComment(){
        return comment;
    }

    public void setComment_id(int comment_id){
        this.comment_id = comment_id;
    }

}


