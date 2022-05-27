package com.OnlineShop;

import com.OnlineShop.dto.request.AppUserRequest;
import com.OnlineShop.entity.AppRole;
import com.OnlineShop.entity.Country;
import com.OnlineShop.enums.CountryEnum;
import com.OnlineShop.enums.RoleEnum;
import com.OnlineShop.service.ICategoryService;
import com.OnlineShop.service.ICountryService;
import com.OnlineShop.service.IRoleService;
import com.OnlineShop.service.IUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class OnlineShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineShopApplication.class, args);
	}

	@Bean
	CommandLineRunner runner (IRoleService roleService, IUserService userService ,ICountryService countryService, ICategoryService categoryService)
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

			germany = countryService.createCountry(germany);
			usa = countryService.createCountry(usa);

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
		};
	}
}
