package org.blogstagram.blogLike.blogLikeConstants;

public class BlogLikeQueries {
    public static final String LIKE_BLOG = "INSERT INTO likes(user_id, blog_id, created_at) values(?, ?, ?)";
    public static final String UNLIKE_BLOG = "DELETE FROM likes WHERE blog_id = ? AND user_id = ?";
    public static final String BLOG_LIKES = "SELECT u.firstname, u.lastname, u.nickname, u.image FROM users u " +
            "INNER join likes l on (l.user_id = u.id) " +
            "WHERE l.blog_id = ?";
}
