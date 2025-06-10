package learn.noodemy.data;

import learn.noodemy.model.UserProfile;
import learn.noodemy.model.authentication.UserProfileAppUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserProfileRepository {

    List<UserProfile> findAll();
    List<UserProfileAppUser> findAllFullProfileByUserRole();
    UserProfile findByUsername(String username);
    UserProfile findByEmail(String email);
    UserProfile findById(int id);
    UserProfile add(UserProfile userProfile);
    boolean update(UserProfile userProfile);

    @Transactional
    boolean deleteById(int userId);
}
