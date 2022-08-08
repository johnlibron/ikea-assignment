package com.ikea.warehouseapp;

import com.ikea.warehouseapp.data.dao.ArticleRepository;
import com.ikea.warehouseapp.data.dao.InventoryRepository;
import com.ikea.warehouseapp.data.dao.ProductRepository;
import com.ikea.warehouseapp.data.json.object.ArticleJsonObject;
import com.ikea.warehouseapp.data.json.object.InventoryJsonObject;
import com.ikea.warehouseapp.data.json.object.ProductJsonObject;
import com.ikea.warehouseapp.data.model.Article;
import com.ikea.warehouseapp.data.model.Inventory;
import com.ikea.warehouseapp.data.model.Product;
import com.ikea.warehouseapp.util.FileReaderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class WarehouseApplication implements CommandLineRunner {

	private static String productsJsonFile = "test-products.json";

	private static String inventoryJsonFile = "test-inventory.json";

	private final ProductRepository productRepository;

	private final ArticleRepository articleRepository;

	private final InventoryRepository inventoryRepository;

	@Autowired
	public WarehouseApplication(ProductRepository productRepository, ArticleRepository articleRepository, InventoryRepository inventoryRepository) {
		this.productRepository = productRepository;
		this.articleRepository = articleRepository;
		this.inventoryRepository = inventoryRepository;
	}

	public static void main(String[] args) {
//		if (args.length == 2) {
//			productsJsonFile = args[0];
//			inventoryJsonFile = args[1];
//		}
		SpringApplication.run(WarehouseApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		saveProducts(productsJsonFile);
//		saveInventory(inventoryJsonFile);
	}

	@Transactional
	void saveProducts(String filename) throws IOException {
		Set<Article> allArticles = new HashSet<>();
		for (ProductJsonObject productJsonObject : FileReaderUtils.getProducts(filename)) {
			Product product = new Product();
			product.setName(productJsonObject.getName());
			product.setPrice(productJsonObject.getPrice());
			List<Article> articles = new ArrayList<>();
			for (ArticleJsonObject articleJsonObject : productJsonObject.getArticles()) {
//				articles.add(new Article(
//					Long.parseLong(articleJsonObject.getAmountOf()),
//					Long.parseLong(articleJsonObject.getArticleId()),
//					product
//				));
			}
			allArticles.addAll(articles);
			product.setArticles(articles);
			productRepository.save(product);
		}
		articleRepository.saveAll(allArticles);
	}

	@Transactional
	void saveInventory(String filename) throws IOException {
		List<Inventory> inventoryList = new ArrayList<>();
		for (InventoryJsonObject inventoryJsonObject : FileReaderUtils.getInventory(filename)) {
			inventoryList.add(new Inventory(
					Long.parseLong(inventoryJsonObject.getArticleId()),
					inventoryJsonObject.getName(),
					Long.parseLong(inventoryJsonObject.getStock())
			));
		}
		inventoryRepository.saveAll(inventoryList);
	}
}
