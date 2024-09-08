package com.ecommerce.mazdacart.security.servicesofjwt;

import com.ecommerce.mazdacart.model.Users;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

	@Getter
	@Setter
	private Long userId;

	private String userName;

	@Getter
	@Setter
	private String emailId;

	@JsonIgnore
	private String password;

	@Getter
	@Setter
	private Collection<? extends GrantedAuthority> authorities;


	public static UserDetailsImpl build (Users user) {
		List<GrantedAuthority> authorities =
			user.getRoles().stream().map(roles -> new SimpleGrantedAuthority(roles.getRoleName().name()))
				.collect(Collectors.toList());

		return new UserDetailsImpl(user.getUserId(), user.getUserName(), user.getEmailId(), user.getPassword(),
			authorities);
	}

	/**
	 * Returns the authorities granted to the user. Cannot return <code>null</code>.
	 *
	 * @return the authorities, sorted by natural key (never <code>null</code>)
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities () {
		return this.authorities;
	}

	/**
	 * Returns the password used to authenticate the user.
	 *
	 * @return the password
	 */
	@Override
	public String getPassword () {
		return this.password;
	}

	/**
	 * Returns the username used to authenticate the user. Cannot return
	 * <code>null</code>.
	 *
	 * @return the username (never <code>null</code>)
	 */
	@Override
	public String getUsername () {
		return this.userName;
	}

	/**
	 * Indicates whether the user's account has expired. An expired account cannot be
	 * authenticated.
	 *
	 * @return <code>true</code> if the user's account is valid (ie non-expired),
	 * <code>false</code> if no longer valid (ie expired)
	 */
	@Override
	public boolean isAccountNonExpired () {
		return UserDetails.super.isAccountNonExpired();
	}

	/**
	 * Indicates whether the user is locked or unlocked. A locked user cannot be
	 * authenticated.
	 *
	 * @return <code>true</code> if the user is not locked, <code>false</code> otherwise
	 */
	@Override
	public boolean isAccountNonLocked () {
		return UserDetails.super.isAccountNonLocked();
	}

	/**
	 * Indicates whether the user's credentials (password) has expired. Expired
	 * credentials prevent authentication.
	 *
	 * @return <code>true</code> if the user's credentials are valid (ie non-expired),
	 * <code>false</code> if no longer valid (ie expired)
	 */
	@Override
	public boolean isCredentialsNonExpired () {
		return UserDetails.super.isCredentialsNonExpired();
	}

	/**
	 * Indicates whether the user is enabled or disabled. A disabled user cannot be
	 * authenticated.
	 *
	 * @return <code>true</code> if the user is enabled, <code>false</code> otherwise
	 */
	@Override
	public boolean isEnabled () {
		return UserDetails.super.isEnabled();
	}

}
