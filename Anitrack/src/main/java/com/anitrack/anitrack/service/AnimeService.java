package com.anitrack.anitrack.service;

import com.anitrack.anitrack.entity.AdultFilter;
import com.anitrack.anitrack.entity.Anime;
import com.anitrack.anitrack.entity.Genre;
import com.anitrack.anitrack.repository.AnimeRepository;
import com.anitrack.anitrack.repository.GenreRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class AnimeService {
    private final AnimeRepository animeRepository;
    private final GenreRepository genreRepository;

    public AnimeService(AnimeRepository animeRepository, GenreRepository genreRepository) {
        this.animeRepository = animeRepository;
        this.genreRepository = genreRepository;
    }

    //1.增加动漫
    public Anime addAnime(Anime anime) {
        if (StringUtils.isBlank(anime.getTitle())) {
            throw new IllegalArgumentException("动漫标题不能为空");
        }
        return animeRepository.save(anime);
    }

    //2.删除动漫
    public void deleteAnime(UUID id) {
        if (!animeRepository.existsById(id)) {
            throw new IllegalArgumentException("该动漫不存在");
        }
        animeRepository.deleteById(id);
    }

    //4.获取所有动漫
    public List<Anime> getAllAnime() {
        return animeRepository.findAll();
    }

    //5.分页查询
    public Page<Anime> getAnimePage(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return animeRepository.findAll(pageable);
    }

    //6.根据id查询
    public Anime getAnimeById(UUID id) {
        return animeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("该动漫不存在"));
    }

    //3.更新动漫信息
    public Anime updateAnime(UUID id, Anime newAnime) {
        Anime anime = getAnimeById(id);
        if (newAnime.getTitle() != null) {
            anime.setTitle(newAnime.getTitle());
        }
        if (newAnime.getOriginalTitle() != null) {
            anime.setOriginalTitle(newAnime.getOriginalTitle());
        }
        if (newAnime.getCoverUrl() != null) {
            anime.setCoverUrl(newAnime.getCoverUrl());
        }
        if (newAnime.getBackgroundUrl() != null) {
            anime.setBackgroundUrl(newAnime.getBackgroundUrl());
        }
        if (newAnime.getEpisodeCount() != null) {
            anime.setEpisodeCount(newAnime.getEpisodeCount());
        }
        if (newAnime.getExternalScore() != null) {
            anime.setExternalScore(newAnime.getExternalScore());
        }
        if (newAnime.getStatus() != null) {
            anime.setStatus(newAnime.getStatus());
        }
        return animeRepository.save(anime);
    }

    //7.按标题精确查询
    public Anime getAnimeByTitle(String title) {
        return animeRepository.findByTitle(title).orElseThrow(() -> new IllegalArgumentException("该动漫不存在"));
    }

    //8.按标题模糊查询
    public List<Anime> searchAnimeByTitle(String title) {
        if (StringUtils.isBlank(title)) {
            return Collections.emptyList();
        }
        return animeRepository.findByTitleContainingIgnoreCase(title);
    }

    //9.按原本标题精确查询
    public Anime getAnimeByOriginalTitle(String originalTitle) {
        return animeRepository.findByOriginalTitle(originalTitle).orElseThrow(() -> new IllegalArgumentException("该动漫不存在"));
    }

    //10.按年份查找
    public List<Anime> getAnimeByReleaseYear(Integer releaseYear) {
        if (releaseYear == null) {
            throw new IllegalArgumentException("年份不能为空");
        }
        return animeRepository.findByReleaseYear(releaseYear);
    }

    //11.按季度查询
    public List<Anime> getAnimeBySeason(String season) {
        if (StringUtils.isBlank(season)) {
            throw new IllegalArgumentException("季节不能为空");
        }
        return animeRepository.findBySeason(season);
    }

    //12.按连载状态查询
    public List<Anime> getAnimeByStatus(String status) {
        if (StringUtils.isBlank(status)) {
            throw new IllegalArgumentException("连载状态不能为空");
        }
        return animeRepository.findByStatus(status);
    }

    //13.按类型查询 tv
    public List<Anime> getAnimeByType(String type) {
        if (StringUtils.isBlank(type)) {
            throw new IllegalArgumentException("类型不能为空");
        }
        return animeRepository.findByType(type);
    }

    //14.按成人内容查找
    public List<Anime> getAnimeByIsAdult(AdultFilter filter) {
        if (filter == null) {
            throw new IllegalArgumentException("成人内容过滤不可为空");
        }
        return switch (filter) {
            case ONLY_ADULT -> animeRepository.findByIsAdult(Boolean.TRUE);
            case EXCLUDE_ADULT -> animeRepository.findByIsAdult(Boolean.FALSE);
            case INCLUDE_ADULT -> animeRepository.findAll();
        };
    }

    //15.按是否是官方授权查询
    public List<Anime> getAnimeByIsOfficial(Boolean isOfficial) {
        if (isOfficial == null) {
            throw new IllegalArgumentException("是否是官方授权不可以为空");
        }
        return animeRepository.findByIsOfficial(isOfficial);
    }

    //16.原本标题精确查询
    public Anime searchAnimeByOriginalTitle(String originalTitle) {
        return animeRepository.findByOriginalTitle(originalTitle).orElseThrow(() -> new IllegalArgumentException("原本标题不能为空"));
    }

    //17.别名模糊查询
    public List<Anime> searchAnimeByAlias(String originalTitle) {
        if (StringUtils.isBlank(originalTitle)) {
            throw new IllegalArgumentException("原本标题不能为空");
        }
        return animeRepository.findByOriginalTitleContainingIgnoreCase(originalTitle);
    }

    //19.为动漫添加类型
    public Anime addGenreToAnime(UUID id, Genre genre) {
        if (id == null) {
            throw new IllegalArgumentException("id不能为空");
        }
        Anime anime = getAnimeById(id);
        anime.getGenres().add(genre);
        return animeRepository.save(anime);
    }

    //18.组合查询（名字 年份 类型 得分等等等 ）
    public List<Anime> searchAnimeCombined(String title, Integer releaseYear, String season, String type, String status, BigDecimal minScore, BigDecimal maxScore, Boolean isAdult, Boolean isOfficial, String sortBy, String sortDir) {
        // 空字符串转为 null，让 SQL 的 IS NULL 判断生效
        title = StringUtils.defaultIfBlank(title, null);
        season = StringUtils.defaultIfBlank(season, null);
        type = StringUtils.defaultIfBlank(type, null);
        status = StringUtils.defaultIfBlank(status, null);

        // 调用 Repository 查询（SQL 默认 DESC 排序）
        List<Anime> results = animeRepository.searchCombined(
                title, type, releaseYear, season, status,
                minScore, maxScore, isAdult, isOfficial, sortBy
        );

        // 如果要求升序，在 Java 里反转
        if ("asc".equalsIgnoreCase(sortDir)) {
            Collections.reverse(results);
        }

        return results;
    }

    //20.移除类型
    @Transactional
    public void removeGenreFromAnime(UUID id, Genre genre) {
        if (id == null) {
            throw new IllegalArgumentException("id不能为空");
        }
        Anime anime = getAnimeById(id);
        anime.getGenres().remove(genre);
        animeRepository.save(anime);
    }

    //21.获取动漫的所有类型
    @Transactional(readOnly = true)
    public Set<Genre> getAnimeGenres(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("id不能为空");
        }
        Anime anime = getAnimeById(id);
        return anime.getGenres();
    }

    //22.批量设置类型（覆盖原有类型）
    @Transactional
    public Anime setAnimeGenres(UUID id, List<UUID> genreIds) {
        if (id == null) {
            throw new IllegalArgumentException("id不能为空");
        }
        Anime anime = getAnimeById(id);
        List<Genre> genres = genreRepository.findAllById(genreIds);
        if (genres.size() != genreIds.size()) {
            throw new IllegalArgumentException("部分类型ID不存在");
        }
        anime.setGenres(new HashSet<>(genres));
        return animeRepository.save(anime);
    }

    //23.动漫总数 countAnime()
    public long countAnime() {
        return animeRepository.count();
    }

    //24.按状态统计
    public long countByStatus(String status) {
        if (StringUtils.isBlank(status)) {
            throw new IllegalArgumentException("状态不能为空");
        }
        return animeRepository.findByStatus(status).size();
    }

    //25.按类型
    public long countByType(String type) {
        if (StringUtils.isBlank(type)) {
            throw new IllegalArgumentException("类型不能为空");
        }
        return animeRepository.findByType(type).size();
    }

    //26.按年份
    public long countByReleaseYear(Integer releaseYear) {
        if (releaseYear == null) {
            throw new IllegalArgumentException("年份不能为空");
        }
        return animeRepository.findByReleaseYear(releaseYear).size();
    }

    //27.各个状态数量分布
    public Map<String, Long> getStatusDistribution() {
        List<Object[]> results = animeRepository.countByStatusGrouped();
        Map<String, Long> result = new HashMap<>();
        for (Object[] obj : results) {
            result.put((String) obj[0], (Long) obj[1]);
        }
        return result;
    }

    //28.各个年份分类 getYearDistribution()
    public Map<String, Long> getTypeDistribution() {
        List<Object[]> results = animeRepository.countByReleaseYearGrouped();
        Map<String, Long> result = new HashMap<>();
        for (Object[] obj : results) {
            result.put((String) obj[0], (Long) obj[1]);
        }
        return result;
    }

    //29.检查中文标题是否存在
    public Boolean existsByTitle(String title) {
        if (StringUtils.isBlank(title)) {
            throw new IllegalArgumentException("标题不能为空");
        }
        return animeRepository.existsByTitle(title);
    }

    //30.检查原标题是否存在
    public Boolean existsByOriginalTitle(String originalTitle) {
        if (StringUtils.isBlank(originalTitle)) {
            throw new IllegalArgumentException("原标题不能为空");
        }
        return animeRepository.existsByOriginalTitle(originalTitle);
    }

    //31.获取该类型所有动漫
    public List<Anime> getAnimeByGenreId(UUID genreId) {
        if (genreId == null) {
            throw new IllegalArgumentException("类型不能为空");
        }
        return animeRepository.findByGenreId(genreId);
    }

    //32.批量删除
    public void deleteAnimeInBatch(List<UUID> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("id集合不能为空");
        }
        animeRepository.deleteByIdIn(ids);
    }

    //33.评分过滤（高于多少 低于多少 评分区间 降序排列） filterByScore(BigDecimal, BigDecimal, String)
    public List<Anime> filterByScore(BigDecimal minScore, BigDecimal maxScore) {
        if (minScore == null || maxScore == null) {
            return animeRepository.findAll();
        }
        if (minScore != null && maxScore == null) {
            return animeRepository.findByExternalScoreGreaterThanEqual(minScore);
        }
        if (minScore == null && maxScore != null) {
            return animeRepository.findByExternalScoreBetween(BigDecimal.ZERO, maxScore);
        }
        return animeRepository.findByExternalScoreBetween(minScore, maxScore);
    }

    //34.个性化过滤 customFilter(AnimeFilter)
    public List<Anime> customFilter(AdultFilter filter, Boolean isOfficial) {
        List<Anime> results = switch (filter) {
            case ONLY_ADULT -> animeRepository.findByIsAdult(true);
            case EXCLUDE_ADULT -> animeRepository.findByIsAdult(false);
            case INCLUDE_ADULT -> animeRepository.findAll();
        };

        if (isOfficial != null) {
            results = results.stream()
                    .filter(a -> isOfficial.equals(a.getIsOfficial()))
                    .toList();
        }
        return results;
    }

    //35.评分区间数量
    public long countByScoreRange(BigDecimal minScore, BigDecimal maxScore) {
        List<Anime> results = filterByScore(minScore, maxScore);
        return results.size();
    }

    //36.按季度统计数量 countBySeasonGrouped()
    public Map<String, Long> countBySeasonGrouped() {
        List<Object[]> results = animeRepository.countBySeasonGrouped();
        Map<String, Long> result = new HashMap<>();
        for (Object[] obj : results) {
            result.put((String) obj[0], (Long) obj[1]);
        }
        return result;
    }

    //37.评分top getTopRatedAnime(int)
    public List<Anime> getTopRatedAnime(int topCount) {
        if (topCount < 1) {
            throw new IllegalArgumentException("topCount必须大于0");
        }
        if (topCount > animeRepository.count()) {
            throw new IllegalArgumentException("topCount必须小于动漫总数");
        }
        return animeRepository.findTopRated(PageRequest.of(0, topCount));
    }


}


