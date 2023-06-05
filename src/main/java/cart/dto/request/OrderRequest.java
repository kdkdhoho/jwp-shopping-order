package cart.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderRequest {

    private final List<CartItemInfoRequest> cartItems;
    private final Integer totalProductPrice;
    private final Integer totalDeliveryFee;
    private final Integer usePoint;
    private final Integer totalPrice;

    private OrderRequest() {
        this(null, null, null, null, null);
    }
}