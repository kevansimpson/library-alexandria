package com.aravo.library.service;

import com.aravo.library.data.entity.Work;
import com.aravo.library.data.repository.WorkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibraryService implements WorkService {
    private final WorkRepository workRepository;

    @Autowired
    public LibraryService(WorkRepository repository) {
        workRepository = repository;
    }

    @Override
    public Work createWork(Work work) {
        return workRepository.save(work);
    }

    @Override
    public List<Work> fetchWorks() {
        return workRepository.findAll();
    }

    @Override
    public Work findWorkById(long id) {
        return workRepository.findById(id);
    }

    @Override
    public Work updateWork(Work work) {
        return workRepository.save(work);
    }

    @Override
    public Work deleteWork(Work work) {
        workRepository.delete(work);
        return findWorkById(work.getId());
    }
}
