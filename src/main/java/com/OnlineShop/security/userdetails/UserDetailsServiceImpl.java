package com.OnlineShop.security.userdetails;

import com.OnlineShop.entity.AppUser;
import com.OnlineShop.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
    private final IUserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(IUserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        Optional<AppUser> result = userRepository.findByUsername(username);

        if (result.isEmpty())
            throw new UsernameNotFoundException("User with username: [" + username +"] cannot be found.");

        AppUser user = result.get();

        return UserDetailsImpl.buildUserDetails(user);
    }
}
