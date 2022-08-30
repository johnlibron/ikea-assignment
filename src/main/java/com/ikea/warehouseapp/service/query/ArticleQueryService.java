package com.ikea.warehouseapp.service.query;

import com.ikea.warehouseapp.data.dao.ArticleRepository;
import com.ikea.warehouseapp.data.model.Inventory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ArticleQueryService {

    private ArticleRepository articleRepository;

    public List<Inventory> findByArticleIdIn(List<String> articleIds) {
        return articleRepository.findByArticleIdIn(articleIds);
    }
}
