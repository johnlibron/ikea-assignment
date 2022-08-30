package com.ikea.warehouseapp.service.impl;

import com.ikea.warehouseapp.data.dao.ArticleRepository;
import com.ikea.warehouseapp.data.dto.ArticleDto;
import com.ikea.warehouseapp.data.dto.ArticleIncomingDto;
import com.ikea.warehouseapp.data.model.Article;
import com.ikea.warehouseapp.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private static final String INVENTORY_UPDATED_LOG = "Inventory: {} was updated";

    private final ArticleRepository articleRepository;

    @Transactional
    @Override
    public ArticleDto addInventory(ArticleIncomingDto articleIncomingDto) {
        Article article = new Article();
        try {
            BeanUtils.copyProperties(articleIncomingDto, article);
            Optional<Article> optionalInventory = articleRepository.findByName(article.getName());
            if (optionalInventory.isPresent()) {
                return null;
            }
            article = articleRepository.save(article);
        } catch(Exception e) {
            System.out.println(e.toString());
        }
        ArticleDto articleDto = new ArticleDto();
        BeanUtils.copyProperties(article, articleDto);
        return articleDto;
    }

    @Transactional
    @Override
    public void importInventory(String pathname) throws IOException {
        // TODO - Add batch insert support, check deadlock scenario, and add logs
        // TODO - Check duplicate inventories (article id, name)
//		inventoryRepository.saveAll(jsonParserService.getInventory(pathname));
    }

    @Override
    public boolean checkExistingInventory(List<String> articleIds) {
        return articleRepository.existsByArticleIdIn(articleIds);
    }
}
