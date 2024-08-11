package com.jorge.ecommerce;

import com.jorge.ecommerce.model.Category;
import com.jorge.ecommerce.model.Product;
import com.jorge.ecommerce.model.Role;
import com.jorge.ecommerce.repository.CategoryRepository;
import com.jorge.ecommerce.repository.ProductRepository;
import com.jorge.ecommerce.repository.RoleRepository;
import com.jorge.ecommerce.service.CategoryService;
import com.jorge.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import java.math.BigDecimal;

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
		Category saved = categoryRepository.save(category);

		Product product = Product.builder()
				.name("iPhone 14")
				.price(BigDecimal.valueOf(4000.00))
				.category(saved)
				.stockQuantity(5)
				.build();

		productRepository.save(product);

		Product product2 = Product.builder()
				.name("iPhone 15")
				.price(BigDecimal.valueOf(5000.00))
				.category(saved)
				.stockQuantity(5)
				.build();

		productRepository.save(product2);

		Role role = Role.builder().name("CUSTOMER").build();
		roleRepository.save(role);

		Role role1 = Role.builder().name("ADMIN").build();
		roleRepository.save(role1);
	}
}
