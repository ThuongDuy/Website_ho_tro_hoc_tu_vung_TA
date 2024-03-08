package com.elearning.studyvocabulary.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.elearning.studyvocabulary.common.FileUploadUtil;
import com.elearning.studyvocabulary.common.MyEmailService;
import com.elearning.studyvocabulary.common.RandomString;
import com.elearning.studyvocabulary.model.entity.Course;
import com.elearning.studyvocabulary.model.entity.Roles;
import com.elearning.studyvocabulary.model.entity.Test;
import com.elearning.studyvocabulary.model.entity.TestResults;
import com.elearning.studyvocabulary.model.entity.Topic;
import com.elearning.studyvocabulary.model.entity.UserProgression;
import com.elearning.studyvocabulary.model.entity.Users;
import com.elearning.studyvocabulary.model.service.BilingualStoryService;
import com.elearning.studyvocabulary.model.service.CourseService;
import com.elearning.studyvocabulary.model.service.FeedbackService;
import com.elearning.studyvocabulary.model.service.RoleService;
import com.elearning.studyvocabulary.model.service.UserProgressionService;
import com.elearning.studyvocabulary.model.service.UserService;
import com.elearning.studyvocabulary.payload.request.UserRequest;
import com.elearning.studyvocabulary.security.CustomUserDetails;

