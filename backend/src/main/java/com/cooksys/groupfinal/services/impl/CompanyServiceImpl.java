package com.cooksys.groupfinal.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.cooksys.groupfinal.dtos.*;
import com.cooksys.groupfinal.exceptions.BadRequestException;
import com.cooksys.groupfinal.repositories.UserRepository;
import com.cooksys.groupfinal.repositories.AnnouncementRepository;
import org.springframework.stereotype.Service;

import com.cooksys.groupfinal.dtos.AnnouncementDto;
import com.cooksys.groupfinal.dtos.FullUserDto;
import com.cooksys.groupfinal.dtos.ProjectDto;
import com.cooksys.groupfinal.dtos.TeamDto;
import com.cooksys.groupfinal.entities.Announcement;
import com.cooksys.groupfinal.entities.Company;
import com.cooksys.groupfinal.entities.Project;
import com.cooksys.groupfinal.entities.Team;
import com.cooksys.groupfinal.entities.User;
import com.cooksys.groupfinal.exceptions.NotFoundException;
import com.cooksys.groupfinal.mappers.AnnouncementMapper;
import com.cooksys.groupfinal.mappers.ProjectMapper;
import com.cooksys.groupfinal.mappers.TeamMapper;
import com.cooksys.groupfinal.mappers.FullUserMapper;
import com.cooksys.groupfinal.repositories.CompanyRepository;
import com.cooksys.groupfinal.repositories.ProjectRepository;
import com.cooksys.groupfinal.repositories.TeamRepository;
import com.cooksys.groupfinal.services.CompanyService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
	
	private final CompanyRepository companyRepository;
	private final TeamRepository teamRepository;
	private final FullUserMapper fullUserMapper;
	private final AnnouncementMapper announcementMapper;
	private final TeamMapper teamMapper;
	private final ProjectMapper projectMapper;
	private final UserRepository userRepository;

	private final ProjectRepository projectRepository;
	private final AnnouncementRepository announcementRepository;
	
	private Company findCompany(Long id) {
        Optional<Company> company = companyRepository.findById(id);
        if (company.isEmpty()) {
            throw new NotFoundException("A company with the provided id does not exist.");
        }
        return company.get();
    }
	
	private Team findTeam(Long id) {
        Optional<Team> team = teamRepository.findById(id);
        if (team.isEmpty()) {
            throw new NotFoundException("A team with the provided id does not exist.");
        }
        return team.get();
    }
	
	
	// Originally, a user will only be displayed if their active status is true
	// I've changed it to display all user regardless of their active status on the front end
	// ADMINS will be able to see active users, but normal WORKERS will not.
	// TODO: figure out how to handle what can be visible depending on if an ADMIN or USER is logged in
	@Override
	public Set<FullUserDto> getAllUsers(Long id) {
		Company company = findCompany(id);
//		Set<User> filteredUsers = new HashSet<>();
//		company.getEmployees().forEach(filteredUsers::add);
//		filteredUsers.removeIf(user -> !user.isActive());
//		return fullUserMapper.entitiesToFullUserDtos(filteredUsers);
		return fullUserMapper.entitiesToFullUserDtos(company.getEmployees());
	}

	@Override
	public Set<AnnouncementDto> getAllAnnouncements(Long id) {
		Company company = findCompany(id);
		List<Announcement> sortedList = new ArrayList<Announcement>(company.getAnnouncements());
		sortedList.sort(Comparator.comparing(Announcement::getDate).reversed());
		Set<Announcement> sortedSet = new HashSet<Announcement>(sortedList);
		return announcementMapper.entitiesToDtos(sortedSet);
	}

	@Override
	public Set<TeamDto> getAllTeams(Long id) {
		Company company = findCompany(id);
		return teamMapper.entitiesToDtos(company.getTeams());
	}
	
	// Originally, a team will only be displayed if their active status is set to true
	// I've changed it to display all projects regardless of their active status on the front end
	// ADMINS will be able to see active projects, but normal WORKERS will not.
	// TODO: figure out how to handle what can be visible depending on if an ADMIN or USER is logged in
	@Override
	public Set<ProjectDto> getAllProjects(Long companyId, Long teamId) {
		Company company = findCompany(companyId);
		Team team = findTeam(teamId);
		if (!company.getTeams().contains(team)) {
			throw new NotFoundException("A team with id " + teamId + " does not exist at company with id " + companyId + ".");
		}
//		Set<Project> filteredProjects = new HashSet<>();
//		team.getProjects().forEach(filteredProjects::add);
//		filteredProjects.removeIf(project -> !project.isActive());
//		return projectMapper.entitiesToDtos(filteredProjects);
		return projectMapper.entitiesToDtos(team.getProjects());
	}

	@Override
	public ProjectDto createProject(Long companyId, Long teamId, ProjectDto project) {
		Project projectToAdd = projectMapper.DtoToEntity(project);
		// im going to assume the default active status of a newly created project is false
		projectToAdd.setActive(false);
		// the team needs to be associated with a company, then needs to be added to a project
		// (in that order)
		Team team = teamRepository.getReferenceById(teamId);
		team.setCompany(companyRepository.getReferenceById(companyId));
		projectToAdd.setTeam(team);
		return projectMapper.entityToDto(projectRepository.saveAndFlush(projectToAdd));
	}

	@Override
	public AnnouncementDto createAnnouncement(Long companyId, AnnouncementDto announcementDto) {
		Announcement announcement = announcementMapper.DtoToEntity(announcementDto);
		announcement.setCompany(companyRepository.getReferenceById(companyId));
		return announcementMapper.entityToDto(announcementRepository.saveAndFlush(announcement));
	}

	@Override
	public TeamDto createTeam(Long companyId, TeamDto teamDto) {
		Team team = teamMapper.DtoToEntity(teamDto);
		team.setCompany(companyRepository.getReferenceById(companyId));
		return teamMapper.entityToDto(teamRepository.saveAndFlush(team));
	}

	public FullUserDto addUser(Long id, UserRequestDto userRequestDto) {
		if(id == null) {
			throw new BadRequestException("Company id can't be null.");
		}
		if(companyRepository.findById(id).isEmpty()) {
			throw new BadRequestException("A company with the provided id does not exist.");
		}
		if(userRequestDto == null){
			throw new BadRequestException("User request can't be null.");
		}
		List<User> users= userRepository.findAll();
		for(User user: users){
			if(user.getProfile().getEmail().equals(userRequestDto.getProfile().getEmail())){
				throw new BadRequestException("User with that email already exists.");
			}
		}
		User user = fullUserMapper.requestDtoToEntity(userRequestDto);
		user.setActive(true);
		user.getCompanies().add(companyRepository.findById(id).get());
		userRepository.saveAndFlush(user);
		Company company = findCompany(id);
		company.getEmployees().add(user);
		companyRepository.saveAndFlush(company);
		return fullUserMapper.entityToFullUserDto(user);
	}

}
