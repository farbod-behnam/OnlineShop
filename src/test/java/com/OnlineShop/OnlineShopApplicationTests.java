package com.OnlineShop;

import com.OnlineShop.entity.Product;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OnlineShopApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;



	@Test
	@Disabled
	void contextLoads() {
	}


	@Test
	public void getProduct_returnsProductDetails()
	{
		// given

		// when
		ResponseEntity<Product> response= restTemplate.getForEntity("/api/products/1", Product.class);

		// then
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(Objects.requireNonNull(response.getBody()).getName()).isEqualTo("Bloodborne");
		assertThat(Objects.requireNonNull(response.getBody()).getCategory().name()).isEqualTo("Video_Games");
	}

}
