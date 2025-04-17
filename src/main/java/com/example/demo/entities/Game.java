package com.example.demo.entities;

import com.example.demo.status.GameStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private GameStatus status;

    private Integer rating;

    private String comment;

    private Double hoursPlayed;

    private String genre;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Achievement> achievements = new ArrayList<>();

    public Game() {}

    public Game(String title, GameStatus status, Integer rating, String comment, Double hoursPlayed, String genre, Users user) {
        this.title = title;
        this.status = status;
        this.rating = rating;
        this.comment = comment;
        this.hoursPlayed = hoursPlayed;
        this.genre = genre;
        this.user = user;
    }

    // Геттеры и сеттеры остаются без изменений
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public GameStatus getStatus() { return status; }
    public void setStatus(GameStatus status) { this.status = status; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Double getHoursPlayed() { return hoursPlayed; }
    public void setHoursPlayed(Double hoursPlayed) { this.hoursPlayed = hoursPlayed; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public Users getUser() { return user; }
    public void setUser(Users user) { this.user = user; }

    public List<Achievement> getAchievements() { return achievements; }
    public void setAchievements(List<Achievement> achievements) { this.achievements = achievements; }
}