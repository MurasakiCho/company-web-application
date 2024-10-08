package com.cooksys.groupfinal.services;

import java.util.Set;

import com.cooksys.groupfinal.dtos.*;

public interface CompanyService {

	Set<FullUserDto> getAllUsers(Long id);

	Set<AnnouncementDto> getAllAnnouncements(Long id);

	Set<TeamDto> getAllTeams(Long id);

	Set<ProjectDto> getAllProjects(Long companyId, Long teamId);

	FullUserDto addUser(Long id, UserRequestDto userRequestDto);

	ProjectDto createProject(Long companyId, Long teamId, ProjectDto project);

    AnnouncementDto createAnnouncement(Long companyId, AnnouncementDto announcementDto);

	TeamDto createTeam(Long companyId, TeamDto teamDto);
}
