package learn.noodemy.data;

import learn.noodemy.model.Category;

import java.util.List;

public interface CategoryRepository {

    List<Category> findAll();
    Category findById(int id);
}
