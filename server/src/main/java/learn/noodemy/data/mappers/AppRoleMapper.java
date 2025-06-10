package learn.noodemy.data.mappers;

import learn.noodemy.model.AppRole;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AppRoleMapper implements RowMapper<AppRole> {
    @Override
    public AppRole mapRow(ResultSet rs, int rowNum) throws SQLException {

        AppRole appRole = new AppRole();

        appRole.setRoleId(rs.getInt("role_id"));
        appRole.setRoleName(rs.getString("role_name"));
        appRole.setRoleDescription(rs.getString("role_description"));

        return appRole;
    }
}
