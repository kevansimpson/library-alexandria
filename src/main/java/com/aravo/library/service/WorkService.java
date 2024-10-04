package com.aravo.library.service;

import com.aravo.library.data.entity.Work;

import java.util.List;

public interface WorkService {
    Work createWork(Work work);
    List<Work> fetchWorks();
    Work findWorkById(long id);
    Work updateWork(Work work);
    Work deleteWork(long id);
}
