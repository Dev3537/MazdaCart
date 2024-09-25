package com.ecommerce.mazdacart.security.jwt;

import com.ecommerce.mazdacart.security.servicesofjwt.UserDetailsImpl;
import com.ecommerce.mazdacart.security.servicesofjwt.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthTokenFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	/**
	 * Same contract as for {@code doFilter}, but guaranteed to be
	 * just invoked once per request within a single request thread.
	 * See {@link #shouldNotFilterAsyncDispatch()} for details.
	 * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
	 * default ServletRequest and ServletResponse ones.
	 *
	 * @param request
	 * @param response
	 * @param filterChain
	 */
	@Override
	protected void doFilterInternal (HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {
		log.debug("Inside doFilterInternal() AuthTokenFilter");
		try {
			String jwtToken = jwtUtils.getJwtFromHeader(request);
			if (!jwtToken.isEmpty() && jwtUtils.validateJwt(jwtToken)) {
				String userName = jwtUtils.getUserNameFromJwt(jwtToken);

				UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(userName);

				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
					new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

				usernamePasswordAuthenticationToken.setDetails(
					new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);


			}
		} catch (Exception e) {
			log.error("Exception caught at doFilterInternal() AuthTokenFilter:{}", e.getMessage());
		}

		filterChain.doFilter(request, response);

	}
}
