package org.blogstagram.dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class CommentDAO{

    private static final String SEARCH_COMMENTS_FOR_BLOG = "SELECT * FROM  comments WHERE blog_id = ?";
    private static final String ADD_NEW_COMMENT = "INSERT INTO comments(id, user_id, blog_id, comment, created_at) VALUES (?,?,?,?,?)";
    private static final String DELETE_COMMENT = "DELETE FROM comments WHERE id = ?";
    private static final String EDIT_COMMENT = "UPDATE comments SET comment = ? WHERE id = ?";
    private static final String SEARCH_USER_LIKES = "SELECT u.firstname, u.lastname, u.nickname, u.image FROM users u " +
            "INNER join likes l on (l.user_id = u.id) " +
            "WHERE l.comment_id = ?";

    private static final String ADD_LIKE = "INSERT INTO likes(user_id, comment_id, created_at) values(?, ?, ?)";
    Connection connection;

    public CommentDAO(Connection connection){
        this.connection = connection;
    }

    // receives blog id in parameters
    // return list of comment object of the blog
    public List<Comment> getComments(int blogId) throws SQLException {
        // result list to keep all comments of blog
        List<Comment> blogComments = new ArrayList<>();
        // string keeps query to execute
        PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_COMMENTS_FOR_BLOG);
        preparedStatement.setInt(1, blogId);
        // result set keeps all rows fetched by statement
        ResultSet resultSet = preparedStatement.executeQuery();
        // convert all rows into comment objects and add into a list
        while(resultSet.next()){
            int comment_id = resultSet.getInt(1);
            int user_id = resultSet.getInt(2);
            int blog_id = resultSet.getInt(3);
            String comment = resultSet.getString(4);
            Date comment_creation_date = resultSet.getDate(5);
            blogComments.add(new Comment(comment_id, user_id, blog_id, comment, comment_creation_date));
        }
        return blogComments;
    }

    // receives comment id and new comment string in parameters
    // edits comment with comment id and sets updates its comment
    public void editComment(int comment_id, String editedComment) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(EDIT_COMMENT);
        preparedStatement.setString(1, editedComment);
        preparedStatement.setInt(2, comment_id);
        int affectedRows = preparedStatement.executeUpdate();
        if(affectedRows == 0) {
            throw new SQLException("Editing comment failed");
        }
    }

    // receives comment object in parameters
    // adds comment to database
    public void addComment(Comment newComment) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(ADD_NEW_COMMENT);
        preparedStatement.setInt(1, newComment.getComment_id());
        preparedStatement.setInt(2, newComment.getUser_id());
        preparedStatement.setInt(3, newComment.getBlog_id());
        preparedStatement.setString(4, newComment.getComment());
        preparedStatement.setDate(5, newComment.getCommentDate());

        int affectedRows = preparedStatement.executeUpdate();
        if(affectedRows == 0) {
            throw new SQLException("Adding comment failed");
        }
    }

    // receives comment id in parameters
    // deletes comment with comment id
    public void deleteComment(int comment_id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(DELETE_COMMENT);
        preparedStatement.setInt(1, comment_id);

        int affectedRows = preparedStatement.executeUpdate();
        if(affectedRows == 0) {
            throw new SQLException("Deleting comment failed");
        }
    }

    // receives comment_id int parameter
    // return list of user objects who liked the comment
    public List<User> getCommentLikeUsers(int comment_id) throws SQLException {
        // result list to keep users
        List<User> resultList = new ArrayList<>();
        // sql statement
        PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_USER_LIKES);
        preparedStatement.setInt(1, comment_id);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            String firstName = resultSet.getString(1);
            String lastName = resultSet.getString(2);
            String nickname = resultSet.getString(3);
            String image = resultSet.getString(4);

            // the rest of the fields that are not needed to display comment like users are default
            int userId = -1;
            String email = null;
            String role = null;
            String password = null;
            Date birthDate = null;
            int gender = -1;
            int privacy = -1;
            String country = null;
            String city = null;
            String website = null;
            String bio = null;
            Date createdAt = null;

            User currentUser = new User(userId, firstName, lastName, nickname, role, email,
                    gender, privacy, birthDate, image, country, city, website,  bio, createdAt);

            resultList.add(currentUser);
        }

        return resultList;
    }

    // receives comment id in parameter
    // returns number of users who liked the comment
    public int getNumberOfLikes(int comment_id) throws SQLException {
        return getCommentLikeUsers(comment_id).size();
    }

    public void likeComment(int comment_id, int user_id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(ADD_LIKE, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, user_id);
        ps.setInt(2, comment_id);
        ps.setDate(3, new Date(System.currentTimeMillis()));
        ps.executeUpdate();

    }

    public void unlikeComment(int comment_id, int user_id) throws SQLException {
        String deleteComment = "DELETE FROM likes WHERE comment_id = ? AND user_id = ?";
        PreparedStatement ps = connection.prepareStatement(deleteComment);
        ps.setInt(1, comment_id);
        ps.setInt(2, user_id);
        ps.executeUpdate();
    }

}