package com.elearning.studyvocabulary.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.elearning.studyvocabulary.model.entity.Course;
import com.elearning.studyvocabulary.model.entity.Feedback;
import com.elearning.studyvocabulary.model.service.FeedbackService;

@Controller
@RequestMapping("/manage")
public class FeedbackController {
	@Autowired
	private FeedbackService feedbackService;
	
	@GetMapping("/feedback")
	public String getFeedback(Model model, @RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size, @RequestParam(required = false) String keyword) {
		try {
			int currentPage = page.orElse(1);
			int pageSize = size.orElse(5);
			Order order = new Order(Sort.Direction.DESC,"feedbackId");
			Pageable paging = PageRequest.of(currentPage - 1, pageSize,Sort.by(order));
			Page<Feedback> feedbackPage;
			if (keyword == null) {
				feedbackPage = feedbackService.findFeedback(paging);
			} else {
				feedbackPage = feedbackService.findByErrorLocationContainingIgnoreCaseOrErrorTypeContainingIgnoreCase(paging, keyword, keyword);
				model.addAttribute("keyword", keyword);
			}
			int totalPages = feedbackPage.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);
			}
			model.addAttribute("currentPage", feedbackPage.getNumber() + 1);
			model.addAttribute("totalItems", feedbackPage.getTotalElements());
			model.addAttribute("totalPages", feedbackPage.getTotalPages());
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("feedbackPage", feedbackPage);
			model.addAttribute("pageTitle", "Phản hồi từ người dùng");
		} catch (Exception e) {
			model.addAttribute("message", e.getMessage());
		}
		return "admin/pages/tables/feedback-table";
	}
	@GetMapping("/feedback/{feedbackId}")
	public String seen(@PathVariable("feedbackId") Integer feedbackId) {
		Feedback feedback = feedbackService.getFeedbackById(feedbackId);
		feedback.setStatus(false);
		feedbackService.save(feedback);
		return "redirect:/manage/feedback";
	}
}
