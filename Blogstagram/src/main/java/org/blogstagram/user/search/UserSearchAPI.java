package org.blogstagram.user.search;

import com.google.gson.Gson;
import org.blogstagram.models.SearchedUser;
import org.blogstagram.models.User;
import org.blogstagram.validators.IllegalCharactersValidator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserSearchAPI {
    private Connection connection;

    public static final String SEARCH_QUERY = " SELECT id,firstname,lastname,nickname,image,users.role FROM users WHERE users.role != \'" + User.ADMIN_ROLE + "\' AND( \n" +
                        "firstname LIKE ? OR lastname LIKE ? \n" +
                        "OR nickname LIKE ? OR CONCAT(firstname,' ',lastname) LIKE ?);";

    public static final String ALL_QUERY = "SELECT id,firstname,lastname,nickname,image,users.role FROM users WHERE users.role != "+User.ADMIN_ROLE+";";

    public UserSearchAPI(Connection connection){
        this.connection = connection;
    }

    public List<SearchedUser> searchUsers(String query) throws SQLException {
        List<SearchedUser> users = new ArrayList<>();
        PreparedStatement stm;
        if(query == null || query.length() == 0 || query.equals("*"))
            stm = connection.prepareStatement(ALL_QUERY);
        else{
            query = "%"+query+"%";
            stm = connection.prepareStatement(SEARCH_QUERY);
            stm.setString(1,query);
            stm.setString(2,query);
            stm.setString(3,query);
            stm.setString(4,query);

        }

        ResultSet result = stm.executeQuery();
        while(result.next()){
            Integer id = result.getInt(1);
            String firstname = result.getString(2);
            String lastname = result.getString(3);
            String nickname = result.getString(4);
            String image = result.getString(5);
            String role = result.getString(6);

            SearchedUser user = new SearchedUser(id,firstname,lastname,nickname,role,image);
            users.add(user);
        }

        return users;
    }
}
