package com.example.springboot.Repositories;


import com.example.springboot.Models.GroupModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<GroupModel, Long> {
    public GroupModel findGroupModelByName(String groupName);
}
