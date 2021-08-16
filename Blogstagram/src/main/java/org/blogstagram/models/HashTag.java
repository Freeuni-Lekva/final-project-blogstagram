package org.blogstagram.models;

public class HashTag {
    private final String hashTag;
    private Integer blogId;
    private Integer id;
    public static Integer NO_ID = -1;
    public Integer getId() {
        return id;
    }


    public HashTag(String hashTag){
        if(hashTag == null) throw new NullPointerException("Hash tag string can't be null.");
        this.hashTag = hashTag;
        this.id = NO_ID;
    }


    public void setId(Integer id) {
        if(id == null) throw new NullPointerException("id can't be null");
        this.id = id;
    }

    public Integer getBlogId() {
        return blogId;
    }

    public void setBlogId(Integer blogId) {
        if (blogId == null) throw new NullPointerException("Blog id can't be null.");
        this.blogId = blogId;
    }


    public String getHashTag(){
        return hashTag;
    }
}
