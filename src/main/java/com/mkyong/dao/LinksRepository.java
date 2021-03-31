package com.mkyong.dao;

import com.mkyong.model.Knowledgepool;
import com.mkyong.model.LinksLabels;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface LinksRepository extends PagingAndSortingRepository<LinksLabels, String> {
    Page<LinksLabels> findAll(Pageable pageable);

    List<LinksLabels> findAllByParenttype(Short parenttype);

    List<LinksLabels> findAllByItemid(String parentId);

    @Override
    List<LinksLabels> findAll();

}
