package org.example.service;

import org.example.entities.UserInfo;
import org.example.entities.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails extends UserInfo implements UserDetails {

     String username;
     String password;
     Collection<? extends GrantedAuthority> authorities;

     public CustomUserDetails(UserInfo userInfo) {
         this.username = userInfo.getUsername();
         this.password = userInfo.getPassword();
         List<GrantedAuthority> authoritiesList = new ArrayList<GrantedAuthority>();
         for(UserRole role : userInfo.getRoles()) {
             authoritiesList.add(new SimpleGrantedAuthority(role.getName().toUpperCase()));
         }
         this.authorities = authoritiesList;
     }

    @Override
    public String getUsername() {
        return username;
    }


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
