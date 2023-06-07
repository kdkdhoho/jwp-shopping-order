package cart.service;

import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.domain.Quantity;
import cart.dto.request.CartItemAddRequest;
import cart.dto.request.CartItemQuantityUpdateRequest;
import cart.dto.response.CartItemResponse;
import cart.repository.CartItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartItemService {

    private final ProductDao productDao;
    private final CartItemRepository cartItemRepository;

    public CartItemService(ProductDao productDao, CartItemRepository cartItemRepository) {
        this.productDao = productDao;
        this.cartItemRepository = cartItemRepository;
    }

    public Long add(final Member member, final CartItemAddRequest cartItemAddRequest) {
        Product product = productDao.findById(cartItemAddRequest.getProductId());

        CartItem cartItem = new CartItem(member, product);

        return cartItemRepository.save(cartItem);
    }

    public List<CartItem> findByMember(final Member member) {
        List<CartItem> cartItems = cartItemRepository.findByMemberId(member.getId());

        for (final CartItem cartItem : cartItems) {
            cartItem.validateOwner(member);
        }

        return cartItems;
    }

    public void updateQuantity(final Member member, final Long id, final CartItemQuantityUpdateRequest request) {
        CartItem cartItem = cartItemRepository.findById(id);
        cartItem.validateOwner(member);

        if (request.getQuantity() == 0) {
            cartItemRepository.deleteById(id);
            return;
        }

        CartItem changedCartItem = cartItem.changeQuantity(new Quantity(request.getQuantity()));
        cartItemRepository.update(changedCartItem);
    }

    public void remove(final Member member, final Long id) {
        CartItem cartItem = cartItemRepository.findById(id);
        cartItem.validateOwner(member);

        cartItemRepository.deleteById(id);
    }
}
