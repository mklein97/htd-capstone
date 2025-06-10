package learn.noodemy.data.mappers;

import learn.noodemy.model.authentication.UserProfileAppUser;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserProfileAppUserMapper implements RowMapper<UserProfileAppUser> {
    @Override
    public UserProfileAppUser mapRow(ResultSet rs, int rowNum) throws SQLException {

        UserProfileAppUser user = new UserProfileAppUser();

        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setEmail(rs.getString("email"));
        if (rs.getDate("dob") != null) {
            user.setDob(rs.getDate("dob").toLocalDate());
        }
        user.setAppUserId(rs.getInt("app_user_id"));
        user.setDisabled(rs.getBoolean("disabled"));

        return user;
    }
}
