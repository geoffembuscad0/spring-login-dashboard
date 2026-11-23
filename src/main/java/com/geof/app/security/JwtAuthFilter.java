package com.geof.app.security;
import com.geof.app.repository.UserRepository; import jakarta.servlet.*; import jakarta.servlet.http.*; import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; import org.springframework.security.core.authority.SimpleGrantedAuthority; import org.springframework.security.core.context.SecurityContextHolder; import org.springframework.stereotype.Component; import org.springframework.web.filter.OncePerRequestFilter; import java.io.IOException; import java.util.List;
@Component public class JwtAuthFilter extends OncePerRequestFilter {
  private final JwtService jwt; private final UserRepository users;
  public JwtAuthFilter(JwtService j, UserRepository u){jwt=j; users=u;}
  @Override protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException,IOException {
    String h=req.getHeader("Authorization");
    if(h!=null && h.startsWith("Bearer ")){
      String token=h.substring(7);
      if(jwt.valid(token)){
        String name=jwt.username(token);
        users.findByUsername(name).ifPresent(u->{
          var auth=new UsernamePasswordAuthenticationToken(u.getUsername(),null,List.of(new SimpleGrantedAuthority("ROLE_"+u.getRole())));
          SecurityContextHolder.getContext().setAuthentication(auth);
        });
      }
    }
    chain.doFilter(req,res);
  }
}
