//package com.group3.xecare2.security;
//
//import java.io.IOException;
//import java.util.List;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import com.group3.xecare2.security.jwt.JwtUtils;
//
//import io.jsonwebtoken.Claims;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//@Component
//@Order(1)
//public class AuthTokenFilter extends OncePerRequestFilter {
//
//	@Autowired
//	private JwtUtils jwtUtils;
//
//	@Autowired
//	private AppUserDetailsService userDetailsService;
//
//	private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);
//
//	@Override
//	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//		String path = request.getServletPath();
//		System.out.println("------------shouldNotFilter------------" + path);
//		return !path.startsWith("/apis/") || path.startsWith("/apis/v1/login");
//	}
//
//	@Override
//	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//			throws ServletException, IOException {
//
//		System.out.println("doFilterInternal::");
//
//		try {
//			String jwt = parseJwt(request);
//			if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
//				String username = jwtUtils.getUserNameFromJwtToken(jwt);
//				Claims claims = jwtUtils.getClaimsFromJwtToken(jwt);
//				//String roles = (String) claims.get("roles");
//				List<String> roles = claims.get("roles", List.class);
//
//				AppUserDetails userDetails = (AppUserDetails) userDetailsService.loadUserByUsername(username);
//				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//						userDetails, null, userDetails.getAuthorities());
//				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//				System.out.println("subject: " + username);
//				System.out.println("claims roles: " + roles);
//				System.out.println("userDetails roles: " + userDetails.roles());
//
//				SecurityContextHolder.getContext().setAuthentication(authentication);
//			}
//		} catch (Exception e) {
//			logger.error("Cannot set user authentication: {}", e);
//		}
//
//		filterChain.doFilter(request, response);
//	}
//
//	private String parseJwt(HttpServletRequest request) {
//		String headerAuth = request.getHeader("Authorization");
//
//		System.out.println("headerAuth: " + headerAuth);
//
//		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
//			return headerAuth.substring(7);
//		}
//
//		return null;
//	}
//}
