package com.ecommerce.mazdacart.security.servicesofjwt;

import com.ecommerce.mazdacart.model.Users;
import com.ecommerce.mazdacart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	/**
	 * Locates the user based on the username. In the actual implementation, the search
	 * may possibly be case sensitive, or case insensitive depending on how the
	 * implementation instance is configured. In this case, the <code>UserDetails</code>
	 * object that comes back may have a username that is of a different case than what
	 * was actually requested..
	 *
	 * @param username the username identifying the user whose data is required.
	 * @return a fully populated user record (never <code>null</code>)
	 * @throws UsernameNotFoundException if the user could not be found or the user has no
	 * GrantedAuthority
	 */
	@Override
	@Transactional
	public UserDetailsImpl loadUserByUsername (String username) throws UsernameNotFoundException {
		Users user = userRepository.findByUserNameIgnoreCase(username).orElseThrow(
			() -> new UsernameNotFoundException("User for the given username does not exist:" + username));


		return UserDetailsImpl.build(user);
	}
}
