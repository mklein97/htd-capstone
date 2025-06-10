package learn.noodemy.data.mappers;

import learn.noodemy.model.UserProfile;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserProfileMapper implements RowMapper<UserProfile> {
    @Override
    public UserProfile mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserProfile profile = new UserProfile();

        profile.setUserId(rs.getInt("user_id"));
        profile.setFirstName(rs.getString("first_name"));
        profile.setLastName(rs.getString("last_name"));
        profile.setEmail(rs.getString("email"));
        if (rs.getDate("dob") != null) {
            profile.setDob(rs.getDate("dob").toLocalDate());
        }
        profile.setAppUserId(rs.getInt("app_user_id"));

        return profile;
    }
}
