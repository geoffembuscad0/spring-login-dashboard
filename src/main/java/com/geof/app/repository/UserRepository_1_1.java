package com.geof.app.repository;
import com.geof.app.model.User; import org.springframework.data.jpa.repository.JpaRepository; import java.util.Optional;
public interface UserRepository extends JpaRepository<User,Long>{ Optional<User> findByUsername(String u); Optional<User> findByEmail(String e); boolean existsByUsername(String u); }
