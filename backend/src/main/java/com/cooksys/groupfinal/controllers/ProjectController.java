package com.cooksys.groupfinal.controllers;

import java.util.Set;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.groupfinal.dtos.ProjectDto;
import com.cooksys.groupfinal.services.ProjectService;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {
	
	private final ProjectService projectService;

	@PatchMapping("/{projectId}")
	public ProjectDto editProject(@PathVariable Long projectId, @RequestBody ProjectDto projectDto) {
		return projectService.editProject(projectId, projectDto);
	}
	
	@GetMapping("/company/{companyId}")
	public Set<ProjectDto> getProjectsFromCompany(@PathVariable Long companyId){
		return projectService.getProjectsFromCompany(companyId);
	}

}
