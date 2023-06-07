package cart.dao;

import cart.domain.MemberEntity;
import cart.domain.OrderEntity;
import cart.domain.Product;
import cart.exception.DataNotFoundException;
import cart.util.CurrentTimeUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class OrderDaoTest extends DaoTest {

    private MemberDao memberDao;
    private ProductDao productDao;
    private OrderDao orderDao;

    private final MemberEntity memberEntity = new MemberEntity(1L, "1@email.com", "11", 1_000);
    private final Product product = new Product(1L, "productName", new BigDecimal(1_000), "imageUrl", 10);
    private OrderEntity orderEntity;

    @BeforeEach
    void setUp() {
        this.memberDao = new MemberDao(jdbcTemplate);
        this.productDao = new ProductDao(jdbcTemplate);
        this.orderDao = new OrderDao(jdbcTemplate);

        this.orderEntity = new OrderEntity(
                1L,
                CurrentTimeUtil.asString(),
                memberEntity.getId(),
                1_000,
                3_000,
                0,
                4_000
        );

        setDummyData();
    }

    private void setDummyData() {
        memberDao.insert(memberEntity);
        productDao.insert(product);
    }

    @Test
    void 주문을_삽입한다() {
        assertThatNoException().isThrownBy(
                () -> orderDao.insert(orderEntity)
        );
    }

    @Test
    void 주문을_ID로_조회한다() {
        // given
        long savedId = orderDao.insert(orderEntity);

        // when
        OrderEntity result = orderDao.findById(savedId);

        // then
        assertThat(result).isEqualTo(orderEntity);
    }

    @Test
    void 모든_주문을_조회한다() {
        // given
        long savedId = orderDao.insert(orderEntity);

        // when
        List<OrderEntity> result = orderDao.findAll();

        // then
        assertThat(result).hasSize(1);
        assertThat(result).containsOnly(orderEntity);
    }

    @Test
    void 회원ID로_모든_주문을_조회한다() {
        // given
        orderDao.insert(orderEntity);

        // when
        List<OrderEntity> result = orderDao.findAllByMemberId(memberEntity.getId());

        // then
        assertThat(result).containsOnly(orderEntity);
    }

    @Test
    void 주문을_수정한다() {
        // given
        long savedId = orderDao.insert(orderEntity);

        OrderEntity newOrderEntity = new OrderEntity(
                orderEntity.getId(),
                orderEntity.getCreatedAt(),
                orderEntity.getMemberId(),
                11,
                22,
                33,
                44
        );

        // when
        orderDao.update(newOrderEntity);

        // then
        OrderEntity result = orderDao.findById(newOrderEntity.getId());
        assertThat(result.getTotalProductPrice()).isEqualTo(11);
        assertThat(result.getTotalDeliveryFee()).isEqualTo(22);
        assertThat(result.getUsePoint()).isEqualTo(33);
        assertThat(result.getTotalPrice()).isEqualTo(44);
    }

    @Test
    void ID로_주문을_삭제한다() {
        // given
        long savedId = orderDao.insert(orderEntity);

        // when
        orderDao.deleteById(savedId);

        // then
        assertThatThrownBy(() -> orderDao.findById(savedId))
                .isInstanceOf(DataNotFoundException.class);
    }
}
