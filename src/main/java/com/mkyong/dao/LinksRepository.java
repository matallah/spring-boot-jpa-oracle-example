package com.mkyong.dao;

import com.mkyong.model.LinksLabels;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LinksRepository extends CrudRepository<LinksLabels, String> {
    List<LinksLabels> findTop100By();
}
