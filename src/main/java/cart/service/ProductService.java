package cart.service;

import cart.dao.ProductDao;
import cart.domain.Product;
import cart.dto.request.ProductAddRequest;
import cart.dto.response.ProductResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productDao.findAll();

        return products.stream().map(ProductResponse::from).collect(Collectors.toList());
    }

    public ProductResponse getProductById(Long productId) {
        Product product = productDao.findById(productId);

        return ProductResponse.from(product);
    }

    public Long createProduct(ProductAddRequest productAddRequest) {
        Product product = new Product(productAddRequest.getName(), new BigDecimal(productAddRequest.getPrice()), productAddRequest.getImageUrl(), productAddRequest.getStock());

        return productDao.insert(product);
    }

    public void updateProduct(Long productId, ProductAddRequest productAddRequest) {
        Product newProduct = new Product(productId, productAddRequest.getName(), new BigDecimal(productAddRequest.getPrice()), productAddRequest.getImageUrl(), productAddRequest.getStock());

        productDao.update(newProduct);
    }

    public void deleteProduct(Long productId) {
        productDao.deleteById(productId);
    }
}
