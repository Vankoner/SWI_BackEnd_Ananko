package com.example.demo.entities;

import com.example.demo.status.AchievementStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
public class Achievement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private AchievementStatus status;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    @JsonBackReference
    private Game game;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    public Achievement() {}

    public Achievement(String title, String description, AchievementStatus status, Game game, Users user) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.game = game;
        this.user = user;
    }

    // Геттеры и сеттеры остаются без изменений
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public AchievementStatus getStatus() { return status; }
    public void setStatus(AchievementStatus status) { this.status = status; }

    public Game getGame() { return game; }
    public void setGame(Game game) { this.game = game; }

    public Users getUser() { return user; }
    public void setUser(Users user) { this.user = user; }
}