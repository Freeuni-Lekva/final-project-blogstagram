package org.blogstagram.models;

public class UserProvidedBlog extends Blog{

    private String userFirstname;
    private String userLastname;
    private String userImage;

    public UserProvidedBlog(String userFirstname,String userLastname,String userImage,Blog blog){
        super(blog);
        this.userFirstname = userFirstname;
        this.userLastname = userLastname;
        this.userImage = userImage;
    }


    public String getUserFirstname() {
        return userFirstname;
    }

    public void setUserFirstname(String userFirstname) {
        this.userFirstname = userFirstname;
    }

    public String getUserLastname() {
        return userLastname;
    }

    public void setUserLastname(String userLastname) {
        this.userLastname = userLastname;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}
