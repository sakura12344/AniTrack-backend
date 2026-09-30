package com.anitrack.anitrack.service;

import com.anitrack.anitrack.entity.Anime;
import com.anitrack.anitrack.entity.Genre;
import com.anitrack.anitrack.repository.AnimeRepository;
import com.anitrack.anitrack.repository.GenreRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GenreService {
    private final GenreRepository genreRepository;
    private final AnimeRepository animeRepository;

    public GenreService(GenreRepository genreRepository, AnimeRepository animeRepository) {
        this.genreRepository = genreRepository;
        this.animeRepository = animeRepository;
    }

    //7。检查该类型是否已经存在
    public boolean existsGenre(String name) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("名称不能为空");
        }
        return genreRepository.existsByName(name);
    }

    //1.查询所有类别
    public long conutGenre() {
        return genreRepository.count();
    }

    //2.增加类别
    public Genre addGenre(Genre genre) {
        if (genreRepository.existsByName(genre.getName())) {
            throw new IllegalArgumentException("该类别已存在");
        }
        return genreRepository.save(genre);
    }

    //9.批量新增（不重复）
    public List<Genre> addGenres(List<Genre> genres) {
        return genres.stream()
                .filter(genre -> !genreRepository.existsByName(genre.getName()))
                .map(genreRepository::save)
                .toList();
    }

    //根据id搜索
    public Genre getGenreById(UUID id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("该类别不存在"));
    }

    //11.根据id集合批量查询
    public List<Genre> getGenresByIds(List<UUID> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("ID集合不能为空");
        }
        return genreRepository.findAllById(ids);
    }

    //3.删除类别
    public void deleteGenre(UUID id) {
        Genre genre = getGenreById(id);
        genreRepository.delete(genre);
    }


    //4.更改类别
    public Genre updateGenre(UUID id, Genre genre) {
        Genre newgenre = getGenreById(id);
        newgenre.setName(genre.getName());
        newgenre.setSlug(genre.getSlug());
        return genreRepository.save(newgenre);
    }

    //5.获取该类型的所有动漫
    public List<Anime> getAnimeByGenreId(UUID id) {
        if (!genreRepository.existsById(id)) {
            throw new IllegalArgumentException("该类型不存在");
        }
        return animeRepository.findByGenreId(id);
    }

    //6.统计该类型动漫的数量
    public Long countAnimeByGenre(UUID id) {
        if (!genreRepository.existsById(id)) {
            throw new IllegalArgumentException("该类型不存在");
        }
        return animeRepository.countAnimeByGenreId(id);
    }


}