package cart.repository;

import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CartItemRepository {

    private final MemberRepository memberRepository;
    private final ProductDao productDao;
    private final CartItemDao cartItemDao;

    public CartItemRepository(MemberRepository memberRepository, ProductDao productDao, CartItemDao cartItemDao) {
        this.memberRepository = memberRepository;
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
    }

    public Long save(final CartItem cartItem) {
        CartItemEntity cartItemEntity = new CartItemEntity(
                cartItem.getMember(),
                cartItem.getProduct(),
                cartItem.getQuantity()
        );

        return cartItemDao.insert(cartItemEntity);
    }

    public CartItem findById(final Long cartItemId) {
        CartItemEntity foundCartItemEntity = cartItemDao.findById(cartItemId);

        return toDomain(foundCartItemEntity);
    }

    private CartItem toDomain(final CartItemEntity cartItemEntity) {
        Member member = memberRepository.findById(cartItemEntity.getMemberId());
        Product product = productDao.findById(cartItemEntity.getProductId());
        Quantity quantity = new Quantity(cartItemEntity.getQuantity());

        return new CartItem(cartItemEntity.getId(), member, product, quantity);
    }

    public List<CartItem> findByMemberId(final Long memberId) {
        List<CartItemEntity> cartItemEntities = cartItemDao.findByMemberId(memberId);

        return cartItemEntities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    public void update(final CartItem cartItem) {
        CartItemEntity cartItemEntity = new CartItemEntity(
                cartItem.getId(),
                cartItem.getMember(),
                cartItem.getProduct(),
                cartItem.getQuantity()
        );

        cartItemDao.update(cartItemEntity);
    }

    public void deleteById(final Long cartItemId) {
        cartItemDao.deleteById(cartItemId);
    }
}
