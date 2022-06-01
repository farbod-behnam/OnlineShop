package com.OnlineShop.seeders;

import com.OnlineShop.entity.*;
import com.OnlineShop.enums.CategoryEnum;
import com.OnlineShop.enums.CountryEnum;
import com.OnlineShop.enums.RoleEnum;
import com.OnlineShop.repository.*;
import com.OnlineShop.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A class that seeds the database when the application starts
 */
@Component
public class SeedDatabase
{
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final ICountryRepository countryRepository;
    private final IProductRepository productRepository;
    private final ICategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SeedDatabase(IRoleService roleService, IUserService userService, ICountryService countryService, ICategoryService categoryService, IProductService productService, IUserRepository userRepository, IRoleRepository roleRepository, ICountryRepository countryRepository, IProductRepository productRepository, ICategoryRepository categoryRepository, PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.countryRepository = countryRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @EventListener
    public void seed(ContextRefreshedEvent event)
    {

        deleteAllTable();

        createUsers();

        createProducts();

    }

    private void createUsers()
    {

        // role
        AppRole roleUser = new AppRole(null, RoleEnum.ROLE_USER.name());
        AppRole roleAdmin = new AppRole(null, RoleEnum.ROLE_ADMIN.name());

        roleUser = roleRepository.save(roleUser);
        roleAdmin = roleRepository.save(roleAdmin);

        // country
        Country germany = new Country(null, CountryEnum.Germany.toString());
        Country usa = new Country(null, CountryEnum.USA.toString());
        Country uk = new Country(null, CountryEnum.UK.toString());

        germany = countryRepository.save(germany);
        usa = countryRepository.save(usa);
        uk = countryRepository.save(uk);

        Set<AppRole> userRoleList = new HashSet<>();
        userRoleList.add(roleUser);

        //
        AppUser user = new AppUser(
                null,
                "John",
                "Wick",
                "0016666666666",
                "john.wick@gmail.com",
                userRoleList,
                "john", // j.wick
                passwordEncoder.encode("1234"),
                germany,
                "Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522 (257) 563-7401",
                LocalDateTime.now(),
                LocalDateTime.now()
        );


        userRepository.save(user);


        Set<AppRole> adminRoleList = new HashSet<>();
        adminRoleList.add(roleAdmin);

        AppUser admin = new AppUser(
                null,
                "Arnold",
                "Schwarzenegger",
                "00112345667910",
                "arnold.schwarzenegger@gmail.com",
                adminRoleList,
                "arnold", // j.wick
                passwordEncoder.encode("1234"),
                germany,
                "Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522 (257) 563-7401",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        userRepository.save(admin);
    }

    private void createProducts()
    {

        Category digitalDevices = new Category(null, CategoryEnum.digital_devices.toString());
        Category videoGames = new Category(null, CategoryEnum.video_games.toString());
        Category clothes = new Category(null, CategoryEnum.clothes.toString());

        digitalDevices = categoryRepository.save(digitalDevices);
        videoGames = categoryRepository.save(videoGames);
        clothes = categoryRepository.save(clothes);

        Product bloodborne = new Product(
                null,
                "Bloodborne",
                "a souls like game by from software company",
                new BigDecimal("69.99"),
                19,
                "https://image_url",
                videoGames,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        productRepository.save(bloodborne);

        Product devilMayCry5 = new Product(
                null,
                "Devil May Cry 5 Special Edition",
                "An Action beatem up and the fifth installment of popular devil may cry series",
                new BigDecimal("69.99"),
                95,
                "https://image_url",
                videoGames,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        productRepository.save(devilMayCry5);

        Product returnal = new Product(
                null,
                "Returnal",
                "A rogue like experience and in style of metroid game",
                new BigDecimal("69.99"),
                95,
                "https://image_url",
                videoGames,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        productRepository.save(returnal);

        Product LGC1TV = new Product(
                null,
                "LG C1",
                "An OLED TV produced by LG company",
                new BigDecimal("999.99"),
                20,
                "https://image_url",
                digitalDevices,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        productRepository.save(LGC1TV);

        Product ps5 = new Product(
                null,
                "PlayStation 5 standard edition",
                "The fifth generation of console from Sony",
                new BigDecimal("499.99"),
                20,
                "https://image_url",
                digitalDevices,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        productRepository.save(ps5);

        Product lenovoLegion7 = new Product(
                null,
                "Lenovo Legion Slim 7",
                "The slim version of lenovo legion 7",
                new BigDecimal("1799.99"),
                20,
                "https://image_url",
                digitalDevices,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        productRepository.save(lenovoLegion7);

        Product iphone13 = new Product(
                null,
                "Iphone 13",
                "The 13 iteration of apple iphones",
                new BigDecimal("699.99"),
                20,
                "https://image_url",
                digitalDevices,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        productRepository.save(iphone13);
    }

    private void deleteAllTable()
    {
        userRepository.deleteAll();
        roleRepository.deleteAll();
        countryRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();
    }
}
