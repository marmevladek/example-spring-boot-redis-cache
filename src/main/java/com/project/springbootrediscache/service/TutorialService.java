package com.project.springbootrediscache.service;

import com.project.springbootrediscache.model.Tutorial;
import com.project.springbootrediscache.repository.TutorialRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@EnableCaching
@Log4j2
public class TutorialService {

    @Autowired
    private TutorialRepository tutorialRepository;

    @Cacheable("tutorials")
    public List<Tutorial> findAll() {
        doLongRunningTask();

        return tutorialRepository.findAll();
    };

    @Cacheable("tutorials")
    public List<Tutorial> findByTitleContaining(String title) {
        return tutorialRepository.findByTitleContaining(title);
    };

    @Cacheable("tutorial")
    public Optional<Tutorial> findById(Long id) {
        doLongRunningTask();

        return tutorialRepository.findById(id);
    };

    @Cacheable("published_tutorials")
    public List<Tutorial> findByPublished(boolean isPublished) {
        doLongRunningTask();

        return tutorialRepository.findByPublished(isPublished);
    };

    @CacheEvict(value = "tutorial", key="#tutorial.id")
    public Tutorial update(Tutorial tutorial) {
        return tutorialRepository.save(tutorial);
    }

    @CacheEvict(value = "tutorial", key = "#id")
    public void deleteById(Long id) {
        tutorialRepository.deleteById(id);
    }

    @CacheEvict(value = { "tutorial", "tutorials", "published_tutorials" }, allEntries = true)
    public void deleteAll() {
        tutorialRepository.deleteAll();
    }

    public Tutorial save(Tutorial tutorial) {
        return tutorialRepository.save(tutorial);
    }


    private void doLongRunningTask() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            log.error("Error occurred: {}", e.getMessage());
        }

    }
}
