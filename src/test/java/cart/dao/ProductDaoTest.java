package cart.dao;

import cart.domain.Product;
import cart.exception.DataNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ProductDaoTest extends DaoTest {

    private static final Product dummyProduct = new Product(1L, "name", 100, "imageUrl", 10);

    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        this.productDao = new ProductDao(jdbcTemplate);
    }

    @Test
    void 상품을_추가한다() {
        // when
        long savedId = productDao.insert(dummyProduct);

        // then
        assertThat(savedId).isNotZero();
    }

    @Test
    void ID로_상품을_찾는다() {
        // given
        long savedId = productDao.insert(dummyProduct);

        // when
        Product foundProduct = productDao.findById(savedId);

        // then
        assertThat(foundProduct).isEqualTo(dummyProduct);
    }

    @Test
    void 존재하지_않은_ID로_상품을_조회하면_예외가_발생한다() {
        // given
        long id = 21414L;

        // expect
        assertThatThrownBy(() -> productDao.findById(id))
                .isInstanceOf(DataNotFoundException.class);
    }

    @Test
    void 모든_상품을_조회한다() {
        // given
        productDao.insert(dummyProduct);

        // when
        List<Product> result = productDao.findAll();

        // then
        assertThat(result).hasSize(1);
    }

    @Test
    void 상품을_수정한다() {
        // given
        long savedId = productDao.insert(dummyProduct);

        Product newProduct = new Product(savedId, "newName", 200, "newImageUrl", 5);

        // when
        productDao.update(newProduct);

        // then
        Product foundProduct = productDao.findById(savedId);
        assertThat(foundProduct.getName()).isEqualTo("newName");
    }

    @Test
    void 상품의_수량만_수정한다() {
        // given
        long savedId = productDao.insert(dummyProduct);

        int newStock = dummyProduct.getStock() + 1;

        // when
        productDao.updateStockById(savedId, newStock);

        // then
        int result = productDao.findById(savedId).getStock();
        assertThat(result).isEqualTo(newStock);
    }

    @Test
    void 상품을_삭제한다() {
        // given
        long savedId = productDao.insert(dummyProduct);

        // when
        productDao.deleteById(savedId);

        // then
        assertThatThrownBy(() -> productDao.findById(savedId))
                .isInstanceOf(DataNotFoundException.class);
    }
}
