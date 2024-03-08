package com.elearning.studyvocabulary.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.elearning.studyvocabulary.common.FileUploadUtil;
import com.elearning.studyvocabulary.common.ReadExcel;
import com.elearning.studyvocabulary.model.entity.Course;
import com.elearning.studyvocabulary.model.entity.Question;
import com.elearning.studyvocabulary.model.entity.Test;
import com.elearning.studyvocabulary.model.entity.Topic;
import com.elearning.studyvocabulary.model.entity.UserProgression;
import com.elearning.studyvocabulary.model.entity.Users;
import com.elearning.studyvocabulary.model.entity.Vocabulary;
import com.elearning.studyvocabulary.model.service.CourseService;
import com.elearning.studyvocabulary.model.service.QuestionService;
import com.elearning.studyvocabulary.model.service.TestService;
import com.elearning.studyvocabulary.model.service.TopicService;
import com.elearning.studyvocabulary.model.service.UserProgressionService;
import com.elearning.studyvocabulary.model.service.UserService;
import com.elearning.studyvocabulary.model.service.VocabularyService;

@Controller
@RequestMapping("/manage")
public class CourseController {
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private TopicService topicService;
	
	@Autowired
	private VocabularyService vocabularyService;
	
	@Autowired
	private TestService testService;
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private UserProgressionService progressionService;
	
	@Autowired
	private UserService userService;

	@GetMapping("/courses")
	public String manageCourse(Model model, @RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size, @RequestParam(required = false) String keyword) {
		try {
			int currentPage = page.orElse(1);
			int pageSize = size.orElse(5);
			Pageable paging = PageRequest.of(currentPage - 1, pageSize);
			Page<Course> coursePage;
			if (keyword == null) {
				coursePage = courseService.findCourse(paging);
			} else {
				coursePage = courseService.findByCourseNameContainingIgnoreCase(keyword, paging);
				model.addAttribute("keyword", keyword);
			}
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
			model.addAttribute("pageTitle", "Danh sách khóa học");
			model.addAttribute("sumUser", userService.countUser());
		} catch (Exception e) {
			model.addAttribute("message", e.getMessage());
		}
		return "admin/pages/tables/list_course";
	}


