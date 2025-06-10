package learn.noodemy.data.mappers;

import learn.noodemy.model.Category;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryMapper implements RowMapper<Category> {
    @Override
    public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
        Category category = new Category();

        category.setCategoryId(rs.getInt("category_id"));
        category.setCategoryName(rs.getString("category_name"));
        category.setCategoryDescription(rs.getString("category_description"));
        category.setCategoryCode(rs.getString("category_code"));

        return category;
    }
}
