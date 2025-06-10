package learn.noodemy.data;

import learn.noodemy.data.mappers.AppUserMapper;
import learn.noodemy.model.AppUser;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class AppUserJdbcTemplateRepository implements AppUserRepository {

    private final JdbcTemplate jdbcTemplate;

    public AppUserJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public AppUser findById(int id) {

        List<String> roles = getRolesById(id);

        final String sql =
                "SELECT app_user_id, username, password_hash, disabled, role_id " +
                "FROM app_user " +
                "WHERE app_user_id = ?;";

        return jdbcTemplate.query(sql, new AppUserMapper(roles), id)
                .stream()
                .findAny().orElse(null);
    }

    @Override
    public AppUser findByUsername(String username) {

        List<String> roles = getRolesByUsername(username);

        final String sql =
                "SELECT au.app_user_id, au.username, au.password_hash, au.disabled, au.role_id " +
                "FROM app_user au " +
                "WHERE au.username = ?;";

        return jdbcTemplate.query(sql, new AppUserMapper(roles), username)
                .stream()
                .findAny().orElse(null);
    }

    @Override
    public AppUser add(AppUser appUser) {

        final String sql =
                "INSERT INTO app_user(username, password_hash, disabled, role_id) " +
                "VALUES (?, ?, ?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, appUser.getUsername());
            ps.setString(2, appUser.getPassword());
            ps.setBoolean(3, appUser.isDisabled());
            ps.setInt(4, appUser.getRoleId());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        appUser.setAppUserId(keyHolder.getKey().intValue());
        return appUser;
    }

    @Override
    public boolean deleteById(int appUserId) {

        if (!isEnabled(appUserId)) return false;

        final String sql =
            "UPDATE app_user " +
            "SET disabled = true " +
            "WHERE app_user_id = ?;";
        return jdbcTemplate.update(sql, appUserId) > 0;
    }

    @Override
    public boolean enableById(int appUserId) {

        if (isEnabled(appUserId)) return false;

        final String sql =
                "UPDATE app_user " +
                "SET disabled = false " +
                "WHERE app_user_id = ?;";

        return jdbcTemplate.update(sql, appUserId) > 0;
    }

    @Override
    public boolean isEnabled(int appUserId) {
        final String sql =
            "SELECT COUNT(*) " +
            "FROM app_user " +
            "WHERE app_user_id = ? AND disabled = false;";

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, appUserId);
        return count != null && count > 0;
    }

    private List<String> getRolesByUsername(String username) {
        final String sql =
                "SELECT ar.role_name " +
                        "FROM app_role ar " +
                        "INNER JOIN app_user au ON au.role_id = ar.role_id " +
                        "WHERE au.username = ?;";
        return jdbcTemplate.query(sql, (rs, rowId) -> rs.getString("role_name"), username);
    }
    private List<String> getRolesById(int id) {
        final String sql =
                "SELECT ar.role_name " +
                "FROM app_role ar " +
                "INNER JOIN app_user au ON au.role_id = ar.role_id " +
                "WHERE au.app_user_id = ?;";
        return jdbcTemplate.query(sql, (rs, rowId) -> rs.getString("role_name"), id);
    }
}
