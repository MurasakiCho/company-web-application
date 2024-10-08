package com.cooksys.groupfinal.controllers;

import java.util.Set;

import com.cooksys.groupfinal.dtos.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.groupfinal.dtos.AnnouncementDto;
import com.cooksys.groupfinal.dtos.FullUserDto;
import com.cooksys.groupfinal.dtos.ProjectDto;
import com.cooksys.groupfinal.dtos.TeamDto;
import com.cooksys.groupfinal.services.CompanyService;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyController {

	private final CompanyService companyService;

	@GetMapping("/{id}/users")
	public Set<FullUserDto> getAllUsers(@PathVariable Long id) {
		return companyService.getAllUsers(id);
	}

	@GetMapping("/{id}/announcements")
	public Set<AnnouncementDto> getAllAnnouncements(@PathVariable Long id) {
		return companyService.getAllAnnouncements(id);
	}

	@GetMapping("/{id}/teams")
	public Set<TeamDto> getAllTeams(@PathVariable Long id) {
		return companyService.getAllTeams(id);
	}

	@GetMapping("/{companyId}/teams/{teamId}/projects")
	public Set<ProjectDto> getAllProjects(@PathVariable Long companyId, @PathVariable Long teamId) {
		return companyService.getAllProjects(companyId, teamId);
	}

    @PostMapping("/{id}/users")
    public FullUserDto addUser(@PathVariable Long id, @RequestBody UserRequestDto userRequestDto) {
        return companyService.addUser(id, userRequestDto);
    }
	@PostMapping("/{companyId}/teams/{teamId}/projects")
	public ProjectDto createProject(@PathVariable Long companyId, @PathVariable Long teamId, @RequestBody ProjectDto projectDto) {
		return companyService.createProject(companyId, teamId, projectDto);
	}

	@PostMapping("/{companyId}/announcements")
	public AnnouncementDto createAnnouncement(@PathVariable Long companyId, @RequestBody AnnouncementDto announcementDto){
		return companyService.createAnnouncement(companyId, announcementDto);
	}

	@PostMapping("/{companyId}/teams")
	public TeamDto createTeam(@PathVariable Long companyId, @RequestBody TeamDto teamDto){
		return companyService.createTeam(companyId, teamDto);
	}

}
