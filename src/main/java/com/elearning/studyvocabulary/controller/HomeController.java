package com.elearning.studyvocabulary.controller;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.elearning.studyvocabulary.common.FileUploadUtil;
import com.elearning.studyvocabulary.common.ReadWordFile;
import com.elearning.studyvocabulary.model.entity.AnhViet;
import com.elearning.studyvocabulary.model.entity.BilingualStory;
import com.elearning.studyvocabulary.model.entity.Course;
import com.elearning.studyvocabulary.model.entity.Feedback;
import com.elearning.studyvocabulary.model.entity.Question;
import com.elearning.studyvocabulary.model.entity.Roles;
import com.elearning.studyvocabulary.model.entity.Test;
import com.elearning.studyvocabulary.model.entity.TestResults;
import com.elearning.studyvocabulary.model.entity.Topic;
import com.elearning.studyvocabulary.model.entity.UserProgression;
import com.elearning.studyvocabulary.model.entity.Users;
import com.elearning.studyvocabulary.model.entity.Vocabulary;
import com.elearning.studyvocabulary.model.service.AnhVietService;
import com.elearning.studyvocabulary.model.service.BilingualStoryService;
import com.elearning.studyvocabulary.model.service.CourseService;
import com.elearning.studyvocabulary.model.service.FeedbackService;
import com.elearning.studyvocabulary.model.service.TestResultsService;
import com.elearning.studyvocabulary.model.service.TestService;
import com.elearning.studyvocabulary.model.service.TopicService;
import com.elearning.studyvocabulary.model.service.UserProgressionService;
import com.elearning.studyvocabulary.model.service.UserService;
import com.elearning.studyvocabulary.security.CustomUserDetails;
import com.fasterxml.jackson.core.JsonProcessingException;



//@CrossOrigin(origins = "*")
@Controller
@RequestMapping("/app")
public class HomeController {
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserProgressionService progressionService;
	
	@Autowired
	private TopicService topicService;
	
	@Autowired
	private TestService testService;
	
	@Autowired
	private AnhVietService anhVietService;
	
	@Autowired
	private TestResultsService resultsService;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private FeedbackService feedbackService;
	
	@Autowired
	private BilingualStoryService storyService;
	
