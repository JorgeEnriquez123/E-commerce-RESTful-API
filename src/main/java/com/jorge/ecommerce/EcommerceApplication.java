package com.jorge.ecommerce;

import com.jorge.ecommerce.model.Category;
import com.jorge.ecommerce.model.Product;
import com.jorge.ecommerce.model.Role;
import com.jorge.ecommerce.repository.CategoryRepository;
import com.jorge.ecommerce.repository.ProductRepository;
import com.jorge.ecommerce.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@EnableCaching
@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
@RequiredArgsConstructor
public class EcommerceApplication implements CommandLineRunner {
	private final CategoryRepository categoryRepository;
	private final ProductRepository productRepository;
	private final RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(EcommerceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Category category = Category.builder().name("Phones").build();
		Category savedCategory = categoryRepository.save(category);

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

		Role role = Role.builder().name("CUSTOMER").build();
		roleRepository.save(role);

		Role role1 = Role.builder().name("ADMIN").build();
		roleRepository.save(role1);
	}
}
