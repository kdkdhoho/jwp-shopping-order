package cart.dao;

import cart.domain.OrderEntity;
import cart.exception.DataNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderDao {

    private static final String TABLE = "orders";
    private static final String ID = "id";
    private static final String CREATED_AT = "created_At";
    private static final String MEMBER_ID = "member_id";
    private static final String TOTAL_PRODUCT_PRICE = "total_product_price";
    private static final String TOTAL_DELIVERY_FEE = "total_delivery_fee";
    private static final String USE_POINT = "use_point";
    private static final String TOTAL_PRICE = "total_price";
    private static final String ALL_COLUMN = String.join(", ",
            ID,
            CREATED_AT,
            MEMBER_ID,
            TOTAL_PRODUCT_PRICE,
            TOTAL_DELIVERY_FEE,
            USE_POINT,
            TOTAL_PRICE
    );

    private static final RowMapper<OrderEntity> rowMapper = (resultSet, rowNum) -> {
        String current = resultSet.getTimestamp(CREATED_AT).toString();

        return new OrderEntity(
                resultSet.getLong(ID),
                current.substring(0, current.length() - 2),
                resultSet.getLong(MEMBER_ID),
                resultSet.getInt(TOTAL_PRODUCT_PRICE),
                resultSet.getInt(TOTAL_DELIVERY_FEE),
                resultSet.getInt(USE_POINT),
                resultSet.getInt(TOTAL_PRICE)
        );
    };

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(TABLE)
                .usingGeneratedKeyColumns(ID);
    }

    public long insert(final OrderEntity orderEntity) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(orderEntity);

        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public OrderEntity findById(final Long id) {
        String sql = "SELECT " + ALL_COLUMN + " FROM " + TABLE + " WHERE id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (final EmptyResultDataAccessException e) {
            throw new DataNotFoundException("주문을 찾을 수 없습니다.");
        }
    }

    public List<OrderEntity> findAll() {
        String sql = "SELECT " + ALL_COLUMN + " FROM " + TABLE + ";";

        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<OrderEntity> findAllByMemberId(final Long memberId) {
        String sql = "SELECT " + ALL_COLUMN + " FROM " + TABLE + " WHERE member_id = ?;";

        return jdbcTemplate.query(sql, rowMapper, memberId);
    }

    public void update(final OrderEntity newOrderEntity) {
        String sql = "UPDATE " + TABLE + " SET " +
                "created_At = ?, " +
                "total_product_price = ?, " +
                "total_delivery_fee = ?, " +
                "use_point = ?, " +
                "total_price = ? " +
                "WHERE id = ?;";

        jdbcTemplate.update(sql,
                newOrderEntity.getCreatedAt(),
                newOrderEntity.getTotalProductPrice(),
                newOrderEntity.getTotalDeliveryFee(),
                newOrderEntity.getUsePoint(),
                newOrderEntity.getTotalPrice(),
                newOrderEntity.getId()
        );
    }

    public void deleteById(final Long id) {
        String sql = "DELETE FROM " + TABLE + " WHERE id = ?;";

        jdbcTemplate.update(sql, id);
    }
}
