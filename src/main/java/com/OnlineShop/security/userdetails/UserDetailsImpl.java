package com.OnlineShop.security.userdetails;

import com.OnlineShop.entity.AppUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails
{
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String username;
    @JsonIgnore
    private String password;
    private Collection<GrantedAuthority> authorities;

    public UserDetailsImpl(String id, String firstName, String lastName, String email, String phoneNumber, String username, String password, Collection<GrantedAuthority> authorities)
    {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public String getId()
    {
        return id;
    }

    public String getEmail()
    {
        return email;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    @Override
    public String getPassword()
    {
        return password;
    }

    @Override
    public String getUsername()
    {
        return username;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        return true;
    }

    /**
     * A method to convert the authorities ({@link GrantedAuthority})
     * to a list of string
     *
     * @return the authenticated user roles as a list as string
     */
    public List<String> getStringListRoles()
    {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

    }

    /**
     * A method to construct UserDetailsImpl class
     *
     * @param user {@link AppUser} entity
     * @return userDetailsImpl the implementation of {@link UserDetails}
     */
    public static UserDetailsImpl buildUserDetails(AppUser user)
    {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof UserDetailsImpl)) return false;

        UserDetailsImpl that = (UserDetailsImpl) o;

        if (!id.equals(that.id)) return false;
        if (!username.equals(that.username)) return false;
        if (!email.equals(that.email)) return false;
        if (!password.equals(that.password)) return false;
        return authorities.equals(that.authorities);
    }

    @Override
    public int hashCode()
    {
        int result = id.hashCode();
        result = 31 * result + username.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + authorities.hashCode();
        return result;
    }
}
