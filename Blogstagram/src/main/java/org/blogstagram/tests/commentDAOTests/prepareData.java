package org.blogstagram.tests.commentDAOTests;

import org.blogstagram.StringHasher;
import org.blogstagram.dao.CommentDAO;
import org.blogstagram.dao.UserDAO;
import org.blogstagram.models.Comment;
import org.blogstagram.models.User;

import java.sql.*;
import java.util.Random;

public class prepareData {
    private Connection connection;
    private UserDAO userDAO;
    private Random random;
    private CommentDAO commentDAO;
    private static final String EMPTY_LIKES = "DELETE FROM likes";
    private static final String EMPTY_COMMENTS = "DELETE FROM comments";
    private static final String EMPTY_USERS = "DELETE FROM users";
    private static final String EMPTY_BLOGS = "DELETE FROM blogs";
    private static final String ADD_BLOG = "INSERT INTO blogs(user_id ,title, content, created_at) values(?,?,?,?)";


    public User[] users;

    public prepareData(Connection connection){
        random = new Random();
        this.connection = connection;
        userDAO = new UserDAO(connection);
        commentDAO = new CommentDAO(connection);
    }

    public User[] createUsers(int numUsers) throws SQLException {
        User [] usersRes = new User[numUsers];
        for(int i = 0; i < numUsers; i++){
            usersRes[i] = createDummyUser(i);
        }
        this.users = usersRes;
        return usersRes;
    }

    public int[] createBlogs() throws SQLException {
        int [] res = new int[2*users.length];
        for(int i = 0; i < users.length; i++){
            res[2*i] = createDummyBlog(users[i].getId());
            res[2*i+1] = createDummyBlog(users[i].getId());
        }
        return res;
    }

    private User createDummyUser(int identificator) throws SQLException {
        String firstname = "firstname" + String.valueOf(identificator);
        String lastname = "lastname" + String.valueOf(identificator);
        String nickname = "nickname" + String.valueOf(identificator);
        String email = "email" + String.valueOf(identificator)+"@gmail.com";
        Date birthday = Date.valueOf("2001-08-07");
        Integer gender = random.nextInt(2);
        Integer privacy = random.nextInt(2);
        String image = "image" + String.valueOf(identificator);
        String country = "Georgia";
        String city = "Tbilisi";
        String website = "website" + String.valueOf(identificator);
        String bio ="Xelou Gaiz " + String.valueOf(identificator);
        Date createdAt = new Date(System.currentTimeMillis());

        User user = new User(null,firstname,lastname,nickname,User.DEFAULT_ROLE,email,gender,privacy,birthday,image,country,city,website,bio,createdAt);
        userDAO.addUser(user, StringHasher.hashString("Password12345"));
        return user;
    }

    private int createDummyBlog(int user_id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(ADD_BLOG, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1,user_id);
        ps.setString(2, "dummy_title");
        ps.setString(3, "dummy_content");
        ps.setDate(4, new Date(System.currentTimeMillis()));
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        ResultSet generatedKeys = ps.getGeneratedKeys();
        if(generatedKeys.next()){
            return generatedKeys.getInt(1);
        }
        return -1;
    }

    public int addDummyComment(int userId, int blog_id) throws SQLException {
        Comment comment = new Comment(userId, blog_id, "dummyCom", new Date(System.currentTimeMillis()) );
        comment = commentDAO.addComment(comment);
        return comment.getComment_id();
    }

    public void cleanUpData() throws SQLException {
        Statement stm = connection.createStatement();
        stm.execute(EMPTY_USERS);
        Statement stm1 = connection.createStatement();
        stm1.execute(EMPTY_BLOGS);
        Statement stm2 = connection.createStatement();
        stm2.execute(EMPTY_COMMENTS);
        Statement stm3 = connection.createStatement();
        stm3.execute(EMPTY_LIKES);
    }

}
