package sn.dev.product_service.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import sn.dev.product_service.data.entities.Product;
import sn.dev.product_service.data.repo.ProductRepo;
import sn.dev.product_service.services.impl.ProductServiceImpl;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepo productRepo;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void testCreateProduct() {
        Product product = new Product();
        product.setId(UUID.randomUUID().toString());
        product.setName("Test Product");
        product.setDescription("This is a test product.");
        product.setPrice(19.99);
        product.setQuantity(100);
        product.setUserId("user123");

        // Mock the behavior of the repository
        when(productRepo.save(product)).thenReturn(product);

        // Call the service method
        Product createdProduct = productService.create(product);

        // Verify the result
        assertNotNull(createdProduct);
        assertEquals("Test Product", createdProduct.getName());
        assertEquals("This is a test product.", createdProduct.getDescription());
        assertEquals(19.99, createdProduct.getPrice());
        assertEquals(100, createdProduct.getQuantity());
        assertEquals("user123", createdProduct.getUserId());

        // Verify that the repository was called
        verify(productRepo, times(1)).save(product);

        System.out.println("✅ : testCreateProduct() passed successfully.");
    }
}