	@GetMapping("/courses/list-topic/courseId={courseId}")
	public String listTopic(Model model, @PathVariable("courseId") Integer courseId,
			@RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size,
			@RequestParam(required = false) String keyword) {
		try {
			int currentPage = page.orElse(1);
			int pageSize = size.orElse(5);
			Order order = new Order(Sort.Direction.ASC,"level");
			Pageable paging = PageRequest.of(currentPage - 1, pageSize, Sort.by(order));
			Course course = courseService.getCourseById(courseId);
			Page<Topic> topicPage;
			if (keyword == null) {
				topicPage = topicService.findByCourse(course, paging);
			} else {
				topicPage = topicService.findByCourseAndTopicNameContainingIgnoreCase(course, keyword, paging);
				model.addAttribute("keyword", keyword);
			}
			int totalPages = topicPage.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);
			}
			model.addAttribute("currentPage", topicPage.getNumber() + 1);
			model.addAttribute("totalItems", topicPage.getTotalElements());
			model.addAttribute("totalPages", topicPage.getTotalPages());
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("topicPage", topicPage.getContent());
			model.addAttribute("courseId", courseId);
			model.addAttribute("pageTitle", "Danh sách chủ đề khóa học :" + course.getCourseName());
			return "admin/pages/tables/topic-table";
		} catch (Exception e) {
			model.addAttribute("message", e.getMessage());
			return "redirect:/manage/courses";
		}
	}
	@GetMapping("/courses/topic/list-vocabulary/courseId={courseId}/topicId={topicId}")
	public String listVocab(Model model, @PathVariable("courseId") Integer courseId,
			@PathVariable("topicId") Integer topicId,
			@RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size,
			@RequestParam(required = false) String keyword) {
		try {
			int currentPage = page.orElse(1);
			int pageSize = size.orElse(5);
			Order order = new Order(Sort.Direction.ASC,"vocab");
			Pageable paging = PageRequest.of(currentPage - 1, pageSize, Sort.by(order));
			Topic topic = topicService.getTopicByTopicId(topicId);
			Page<Vocabulary> vocabPage;
			if (keyword == null) {
				vocabPage = vocabularyService.findByTopic(topic, paging);
			} else {
				vocabPage = vocabularyService.findByTopicAndVocabContainingIgnoreCase(topic, keyword, paging);
				model.addAttribute("keyword", keyword);
			}
			int totalPages = vocabPage.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);
			}
			model.addAttribute("currentPage", vocabPage.getNumber() + 1);
			model.addAttribute("totalItems", vocabPage.getTotalElements());
			model.addAttribute("totalPages", vocabPage.getTotalPages());
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("vocabPage", vocabPage.getContent());
			model.addAttribute("courseId", courseId);
			model.addAttribute("topicId", topicId);
			model.addAttribute("path", "/manage/courses/topic/new-vocab?courseId="+courseId+"&"+"topicId="+topicId);
			model.addAttribute("pageTitle", "Danh sách từ vựng chủ đề " + topic.getTopicName());
			return "admin/pages/tables/vocab-table";
		} catch (Exception e) {
			model.addAttribute("message", e.getMessage());
			return "redirect:/manage/courses";
		}
	}
	private List<Users> findUser(List<Users> listUser, String keyword){
		List<Users> result = new ArrayList<Users>();
		for(Users user:listUser) {
			if(user.getUsername().toLowerCase().contains(keyword)) {
				result.add(user);
			}
		}
		return result;
	}
	@GetMapping("/courses/list-user/courseId={courseId}")
	public String listUser(Model model, @PathVariable("courseId") Integer courseId,
			@RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size,
			@RequestParam(required = false) String keyword) {
		try {
			int currentPage = page.orElse(1);
			int pageSize = size.orElse(5);
//			Order order = new Order(Sort.Direction.ASC,"level");
			Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
			Course course = courseService.getCourseById(courseId);
			List<Users> listUser=course.getListUser();
			if (keyword != null) {
				listUser=findUser(listUser, keyword);
				model.addAttribute("keyword", keyword);
			}
			final int start = (int)pageable.getOffset();
			final int end = Math.min((start + pageable.getPageSize()), listUser.size());
			final Page<Users> userPage = new PageImpl<>(listUser.subList(start, end), pageable, listUser.size());
			int totalPages = userPage.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);
			}
			model.addAttribute("currentPage", userPage.getNumber() + 1);
			model.addAttribute("totalItems", userPage.getTotalElements());
			model.addAttribute("totalPages", userPage.getTotalPages());
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("userPage", userPage);
			model.addAttribute("courseId", courseId);
			model.addAttribute("pageTitle", "Danh sách người dùng đăng ký " + course.getCourseName());
			model.addAttribute("path", "/manage/courses/list-user/courseId="+courseId);
			return "/admin/pages/tables/basic-table";
		} catch (Exception e) {
			model.addAttribute("message", e.getMessage());
			return "redirect:/manage/courses";
		}
	}
	
	@GetMapping("/courses/edit")
	public String editCourse(@RequestParam("id") Integer id, Model model) {
		System.out.println("abcd "+id);
		try {
			Course course = courseService.getCourseById(id);
			model.addAttribute("course", course);
			model.addAttribute("pageTitle", "Chỉnh sửa khóa học: ( " + course.getCourseName() + ")");
			return "/admin/pages/forms/course_form";
		} catch (Exception e) {
			return "redirect:/manage/courses";
		}
	}

	@GetMapping("/courses/delete")
	public String deleteCourse(@RequestParam("id") Integer id, RedirectAttributes redirectAttributes) {
		System.out.println("aaaaa");
		try {
			System.out.println("abcd "+id);
			courseService.deleteCourse(id);
			
			return "redirect:/manage/courses";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "abc";
		}
	}
	@GetMapping("/courses/topic")
	public String editTopic(@RequestParam("courseId") int courseId,
			@RequestParam("topicId") int topicId, Model model) {
		try {
			Topic topic = topicService.getTopicByTopicId(topicId);
			model.addAttribute("topic", topic);
			model.addAttribute("courseId", courseId);
			model.addAttribute("pageTitle", "Chỉnh sửa chủ đề: ( " + topic.getTopicName() + ")");
			model.addAttribute("state", 1);
			return "/admin/pages/forms/topic-form";
		} catch (Exception e) {
			return "redirect:/manage/courses/list-topic/courseId="+courseId;
		}
	}
	@GetMapping("/courses/topic/new-vocab")
	public String addVocabToTopic(
			@RequestParam("courseId") int courseId,
			@RequestParam("topicId") int topicId, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			Vocabulary vocab = new Vocabulary();
			model.addAttribute("vocab", vocab);
			model.addAttribute("courseId", courseId);
			model.addAttribute("topicId", topicId);
			model.addAttribute("pageTitle", "Thêm từ vựng mới");
			return "/admin/pages/forms/vocab-form";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/manage/courses/topic/list-vocabulary/courseId="+courseId+"/topicId="+topicId;
		}
	}
	@PostMapping("/courses/topic/new-vocab/save")
	public String saveVocabToTopic(@RequestParam("courseId") int courseId,
			@RequestParam("topicId") int topicId,Vocabulary vocab,
			@RequestParam("image") MultipartFile image,
			@RequestParam("sound") MultipartFile sound){
		try {
			Vocabulary v = vocabularyService.getVocabulary(vocab.getVocabularyId());
			if(image.getSize()>0) {
				String fileName = StringUtils.cleanPath(image.getOriginalFilename());
				vocab.setIllustration(fileName);
				String upload = "images/" + vocab.getVocab();
				FileUploadUtil.saveFile(upload, fileName, image);
			}else {
				vocab.setIllustration(v.getIllustration());
			}
			if(sound.getSize()>0) {
				String fileName = StringUtils.cleanPath(sound.getOriginalFilename());
				vocab.setPronunciation(fileName);
				String upload = "images/" + vocab.getVocab();
				FileUploadUtil.saveFile(upload, fileName, sound);
			}else {
				vocab.setPronunciation(v.getPronunciation());
			}
			Topic topic = topicService.getTopicByTopicId(topicId);
			
			vocab.setTopic(topic);
			vocabularyService.saveVocabulary(vocab);
			return "redirect:/manage/courses/topic/list-vocabulary/courseId="+courseId+"/topicId="+topicId;
		} catch (Exception e) {
			return "redirect:/manage/courses/list-topic/courseId="+courseId;
		}
	}

	@GetMapping("/courses/topic/delete")
	public String deleteTopic(@RequestParam("topicId") int topicId,
			@RequestParam("courseId") int courseId,
			RedirectAttributes redirectAttributes) {
		try {
			topicService.deleteTopic(topicId);
			redirectAttributes.addFlashAttribute("message",
					"The topic with id =" + topicId + " has been deleted successfully!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}

		return "redirect:/manage/courses/list-topic/courseId="+courseId;
	}
	
	@GetMapping("/courses/topic/vocabury/delete")
	public String deleteVocabury(@RequestParam("vocabId") int vocabId,
			@RequestParam("courseId") int courseId,
			@RequestParam("topicId") int topicId,
			RedirectAttributes redirectAttributes) {
		try {
			vocabularyService.deleteVocabulary(vocabId);
			redirectAttributes.addFlashAttribute("message",
					"The vocabury with id =" + vocabId + " has been deleted successfully!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}

		return "redirect:/manage/courses/topic/list-vocabulary/courseId="+courseId+"/topicId="+topicId;
	}

	@GetMapping("/courses/topic/vocabury/edit")
	public String editVocabury(@RequestParam("vocabId") int vocabId,
			@RequestParam("courseId") int courseId,
			@RequestParam("topicId") int topicId, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			Vocabulary vocab = vocabularyService.getVocabulary(vocabId);
			model.addAttribute("vocab", vocab);
			model.addAttribute("courseId", courseId);
			model.addAttribute("topicId", topicId);
			model.addAttribute("pageTitle", "Chỉnh sửa từ vựng: ( " + vocab.getVocab() + ")");
			return "/admin/pages/forms/vocab-form";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/manage/courses/topic/list-vocabulary/courseId="+courseId+"/topicId="+topicId;
		}
	}
	
	@GetMapping("/courses/new")
	public String createCourse(Model model) {
		Course course = new Course();
		model.addAttribute("course", course);
		model.addAttribute("pageTitle", "Tạo khóa học mới");
		return "/admin/pages/forms/course_form";
	}

	@PostMapping("/courses/save")
	public String saveCourse(Course course, @RequestParam("image") MultipartFile multipartFile) throws IOException {
		if (multipartFile.getSize()>0) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());	
			course.setIllustration(fileName);
			String upload = "images/" + course.getCourseName();
			FileUploadUtil.saveFile(upload, fileName, multipartFile);
			courseService.save(course);
		} else {
			Course c = courseService.getCourseById(course.getCourseId());
			course.setIllustration(c.getIllustration());
			courseService.save(course);
		}
		
		return "redirect:/manage/courses";
	}
	@GetMapping("/courses/new-topic/courseId={courseId}")
	public String createTopic(Model model, @PathVariable("courseId") Integer courseId) {
		Topic topic = new Topic();
		Course course = courseService.getCourseById(courseId);
		model.addAttribute("topic", topic);
		model.addAttribute("courseId", courseId);
		model.addAttribute("pageTitle", "Tạo chủ đề mới cho khóa học :"+course.getCourseName());
		model.addAttribute("state", 0);
		return "/admin/pages/forms/topic-form";
	}
	@PostMapping("/courses/topic/save/courseId={courseId}")
	public String saveTopic(Model model, @PathVariable("courseId") Integer courseId,
			@RequestParam("file") MultipartFile file,
			Topic topic) throws IOException {
		Course course = courseService.getCourseById(courseId);
		if(file.getSize()>0) {
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			String upload = "images/" + course.getCourseName()+"/"+ topic.getTopicName();
			FileUploadUtil.saveFile(upload, fileName, file);
			List<Vocabulary> listVocabularies= ReadExcel.readExcelVocab(upload+'/'+fileName,topic);
			System.out.println("aaa "+ upload+'/'+fileName);
			for(Vocabulary v: listVocabularies) {
				System.out.println(v);
			}
			topic.setListVocab(listVocabularies);
		}	
		topic.setCourse(course);
		
		if(topic.getTopicId()<=0) {
			for(Users user: course.getListUser()) {
				UserProgression p = new UserProgression();
				p.setUser(user);
				p.setTopic(topic);
				p.setProgress(0);
				progressionService.saveUserProgression(p);
			}
		}
		topicService.save(topic);
		return "redirect:/manage/courses/list-topic/courseId="+courseId;
	}
	@GetMapping("/courses/list-test/courseId={courseId}")
	public String listTest(Model model, @PathVariable("courseId") Integer courseId,
			@RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size,
			@RequestParam(required = false) String keyword) {
		try {
			int currentPage = page.orElse(1);
			int pageSize = size.orElse(5);
			Order order = new Order(Sort.Direction.ASC,"testNumber");
			Pageable paging = PageRequest.of(currentPage - 1, pageSize, Sort.by(order));
			Course course = courseService.getCourseById(courseId);
			Page<Test> testPage;
			if (keyword == null) {
				testPage = testService.findByCourse(course, paging);
			} else {
				testPage = testService.findByCourseAndTestNumberContainingIgnoreCase(course, keyword, paging);
				model.addAttribute("keyword", keyword);
			}
			int totalPages = testPage.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);
			}
			model.addAttribute("currentPage", testPage.getNumber() + 1);
			model.addAttribute("totalItems", testPage.getTotalElements());
			model.addAttribute("totalPages", testPage.getTotalPages());
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("testPage", testPage.getContent());
			model.addAttribute("courseId", courseId);
			model.addAttribute("pageTitle", "Danh sách đề luyện tập khóa học :" + course.getCourseName());
			model.addAttribute("path", "/manage/courses/new-test?courseId="+courseId);
			return "admin/pages/tables/test-table";
		} catch (Exception e) {
			model.addAttribute("message", e.getMessage());
			return "redirect:/manage/courses";
		}
	}
	@GetMapping("/courses/new-test")
	public String createTest(Model model, @PathParam("courseId") Integer courseId) {
		Test test=new Test();
		Course course = courseService.getCourseById(courseId);
		model.addAttribute("test", test);
		model.addAttribute("courseId", courseId);
		model.addAttribute("state", 0);
		model.addAttribute("pageTitle", "Tạo đề luyện cho khóa học :"+course.getCourseName());
		return "/admin/pages/forms/test-form";
	}
	@PostMapping("/courses/test/save")
	public String saveTest(Model model,
			@PathParam("courseId") Integer courseId, Test test,
			@RequestParam("file") MultipartFile file) throws IOException {
		Course course = courseService.getCourseById(courseId);
		if(file.getSize()>0) {
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			String upload = "images/" + course.getCourseName()+"/"+test.getTestNumber();
			FileUploadUtil.saveFile(upload, fileName, file);
			List<Question> listQuestions = ReadExcel.readExcelQuestion(upload+'/'+fileName,test);
			test.setListQuestion(listQuestions);
		}
		test.setCourse(course);
		testService.save(test);
		return "redirect:/manage/courses/list-test/courseId="+courseId;
	}
	@GetMapping("/courses/test/delete")
	public String deleteTest(@RequestParam("testId") int testId,
			@RequestParam("courseId") int courseId,
			RedirectAttributes redirectAttributes) {
		try {
			testService.deleteTest(testId);
			redirectAttributes.addFlashAttribute("message",
					"The test with id =" + testId + " has been deleted successfully!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}

		return "redirect:/manage/courses/list-test/courseId="+courseId;
	}
	@GetMapping("/courses/test")
	public String editTest(@RequestParam("courseId") int courseId,
			@RequestParam("testId") int testId, Model model) {
		try {
			Test test = testService.getTestByTestId(testId);
			model.addAttribute("test", test);
			model.addAttribute("courseId", courseId);
			model.addAttribute("state", 1);
			model.addAttribute("pageTitle", "Chỉnh sửa bộ đề: ( " + test.getTestNumber() + ")");
			return "/admin/pages/forms/test-form";
		} catch (Exception e) {
			return "redirect:/manage/courses/list-test/courseId="+courseId;
		}
	}
	@GetMapping("/courses/{courseId}/test/{testId}/list-question")
	public String listQuestion(Model model, @PathVariable("courseId") Integer courseId,
			@PathVariable("testId") Integer testId,
			@RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size,
			@RequestParam(required = false) String keyword) {
		try {
			int currentPage = page.orElse(1);
			int pageSize = size.orElse(5);
			Order order = new Order(Sort.Direction.ASC,"questionNumber");
			Pageable paging = PageRequest.of(currentPage - 1, pageSize, Sort.by(order));
			Test test =testService.getTestByTestId(testId);
			Page<Question> questionPage;
			if (keyword == null) {
				questionPage = questionService.findByTest(test, paging);
			} else {
				questionPage = questionService.findByTestAndQuestionNumberContainingIgnoreCase(test, keyword, paging);
				model.addAttribute("keyword", keyword);
			}
			int totalPages = questionPage.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);
			}
			model.addAttribute("currentPage", questionPage.getNumber() + 1);
			model.addAttribute("totalItems", questionPage.getTotalElements());
			model.addAttribute("totalPages", questionPage.getTotalPages());
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("questionPage", questionPage.getContent());
			model.addAttribute("courseId", courseId);
			model.addAttribute("testId", testId);
			model.addAttribute("pageTitle", "Danh sách câu hỏi bộ:" + test.getTestNumber());
			model.addAttribute("path", "/manage/courses/"+courseId+"/test/"+testId+"/list-question");
			return "admin/pages/tables/question-table";
		} catch (Exception e) {
			model.addAttribute("message", e.getMessage());
			return "redirect:/manage/courses";
		}
	}
	@GetMapping("/courses/{courseId}/test/{testId}/new-question")
	public String newQuestion( Model model,@PathVariable("courseId") Integer courseId,
			@PathVariable("testId") Integer testId) {
		Question question = new Question();
		Test test = testService.getTestByTestId(testId);
		model.addAttribute("question", question);
		model.addAttribute("courseId", courseId);
		model.addAttribute("testId", testId);
		model.addAttribute("pageTitle", "Tạo đề luyện cho khóa học :"+test.getTestNumber());
		return "/admin/pages/forms/question-form";
	}
	@PostMapping("/courses/{courseId}/test/{testId}/new-question/save")
	public String saveQuestion( Model model,@PathVariable("courseId") Integer courseId,
			@PathVariable("testId") Integer testId, Question question) {
		Test test = testService.getTestByTestId(testId);
		question.setTest(test);
		questionService.save(question);
		model.addAttribute("question", question);
		model.addAttribute("courseId", courseId);
		model.addAttribute("testId", testId);
		return "redirect:/manage/courses/"+courseId+"/test/"+testId+"/list-question";
	}
	@GetMapping("/courses/{courseId}/test/{testId}/delete")
	public String deleteQuestion(@PathVariable("testId") int testId,
			@PathVariable("courseId") int courseId,
			@RequestParam("questionId") int questionId,
			RedirectAttributes redirectAttributes) {
		try {
			System.out.println(questionService.deleteQuestion(questionId));
			redirectAttributes.addFlashAttribute("message",
					"The test with id =" + questionId + " has been deleted successfully!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/manage/courses/"+courseId+"/test/"+testId+"/list-question";
	}
	@GetMapping("/courses/{courseId}/test/{testId}/edit")
	public String editQuestion(@PathVariable("courseId") int courseId,
			@PathVariable("testId") int testId,
			@RequestParam("questionId") int questionId,
			Model model) {
		try {
			Question question = questionService.getQuestionById(questionId);
			Test test =testService.getTestByTestId(testId);
			model.addAttribute("question", question);
			model.addAttribute("courseId", courseId);
			model.addAttribute("testId", testId);
			model.addAttribute("pageTitle", "Tạo đề luyện cho khóa học :"+test.getTestNumber());
			return "/admin/pages/forms/question-form";
		} catch (Exception e) {
			return "redirect:/manage/courses/list-test/courseId="+courseId;
		}
	}
}
