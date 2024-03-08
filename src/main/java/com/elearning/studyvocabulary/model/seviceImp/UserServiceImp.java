package com.elearning.studyvocabulary.model.seviceImp;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.elearning.studyvocabulary.exceptions.NotFoundException;
import com.elearning.studyvocabulary.model.entity.Course;
import com.elearning.studyvocabulary.model.entity.Feedback;
import com.elearning.studyvocabulary.model.entity.Roles;
import com.elearning.studyvocabulary.model.entity.Test;
import com.elearning.studyvocabulary.model.entity.TestResults;
import com.elearning.studyvocabulary.model.entity.Topic;
import com.elearning.studyvocabulary.model.entity.UserProgression;
import com.elearning.studyvocabulary.model.entity.Users;
import com.elearning.studyvocabulary.model.repository.CourseRepository;
import com.elearning.studyvocabulary.model.repository.FeedbackRepository;
import com.elearning.studyvocabulary.model.repository.RoleRepository;
import com.elearning.studyvocabulary.model.repository.TestRepository;
import com.elearning.studyvocabulary.model.repository.TestResultsRepository;
import com.elearning.studyvocabulary.model.repository.TopicRepository;
import com.elearning.studyvocabulary.model.repository.UserProgressionRepository;
import com.elearning.studyvocabulary.model.repository.UserRepository;
import com.elearning.studyvocabulary.model.service.CourseService;
import com.elearning.studyvocabulary.model.service.UserService;


@Service
public class UserServiceImp implements UserService{
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CourseRepository courseRepository;
	@Autowired
	private FeedbackRepository feedbackRepository;
	@Autowired
	private UserProgressionRepository progressionRepository;
	@Autowired
	private TestResultsRepository testResultsRepository;
	@Override
	public Users findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	@Override
	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	@Override
	public Users save(Users user) {
		return userRepository.save(user);
	}

	@Override
	public List<Users> getAllUser() {
		return userRepository.findAll();
	}

	@Override
	public Page<Users> findUser(Pageable pageable) {
		Page<Users> userPage=userRepository.findAll(pageable);
		return userPage;
	}

	@Override
	public Page<Users> findByUsernameContainingIgnoreCase(String keyword, Pageable pageable) {
		return userRepository.findByUsernameContainingIgnoreCase(keyword, pageable);
	}

	@Override
	public boolean deleteUser(int idUser) {
		Optional<Users> userDB = userRepository.findById(idUser);
		if (userDB.isPresent()) {
			Users user = userDB.get();
			
			for(UserProgression p: user.getListUserProgression()) {
				p.setTopic(null);
				p.setUser(null);
				progressionRepository.save(p);
				progressionRepository.delete(p);
				
			}
			user.setListUserProgression(null);
			for(TestResults t: user.getListTestResults()) {
				t.setTest(null);
				t.setUser(null);
				testResultsRepository.save(t);
				testResultsRepository.delete(t);
			}
			for(Course c: user.getListCourse()) {
				List<Users> list = c.getListUser();
				list.remove(user);
				c.setListUser(list);
				courseRepository.save(c);
			}
			List<Feedback> list = feedbackRepository.findByUser(user);
			for(Feedback f:list) {
				feedbackRepository.delete(f);
			}
			user.setListRoles(null);
			user.setListTestResults(null);
			user.setListBilingualStories(null);
			user.setListCourse(null);
			
			userRepository.save(user);
			System.out.println("aaa");
			userRepository.delete(user);
			return true;
		} else {
			throw new NotFoundException("Not Found User By Id: " + idUser);
		}
	}

	@Override
	public Users findById(int idUser) {
		Users user = userRepository.findById(idUser)
				.orElseThrow(() -> new NotFoundException("Not Found User By Id: " + idUser));
		return user;
	}

	@Override
	public Users update(int id, Users user) {
		Optional<Users> userDB = userRepository.findById(id);
		if (userDB.isPresent()) {
			Users userUpdate = userDB.get();
			userUpdate.setUsername(user.getUsername());
			userUpdate.setEmail(user.getEmail());
			userUpdate.setAvatar(user.getAvatar());
			userUpdate.setLearningMode(user.getLearningMode());
			userUpdate.setReviewAgainMode(user.getReviewAgainMode());
			userUpdate.setUserStatus(user.isUserStatus());
			userUpdate.setFullName(user.getFullName());
			userUpdate.setAddress(user.getAddress());
			Users userUpdated = userRepository.save(userUpdate);
			return userUpdated;
		} else {
			throw new NotFoundException("Not Found User By Id: " + id);
		}
	}

	@Override
	public Users findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public Users setup(int learn, int review) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long countUser() {
		return userRepository.count();
	}

	
	
}
