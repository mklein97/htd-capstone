package learn.noodemy.data;

import learn.noodemy.data.mappers.CategoryMapper;
import learn.noodemy.model.Category;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryJdbcTemplateRepository implements CategoryRepository {

    private final JdbcTemplate jdbcTemplate;

    public CategoryJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Category> findAll() {
        final String sql =
                "SELECT category_id, category_name, category_description, category_code " +
                "FROM category " +
                "LIMIT 100;";

        return jdbcTemplate.query(sql, new CategoryMapper());
    }

    @Override
    public Category findById(int id) {
        final String sql =
            "SELECT category_id, category_name, category_description, category_code " +
            "FROM category " +
            "WHERE category_id = ?;";

        return jdbcTemplate.queryForObject(sql, new CategoryMapper(), id);
    }
}
