package com.anitrack.anitrack.entity;

import jakarta.persistence.*;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "user_anime",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_anime",
                        columnNames = {
                                "user_id",
                                "anime_id"
                        }
                )
        }
)
public class UserAnime {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(
            name = "id",
            nullable = false,
            columnDefinition = "BINARY(16)"
    )
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_user_anime_user"
            )
    )
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "anime_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_user_anime_anime"
            )
    )
    private Anime anime;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;


    @Column(
            name = "created_at",
            nullable = false
    )
    private LocalDateTime createdAt;

    @Column(
            name = "current_episode",
            nullable = false
    )
    private Integer currentEpisode = 0;

    @Column(
            name = "is_favorite",
            nullable = false
    )
    private Boolean isFavorite = false;

    @Column(name = "last_watched_at")
    private LocalDateTime lastWatchedAt;

    @Column(
            name = "note",
            columnDefinition = "TEXT"
    )
    private String note;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(
            name = "note_images",
            columnDefinition = "JSON"
    )
    private List<String> noteImages = new ArrayList<>();

    @Column(name = "rating")
    private Short rating;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(
            name = "updated_at",
            nullable = false
    )
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "watch_status",
            nullable = false,
            length = 30
    )
    private WatchStatus watchStatus;

    @PrePersist
    protected void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        if (createdAt == null) {
            createdAt = now;
        }

        if (updatedAt == null) {
            updatedAt = now;
        }

        if (currentEpisode == null) {
            currentEpisode = 0;
        }

        if (isFavorite == null) {
            isFavorite = false;
        }

        if (noteImages == null) {
            noteImages = new ArrayList<>();
        }
    }

    @PreUpdate
    protected void onUpdate() {

        updatedAt = LocalDateTime.now();
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Anime getAnime() {
        return anime;
    }

    public void setAnime(Anime anime) {
        this.anime = anime;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getCurrentEpisode() {
        return currentEpisode;
    }

    public void setCurrentEpisode(Integer currentEpisode) {
        this.currentEpisode = currentEpisode;
    }

    public Boolean getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(Boolean favorite) {
        isFavorite = favorite;
    }

    public LocalDateTime getLastWatchedAt() {
        return lastWatchedAt;
    }

    public void setLastWatchedAt(LocalDateTime lastWatchedAt) {
        this.lastWatchedAt = lastWatchedAt;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<String> getNoteImages() {
        return noteImages;
    }

    public void setNoteImages(List<String> noteImages) {
        this.noteImages = noteImages;
    }

    public Short getRating() {
        return rating;
    }

    public void setRating(Short rating) {
        this.rating = rating;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public WatchStatus getWatchStatus() {
        return watchStatus;
    }

    public void setWatchStatus(WatchStatus watchStatus) {
        this.watchStatus = watchStatus;
    }
}