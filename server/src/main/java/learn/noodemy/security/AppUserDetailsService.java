package learn.noodemy.security;

import learn.noodemy.data.AppUserRepository;
import learn.noodemy.model.AppUser;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final AppUserRepository repository;

    public AppUserDetailsService(AppUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = repository.findByUsername(username);

        if (appUser == null)
            throw new UsernameNotFoundException(username + " not found");

        if (appUser.isDisabled())
            throw new DisabledException("User is disabled. Please contact admin to resolve this issue.");

        return appUser;
    }
}
