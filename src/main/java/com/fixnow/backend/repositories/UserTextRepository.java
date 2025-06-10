package com.fixnow.backend.repositories;

import com.fixnow.backend.models.UserText;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTextRepository extends JpaRepository<UserText,Long> {
}
