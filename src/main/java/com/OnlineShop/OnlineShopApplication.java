package com.OnlineShop;

import com.OnlineShop.dto.request.AppUserRequest;
import com.OnlineShop.dto.request.ProductRequest;
import com.OnlineShop.entity.AppRole;
import com.OnlineShop.entity.Category;
import com.OnlineShop.entity.Country;
import com.OnlineShop.enums.CategoryEnum;
import com.OnlineShop.enums.CountryEnum;
import com.OnlineShop.enums.RoleEnum;
import com.OnlineShop.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class OnlineShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineShopApplication.class, args);
	}

	@Bean
	CommandLineRunner runner (IRoleService roleService, IUserService userService , ICountryService countryService, ICategoryService categoryService, IProductService productService)
	{
		return args ->
		{
			// role
			AppRole roleUser = new AppRole(null, RoleEnum.ROLE_USER.name());
			AppRole roleAdmin = new AppRole(null, RoleEnum.ROLE_ADMIN.name());

			roleUser = roleService.createRole(roleUser);
			roleAdmin = roleService.createRole(roleAdmin);

			// country
			Country germany = new Country(null, CountryEnum.Germany.toString());
			Country usa = new Country(null, CountryEnum.USA.toString());
			Country uk = new Country(null, CountryEnum.UK.toString());

			germany = countryService.createCountry(germany);
			usa = countryService.createCountry(usa);
			uk = countryService.createCountry(uk);

			List<String> userRoleList = new ArrayList<>();
			userRoleList.add(roleUser.getId());

			//
			AppUserRequest user = new AppUserRequest(
					"19",
					"John",
					"Wick",
					"0016666666666",
					"john.wick@gmail.com",
					userRoleList,
					"john", // j.wick
					"1234",
					germany.getId(),
					"Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522 (257) 563-7401"
			);

			userService.createUser(user);

			List<String> adminRoleList = new ArrayList<>();
			adminRoleList.add(roleAdmin.getId());

			AppUserRequest admin = new AppUserRequest(
					"19",
					"Arnold",
					"Schwarzenegger",
					"00112345667910",
					"arnold.schwarzenegger@gmail.com",
					adminRoleList,
					"arnold", // j.wick
					"1234",
					germany.getId(),
					"Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522 (257) 563-7401"
			);

			userService.createUser(admin);

			Category digitalDevices = new Category(null, CategoryEnum.digital_devices.toString());
			Category videoGames = new Category(null, CategoryEnum.video_games.toString());
			Category clothes = new Category(null, CategoryEnum.clothes.toString());

			digitalDevices = categoryService.createCategory(digitalDevices);
			videoGames = categoryService.createCategory(videoGames);
			clothes = categoryService.createCategory(clothes);

			ProductRequest bloodborne = new ProductRequest(
					null,
					"Bloodborne",
					"a souls like game by from software company",
					new BigDecimal("69.99"),
					19,
					"https://image_url",
					videoGames.getId(),
					true
			);

			productService.createProduct(bloodborne);

			ProductRequest devilMayCry5 = new ProductRequest(
					null,
					"Devil May Cry 5 Special Edition",
					"An Action beatem up and the fifth installment of popular devil may cry series",
					new BigDecimal("69.99"),
					95,
					"https://image_url",
					videoGames.getId(),
					true
			);

			productService.createProduct(devilMayCry5);

			ProductRequest returnal = new ProductRequest(
					null,
					"Returnal",
					"A rogue like experience and in style of metroid game",
					new BigDecimal("69.99"),
					95,
					"https://image_url",
					videoGames.getId(),
					true
			);

			productService.createProduct(returnal);

			ProductRequest LGC1TV = new ProductRequest(
					null,
					"LG C1",
					"An OLED TV produced by LG company",
					new BigDecimal("999.99"),
					20,
					"https://image_url",
					digitalDevices.getId(),
					true
			);

			productService.createProduct(LGC1TV);

			ProductRequest ps5 = new ProductRequest(
					null,
					"PlayStation 5 standard edition",
					"The fifth generation of console from Sony",
					new BigDecimal("499.99"),
					20,
					"https://image_url",
					digitalDevices.getId(),
					true
			);

			productService.createProduct(ps5);

			ProductRequest lenovoLegion7 = new ProductRequest(
					null,
					"Lenovo Legion Slim 7",
					"The slim version of lenovo legion 7",
					new BigDecimal("1799.99"),
					20,
					"https://image_url",
					digitalDevices.getId(),
					true
			);

			productService.createProduct(lenovoLegion7);

			ProductRequest iphone13 = new ProductRequest(
					null,
					"Iphone 13",
					"The 13 iteration of apple iphones",
					new BigDecimal("699.99"),
					20,
					"https://image_url",
					digitalDevices.getId(),
					true
			);

			productService.createProduct(iphone13);
		};
	}
}
