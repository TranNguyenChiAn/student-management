package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.TableTime;
import com.mycompany.myapp.repository.TableTimeRepository;
import com.mycompany.myapp.service.TableTimeService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.TableTime}.
 */
@Service
@Transactional
public class TableTimeServiceImpl implements TableTimeService {

    private static final Logger log = LoggerFactory.getLogger(TableTimeServiceImpl.class);

    private final TableTimeRepository tableTimeRepository;

    public TableTimeServiceImpl(TableTimeRepository tableTimeRepository) {
        this.tableTimeRepository = tableTimeRepository;
    }

    @Override
    public TableTime save(TableTime tableTime) {
        log.debug("Request to save TableTime : {}", tableTime);
        return tableTimeRepository.save(tableTime);
    }

    @Override
    public TableTime update(TableTime tableTime) {
        log.debug("Request to update TableTime : {}", tableTime);
        return tableTimeRepository.save(tableTime);
    }

    @Override
    public Optional<TableTime> partialUpdate(TableTime tableTime) {
        log.debug("Request to partially update TableTime : {}", tableTime);

        return tableTimeRepository.findById(tableTime.getId()).map(tableTimeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TableTime> findAll(Pageable pageable) {
        log.debug("Request to get all TableTimes");
        return tableTimeRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TableTime> findOne(Long id) {
        log.debug("Request to get TableTime : {}", id);
        return tableTimeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TableTime : {}", id);
        tableTimeRepository.deleteById(id);
    }
}
