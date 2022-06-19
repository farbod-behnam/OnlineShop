package com.OnlineShop.seeders;

import com.OnlineShop.entity.*;
import com.OnlineShop.entity.order.Order;
import com.OnlineShop.entity.order.OrderItem;
import com.OnlineShop.enums.CategoryEnum;
import com.OnlineShop.enums.CountryEnum;
import com.OnlineShop.enums.RoleEnum;
import com.OnlineShop.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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
    private final IOrderRepository orderRepository;
    private final IProductRepository productRepository;
    private final ICategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;

    // local variables
    private AppUser johnWickUser;
    private AppUser bruceLeeUser;

    private Product bloodborne;
    private Product devilMayCry5;
    private Product returnal;
    private Product LGC1TV;
    private Product ps5;
    private Product lenovoLegion7;
    private Product iphone13;

    @Autowired
    public SeedDatabase(IUserRepository userRepository, IRoleRepository roleRepository, ICountryRepository countryRepository, IOrderRepository orderRepository, IProductRepository productRepository, ICategoryRepository categoryRepository, PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.countryRepository = countryRepository;
        this.orderRepository = orderRepository;
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

        createOrder();

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
        this.johnWickUser = new AppUser(
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

        userRepository.save(this.johnWickUser);

        this.bruceLeeUser = new AppUser(
                null,
                "Bruce",
                "Lee",
                "0016666666666",
                "bruce.lee@gmail.com",
                userRoleList,
                "bruce",
                passwordEncoder.encode("1234"),
                usa,
                "Aaron Hawkins 5587 Nunc. Avenue Erie Rhode Island 24975",
                LocalDateTime.now(),
                LocalDateTime.now()
        );


        userRepository.save(this.bruceLeeUser);


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

       this.bloodborne = new Product(
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

       this.devilMayCry5 = new Product(
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

        this.returnal = new Product(
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

        this.LGC1TV = new Product(
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

        this.ps5 = new Product(
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

        this.lenovoLegion7 = new Product(
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

       this.iphone13 = new Product(
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

    private void createOrder()
    {
        OrderItem orderItem1 = new OrderItem(this.bloodborne, 2);
        OrderItem orderItem2 = new OrderItem(this.devilMayCry5, 4);


        Order order1 = new Order(
                null,
                Arrays.asList(orderItem1, orderItem2),
                this.johnWickUser,
                false
        );

        orderRepository.save(order1);

        OrderItem orderItem3 = new OrderItem(this.returnal, 1);
        OrderItem orderItem4 = new OrderItem(this.LGC1TV, 1);


        Order order2 = new Order(
                null,
                Arrays.asList(orderItem3, orderItem4),
                this.johnWickUser,
                false
        );

        orderRepository.save(order2);

        OrderItem orderItem5 = new OrderItem(this.lenovoLegion7, 1);

        Order order3 = new Order(
                null,
                List.of(orderItem5),
                this.johnWickUser,
                false
        );

        orderRepository.save(order3);


        OrderItem orderItem6 = new OrderItem(this.ps5, 1);
        OrderItem orderItem7 = new OrderItem(this.devilMayCry5, 1);

        Order order4 = new Order(
                null,
                Arrays.asList(orderItem6, orderItem7),
                this.bruceLeeUser,
                false
        );

        orderRepository.save(order4);


        OrderItem orderItem8 = new OrderItem(this.iphone13, 1);

        Order order5 = new Order(
                null,
                List.of(orderItem8),
                this.bruceLeeUser,
                false
        );

        orderRepository.save(order5);

        OrderItem orderItem9 = new OrderItem(this.LGC1TV, 1);

        Order order6 = new Order(
                null,
                List.of(orderItem9),
                this.bruceLeeUser,
                false
        );

        orderRepository.save(order6);
    }

    private void deleteAllTable()
    {
        userRepository.deleteAll();
        roleRepository.deleteAll();
        orderRepository.deleteAll();
        countryRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();
    }
}
