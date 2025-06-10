package learn.noodemy.data;

import learn.noodemy.data.mappers.AppRoleMapper;
import learn.noodemy.model.AppRole;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class AppRoleJdbcTemplateRepository implements AppRoleRepository {

    private final JdbcTemplate jdbcTemplate;

    public AppRoleJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<AppRole> findAll() {

        final String sql =
            "SELECT ar.role_id, ar.role_name, ar.role_description " +
            "FROM app_role ar " +
            "LIMIT 100";

        return jdbcTemplate.query(sql, new AppRoleMapper());
    }

    @Override
    public AppRole findByRoleName(String roleName) {

        final String sql =
                "SELECT ar.role_id, ar.role_name, ar.role_description " +
                "FROM app_role ar " +
                "WHERE ar.role_name = ?;";

        return jdbcTemplate.query(sql, new AppRoleMapper(), roleName)
                .stream()
                .findAny().orElse(null);
    }

    @Override
    public AppRole add(AppRole appRole) {
        final String sql =
            "INSERT INTO app_role(role_name, role_description) " +
            "VALUES (?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, appRole.getRoleName());
            ps.setString(2, appRole.getRoleDescription());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        appRole.setRoleId(keyHolder.getKey().intValue());
        return appRole;
    }

    @Override
    public boolean deleteById(int roleId) {
        return jdbcTemplate.update("DELETE FROM app_role WHERE role_id = ?", roleId) > 0;
    }
}
