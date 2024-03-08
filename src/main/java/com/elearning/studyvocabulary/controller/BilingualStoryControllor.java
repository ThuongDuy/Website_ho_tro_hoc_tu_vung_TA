package com.elearning.studyvocabulary.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.elearning.studyvocabulary.common.FileUploadUtil;
import com.elearning.studyvocabulary.model.entity.BilingualStory;
import com.elearning.studyvocabulary.model.service.BilingualStoryService;

@Controller
@RequestMapping("/manage")
public class BilingualStoryControllor {
	@Autowired
	private BilingualStoryService storyService;
	
	@GetMapping("/bilingual-story")
	public String manageStory(Model model, @RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size, @RequestParam(required = false) String keyword) {
		try {
			int currentPage = page.orElse(1);
			int pageSize = size.orElse(5);
			Pageable paging = PageRequest.of(currentPage - 1, pageSize);
			Page<BilingualStory> storyPage;
			if (keyword == null) {
				storyPage = storyService.findBilingualStory(paging);
			} else {
				storyPage = storyService.findByCategoryContainingIgnoreCase(keyword, paging);
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
			model.addAttribute("storyPage", storyPage);
			model.addAttribute("pageTitle", "Danh sách truyện song ngữ");
		} catch (Exception e) {
			model.addAttribute("message", e.getMessage());
		}
		return "admin/pages/tables/story-table";
	}
	@GetMapping("/bilingual-story/new")
	public String createCourse(Model model) {
		BilingualStory story = new BilingualStory();
		model.addAttribute("story", story);
		model.addAttribute("state", 0);
		model.addAttribute("pageTitle", "Tạo câu truyện mới");
		return "/admin/pages/forms/story-form";
	}
	@PostMapping("/bilingual-story/save")
	public String saveStory(Model model, @RequestParam("fileImage") MultipartFile fileImage,
			@RequestParam("file") MultipartFile file,
			BilingualStory story) throws IOException {
		BilingualStory old=new BilingualStory();
		if(story.getStoryId()>0) {
			old = storyService.getById(story.getStoryId());
		}
		if(file.getSize()>0) {
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			String upload = "images/" + story.getName();
			FileUploadUtil.saveFile(upload, fileName, file);
			story.setContent(upload+'/'+fileName);
		}else {
			story.setContent(old.getContent());
		}
		if(fileImage.getSize()>0) {
			String fileName = StringUtils.cleanPath(fileImage.getOriginalFilename());
			String upload = "images/" + story.getName();
			FileUploadUtil.saveFile(upload, fileName, fileImage);
			story.setIllustration(fileName);
		}else {
			story.setIllustration(old.getIllustration());
		}
		storyService.save(story);
		return "redirect:/manage/bilingual-story";
	}
	@GetMapping("/bilingual-story/edit")
	public String editStory(@RequestParam("storyId") int storyId, Model model) {
		try {
			BilingualStory story = storyService.getById(storyId);
			model.addAttribute("story", story);
			model.addAttribute("state", 1);
			model.addAttribute("pageTitle", "Chỉnh sửa câu truyện : ( " + story.getName() + ")");
			return "/admin/pages/forms/story-form";
		} catch (Exception e) {
			return "redirect:/manage/bilingual-story";
		}
	}
	@GetMapping("/bilingual-story/delete")
	public String deleteTest(@RequestParam("storyId") int storyId,
			RedirectAttributes redirectAttributes) {
		try {
			storyService.delete(storyId);
			redirectAttributes.addFlashAttribute("message",
					"The story with id =" + storyId + " has been deleted successfully!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}

		return "redirect:/manage/bilingual-story";
	}
}
