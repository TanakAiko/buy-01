package sn.dev.product_service;

import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import sn.dev.product_service.data.entities.Media;
import sn.dev.product_service.data.entities.Product;
import sn.dev.product_service.services.MediaServiceClient;
import sn.dev.product_service.services.ProductService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {
    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private MediaServiceClient mediaServiceClient;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void testGetAllReturnsProductResponseDTOList() throws Exception {
        Product product = new Product("1", "Test Product");
        Media media = new Media("m1", "image.png", "1");

        // Mock service responses
        when(productService.getAll()).thenReturn(List.of(product));
        when(mediaServiceClient.getByProductId("1"))
                .thenReturn(ResponseEntity.ok(List.of(media)));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Product"));
    }
}
