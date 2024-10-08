package com.cooksys.groupfinal.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooksys.groupfinal.entities.Project;
import com.cooksys.groupfinal.entities.Team;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

	Set<Project> getProjectsByTeamCompanyId(Long id);

}