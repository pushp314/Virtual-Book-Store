package com.virtualbookstore.controller;

import com.virtualbookstore.model.Review;
import com.virtualbookstore.service.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public String getAllReviews(Model model) {
        List<Review> reviews = reviewService.getAllReviews();
        model.addAttribute("reviews", reviews);
        return "reviews"; // Thymeleaf template name
    }

    @GetMapping("/book/{bookId}")
    public String getReviewsByBook(@PathVariable Long bookId, Model model) {
        List<Review> reviews = reviewService.getReviewsByBook(bookId);
        model.addAttribute("reviews", reviews);
        return "reviews"; // Thymeleaf template name
    }

    @GetMapping("/user/{userId}")
    public String getReviewsByUser(@PathVariable Long userId, Model model) {
        List<Review> reviews = reviewService.getReviewsByUser(userId);
        model.addAttribute("reviews", reviews);
        return "reviews"; // Thymeleaf template name
    }

    @PostMapping("/add")
    public String addReview(@ModelAttribute Review review) {
        reviewService.addReview(review.getUser(), review.getBook(), review.getRating(), review.getReviewText());
        return "redirect:/reviews"; // Redirect back to the reviews page after adding
    }

    @GetMapping("/delete/{reviewId}")
    public String deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return "redirect:/reviews"; // Redirect back to the reviews page after deletion
    }
}
