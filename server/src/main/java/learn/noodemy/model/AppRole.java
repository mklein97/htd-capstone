package learn.noodemy.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AppRole {

    private int roleId;
    private String roleName;
    private String roleDescription;
    private List<AppUser> users = new ArrayList<>();

    public AppRole() {}
    public AppRole(int roleId, String roleName, String roleDescription) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.roleDescription = roleDescription;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }

    public List<AppUser> getUsers() {
        return new ArrayList<>(users);
    }

    public void setUsers(List<AppUser> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppRole appRole = (AppRole) o;
        return roleId == appRole.roleId && Objects.equals(roleName, appRole.roleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId, roleName);
    }
}
