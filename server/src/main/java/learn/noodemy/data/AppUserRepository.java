package learn.noodemy.data;

import learn.noodemy.model.AppUser;

public interface AppUserRepository {

    AppUser findById(int id);
    AppUser findByUsername(String username);
    AppUser add(AppUser appUser);
    boolean deleteById(int id);
    boolean enableById(int id);
    boolean isEnabled(int id);
}
