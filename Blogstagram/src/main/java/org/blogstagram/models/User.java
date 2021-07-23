package org.blogstagram.models;

import java.util.Date;

public class User {
    private Integer id;
    private String firstname;
    private String lastname;
    private String nickname;
    private String role;
    private String email;
    private Integer gender;
    private Integer privacy;
    private Date birthday;
    private String image;
    private String country;
    private String city;
    private String website;
    private String bio;
    private Date createdAt;

    public static final String DEFAULT_ROLE = "User";
    public static final String MODERATOR_ROLE = "Moderator";
    public static final String ADMIN_ROLE = "Admin";
    public static final String DEFAULT_USER_IMAGE_PATH = "/images/defaultUserImage.jpg";


    public static final Integer MALE = 0;
    public static final Integer FEMALE = 1;
    public static final Integer PUBLIC = 0;
    public static final Integer PRIVATE = 1;


    public User(Integer id,String firstname, String lastname, String nickname, String role, String email,
                Integer gender, Integer privacy, Date birthday, String image, String country,
                String city, String website, String bio, Date createdAt) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.nickname = nickname;
        this.role = role;
        this.email = email;
        this.gender = gender;
        this.privacy = privacy;
        this.birthday = birthday;
        this.image = image;
        this.country = country;
        this.city = city;
        this.website = website;
        this.bio = bio;
        this.createdAt = createdAt;
    }

    public void setId(Integer id){
        this.id = id;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public void setRole(String role) {
        if(!role.equals(DEFAULT_ROLE) && !role.equals(MODERATOR_ROLE) && !role.equals(ADMIN_ROLE))
            throw new IllegalArgumentException("Role must be specific, Default, Moderator or Admin");
        this.role = role;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setGender(Integer gender) {
        if(!gender.equals(MALE) && !gender.equals(FEMALE))
            throw new IllegalArgumentException("Gender must be specific, Male or Female");
        this.gender = gender;
    }
    public void setPrivacy(Integer privacy) {
        if(!privacy.equals(PUBLIC) && !privacy.equals(PRIVATE))
            throw new IllegalArgumentException("Privacy must be specific, Public or Private");
        this.privacy = privacy;
    }
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public void setWebsite(String website) {
        this.website = website;
    }
    public void setBio(String bio) {
        this.bio = bio;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    public Integer getId(){
        return id;
    }
    public String getFirstname() {
        return firstname;
    }
    public String getLastname() {
        return lastname;
    }
    public String getNickname() {
        return nickname;
    }
    public String getRole() {
        return role;
    }
    public String getEmail() {
        return email;
    }
    public Integer getGender() {
        return gender;
    }
    public Integer getPrivacy() {
        return privacy;
    }
    public Date getBirthday() {
        return birthday;
    }
    public String getImage() {
        return image;
    }
    public String getCountry() {
        return country;
    }
    public String getCity() {
        return city;
    }
    public String getWebsite() {
        return website;
    }
    public String getBio() {
        return bio;
    }
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(id + "\n");
        stringBuilder.append(firstname + "\n");
        stringBuilder.append(lastname + "\n");
        stringBuilder.append(nickname + "\n");
        stringBuilder.append(role + "\n");
        stringBuilder.append(String.valueOf(gender) + "\n");
        stringBuilder.append(String.valueOf(privacy) + "\n");
        stringBuilder.append(birthday.toString() + "\n");
        stringBuilder.append(image + "\n");
        stringBuilder.append(country + "\n");
        stringBuilder.append(city + "\n");
        stringBuilder.append(website + "\n");
        stringBuilder.append(bio + "\n");
        stringBuilder.append(createdAt.toString() + "\n");

        return stringBuilder.toString();
    }

}