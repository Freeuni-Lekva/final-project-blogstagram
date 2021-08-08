package org.blogstagram.postLike.postLikeConstants;

public class PostLikeQueries {
    public static final String LIKE_POST = "INSERT INTO likes(user_id, blog_id, created_at) values(?, ?, ?)";
    public static final String UNLIKE_POST = "DELETE FROM likes WHERE post_id = ? AND user_id = ?";
    public static final String POST_LIKES = "SELECT u.firstname, u.lastname, u.nickname, u.image FROM users u " +
            "INNER join likes l on (l.user_id = u.id) " +
            "WHERE l.blog_id = ?";
}
