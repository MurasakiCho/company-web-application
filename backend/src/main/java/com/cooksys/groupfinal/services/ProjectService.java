package com.cooksys.groupfinal.services;

import java.util.Set;

import com.cooksys.groupfinal.dtos.ProjectDto;

public interface ProjectService {

	ProjectDto editProject(Long projectId, ProjectDto projectDto);

	Set<ProjectDto> getProjectsFromCompany(Long companyId);

}
