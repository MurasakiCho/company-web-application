package com.cooksys.groupfinal.services.impl;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.cooksys.groupfinal.dtos.ProjectDto;
import com.cooksys.groupfinal.entities.Project;
import com.cooksys.groupfinal.entities.Team;
import com.cooksys.groupfinal.mappers.ProjectMapper;
import com.cooksys.groupfinal.repositories.ProjectRepository;
import com.cooksys.groupfinal.services.ProjectService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

	private final ProjectRepository projectRepository;
	private final ProjectMapper projectMapper;

	@Override
	public ProjectDto editProject(Long projectId, ProjectDto projectDto) {
		Project projectToEdit = projectRepository.getReferenceById(projectId);
		Project projectChanges = projectMapper.DtoToEntity(projectDto);
		projectToEdit.setName(projectChanges.getName());
		projectToEdit.setDescription(projectChanges.getDescription());
		projectToEdit.setActive(projectChanges.isActive());
		return projectMapper.entityToDto(projectRepository.saveAndFlush(projectToEdit));
	}

	@Override
	public Set<ProjectDto> getProjectsFromCompany(Long companyId) {
		Set<Project> teams = projectRepository.getProjectsByTeamCompanyId(companyId);
		
		return projectMapper.entitiesToDtos(teams);
	}

}
