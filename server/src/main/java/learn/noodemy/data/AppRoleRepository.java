package learn.noodemy.data;

import learn.noodemy.model.AppRole;

import java.util.List;

public interface AppRoleRepository {

    List<AppRole> findAll();
    AppRole findByRoleName(String roleName);
    AppRole add(AppRole appRole);
    boolean deleteById(int id);
}
