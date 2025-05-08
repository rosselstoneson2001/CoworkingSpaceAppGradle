    package com.example.security.config;


    import com.example.domain.entities.User;
    import com.example.domain.entities.security.PermissionEntity;
    import com.example.domain.entities.security.RoleEntity;
    import com.example.services.UserService;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;
    import org.springframework.stereotype.Service;

    import java.util.HashSet;
    import java.util.Set;

    /**
     * Custom implementation of {@link UserDetailsService} for loading user-specific data.
     * This service retrieves the user entity from the database using the {@link UserService}
     * and maps its roles and permissions to Spring Security's {@link GrantedAuthority}.
     */
    @Service
    public class CustomUserDetailsService implements UserDetailsService {

        private final UserService userService;

        public CustomUserDetailsService(UserService userService) {
            this.userService = userService;
        }

        /**
         * Loads the user by their email address and converts their roles and permissions
         * into Spring Security {@link GrantedAuthority} objects.
         *
         * @param email the email of the user to retrieve
         * @return a {@link UserDetails} instance containing the user's credentials and authorities
         * @throws UsernameNotFoundException if the user is not found
         */
        @Override
        public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
            User user = userService.findByEmail(email);

            Set<GrantedAuthority> authorities = new HashSet<>();

            for (RoleEntity role : user.getRoles()) {
                authorities.add(new SimpleGrantedAuthority(role.getName().name()));

                for (PermissionEntity permission : role.getPermissions()) {
                    authorities.add(new SimpleGrantedAuthority(permission.getName().name()));
                }
            }

            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    authorities
            );
        }
    }
