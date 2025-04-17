package com.example.demo.dto;

import java.util.List;

public class GameDTO {
    private Long id;
    private String title;
    private String status;
    private Integer rating;
    private String comment;
    private Double hoursPlayed;
    private String genre;
    private Long userId;
    private List<AchievementDTO> achievements;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Double getHoursPlayed() {
        return hoursPlayed;
    }

    public void setHoursPlayed(Double hoursPlayed) {
        this.hoursPlayed = hoursPlayed;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public List<AchievementDTO> getAchievements() {
        return achievements;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setAchievements(List<AchievementDTO> achievements) {
        this.achievements = achievements;
    }
}

