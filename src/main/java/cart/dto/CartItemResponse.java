package cart.dto;

import cart.domain.CartItem;

public class CartItemResponse {

    private final Long id;
    private final int quantity;
    private final ProductResponse product;

    private CartItemResponse(final Long id, final int quantity, final ProductResponse product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public static CartItemResponse from(final CartItem cartItem) {
        return new CartItemResponse(
                cartItem.getId(),
                cartItem.getQuantity(),
                ProductResponse.from(cartItem.getProduct())
        );
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductResponse getProduct() {
        return product;
    }
}
