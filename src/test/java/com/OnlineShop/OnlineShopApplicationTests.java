package com.OnlineShop;

import com.OnlineShop.controller.CategoryController;
import com.OnlineShop.controller.ProductController;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OnlineShopApplicationTests {

	@Autowired
	private CategoryController categoryController;

	@Autowired
	private ProductController productController;


	@Test
	void contextLoads()
	{
		assertThat(categoryController).isNotNull();
		assertThat(productController).isNotNull();
	}

}
