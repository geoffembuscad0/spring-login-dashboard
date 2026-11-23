package com.geof.app.controller;
import org.springframework.security.core.Authentication; import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController @RequestMapping("/api/dashboard")
public class DashboardController {
  // Empty dashboard data - plug your real widgets here
  @GetMapping("/summary") public Map<String,Object> summary(Authentication auth){
    return Map.of("user", auth.getName(), "widgets", java.util.List.of(
      Map.of("title","Total Users","value",0,"note","Replace with UserRepository.count()"),
      Map.of("title","Active Sessions","value",0,"note","Replace with your metric"),
      Map.of("title","Revenue","value","₱0","note","Replace with your metric")
    ));
  }
}
