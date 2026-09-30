package com.anitrack.anitrack.entity;

import jakarta.persistence.*;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "anime")
public class Anime {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(
            name = "id",
            nullable = false,
            columnDefinition = "BINARY(16)"
    )
    private UUID id;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(
            name = "aliases",
            columnDefinition = "JSON"
    )
    private String aliases;

    @Column(name = "background_url", length = 500)
    private String backgroundUrl;

    @Column(name = "cover_url", length = 500)
    private String coverUrl;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "episode_count")
    private Integer episodeCount;

    @Column(
            name = "external_score",
            precision = 4,
            scale = 1
    )
    private BigDecimal externalScore;

    @Column(name = "is_adult", nullable = false)
    private Boolean isAdult;

    @Column(name = "is_official", nullable = false)
    private Boolean isOfficial;

    @Column(
            name = "original_title",
            nullable = false,
            length = 255
    )
    private String originalTitle;

    @Column(name = "release_year")
    private Integer releaseYear;

    @Column(name = "season", length = 20)
    private String season;

    @Column(
            name = "anime_status",
            nullable = false,
            length = 30
    )
    private String status;

    @Column(
            name = "summary",
            columnDefinition = "TEXT"
    )
    private String summary;

    @Column(
            name = "title",
            nullable = false,
            length = 255
    )
    private String title;

    @Column(
            name = "anime_type",
            nullable = false,
            length = 30
    )
    private String type;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(
            name = "tags",
            columnDefinition = "JSON"
    )
    private String tags;

    @Column(name = "popularity")
    private Integer popularity;

    @Column(name = "trending")
    private Integer trending;

    @Column(name = "favourites")
    private Integer favourites;

    @Column(name = "source", length = 50)
    private String source;

    @ManyToMany
    @JoinTable(
            name = "anime_genres",

            // 当前实体 Anime 对应中间表的 anime_id
            joinColumns = @JoinColumn(name = "anime_id"),

            // Genre 对应中间表的 genre_id
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    @PrePersist
    protected void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        if (createdAt == null) {
            createdAt = now;
        }

        if (updatedAt == null) {
            updatedAt = now;
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

    public String getAliases() {
        return aliases;
    }

    public void setAliases(String aliases) {
        this.aliases = aliases;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getEpisodeCount() {
        return episodeCount;
    }

    public void setEpisodeCount(Integer episodeCount) {
        this.episodeCount = episodeCount;
    }

    public BigDecimal getExternalScore() {
        return externalScore;
    }

    public void setExternalScore(BigDecimal externalScore) {
        this.externalScore = externalScore;
    }

    public Boolean getIsAdult() {
        return isAdult;
    }

    public void setIsAdult(Boolean adult) {
        isAdult = adult;
    }

    public Boolean getIsOfficial() {
        return isOfficial;
    }

    public void setIsOfficial(Boolean official) {
        isOfficial = official;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Integer getPopularity() {
        return popularity;
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }

    public Integer getTrending() {
        return trending;
    }

    public void setTrending(Integer trending) {
        this.trending = trending;
    }

    public Integer getFavourites() {
        return favourites;
    }

    public void setFavourites(Integer favourites) {
        this.favourites = favourites;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }
}