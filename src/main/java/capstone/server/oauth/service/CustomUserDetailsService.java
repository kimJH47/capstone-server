package capstone.server.oauth.service;


import capstone.server.domain.User;
import capstone.server.oauth.entity.UserPrincipal;
import capstone.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        User user = userRepository.findByUserId(username)
                                  .orElseThrow(() -> new UsernameNotFoundException("Can not find username."));
/*        if (user == null) {
            throw new UsernameNotFoundException("Can not find username.");
        }*/
        return UserPrincipal.create(user);
    }

}