@Controller
@RequestMapping("/manage")
public class AdminController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private MyEmailService emailService;
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private BilingualStoryService storyService;
	
	@Autowired
	private FeedbackService feedbackService;
	
	@Autowired
	private UserProgressionService progressionService;
	
	@GetMapping("/")
	public String home(Model model) {
		CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Users user = userService.findByUsername(customUserDetails.getUsername());
		model.addAttribute("user", user);
		int sumUser = userService.getAllUser().size();
		model.addAttribute("sumUser", sumUser);
		List<Course> list =courseService.getAll();
		model.addAttribute("sumCourse", list.size());
		model.addAttribute("sumStory", storyService.countAll());
		List<String> lables = new ArrayList<String>();
		List<Integer> data = new ArrayList<Integer>();
		List<String> background = new ArrayList<String>();
		List<String> borderColor = new ArrayList<String>();
		Map<Course, Integer> unsortedMap = new HashMap<>();
		for(Course i : list) {
			
			unsortedMap.put(i, i.getListUser().size());
		} 
		Map<Course, Integer> sortedMap = unsortedMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        int size = list.size();
        if(sortedMap.size()>10) {
        	size = 10;
        }
        int count=0;
        for (Map.Entry<Course, Integer> entry : sortedMap.entrySet()) {
        	lables.add(entry.getKey().getCourseName());
			data.add(entry.getValue());
			background.add("rgba(54, 162, 235, 0.2)");
			borderColor.add("rgba(54, 162, 235, 1)");
            
            count++;
            if (count == size) break;
        }
		
		model.addAttribute("labels", lables);
        model.addAttribute("data", data);
        model.addAttribute("background", background);
        model.addAttribute("borderColor", borderColor);
        model.addAttribute("tb", feedbackService.countByStatus(true));
        List<Users> listUsers = userService.getAllUser();
        List<Users> member = new ArrayList<Users>();
		Map<Users, Integer> unsorted = new HashMap<>();
		for(Users i : listUsers) {
			int tongTu = 0;
			for (Course c : i.getListCourse()) {
				for (Topic t: c.getListTopic()) {
					UserProgression progress = progressionService.findByTopicAndUser(t, i);
					if(progress!=null) {
						tongTu+=progress.getProgress();
					}
					
				}
			}
			unsorted.put(i, tongTu);
			for(Roles j: i.getListRoles()) {
				if(j.getRoleName().name().toString().equals("ADMIN")) {
					member.add(i);
				}
			}
		} 

		Map<Users, Integer> sorted = unsorted.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        int top = listUsers.size();
        if(sorted.size()>10) {
        	top = 10;
        }
        List<Users> lists = new ArrayList<Users>();
        List<Integer> learned = new ArrayList<Integer>();
        int d=0;
        for (Map.Entry<Users, Integer> entry : sorted.entrySet()) {
        	lists.add(entry.getKey());
        	learned.add(entry.getValue());
            
            d++;
            if (d == top) break;
        }
        model.addAttribute("listUser", lists);
		model.addAttribute("learned", learned);
		model.addAttribute("member", member);
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		model.addAttribute("dateFormat", dateFormat);
		return "admin/index_admin";
	}
	@GetMapping("/users")
	public String manageUsers(Model model, 
		      @RequestParam("page") Optional<Integer> page, 
		      @RequestParam("size") Optional<Integer> size,
		      @RequestParam(required = false) String keyword) {
		try {
		int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Pageable paging = PageRequest.of(currentPage - 1, pageSize);
        Page<Users> userPage;
        if (keyword == null) {
        	userPage = userService.findUser(paging);
          } else {
        	  userPage = userService.findByUsernameContainingIgnoreCase(keyword, paging);
            model.addAttribute("keyword", keyword);
          }
	      int totalPages = userPage.getTotalPages();
	        if (totalPages > 0) {
	            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
	                .boxed()
	                .collect(Collectors.toList());
	            model.addAttribute("pageNumbers", pageNumbers);
	        }
	      DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	      model.addAttribute("currentPage", userPage.getNumber() + 1);
	      model.addAttribute("totalItems", userPage.getTotalElements());
	      model.addAttribute("totalPages", userPage.getTotalPages());
	      model.addAttribute("pageSize", pageSize);
	      model.addAttribute("userPage", userPage);
	      model.addAttribute("dateFormat", dateFormat);
	      model.addAttribute("path","/manage/users");
		} catch (Exception e) {
		      model.addAttribute("message", e.getMessage());
	    }
		return "admin/pages/tables/basic-table";
	}
	@GetMapping("/users/edit")
	public String editUser(Model model, 
		      @RequestParam("id") int id, 
		      RedirectAttributes redirectAttributes) {
		try {
		      Users user = userService.findById(id);
		      List<Roles> listRole= roleService.getAllRole();
			  model.addAttribute("roles",listRole);
		      model.addAttribute("user", user);
		      model.addAttribute("type", 0);
		      model.addAttribute("pageTitle", "Chỉnh sửa người dùng (ID: " + id + ")");
		      return "/admin/pages/forms/basic_elements";
		    } catch (Exception e) {
		      redirectAttributes.addFlashAttribute("message", e.getMessage());
		      return "redirect:/manage/users";
		    }
	}
	@GetMapping("/users/delete")
	public String deleteUser(Model model, 
		      @RequestParam("id") int id,
		      RedirectAttributes redirectAttributes) {
		try {
		      userService.deleteUser(id);
		      redirectAttributes.addFlashAttribute("message", "The user with id=" + id + " has been deleted successfully!");
		      return "redirect:/manage/users";
		    } catch (Exception e) {
		      redirectAttributes.addFlashAttribute("message", e.getMessage());
		      return "redirect:/manage/users";
		    }

		    
	}
	@GetMapping("/users/new")
	  public String addUser(Model model) {
	    UserRequest user = new UserRequest();
	    RandomString rand = new RandomString();
    	String pass=rand.randomAlphaNumeric(8);
    	user.setPassword(pass);
    	System.out.println("aaaaa "+pass);
	    model.addAttribute("user", user);
	    model.addAttribute("type", 1);
	    List<Roles> listRole= roleService.getAllRole();
		model.addAttribute("roles",listRole);
	    model.addAttribute("pageTitle", "Tạo người dùng mới");
	    return "/admin/pages/forms/basic_elements";
	  }
	@PostMapping("/users/save")
	  public String saveTutorial(Users user,@RequestParam("image") MultipartFile multipartFile, 
			  BindingResult bindingResult,
			  RedirectAttributes redirectAttributes) {
		try {
	    	user.setUserStatus(true);
	    	if (!multipartFile.isEmpty()) {
				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				if (fileName.trim() != null) {
					user.setAvatar(fileName);
				} else {
					user.setAvatar(null);
				}
				Users saveUser;
				if (user.getUserId() != 0) {
					saveUser=userService.update(user.getUserId(), user);
				} else {
					user.setCreated(new Date());
					String pass = user.getPassword();
					user.setPassword(encoder.encode(pass));
					if (userService.existsByUsername(user.getUsername())) {
						bindingResult.rejectValue("username", "user.username","Tên đăng nhập đã được sử dụng");
						return "admin/pages/samples/register";
					}
					if (userService.existsByEmail(user.getEmail())) {
						bindingResult.rejectValue("email", "user.email","Email đã được sử dụng");
						return "admin/pages/samples/register";
					}
					saveUser=userService.save(user);
					emailService.sendEmail(user.getEmail(), "Cấp tài khoản",
					user.getUsername(),pass);
				}
				String upload = "images/" + saveUser.getUsername();
				FileUploadUtil.saveFile(upload, fileName, multipartFile);
			} else {
					user.setAvatar(null);
					if (user.getUserId() != 0) {
						userService.update(user.getUserId(), user);
					} else {
						user.setCreated(new Date());
						String pass = user.getPassword();
						if (userService.existsByUsername(user.getUsername())) {
							bindingResult.rejectValue("username", "user.username","Tên đăng nhập đã được sử dụng");
							return "admin/pages/samples/register";
						}
						if (userService.existsByEmail(user.getEmail())) {
							bindingResult.rejectValue("email", "user.email","Email đã được sử dụng");
							return "admin/pages/samples/register";
						}
						user.setPassword(encoder.encode(pass));
						userService.save(user);
						emailService.sendEmail(user.getEmail(), "Cấp tài khoản",
								user.getUsername(),pass);
					}
			}
	    	
	      redirectAttributes.addFlashAttribute("message", "The User has been saved successfully!");
	    } catch (Exception e) {
	      redirectAttributes.addAttribute("message", e.getMessage());
	    }
	    return "redirect:/manage/users";
	  }
}
