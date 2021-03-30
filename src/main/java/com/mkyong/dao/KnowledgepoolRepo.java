package com.mkyong.dao;

import com.mkyong.model.Knowledgepool;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface KnowledgepoolRepo extends CrudRepository<Knowledgepool, String> {
    @Override
    List<Knowledgepool> findAll();
}
