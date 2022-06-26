package com.OnlineShop.controller;

import com.OnlineShop.dto.request.order.OrderItemRequest;
import com.OnlineShop.dto.request.order.OrderRequest;
import com.OnlineShop.entity.*;
import com.OnlineShop.entity.order.Order;
import com.OnlineShop.entity.order.OrderItem;
import com.OnlineShop.enums.CountryEnum;
import com.OnlineShop.enums.OrderStatusEnum;
import com.OnlineShop.enums.RoleEnum;
import com.OnlineShop.security.service.ITokenService;
import com.OnlineShop.service.IOrderService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = OrderController.class)
//@Import(SecurityConfig.class)
class OrderControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IOrderService orderService;

    // need to be mocked because SecurityConfig.class injects
    // these two services ( UserDetailsService, ITokenService ) into itself
    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private ITokenService tokenService;


    @BeforeEach
    void setUp()
    {

        // -----------------------------------------------------
        // for comparison of BigDecimal this config is necessary
        // -----------------------------------------------------

        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);

        Configuration.setDefaults(new Configuration.Defaults() {

            private final JsonProvider jsonProvider = new JacksonJsonProvider(objectMapper);
            private final MappingProvider mappingProvider = new JacksonMappingProvider(objectMapper);

            @Override
            public JsonProvider jsonProvider() {
                return jsonProvider;
            }

            @Override
            public MappingProvider mappingProvider() {
                return mappingProvider;
            }

            @Override
            public Set<Option> options() {
                return EnumSet.noneOf(Option.class);
            }
        });
        // -----------------------------------------------------
        // end of configuration
        // -----------------------------------------------------

    }


    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void getOrders_authorizedByAdmin_shouldReturnOrders() throws Exception
    {
        // given

        // order
        BigDecimal price = new BigDecimal("69.99");
        Category category = new Category("11", "Video Games");

        Product product1 = new Product(
                "18",
                "Bloodborne",
                "A souls like game",
                price,
                19,
                "http://image_url",
                category,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Product product2 = new Product(
                "19",
                "The Last of Us",
                "A narrative game with action sequences",
                price,
                19,
                "http://image_url",
                category,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        OrderItem item1 = new OrderItem(product1, 9);
        OrderItem item2 = new OrderItem(product2, 2);

        List<OrderItem> orderItemList1 = new ArrayList<>();
        orderItemList1.add(item1);
        orderItemList1.add(item2);


        // user
        Set<AppRole> roles = new HashSet<>();

        Country country = new Country("10", CountryEnum.Germany.name());
        AppRole role = new AppRole("11", RoleEnum.ROLE_USER.name());

        roles.add(role);

        AppUser user = new AppUser(
                "19",
                "John",
                "Wick",
                "001666666666",
                "john.wick@gmail.com",
                roles,
                "john.wick",
                "Password!1234",
                country,
                "This is an address",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Order order1 = new Order(
                "11",
                orderItemList1,
                user,
                OrderStatusEnum.IN_PROGRESS.name()
        );


        OrderItem item3 = new OrderItem(product2, 19);

        List<OrderItem> orderItemList2 = new ArrayList<>();
        orderItemList2.add(item3);

        Order order2 = new Order(
               "12",
                orderItemList2,
                user,
                OrderStatusEnum.IN_PROGRESS.name()
        );

        List<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);


        // when
        given(orderService.getOrders()).willReturn(orders);


        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(orders.size()))

                .andExpect(jsonPath("$[0].id").value(equalTo("11")))
                .andExpect(jsonPath("$[0].orderItemList.length()").value(orderItemList1.size()))
                .andExpect(jsonPath("$[0].orderItemList[0].product.id").value(equalTo("18")))
                .andExpect(jsonPath("$[0].orderItemList[0].product.name").value(equalTo("Bloodborne")))
                .andExpect(jsonPath("$[0].orderItemList[0].product.price").value(equalTo(new BigDecimal("69.99"))))
                .andExpect(jsonPath("$[0].orderItemList[0].product.quantity").value(equalTo(19)))
                .andExpect(jsonPath("$[0].orderItemList[0].product.category.id").value(equalTo("11")))
                .andExpect(jsonPath("$[0].orderItemList[0].product.category.name").value(equalTo("Video Games")))
                .andExpect(jsonPath("$[0].orderItemList[0].product.active").value(equalTo(true)))
                .andExpect(jsonPath("$[0].orderItemList[0].quantity").value(equalTo(9)))

                .andExpect(jsonPath("$[0].orderItemList[1].product.id").value(equalTo("19")))
                .andExpect(jsonPath("$[0].orderItemList[1].product.name").value(equalTo("The Last of Us")))
                .andExpect(jsonPath("$[0].orderItemList[1].product.price").value(equalTo(new BigDecimal("69.99"))))
                .andExpect(jsonPath("$[0].orderItemList[1].product.quantity").value(equalTo(19)))
                .andExpect(jsonPath("$[0].orderItemList[1].product.category.id").value(equalTo("11")))
                .andExpect(jsonPath("$[0].orderItemList[1].product.category.name").value(equalTo("Video Games")))
                .andExpect(jsonPath("$[0].orderItemList[1].product.active").value(equalTo(true)))
                .andExpect(jsonPath("$[0].orderItemList[1].quantity").value(equalTo(2)))

                .andExpect(jsonPath("$[0].orderStatus").value(equalTo(OrderStatusEnum.IN_PROGRESS.name())))
                .andExpect(jsonPath("$[0].user.username").value(equalTo("john.wick")))
                .andExpect(jsonPath("$[0].totalPrice").value(equalTo(new BigDecimal("769.89"))))

                .andExpect(jsonPath("$[1].orderItemList.length()").value(orderItemList2.size()))
                .andExpect(jsonPath("$[1].orderItemList[0].product.id").value(equalTo("19")))
                .andExpect(jsonPath("$[1].orderItemList[0].product.name").value(equalTo("The Last of Us")))
                .andExpect(jsonPath("$[1].orderItemList[0].product.price").value(equalTo(new BigDecimal("69.99"))))
                .andExpect(jsonPath("$[1].orderItemList[0].product.quantity").value(equalTo(19)))
                .andExpect(jsonPath("$[1].orderItemList[0].product.category.id").value(equalTo("11")))
                .andExpect(jsonPath("$[1].orderItemList[0].product.category.name").value(equalTo("Video Games")))
                .andExpect(jsonPath("$[1].orderItemList[0].product.active").value(equalTo(true)))
                .andExpect(jsonPath("$[1].orderItemList[0].quantity").value(equalTo(19)))

                .andExpect(jsonPath("$[1].orderStatus").value(equalTo(OrderStatusEnum.IN_PROGRESS.name())))
                .andExpect(jsonPath("$[1].user.username").value(equalTo("john.wick")))
                .andExpect(jsonPath("$[1].totalPrice").value(equalTo(new BigDecimal("1329.81"))));

    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    void getOrders_shouldBeUnauthorizedByUser() throws Exception
    {
        // given

        // order
        BigDecimal price = new BigDecimal("69.99");
        Category category = new Category("11", "Video Games");

        Product product1 = new Product(
                "18",
                "Bloodborne",
                "A souls like game",
                price,
                19,
                "http://image_url",
                category,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Product product2 = new Product(
                "19",
                "The Last of Us",
                "A narrative game with action sequences",
                price,
                19,
                "http://image_url",
                category,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        OrderItem item1 = new OrderItem(product1, 9);
        OrderItem item2 = new OrderItem(product2, 2);

        List<OrderItem> orderItemList1 = new ArrayList<>();
        orderItemList1.add(item1);
        orderItemList1.add(item2);


        // user
        Set<AppRole> roles = new HashSet<>();

        Country country = new Country("10", CountryEnum.Germany.name());
        AppRole role = new AppRole("11", RoleEnum.ROLE_USER.name());

        roles.add(role);

        AppUser user = new AppUser(
                "19",
                "John",
                "Wick",
                "001666666666",
                "john.wick@gmail.com",
                roles,
                "john.wick",
                "Password!1234",
                country,
                "This is an address",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Order order1 = new Order(
                "11",
                orderItemList1,
                user,
                OrderStatusEnum.IN_PROGRESS.name()
        );


        OrderItem item3 = new OrderItem(product2, 19);

        List<OrderItem> orderItemList2 = new ArrayList<>();
        orderItemList2.add(item3);

        Order order2 = new Order(
                "12",
                orderItemList2,
                user,
                OrderStatusEnum.IN_PROGRESS.name()
        );

        List<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);


        // when
        given(orderService.getOrders()).willReturn(orders);


        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders"))
                .andExpect(status().isForbidden());

    }

    @Test
    void getOrders_shouldBeUnauthorized() throws Exception
    {
        // given

        // order
        BigDecimal price = new BigDecimal("69.99");
        Category category = new Category("11", "Video Games");

        Product product1 = new Product(
                "18",
                "Bloodborne",
                "A souls like game",
                price,
                19,
                "http://image_url",
                category,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Product product2 = new Product(
                "19",
                "The Last of Us",
                "A narrative game with action sequences",
                price,
                19,
                "http://image_url",
                category,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        OrderItem item1 = new OrderItem(product1, 9);
        OrderItem item2 = new OrderItem(product2, 2);

        List<OrderItem> orderItemList1 = new ArrayList<>();
        orderItemList1.add(item1);
        orderItemList1.add(item2);


        // user
        Set<AppRole> roles = new HashSet<>();

        Country country = new Country("10", CountryEnum.Germany.name());
        AppRole role = new AppRole("11", RoleEnum.ROLE_USER.name());

        roles.add(role);

        AppUser user = new AppUser(
                "19",
                "John",
                "Wick",
                "001666666666",
                "john.wick@gmail.com",
                roles,
                "john.wick",
                "Password!1234",
                country,
                "This is an address",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Order order1 = new Order(
                "11",
                orderItemList1,
                user,
                OrderStatusEnum.IN_PROGRESS.name()
        );


        OrderItem item3 = new OrderItem(product2, 19);

        List<OrderItem> orderItemList2 = new ArrayList<>();
        orderItemList2.add(item3);

        Order order2 = new Order(
                "12",
                orderItemList2,
                user,
                OrderStatusEnum.IN_PROGRESS.name()
        );

        List<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);


        // when
        given(orderService.getOrders()).willReturn(orders);


        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders"))
                .andExpect(status().isForbidden());

    }


    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void getOrder_authorizedByAdmin_shouldReturnAnOrder() throws Exception
    {
        // given

        // order
        BigDecimal price = new BigDecimal("69.99");
        Category category = new Category("11", "Video Games");

        Product product1 = new Product(
                "18",
                "Bloodborne",
                "A souls like game",
                price,
                19,
                "http://image_url",
                category,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Product product2 = new Product(
                "19",
                "The Last of Us",
                "A narrative game with action sequences",
                price,
                19,
                "http://image_url",
                category,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        OrderItem item1 = new OrderItem(product1, 9);
        OrderItem item2 = new OrderItem(product2, 2);

        List<OrderItem> orderItemList1 = new ArrayList<>();
        orderItemList1.add(item1);
        orderItemList1.add(item2);


        // user
        Set<AppRole> roles = new HashSet<>();

        Country country = new Country("10", CountryEnum.Germany.name());
        AppRole role = new AppRole("11", RoleEnum.ROLE_USER.name());

        roles.add(role);

        AppUser user = new AppUser(
                "19",
                "John",
                "Wick",
                "001666666666",
                "john.wick@gmail.com",
                roles,
                "john.wick",
                "Password!1234",
                country,
                "This is an address",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Order order = new Order(
                "11",
                orderItemList1,
                user,
                OrderStatusEnum.IN_PROGRESS.name()
        );



        // when
        given(orderService.getOrderById(anyString())).willReturn(order);


        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders" + "/19"))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.id").value(equalTo("11")))
                .andExpect(jsonPath("$.orderItemList.length()").value(orderItemList1.size()))
                .andExpect(jsonPath("$.orderItemList[0].product.id").value(equalTo("18")))
                .andExpect(jsonPath("$.orderItemList[0].product.name").value(equalTo("Bloodborne")))
                .andExpect(jsonPath("$.orderItemList[0].product.price").value(equalTo(new BigDecimal("69.99"))))
                .andExpect(jsonPath("$.orderItemList[0].product.quantity").value(equalTo(19)))
                .andExpect(jsonPath("$.orderItemList[0].product.category.id").value(equalTo("11")))
                .andExpect(jsonPath("$.orderItemList[0].product.category.name").value(equalTo("Video Games")))
                .andExpect(jsonPath("$.orderItemList[0].product.active").value(equalTo(true)))
                .andExpect(jsonPath("$.orderItemList[0].quantity").value(equalTo(9)))

                .andExpect(jsonPath("$.orderItemList[1].product.id").value(equalTo("19")))
                .andExpect(jsonPath("$.orderItemList[1].product.name").value(equalTo("The Last of Us")))
                .andExpect(jsonPath("$.orderItemList[1].product.price").value(equalTo(new BigDecimal("69.99"))))
                .andExpect(jsonPath("$.orderItemList[1].product.quantity").value(equalTo(19)))
                .andExpect(jsonPath("$.orderItemList[1].product.category.id").value(equalTo("11")))
                .andExpect(jsonPath("$.orderItemList[1].product.category.name").value(equalTo("Video Games")))
                .andExpect(jsonPath("$.orderItemList[1].product.active").value(equalTo(true)))
                .andExpect(jsonPath("$.orderItemList[1].quantity").value(equalTo(2)))

                .andExpect(jsonPath("$.orderStatus").value(equalTo(OrderStatusEnum.IN_PROGRESS.name())))
                .andExpect(jsonPath("$.user.username").value(equalTo("john.wick")))
                .andExpect(jsonPath("$.totalPrice").value(equalTo(new BigDecimal("769.89"))));


    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    void getOrder_shouldBeUnauthorizedByUser() throws Exception
    {
        // given

        // order
        BigDecimal price = new BigDecimal("69.99");
        Category category = new Category("11", "Video Games");

        Product product1 = new Product(
                "18",
                "Bloodborne",
                "A souls like game",
                price,
                19,
                "http://image_url",
                category,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Product product2 = new Product(
                "19",
                "The Last of Us",
                "A narrative game with action sequences",
                price,
                19,
                "http://image_url",
                category,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        OrderItem item1 = new OrderItem(product1, 9);
        OrderItem item2 = new OrderItem(product2, 2);

        List<OrderItem> orderItemList1 = new ArrayList<>();
        orderItemList1.add(item1);
        orderItemList1.add(item2);


        // user
        Set<AppRole> roles = new HashSet<>();

        Country country = new Country("10", CountryEnum.Germany.name());
        AppRole role = new AppRole("11", RoleEnum.ROLE_USER.name());

        roles.add(role);

        AppUser user = new AppUser(
                "19",
                "John",
                "Wick",
                "001666666666",
                "john.wick@gmail.com",
                roles,
                "john.wick",
                "Password!1234",
                country,
                "This is an address",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Order order = new Order(
                "11",
                orderItemList1,
                user,
                OrderStatusEnum.IN_PROGRESS.name()
        );



        // when
        given(orderService.getOrderById(anyString())).willReturn(order);


        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders" + "/19"))
                .andExpect(status().isForbidden());

    }

    @Test
    void getOrder_shouldBeUnauthorized() throws Exception
    {
        // given

        // order
        BigDecimal price = new BigDecimal("69.99");
        Category category = new Category("11", "Video Games");

        Product product1 = new Product(
                "18",
                "Bloodborne",
                "A souls like game",
                price,
                19,
                "http://image_url",
                category,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Product product2 = new Product(
                "19",
                "The Last of Us",
                "A narrative game with action sequences",
                price,
                19,
                "http://image_url",
                category,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        OrderItem item1 = new OrderItem(product1, 9);
        OrderItem item2 = new OrderItem(product2, 2);

        List<OrderItem> orderItemList1 = new ArrayList<>();
        orderItemList1.add(item1);
        orderItemList1.add(item2);


        // user
        Set<AppRole> roles = new HashSet<>();

        Country country = new Country("10", CountryEnum.Germany.name());
        AppRole role = new AppRole("11", RoleEnum.ROLE_USER.name());

        roles.add(role);

        AppUser user = new AppUser(
                "19",
                "John",
                "Wick",
                "001666666666",
                "john.wick@gmail.com",
                roles,
                "john.wick",
                "Password!1234",
                country,
                "This is an address",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Order order = new Order(
                "11",
                orderItemList1,
                user,
                OrderStatusEnum.IN_PROGRESS.name()
        );



        // when
        given(orderService.getOrderById(anyString())).willReturn(order);


        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders" + "/19"))
                .andExpect(status().isForbidden());

    }


    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    void getUserOrders_authorizedByUser_shouldReturnOrders() throws Exception
    {
        // given

        // order
        BigDecimal price = new BigDecimal("69.99");
        Category category = new Category("11", "Video Games");

        Product product1 = new Product(
                "18",
                "Bloodborne",
                "A souls like game",
                price,
                19,
                "http://image_url",
                category,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Product product2 = new Product(
                "19",
                "The Last of Us",
                "A narrative game with action sequences",
                price,
                19,
                "http://image_url",
                category,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        OrderItem item1 = new OrderItem(product1, 9);
        OrderItem item2 = new OrderItem(product2, 2);

        List<OrderItem> orderItemList1 = new ArrayList<>();
        orderItemList1.add(item1);
        orderItemList1.add(item2);


        // user
        Set<AppRole> roles = new HashSet<>();

        Country country = new Country("10", CountryEnum.Germany.name());
        AppRole role = new AppRole("11", RoleEnum.ROLE_USER.name());

        roles.add(role);

        AppUser user = new AppUser(
                "19",
                "John",
                "Wick",
                "001666666666",
                "john.wick@gmail.com",
                roles,
                "john.wick",
                "Password!1234",
                country,
                "This is an address",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Order order1 = new Order(
                "11",
                orderItemList1,
                user,
                OrderStatusEnum.IN_PROGRESS.name()
        );


        OrderItem item3 = new OrderItem(product2, 19);

        List<OrderItem> orderItemList2 = new ArrayList<>();
        orderItemList2.add(item3);

        Order order2 = new Order(
                "12",
                orderItemList2,
                user,
                OrderStatusEnum.IN_PROGRESS.name()
        );

        List<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);


        // when
        given(orderService.getUserOrders()).willReturn(orders);


        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(orders.size()))

                .andExpect(jsonPath("$[0].id").value(equalTo("11")))
                .andExpect(jsonPath("$[0].orderItemList.length()").value(orderItemList1.size()))
                .andExpect(jsonPath("$[0].orderItemList[0].product.id").value(equalTo("18")))
                .andExpect(jsonPath("$[0].orderItemList[0].product.name").value(equalTo("Bloodborne")))
                .andExpect(jsonPath("$[0].orderItemList[0].product.price").value(equalTo(new BigDecimal("69.99"))))
                .andExpect(jsonPath("$[0].orderItemList[0].product.quantity").value(equalTo(19)))
                .andExpect(jsonPath("$[0].orderItemList[0].product.category.id").value(equalTo("11")))
                .andExpect(jsonPath("$[0].orderItemList[0].product.category.name").value(equalTo("Video Games")))
                .andExpect(jsonPath("$[0].orderItemList[0].product.active").value(equalTo(true)))
                .andExpect(jsonPath("$[0].orderItemList[0].quantity").value(equalTo(9)))

                .andExpect(jsonPath("$[0].orderItemList[1].product.id").value(equalTo("19")))
                .andExpect(jsonPath("$[0].orderItemList[1].product.name").value(equalTo("The Last of Us")))
                .andExpect(jsonPath("$[0].orderItemList[1].product.price").value(equalTo(new BigDecimal("69.99"))))
                .andExpect(jsonPath("$[0].orderItemList[1].product.quantity").value(equalTo(19)))
                .andExpect(jsonPath("$[0].orderItemList[1].product.category.id").value(equalTo("11")))
                .andExpect(jsonPath("$[0].orderItemList[1].product.category.name").value(equalTo("Video Games")))
                .andExpect(jsonPath("$[0].orderItemList[1].product.active").value(equalTo(true)))
                .andExpect(jsonPath("$[0].orderItemList[1].quantity").value(equalTo(2)))

                .andExpect(jsonPath("$[0].orderStatus").value(equalTo(OrderStatusEnum.IN_PROGRESS.name())))
                .andExpect(jsonPath("$[0].user.username").value(equalTo("john.wick")))
                .andExpect(jsonPath("$[0].totalPrice").value(equalTo(new BigDecimal("769.89"))))

                .andExpect(jsonPath("$[1].orderItemList.length()").value(orderItemList2.size()))
                .andExpect(jsonPath("$[1].orderItemList[0].product.id").value(equalTo("19")))
                .andExpect(jsonPath("$[1].orderItemList[0].product.name").value(equalTo("The Last of Us")))
                .andExpect(jsonPath("$[1].orderItemList[0].product.price").value(equalTo(new BigDecimal("69.99"))))
                .andExpect(jsonPath("$[1].orderItemList[0].product.quantity").value(equalTo(19)))
                .andExpect(jsonPath("$[1].orderItemList[0].product.category.id").value(equalTo("11")))
                .andExpect(jsonPath("$[1].orderItemList[0].product.category.name").value(equalTo("Video Games")))
                .andExpect(jsonPath("$[1].orderItemList[0].product.active").value(equalTo(true)))
                .andExpect(jsonPath("$[1].orderItemList[0].quantity").value(equalTo(19)))

                .andExpect(jsonPath("$[1].orderStatus").value(equalTo(OrderStatusEnum.IN_PROGRESS.name())))
                .andExpect(jsonPath("$[1].user.username").value(equalTo("john.wick")))
                .andExpect(jsonPath("$[1].totalPrice").value(equalTo(new BigDecimal("1329.81"))));

    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void getUserOrders_shouldBeUnauthorizedByAdmin() throws Exception
    {
        // given

        // order
        BigDecimal price = new BigDecimal("69.99");
        Category category = new Category("11", "Video Games");

        Product product1 = new Product(
                "18",
                "Bloodborne",
                "A souls like game",
                price,
                19,
                "http://image_url",
                category,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Product product2 = new Product(
                "19",
                "The Last of Us",
                "A narrative game with action sequences",
                price,
                19,
                "http://image_url",
                category,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        OrderItem item1 = new OrderItem(product1, 9);
        OrderItem item2 = new OrderItem(product2, 2);

        List<OrderItem> orderItemList1 = new ArrayList<>();
        orderItemList1.add(item1);
        orderItemList1.add(item2);


        // user
        Set<AppRole> roles = new HashSet<>();

        Country country = new Country("10", CountryEnum.Germany.name());
        AppRole role = new AppRole("11", RoleEnum.ROLE_USER.name());

        roles.add(role);

        AppUser user = new AppUser(
                "19",
                "John",
                "Wick",
                "001666666666",
                "john.wick@gmail.com",
                roles,
                "john.wick",
                "Password!1234",
                country,
                "This is an address",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Order order1 = new Order(
                "11",
                orderItemList1,
                user,
                OrderStatusEnum.IN_PROGRESS.name()
        );


        OrderItem item3 = new OrderItem(product2, 19);

        List<OrderItem> orderItemList2 = new ArrayList<>();
        orderItemList2.add(item3);

        Order order2 = new Order(
                "12",
                orderItemList2,
                user,
                OrderStatusEnum.IN_PROGRESS.name()
        );

        List<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);


        // when
        given(orderService.getUserOrders()).willReturn(orders);


        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/user"))
                .andExpect(status().isForbidden());


    }


    @Test
    void getUserOrders_shouldBeUnauthorized() throws Exception
    {
        // given

        // order
        BigDecimal price = new BigDecimal("69.99");
        Category category = new Category("11", "Video Games");

        Product product1 = new Product(
                "18",
                "Bloodborne",
                "A souls like game",
                price,
                19,
                "http://image_url",
                category,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Product product2 = new Product(
                "19",
                "The Last of Us",
                "A narrative game with action sequences",
                price,
                19,
                "http://image_url",
                category,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        OrderItem item1 = new OrderItem(product1, 9);
        OrderItem item2 = new OrderItem(product2, 2);

        List<OrderItem> orderItemList1 = new ArrayList<>();
        orderItemList1.add(item1);
        orderItemList1.add(item2);


        // user
        Set<AppRole> roles = new HashSet<>();

        Country country = new Country("10", CountryEnum.Germany.name());
        AppRole role = new AppRole("11", RoleEnum.ROLE_USER.name());

        roles.add(role);

        AppUser user = new AppUser(
                "19",
                "John",
                "Wick",
                "001666666666",
                "john.wick@gmail.com",
                roles,
                "john.wick",
                "Password!1234",
                country,
                "This is an address",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Order order1 = new Order(
                "11",
                orderItemList1,
                user,
                OrderStatusEnum.IN_PROGRESS.name()
        );


        OrderItem item3 = new OrderItem(product2, 19);

        List<OrderItem> orderItemList2 = new ArrayList<>();
        orderItemList2.add(item3);

        Order order2 = new Order(
                "12",
                orderItemList2,
                user,
                OrderStatusEnum.IN_PROGRESS.name()
        );

        List<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);


        // when
        given(orderService.getUserOrders()).willReturn(orders);


        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/user"))
                .andExpect(status().isForbidden());


    }



    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    void getUserOrder_authorizedByUser_shouldReturnAnOrder() throws Exception
    {
        // given

        // order
        BigDecimal price = new BigDecimal("69.99");
        Category category = new Category("11", "Video Games");

        Product product1 = new Product(
                "18",
                "Bloodborne",
                "A souls like game",
                price,
                19,
                "http://image_url",
                category,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Product product2 = new Product(
                "19",
                "The Last of Us",
                "A narrative game with action sequences",
                price,
                19,
                "http://image_url",
                category,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        OrderItem item1 = new OrderItem(product1, 9);
        OrderItem item2 = new OrderItem(product2, 2);

        List<OrderItem> orderItemList1 = new ArrayList<>();
        orderItemList1.add(item1);
        orderItemList1.add(item2);


        // user
        Set<AppRole> roles = new HashSet<>();

        Country country = new Country("10", CountryEnum.Germany.name());
        AppRole role = new AppRole("11", RoleEnum.ROLE_USER.name());

        roles.add(role);

        AppUser user = new AppUser(
                "19",
                "John",
                "Wick",
                "001666666666",
                "john.wick@gmail.com",
                roles,
                "john.wick",
                "Password!1234",
                country,
                "This is an address",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Order order = new Order(
                "11",
                orderItemList1,
                user,
                OrderStatusEnum.IN_PROGRESS.name()
        );



        // when
        given(orderService.getUserOrderById(anyString())).willReturn(order);


        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/user/" + "19"))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.id").value(equalTo("11")))
                .andExpect(jsonPath("$.orderItemList.length()").value(orderItemList1.size()))
                .andExpect(jsonPath("$.orderItemList[0].product.id").value(equalTo("18")))
                .andExpect(jsonPath("$.orderItemList[0].product.name").value(equalTo("Bloodborne")))
                .andExpect(jsonPath("$.orderItemList[0].product.price").value(equalTo(new BigDecimal("69.99"))))
                .andExpect(jsonPath("$.orderItemList[0].product.quantity").value(equalTo(19)))
                .andExpect(jsonPath("$.orderItemList[0].product.category.id").value(equalTo("11")))
                .andExpect(jsonPath("$.orderItemList[0].product.category.name").value(equalTo("Video Games")))
                .andExpect(jsonPath("$.orderItemList[0].product.active").value(equalTo(true)))
                .andExpect(jsonPath("$.orderItemList[0].quantity").value(equalTo(9)))

                .andExpect(jsonPath("$.orderItemList[1].product.id").value(equalTo("19")))
                .andExpect(jsonPath("$.orderItemList[1].product.name").value(equalTo("The Last of Us")))
                .andExpect(jsonPath("$.orderItemList[1].product.price").value(equalTo(new BigDecimal("69.99"))))
                .andExpect(jsonPath("$.orderItemList[1].product.quantity").value(equalTo(19)))
                .andExpect(jsonPath("$.orderItemList[1].product.category.id").value(equalTo("11")))
                .andExpect(jsonPath("$.orderItemList[1].product.category.name").value(equalTo("Video Games")))
                .andExpect(jsonPath("$.orderItemList[1].product.active").value(equalTo(true)))
                .andExpect(jsonPath("$.orderItemList[1].quantity").value(equalTo(2)))

                .andExpect(jsonPath("$.orderStatus").value(equalTo(OrderStatusEnum.IN_PROGRESS.name())))
                .andExpect(jsonPath("$.user.username").value(equalTo("john.wick")))
                .andExpect(jsonPath("$.totalPrice").value(equalTo(new BigDecimal("769.89"))));


    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void getUserOrder_shouldBeUnauthorizedByAdmin() throws Exception
    {
        // given

        // order
        BigDecimal price = new BigDecimal("69.99");
        Category category = new Category("11", "Video Games");

        Product product1 = new Product(
                "18",
                "Bloodborne",
                "A souls like game",
                price,
                19,
                "http://image_url",
                category,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Product product2 = new Product(
                "19",
                "The Last of Us",
                "A narrative game with action sequences",
                price,
                19,
                "http://image_url",
                category,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        OrderItem item1 = new OrderItem(product1, 9);
        OrderItem item2 = new OrderItem(product2, 2);

        List<OrderItem> orderItemList1 = new ArrayList<>();
        orderItemList1.add(item1);
        orderItemList1.add(item2);


        // user
        Set<AppRole> roles = new HashSet<>();

        Country country = new Country("10", CountryEnum.Germany.name());
        AppRole role = new AppRole("11", RoleEnum.ROLE_USER.name());

        roles.add(role);

        AppUser user = new AppUser(
                "19",
                "John",
                "Wick",
                "001666666666",
                "john.wick@gmail.com",
                roles,
                "john.wick",
                "Password!1234",
                country,
                "This is an address",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Order order = new Order(
                "11",
                orderItemList1,
                user,
                OrderStatusEnum.IN_PROGRESS.name()
        );



        // when
        given(orderService.getUserOrderById(anyString())).willReturn(order);


        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/user/" + "19"))
                .andExpect(status().isForbidden());


    }

    @Test
    void getUserOrder_shouldBeUnauthorized() throws Exception
    {
        // given

        // order
        BigDecimal price = new BigDecimal("69.99");
        Category category = new Category("11", "Video Games");

        Product product1 = new Product(
                "18",
                "Bloodborne",
                "A souls like game",
                price,
                19,
                "http://image_url",
                category,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Product product2 = new Product(
                "19",
                "The Last of Us",
                "A narrative game with action sequences",
                price,
                19,
                "http://image_url",
                category,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        OrderItem item1 = new OrderItem(product1, 9);
        OrderItem item2 = new OrderItem(product2, 2);

        List<OrderItem> orderItemList1 = new ArrayList<>();
        orderItemList1.add(item1);
        orderItemList1.add(item2);


        // user
        Set<AppRole> roles = new HashSet<>();

        Country country = new Country("10", CountryEnum.Germany.name());
        AppRole role = new AppRole("11", RoleEnum.ROLE_USER.name());

        roles.add(role);

        AppUser user = new AppUser(
                "19",
                "John",
                "Wick",
                "001666666666",
                "john.wick@gmail.com",
                roles,
                "john.wick",
                "Password!1234",
                country,
                "This is an address",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Order order = new Order(
                "11",
                orderItemList1,
                user,
                OrderStatusEnum.IN_PROGRESS.name()
        );



        // when
        given(orderService.getUserOrderById(anyString())).willReturn(order);


        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/user/" + "19"))
                .andExpect(status().isForbidden());


    }


    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    public void createUserOrder_authorizedByUser_shouldReturnCreatedOrder() throws Exception
    {
        // given

        // request
        OrderItemRequest orderItemRequest1 = new OrderItemRequest("19", 9);
        OrderItemRequest orderItemRequest2 = new OrderItemRequest("20", 2);

        List<OrderItemRequest> orderItemRequestList = new ArrayList<>();
        orderItemRequestList.add(orderItemRequest1);
        orderItemRequestList.add(orderItemRequest2);

        OrderRequest orderRequest = new OrderRequest(orderItemRequestList);

        // created

        BigDecimal price = new BigDecimal("69.99");
        Category category = new Category("11", "Video Games");

        Product product1 = new Product(
                "19",
                "Bloodborne",
                "A souls like game",
                price,
                19,
                "http://image_url",
                category,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Product product2 = new Product(
                "20",
                "The Last of Us",
                "A narrative game with action sequences",
                price,
                19,
                "http://image_url",
                category,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        OrderItem item1 = new OrderItem(product1, 9);
        OrderItem item2 = new OrderItem(product2, 2);

        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(item1);
        orderItemList.add(item2);

        // user
        Set<AppRole> roles = new HashSet<>();

        Country country = new Country("10", CountryEnum.Germany.name());
        AppRole role = new AppRole("11", RoleEnum.ROLE_USER.name());

        roles.add(role);

        AppUser user = new AppUser(
                "19",
                "John",
                "Wick",
                "001666666666",
                "john.wick@gmail.com",
                roles,
                "john.wick",
                "Password!1234",
                country,
                "This is an address",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Order order = new Order(
                "11",
                orderItemList,
                user,
                OrderStatusEnum.IN_PROGRESS.name()
        );



        // when
        given(orderService.createUserOrder(any(OrderRequest.class))).willReturn(order);


        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/orders")
                        .content(asJsonString(orderRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(equalTo("11")))
                .andExpect(jsonPath("$.orderItemList.length()").value(orderItemList.size()))
                .andExpect(jsonPath("$.orderItemList[0].product.id").value(equalTo(orderItemRequest1.getProductId())))
                .andExpect(jsonPath("$.orderItemList[0].product.name").value(equalTo("Bloodborne")))
                .andExpect(jsonPath("$.orderItemList[0].product.price").value(equalTo(new BigDecimal("69.99"))))
                .andExpect(jsonPath("$.orderItemList[0].product.quantity").value(equalTo(19)))
                .andExpect(jsonPath("$.orderItemList[0].product.category.id").value(equalTo("11")))
                .andExpect(jsonPath("$.orderItemList[0].product.category.name").value(equalTo("Video Games")))
                .andExpect(jsonPath("$.orderItemList[0].product.active").value(equalTo(true)))
                .andExpect(jsonPath("$.orderItemList[0].quantity").value(equalTo(orderItemRequest1.getQuantity())))

                .andExpect(jsonPath("$.orderItemList[1].product.id").value(equalTo(orderItemRequest2.getProductId())))
                .andExpect(jsonPath("$.orderItemList[1].product.name").value(equalTo("The Last of Us")))
                .andExpect(jsonPath("$.orderItemList[1].product.price").value(equalTo(new BigDecimal("69.99"))))
                .andExpect(jsonPath("$.orderItemList[1].product.quantity").value(equalTo(19)))
                .andExpect(jsonPath("$.orderItemList[1].product.category.id").value(equalTo("11")))
                .andExpect(jsonPath("$.orderItemList[1].product.category.name").value(equalTo("Video Games")))
                .andExpect(jsonPath("$.orderItemList[1].product.active").value(equalTo(true)))
                .andExpect(jsonPath("$.orderItemList[1].quantity").value(equalTo(orderItemRequest2.getQuantity())))

                .andExpect(jsonPath("$.orderStatus").value(equalTo(OrderStatusEnum.IN_PROGRESS.name())))
                .andExpect(jsonPath("$.user.username").value(equalTo("john.wick")))
                .andExpect(jsonPath("$.totalPrice").value(equalTo(new BigDecimal("769.89"))))
                .andDo(print());

    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    public void createUserOrder_shouldBeUnauthorizedByAdmin() throws Exception
    {
        // given

        // request
        OrderItemRequest orderItemRequest1 = new OrderItemRequest("19", 9);
        OrderItemRequest orderItemRequest2 = new OrderItemRequest("20", 2);

        List<OrderItemRequest> orderItemRequestList = new ArrayList<>();
        orderItemRequestList.add(orderItemRequest1);
        orderItemRequestList.add(orderItemRequest2);

        OrderRequest orderRequest = new OrderRequest(orderItemRequestList);

        // created

        BigDecimal price = new BigDecimal("69.99");
        Category category = new Category("11", "Video Games");

        Product product1 = new Product(
                "19",
                "Bloodborne",
                "A souls like game",
                price,
                19,
                "http://image_url",
                category,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Product product2 = new Product(
                "20",
                "The Last of Us",
                "A narrative game with action sequences",
                price,
                19,
                "http://image_url",
                category,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        OrderItem item1 = new OrderItem(product1, 9);
        OrderItem item2 = new OrderItem(product2, 2);

        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(item1);
        orderItemList.add(item2);

        // user
        Set<AppRole> roles = new HashSet<>();

        Country country = new Country("10", CountryEnum.Germany.name());
        AppRole role = new AppRole("11", RoleEnum.ROLE_USER.name());

        roles.add(role);

        AppUser user = new AppUser(
                "19",
                "John",
                "Wick",
                "001666666666",
                "john.wick@gmail.com",
                roles,
                "john.wick",
                "Password!1234",
                country,
                "This is an address",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Order order = new Order(
                "11",
                orderItemList,
                user,
                OrderStatusEnum.IN_PROGRESS.name()
        );



        // when
        given(orderService.createUserOrder(any(OrderRequest.class))).willReturn(order);


        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/orders")
                        .content(asJsonString(orderRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());

    }

    @Test
    public void createUserOrder_shouldBeUnauthorized() throws Exception
    {
        // given

        // request
        OrderItemRequest orderItemRequest1 = new OrderItemRequest("19", 9);
        OrderItemRequest orderItemRequest2 = new OrderItemRequest("20", 2);

        List<OrderItemRequest> orderItemRequestList = new ArrayList<>();
        orderItemRequestList.add(orderItemRequest1);
        orderItemRequestList.add(orderItemRequest2);

        OrderRequest orderRequest = new OrderRequest(orderItemRequestList);

        // created

        BigDecimal price = new BigDecimal("69.99");
        Category category = new Category("11", "Video Games");

        Product product1 = new Product(
                "19",
                "Bloodborne",
                "A souls like game",
                price,
                19,
                "http://image_url",
                category,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Product product2 = new Product(
                "20",
                "The Last of Us",
                "A narrative game with action sequences",
                price,
                19,
                "http://image_url",
                category,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        OrderItem item1 = new OrderItem(product1, 9);
        OrderItem item2 = new OrderItem(product2, 2);

        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(item1);
        orderItemList.add(item2);

        // user
        Set<AppRole> roles = new HashSet<>();

        Country country = new Country("10", CountryEnum.Germany.name());
        AppRole role = new AppRole("11", RoleEnum.ROLE_USER.name());

        roles.add(role);

        AppUser user = new AppUser(
                "19",
                "John",
                "Wick",
                "001666666666",
                "john.wick@gmail.com",
                roles,
                "john.wick",
                "Password!1234",
                country,
                "This is an address",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Order order = new Order(
                "11",
                orderItemList,
                user,
                OrderStatusEnum.IN_PROGRESS.name()
        );



        // when
        given(orderService.createUserOrder(any(OrderRequest.class))).willReturn(order);


        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/orders")
                        .content(asJsonString(orderRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());

    }

    private String asJsonString(final Object obj)
    {
        try
        {
            final ObjectMapper mapper = new ObjectMapper();

            return mapper.writeValueAsString(obj);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

}