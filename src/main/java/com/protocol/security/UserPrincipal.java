package com.protocol.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.protocol.model.Role;
import com.protocol.model.User;

public class UserPrincipal extends User implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User user;
	private Role roles;

	public UserPrincipal(User user, Role roles) {
		this.user = user;
		this.roles = roles;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		List<GrantedAuthority> authorities = new ArrayList<>();
		GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + roles.getName());
		authorities.add(authority);
		return authorities;

	}

	@Override
	public String getPassword() {

		return this.user.getPassword();
	}

	@Override
	public String getUsername() {

		return this.user.getUsername();
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
		if (user.getActive() == 0) {
			return false;
		}
		return true;

	}

}