	@GetMapping("/")
	public String home(Model model) {
		try {
			CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Users user = userService.findByUsername(customUserDetails.getUsername());
			List<Roles> listA = new ArrayList<Roles>();
			listA.addAll(user.getListRoles());
			for(Roles i:listA) {
				if(i.getRoleName().name().toString().equals("ADMIN")) {
					return "redirect:/manage/";
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		List<Course> listCourse = courseService.getAll();
		Map<Course, Integer> unsortedMap = new HashMap<>();
		for(Course i : listCourse) {
			unsortedMap.put(i, i.getListUser().size());
			
		} 
		
		Map<Course, Integer> sortedMap = unsortedMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        int size = listCourse.size();
        if(sortedMap.size()>3) {
        	size = 3;
        }
        List<Course> list = new ArrayList<Course>();
        List<Integer> sum = new ArrayList<Integer>();
        int count=0;
        for (Map.Entry<Course, Integer> entry : sortedMap.entrySet()) {
        	list.add(entry.getKey());
        	sum.add(entry.getValue());
            count++;
            if (count == size) break;
        }
        model.addAttribute("list", list);
		model.addAttribute("sum", sum);
		model.addAttribute("sumUser", userService.countUser());
		model.addAttribute("sumCourse", courseService.countCourse());
		return "user/index";
	}
	
	@GetMapping("/courses")
	@PreAuthorize("hasAuthority('USER')")  
	public String getCourse(Model model, @RequestParam("page") Optional<Integer> page) {
		try {
			CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Users user = userService.findByUsername(customUserDetails.getUsername());
			List<Course> courses = user.getListCourse();
			int currentPage = page.orElse(1);
			int pageSize = 4;
			Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
			final int start = (int)pageable.getOffset();
			final int end = Math.min((start + pageable.getPageSize()), courses.size());
			final Page<Course> coursePage = new PageImpl<>(courses.subList(start, end), pageable, courses.size());
			int totalPages = coursePage.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);
			}
			model.addAttribute("currentPage", coursePage.getNumber() + 1);
			model.addAttribute("totalItems", coursePage.getTotalElements());
			model.addAttribute("totalPages", coursePage.getTotalPages());
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("coursePage", coursePage.getContent());
		} catch (Exception e) {
			System.out.println("aaaa");
			model.addAttribute("message", e.getMessage());
		}
		return "user/courses";
	}
	private List<Course> findCourse(List<Course> listCourse, String keyword){
		List<Course> result = new ArrayList<Course>();
		for(Course course:listCourse) {
			if(course.getCourseName().toLowerCase().contains(keyword)) {
				result.add(course);
			}
		}
		return result;
	}
	@GetMapping("/courses/all")
	public String getAllCourse(Model model, @RequestParam("page") Optional<Integer> page,
			@RequestParam(required = false) String keyword) {
		try {
			int currentPage = page.orElse(1);
			int pageSize = 6;
			Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
			List<Course> listCourse = courseService.getAll();
			try {
				CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				Users user = userService.findByUsername(customUserDetails.getUsername());
				for(Course course:user.getListCourse()) {
					listCourse.remove(course);
				}
			}catch (Exception e) {
				// TODO: handle exception
			}
			if (keyword != null) {
				listCourse = findCourse(listCourse, keyword);
				model.addAttribute("keyword", keyword);
			}
			
			final int start = (int)pageable.getOffset();
			final int end = Math.min((start + pageable.getPageSize()), listCourse.size());
			final Page<Course> coursePage = new PageImpl<>(listCourse.subList(start, end), pageable, listCourse.size());
			int totalPages = coursePage.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);
			}
			model.addAttribute("currentPage", coursePage.getNumber() + 1);
			model.addAttribute("totalItems", coursePage.getTotalElements());
			model.addAttribute("totalPages", coursePage.getTotalPages());
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("coursePage", coursePage);
		} catch (Exception e) {
			model.addAttribute("message", e.getMessage());
		}
		return "user/list-course";
	}
	@GetMapping("/course/{courseId}/join")
	public String joinCourse(@PathVariable("courseId") Integer courseId) {
		Course course = courseService.getCourseById(courseId);
		try {
			CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Users user = userService.findByUsername(customUserDetails.getUsername());
			List<Course> courses = user.getListCourse();
			courses.add(course);
			List<UserProgression> list = user.getListUserProgression();
			for(Topic i : course.getListTopic()) {
				UserProgression progression = new UserProgression();
				progression.setProgress(0);
				progression.setUser(user);
				progression.setTopic(i);
				list.add(progression);
			}
			user.setListCourse(courses);
			user.setListUserProgression(list);
			userService.save(user);
		}catch (Exception e) {
			return "redirect:/app/login";
		}
		return "redirect:/app/courses";
	}
	
	@GetMapping("/courses/{courseId}")
	@PreAuthorize("hasAuthority('USER')") 
	public String renderCourse(Model model, @PathVariable("courseId") Integer courseId) {
		Course course = courseService.getCourseById(courseId);
		CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Users user = userService.findByUsername(customUserDetails.getUsername());
		List<UserProgression> listProgression = new ArrayList<>();
		int tongTu=0;
		int daHoc = 0;
		for (Topic t: course.getListTopic()) {
			UserProgression progress = progressionService.findByTopicAndUser(t, user);
			tongTu+=t.getListVocab().size();
			daHoc+=progress.getProgress();
			listProgression.add(progressionService.findByTopicAndUser(t, user));
		}
		model.addAttribute("listTopic", course.getListTopic());
		model.addAttribute("progression", listProgression);
		model.addAttribute("course", course);
		model.addAttribute("tongTu", tongTu);
		model.addAttribute("daHoc", daHoc);
		float a=daHoc;a=a/tongTu;
		model.addAttribute("my_width", Math.round(a*100));
		return "user/course-detail";
	}
	
	@GetMapping("/course/{courseId}/topic/{topicId}")
	@PreAuthorize("hasAuthority('USER')") 
	public String renderTopic(Model model, @PathVariable("courseId") Integer courseId,
			@PathVariable("topicId") Integer topicId) {
		Topic topic = topicService.getTopicByTopicId(topicId);
		Course course = courseService.getCourseById(courseId);
		CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Users user = userService.findByUsername(customUserDetails.getUsername());
		UserProgression progress = progressionService.findByTopicAndUser(topic, user);
		model.addAttribute("listVocab", topic.getListVocab());
		model.addAttribute("progression", progress);
		model.addAttribute("course", course);
		model.addAttribute("topic", topic);
		float a=progress.getProgress() ;a=a/topic.getListVocab().size();
		model.addAttribute("my_width", Math.round(a*100));
		return "user/list-vocab";
	}
	
	@GetMapping("/learn")
	@PreAuthorize("hasAuthority('USER')") 
	public String learn(Model model, @RequestParam("courseId") Integer courseId,
			@RequestParam("topicId") Integer topicId) throws JsonProcessingException {
		Topic topic = topicService.getTopicByTopicId(topicId);
		Course course = courseService.getCourseById(courseId);
		CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Users user = userService.findByUsername(customUserDetails.getUsername());
		UserProgression progress = progressionService.findByTopicAndUser(topic, user);
		List<Vocabulary> list = new ArrayList<Vocabulary>();
		if(user.getLearningMode()<=topic.getListVocab().size()-progress.getProgress()) {
			list = topic.getListVocab().subList(progress.getProgress(), progress.getProgress()+user.getLearningMode());
		}else {
			list = topic.getListVocab().subList(progress.getProgress(), topic.getListVocab().size());
		}
		int index =0;
		model.addAttribute("listVocab", list);
		model.addAttribute("index", index);
		model.addAttribute("course", course);
		model.addAttribute("topic", topic);
		return "user/learn";
	}
	@PostMapping("/endLearn")
	@PreAuthorize("hasAuthority('USER')") 
	public String endLearn(@RequestParam("courseId") int courseId,
			@RequestParam("topicId") int topicId,
	@RequestParam Map<String, String> params) {
		CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Users user = userService.findByUsername(customUserDetails.getUsername());
		Topic topic = topicService.getTopicByTopicId(topicId);
		UserProgression progress = progressionService.findByTopicAndUser(topic, user);
		int tt = Integer.parseInt(params.get("progress"));
		progress.setProgress(progress.getProgress()+tt);
		progressionService.saveUserProgression(progress);
		return "redirect:/app/courses/"+courseId;
	}
	
	@GetMapping("/review-all")
	@PreAuthorize("hasAuthority('USER')") 
	public String reviewAll(Model model, @RequestParam("courseId") Integer courseId) throws JsonProcessingException {
		Course course = courseService.getCourseById(courseId);
		int index =0;
		model.addAttribute("index", index);
		model.addAttribute("course", course);
		return "user/review";
	}
	@GetMapping("/course/{courseId}/topic/{topicId}/review")
	@PreAuthorize("hasAuthority('USER')") 
	public String reviewAll(Model model, @PathVariable("courseId") Integer courseId,
			@PathVariable("topicId") Integer topicId) throws JsonProcessingException {
		Course course = courseService.getCourseById(courseId);
		Topic topic =topicService.getTopicByTopicId(topicId);
		
		int index =0;
		model.addAttribute("index", index);
		model.addAttribute("course", course);
		model.addAttribute("topic", topic);
		return "user/review-topic";
	}
	
	@GetMapping("/course/{courseId}/allTest")
	@PreAuthorize("hasAuthority('USER')") 
	public String testOfCourse(Model model, @PathVariable("courseId") Integer courseId, 
			@RequestParam("level") Optional<String> level) {
		String cap_do = level.orElse("Tất cả");
		
		Course course = courseService.getCourseById(courseId);
		List<Test> test = new ArrayList<Test>();
		if(cap_do.equals("Tất cả")) {
			test=course.getListTest();
		}else {
			for(Test t: course.getListTest()) {
				if(t.getLevel().equals(cap_do)) {
					test.add(t);
				}
			}
		}
		
		List<Users> listUsers = course.getListUser();
		Map<Users, Integer> unsortedMap = new HashMap<>();
		for(Users i : listUsers) {
			int diem = 0;
			
			for (Test t:course.getListTest()) {
				try {
					TestResults results = resultsService.findByTestAndUser(t, i);
						diem+= results.getScores();

				}catch (Exception e) {
					// TODO: handle exception
				}
			}

			unsortedMap.put(i, diem);

		} 

		Map<Users, Integer> sortedMap = unsortedMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        int size = listUsers.size();
        if(sortedMap.size()>10) {
        	size = 10;
        }
        List<Users> list = new ArrayList<Users>();
        List<Integer> scores = new ArrayList<Integer>();
        int count=0;
        for (Map.Entry<Users, Integer> entry : sortedMap.entrySet()) {
        	list.add(entry.getKey());
        	scores.add(entry.getValue());
            
            count++;
            if (count == size) break;
        }
		model.addAttribute("listTest", test);
		model.addAttribute("list", list);
		model.addAttribute("scores", scores);
		model.addAttribute("course", course);
		model.addAttribute("level", cap_do);
		return "user/list-exam";
	}
	@GetMapping("/course/{courseId}/doTest/{testId}")
	@PreAuthorize("hasAuthority('USER')") 
	public String questionOfTest(Model model, @PathVariable("courseId") Integer courseId,
			@PathVariable("testId") Integer testId) {
		Course course = courseService.getCourseById(courseId);
		Test test = testService.getTestByTestId(testId);
		model.addAttribute("test", test);
		model.addAttribute("course", course);
		int time =  test.getListQuestion().size()*45;
		int h_val = time/3600;
		int m_val = (time%3600)/60;
		model.addAttribute("h_val", h_val);
		model.addAttribute("m_val", m_val);
		model.addAttribute("s_val", time-h_val*60*60-m_val*60);
		model.addAttribute("size", test.getListQuestion().size());
		return "user/exam";
	}
	@PostMapping("/course/{courseId}/doTest/{testId}/submit")
	@PreAuthorize("hasAuthority('USER')") 
	public String submitOfTest(Model model, @PathVariable("courseId") Integer courseId,
			@PathVariable("testId") Integer testId,
			@RequestParam Map<String, String> params) {
		CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Users user = userService.findByUsername(customUserDetails.getUsername());
		Course course = courseService.getCourseById(courseId);
		Test test = testService.getTestByTestId(testId);
		model.addAttribute("test", test);
		model.addAttribute("course", course);
		List<String> answers = new ArrayList<String>();
		int correct = 0;
		int notDone=0;
		List<String> correctAnswers=new ArrayList<String>();
		for (Question i:test.getListQuestion()) {
	        String answer = params.get("option-" + i.getQuestionNumber());
	        if(answer==null) {
	        	notDone+=1;
	        }else if(answer.equals(i.getCorrectAnswer())) {
	        	correct+=1;
	        }
	        answers.add(answer);
	        switch(i.getCorrectAnswer()) {
		        case "A":
		        	correctAnswers.add(i.getOption_1());
		        	break;
		        case "B":
		        	correctAnswers.add(i.getOption_2());
		        	break;
		        case "C":
		        	correctAnswers.add(i.getOption_3());
		        	break;
		        case "D":
		        	correctAnswers.add(i.getOption_4());
		        	break;
	      }
	    }
		int total = test.getListQuestion().size();
		model.addAttribute("total", total);
		double c = correct;
		long scores =  Math.round((c/total)*100);
		TestResults results ;
		try {
			results=resultsService.findByTestAndUser(test, user);
			results.setScores(scores);
		} catch (Exception e) {
			results = new TestResults();
			results.setTest(test);
			results.setUser(user);
			results.setScores(scores);
		}
		
		List<TestResults> listResults = user.getListTestResults();
		listResults.add(results);
		user.setListTestResults(listResults);
		userService.save(user);
		model.addAttribute("wrong", total-correct-notDone);
		model.addAttribute("correct", correct);
		model.addAttribute("scores", scores);
		model.addAttribute("notDone", notDone);
		model.addAttribute("answers", answers);
		model.addAttribute("username", user.getUsername());
		model.addAttribute("correctAnswers", correctAnswers);
		int h = Integer.parseInt(params.get("hour"));
		int m = Integer.parseInt(params.get("minute"));
		int s = Integer.parseInt(params.get("second"));
		int timeLeft = h*60*60+m*60+s;
		int time =  test.getListQuestion().size()*45;
		int sum = time-timeLeft;
		int h_val = sum/3600;
		int m_val = (sum%3600)/60;
		int s_val = sum-h_val*60*60-m_val*60;
		model.addAttribute("h", h_val);
		model.addAttribute("m", m_val);
		model.addAttribute("s", s_val);
		return "user/result_exam";
	}
	@GetMapping("/dictionary")
	public String testt(Model model,@RequestParam(required = false) String keyword,
			@RequestParam(required = false) Optional<Integer> id) {
		Pageable paging = PageRequest.of(0, 20);
        Page<AnhViet> dictPage;
        List<AnhViet> list = new ArrayList<AnhViet>();
        if (keyword == null||keyword.isEmpty()) {
        	dictPage = anhVietService.findWord(paging);
        	list = dictPage.getContent();
          } else {
        	  String key = keyword;
        	  while(list.size()<20) {
        		  dictPage = anhVietService.findByWord(key, paging);
            	  list.addAll(dictPage.getContent());
            	  key = key.substring(0, key.length()-1);
            	  if(key.isEmpty()) {
            		  break;
            	  }
        	  }
        	  if(list.size()>20) {
        		  list = list.subList(0, 20);
        	  }
        	  model.addAttribute("keyword", keyword);
          }
        if(id.isEmpty()) {
        	model.addAttribute("dictionary", "");
        }else {
        	int a=id.get();
        	AnhViet dictionary = anhVietService.getById(a);
        	model.addAttribute("dictionary", dictionary);
        }
        model.addAttribute("dictionarys", list);
        
		return "user/dictionary";

	}
	@GetMapping("/user/profile")
	@PreAuthorize("hasAuthority('USER')") 
	public String getprofile(Model model) throws JsonProcessingException {
		CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Users user = userService.findByUsername(customUserDetails.getUsername());
		int sumVocab=0;
		if(user.getListUserProgression()!=null) {
			for (UserProgression i:user.getListUserProgression()) {
				sumVocab+=i.getProgress();
			}
		}
		int sumTest=user.getListTestResults().size();;
		System.out.println("aaa"+sumTest);

		model.addAttribute("user", user);
		model.addAttribute("courses", user.getListCourse().size());
		model.addAttribute("sumVocab", sumVocab);
		model.addAttribute("sumTest", sumTest);
		float diem =0;
		for(TestResults t: user.getListTestResults()) {
			diem+=t.getScores();
		}
		if(sumTest>0) {
			model.addAttribute("diem", Math.round(diem/sumTest));
		}else {
			model.addAttribute("diem", 0);
		}
		return "user/profile";
	}
	@GetMapping("/user/edit-profile")
	@PreAuthorize("hasAuthority('USER')") 
	public String editprofile(Model model) throws JsonProcessingException {
		CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Users user = userService.findByUsername(customUserDetails.getUsername());
		int sumVocab=0;
		for (UserProgression i:user.getListUserProgression()) {
			sumVocab+=i.getProgress();
		}
		int sumTest=user.getListTestResults().size();
		System.out.println("aaa"+sumTest);
		float diem =0;
		for(TestResults t: user.getListTestResults()) {
			diem+=t.getScores();
		}
		model.addAttribute("user", user);
		model.addAttribute("courses", user.getListCourse().size());
		model.addAttribute("sumVocab", sumVocab);
		model.addAttribute("sumTest", sumTest);

		model.addAttribute("diem", diem);
		
		return "user/edit-profile";
	}
	@PostMapping("/user/edit-profile")
	@PreAuthorize("hasAuthority('USER')") 
	public String saveprofile(Model model, Users user,BindingResult bindingResult,
			@RequestParam("file") MultipartFile multipartFile) throws IOException {
		CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Users users = userService.findByUsername(customUserDetails.getUsername());
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			if (fileName.trim() != null) {
				users.setAvatar(fileName);
			}
			String upload = "images/" + user.getUsername();
			FileUploadUtil.saveFile(upload, fileName, multipartFile);
		} 
		if(user.getPassword()!=null && !user.getPassword().trim().isEmpty()) {
			String pass = user.getPassword();
			users.setPassword(encoder.encode(pass));
		}
		users.setFullName(user.getFullName());
		users.setEmail(user.getEmail());
		String bd = users.getUsername();
		users.setUsername(user.getUsername());
		users.setAddress(user.getAddress());
		if (userService.existsByUsername(user.getUsername())) {
			bindingResult.rejectValue("username", "user.username","Tên đăng nhập đã được sử dụng");
			return "admin/pages/samples/register";
		}
		if (userService.existsByEmail(user.getEmail())) {
			bindingResult.rejectValue("email", "user.email","Email đã được sử dụng");
			return "admin/pages/samples/register";
		}
		Users saveUser=userService.save(users);
		if(!bd.equals(user.getUsername())) {
			System.out.println(saveUser.getUsername());
			System.out.println(saveUser.getUsername());
			return "redirect:/logout";
		}else {
			int sumVocab=0;
			for (UserProgression i:saveUser.getListUserProgression()) {
				sumVocab+=i.getProgress();
			}
			int sumTest=saveUser.getListTestResults().size();
			model.addAttribute("user", saveUser);
			model.addAttribute("courses", saveUser.getListCourse().size());
			model.addAttribute("sumVocab", sumVocab);
			model.addAttribute("sumTest", sumTest);
			return "redirect:/app/user/profile";
		}
		
	}
	@PostMapping("/user/setup")
	@PreAuthorize("hasAuthority('USER')") 
	public String setup(Model model, Users user) {
		CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Users users = userService.findByUsername(customUserDetails.getUsername());
		users.setLearningMode(user.getLearningMode());
		users.setReviewAgainMode(user.getReviewAgainMode());
		Users saveUser=userService.save(users);

		int sumVocab=0;
		for (UserProgression i:saveUser.getListUserProgression()) {
			sumVocab+=i.getProgress();
		}
		int sumTest=saveUser.getListTestResults().size();
		model.addAttribute("user", saveUser);
		model.addAttribute("courses", saveUser.getListCourse().size());
		model.addAttribute("sumVocab", sumVocab);
		model.addAttribute("sumTest", sumTest);
		return "redirect:/app/user/profile";
	}
	@GetMapping("/testtt")
	public String test() {
		String a = "hello";
		String b = encoder.encode(a);
		System.out.println(a);
		System.out.println(b);
		return "user/contact";
	}
	@GetMapping("/courses/{courseId}/start-again")
	@PreAuthorize("hasAuthority('USER')")
	public String startAgain(Model model,@PathVariable("courseId") Integer courseId) {
		CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Users user = userService.findByUsername(customUserDetails.getUsername());
		Course course = courseService.getCourseById(courseId);
		for(int i =0;i<course.getListTopic().size();i++) {
			UserProgression progress = progressionService.findByTopicAndUser(course.getListTopic().get(i), user);
			int tt = 0;
			progress.setProgress(tt);
			progressionService.saveUserProgression(progress);
			
		}
		return "redirect:/app/courses/"+courseId;
	}
	@GetMapping("/course/{courseId}/topic/{topicId}/start-again")
	@PreAuthorize("hasAuthority('USER')")
	public String startAgainTopic(Model model,@PathVariable("courseId") Integer courseId,
			@PathVariable("topicId") Integer topicId) {
		CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Users user = userService.findByUsername(customUserDetails.getUsername());
		Topic topic = topicService.getTopicByTopicId(topicId);
		UserProgression progress = progressionService.findByTopicAndUser(topic, user);
		progress.setProgress(0);
		progressionService.saveUserProgression(progress);
		return "redirect:/app/courses/"+courseId;
	}
	@GetMapping("/courses/{courseId}/unregister")
	@PreAuthorize("hasAuthority('USER')")
	public String unregister(Model model,@PathVariable("courseId") Integer courseId) {
		CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Users user = userService.findByUsername(customUserDetails.getUsername());
		Course course = courseService.getCourseById(courseId);
		List<Course> list = user.getListCourse();
		for(int i =0;i<course.getListTopic().size();i++) {
			UserProgression progress = progressionService.findByTopicAndUser(course.getListTopic().get(i), user);
			progressionService.delete(progress);
			
		}
		list.remove(course);
		user.setListCourse(list);
		userService.save(user);
		return "redirect:/app/courses";
	}
	
	@GetMapping("/user/feedback")
	public String getForm(Model model) {
		try {
			CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Users user = userService.findByUsername(customUserDetails.getUsername());
			Feedback feedback=new Feedback();
			feedback.setUser(user);
			model.addAttribute("feedback", feedback);
			return "user/feedback_form";
		}catch (Exception e) {
			return "redirect:/app/login";
		}
		
	}
	@PostMapping("/user/feedback/send")
	@PreAuthorize("hasAuthority('USER')")
	public String sendForm(Model model,Feedback feedback,@RequestParam Map<String, String> params) {
		CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Users user = userService.findByUsername(customUserDetails.getUsername());
		feedback.setUser(user);
		feedback.setErrorLocation(params.get("ans1"));
		feedback.setErrorType(params.get("ans"));
		feedbackService.save(feedback);
		return "redirect:/app/";
	}
	@GetMapping("/bilingual-story")
	public String getStory(Model model,@RequestParam("page") Optional<Integer> page,
			@RequestParam(required = false) String keyword) {
		try {
			
			int currentPage = page.orElse(1);
			int pageSize = 6;
			Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
			Page<BilingualStory> storyPage;
			if (keyword == null) {
				storyPage = storyService.findBilingualStory(pageable);
			} else {
				storyPage = storyService.findByCategoryContainingIgnoreCase(keyword, pageable);
				model.addAttribute("keyword", keyword);
			}
			int totalPages = storyPage.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);
			}
			
			model.addAttribute("currentPage", storyPage.getNumber() + 1);
			model.addAttribute("totalItems", storyPage.getTotalElements());
			model.addAttribute("totalPages", storyPage.getTotalPages());
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("storyPage", storyPage.getContent());
		} catch (Exception e) {
			System.out.println("aaaa");
			model.addAttribute("message", e.getMessage());
		}
		return "/user/list-story";
	}
	@GetMapping("/bilingual-story/{storyId}")
	public String getStoryById(Model model,@PathVariable("storyId") Integer storyId) {
		try {
			BilingualStory story = storyService.getById(storyId);
			model.addAttribute("story", story);
			List<String> content = ReadWordFile.read(story.getContent());
			model.addAttribute("content", content);
		} catch (Exception e) {
			System.out.println("aaaa");
			model.addAttribute("message", e.getMessage());
		}
		return "/user/story-detail";
	}
}
