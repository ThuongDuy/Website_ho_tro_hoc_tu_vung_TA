package com.elearning.studyvocabulary.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.elearning.studyvocabulary.model.entity.Comments;
import com.elearning.studyvocabulary.model.entity.Roles;
import com.elearning.studyvocabulary.model.entity.TestResults;
import com.elearning.studyvocabulary.model.entity.UserProgression;
import com.elearning.studyvocabulary.model.entity.Users;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
//@AllArgsConstructor //tao constructor voi tat ca tham so
public class CustomUserDetails implements UserDetails{
	
	 private Users user;
    
    public CustomUserDetails(Users user) {
        this.user = user;
    }
 
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Roles> roles = user.getListRoles();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
         
        for (Roles role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName().name().toString()));
        }
         
        return authorities;
    }
 
    @Override
    public String getPassword() {
        return user.getPassword();
    }
 
    @Override
    public String getUsername() {
        return user.getUsername();
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
