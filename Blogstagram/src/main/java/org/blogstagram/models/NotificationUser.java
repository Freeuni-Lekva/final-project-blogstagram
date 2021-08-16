package org.blogstagram.models;

public class NotificationUser extends Notification{
    private String firstName;
    private String lastName;
    private String image;



    public NotificationUser(String firstName, String lastName, String image, Notification notification) {
        super(notification);
        this.firstName = firstName;
        this.lastName = lastName;
        this.image = image;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getImage() {
        return image;
    }

}
