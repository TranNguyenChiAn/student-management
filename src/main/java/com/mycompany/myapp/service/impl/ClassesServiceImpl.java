package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Classes;
import com.mycompany.myapp.repository.ClassesRepository;
import com.mycompany.myapp.service.ClassesService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Classes}.
 */
@Service
@Transactional
public class ClassesServiceImpl implements ClassesService {

    private static final Logger log = LoggerFactory.getLogger(ClassesServiceImpl.class);

    private final ClassesRepository classesRepository;

    public ClassesServiceImpl(ClassesRepository classesRepository) {
        this.classesRepository = classesRepository;
    }

    @Override
    public Classes save(Classes classes) {
        log.debug("Request to save Classes : {}", classes);
        return classesRepository.save(classes);
    }

    @Override
    public Classes update(Classes classes) {
        log.debug("Request to update Classes : {}", classes);
        return classesRepository.save(classes);
    }

    @Override
    public Optional<Classes> partialUpdate(Classes classes) {
        log.debug("Request to partially update Classes : {}", classes);

        return classesRepository
            .findById(classes.getId())
            .map(existingClasses -> {
                if (classes.getClassName() != null) {
                    existingClasses.setClassName(classes.getClassName());
                }

                return existingClasses;
            })
            .map(classesRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Classes> findAll(Pageable pageable) {
        log.debug("Request to get all Classes");
        return classesRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Classes> findOne(Long id) {
        log.debug("Request to get Classes : {}", id);
        return classesRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Classes : {}", id);
        classesRepository.deleteById(id);
    }
}
