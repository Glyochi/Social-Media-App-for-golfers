package com.example.backend.Repository;

import com.example.backend.Model.GroupModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<GroupModel, Long> {
}
