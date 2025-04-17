package com.example.demo.dto;

import com.example.demo.status.AchievementStatus;

public class AchievementDTO {
    private Long id;
    private String title;
    private String description;
    private AchievementStatus status;
    private String game;
    private Long userId;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AchievementStatus getStatus() {
        return status;
    }

    public void setStatus(AchievementStatus status) {
        this.status = status;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}