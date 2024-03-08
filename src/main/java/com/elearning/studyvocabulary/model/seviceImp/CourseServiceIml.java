package com.elearning.studyvocabulary.model.seviceImp;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.elearning.studyvocabulary.exceptions.NotFoundException;
import com.elearning.studyvocabulary.model.entity.Course;
import com.elearning.studyvocabulary.model.entity.Test;
import com.elearning.studyvocabulary.model.entity.Topic;
import com.elearning.studyvocabulary.model.entity.Users;
import com.elearning.studyvocabulary.model.repository.CourseRepository;
import com.elearning.studyvocabulary.model.repository.TopicRepository;
import com.elearning.studyvocabulary.model.repository.UserRepository;
import com.elearning.studyvocabulary.model.service.CourseService;
import com.elearning.studyvocabulary.model.service.TestService;
import com.elearning.studyvocabulary.model.service.TopicService;

@Service
public class CourseServiceIml implements CourseService{
	@Autowired
	private CourseRepository courseRepository;
	@Autowired
	private TopicService topicService;
	@Autowired
	private TestService testService;
	@Autowired
	private UserRepository userRepository;
	@Override
	public boolean deleteCourse(int id) {
		Optional<Course> courseDB = courseRepository.findById(id);
		if (courseDB.isPresent()) {
			Course course = courseDB.get();
			for(Test t: course.getListTest()) {
				testService.deleteTest(t.getTestId());
			}
			for(Topic t: course.getListTopic()) {
				topicService.deleteTopic(t.getTopicId());
			}
			List<Users> ds = course.getListUser();
			course.setListUser(null);
			
			for(Users u:ds) {
				List<Course> list=u.getListCourse();
				list.remove(course);
				u.setListCourse(list);
		        userRepository.save(u);
			}
			courseRepository.save(course);
			courseRepository.delete(course);
			return true;
		} else {
			throw new NotFoundException("Not Found Course By Id: " + id);
		}
	}
	
    public void deleteUserCourseRelationship(Users user, Course course) {
        user.getListCourse().remove(course);
        userRepository.save(user);
        courseRepository.save(course);
        
    }

	@Override
	public Page<Course> findCourse(Pageable pageable) {
		Page<Course> pageCourse = courseRepository.findAll(pageable);
		return pageCourse;
	}

	@Override
	public Course save(Course course) {
		Course courseCreated = courseRepository.save(course);
		return courseCreated;
	}



	@Override
	public Course getCourseById(int courseId) {
		Course course = courseRepository.findById(courseId)
				.orElseThrow(() -> new NotFoundException("Not Found Course By Id: " + courseId));
		return course;
	}

	@Override
	public Course updateCourse(int courseId, Course course) {
		Optional<Course> courseDB = courseRepository.findById(courseId);
		if (courseDB.isPresent()) {
			Course courseUpdate = courseDB.get();
			courseUpdate.setCourseName(course.getCourseName());
			courseUpdate.setIntroduce(course.getIntroduce());
			courseUpdate.setIllustration(course.getIllustration());
			Course courseUpdated = courseRepository.save(courseUpdate);
			return courseUpdated;
		} else {
			throw new NotFoundException("Not Found Course By Id: " + courseId);
		}
	}


	@Override
	public Page<Course> findByCourseNameContainingIgnoreCase(String keyword, Pageable pageable) {
		return courseRepository.findByCourseNameContainingIgnoreCase(keyword, pageable);
	}

	@Override
	public List<Course> getAll() {
		
		return courseRepository.findAll();
	}

	@Override
	public long countCourse() {
		return courseRepository.count();
	}

}
