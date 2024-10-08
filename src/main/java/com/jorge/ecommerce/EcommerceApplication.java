package com.jorge.ecommerce;

import com.jorge.ecommerce.dto.create.CreateUserDto;
import com.jorge.ecommerce.model.Category;
import com.jorge.ecommerce.model.Product;
import com.jorge.ecommerce.model.Role;
import com.jorge.ecommerce.model.User;
import com.jorge.ecommerce.repository.CategoryRepository;
import com.jorge.ecommerce.repository.ProductRepository;
import com.jorge.ecommerce.repository.RoleRepository;
import com.jorge.ecommerce.repository.UserRepository;
import com.jorge.ecommerce.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@EnableCaching
@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class EcommerceApplication {
	private static final Logger log = LoggerFactory.getLogger(EcommerceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(EcommerceApplication.class, args);
	}

	@Bean
	public CommandLineRunner init(CategoryRepository categoryRepository,
								  ProductRepository productRepository,
								  RoleRepository roleRepository, UserRepository userRepository, UserService userService,
								  PasswordEncoder passwordEncoder
	) {
		return args -> {
			if(categoryRepository.count() == 0) {
				Category category = Category.builder().name("Phones").build();
				Category savedCategory = categoryRepository.save(category);

				if(productRepository.count() == 0) {
					List<Product> products = Arrays.asList(
							Product.builder()
									.name("iPhone 15")
									.price(BigDecimal.valueOf(3200.00))
									.category(savedCategory)
									.stockQuantity(5)
									.build(),
							Product.builder()
									.name("Samsung Galaxy S24")
									.price(BigDecimal.valueOf(3100.00))
									.category(savedCategory)
									.stockQuantity(20)
									.build(),
							Product.builder()
									.name("Xiaomi 14 Ultra")
									.price(BigDecimal.valueOf(4200.00))
									.category(savedCategory)
									.stockQuantity(17)
									.build(),
							Product.builder()
									.name("Huawei Pura 70 Pro")
									.price(BigDecimal.valueOf(4400.00))
									.category(savedCategory)
									.stockQuantity(8)
									.build(),
							Product.builder()
									.name("Google Pixel 8 pro")
									.price(BigDecimal.valueOf(4600.00))
									.category(savedCategory)
									.stockQuantity(12)
									.build()
					);
					productRepository.saveAll(products);
				}
			}

			if(roleRepository.count() == 0) {
				Role role = Role.builder().name("CUSTOMER").build();
				roleRepository.save(role);

				Role role1 = Role.builder().name("ADMIN").build();
				roleRepository.save(role1);
			}
			if(userRepository.count() == 0){
				log.info("CREATING TEST USER");
				CreateUserDto createUserDto = CreateUserDto.builder()
						.firstName("Test")
						.lastName("Subject")
						.username("Test111")
						.password("test")
						.role("ADMIN")
						.build();
				userService.registerUser(createUserDto);
			}
		};
	}
}
