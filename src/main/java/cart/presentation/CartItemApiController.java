package cart.presentation;

import cart.domain.Member;
import cart.dto.request.CartItemAddRequest;
import cart.dto.request.CartItemQuantityUpdateRequest;
import cart.dto.response.CartItemResponse;
import cart.service.CartItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/cart-items")
public class CartItemApiController {

    private final CartItemService cartItemService;

    public CartItemApiController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @PostMapping
    public ResponseEntity<Void> addCartItems(final Member member, final @RequestBody CartItemAddRequest cartItemAddRequest) {
        Long cartItemId = cartItemService.add(member, cartItemAddRequest);

        return ResponseEntity.created(URI.create("/cart-items/" + cartItemId)).build();
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> showCartItems(final Member member) {
        return ResponseEntity.ok(cartItemService.findByMember(member));
    }

    @PatchMapping("/{cartItemId}")
    public ResponseEntity<Void> updateCartItemQuantity(final Member member, final @PathVariable Long cartItemId, final @RequestBody CartItemQuantityUpdateRequest request) {
        cartItemService.updateQuantity(member, cartItemId, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> removeCartItems(final Member member, final @PathVariable Long cartItemId) {
        cartItemService.remove(member, cartItemId);

        return ResponseEntity.noContent().build();
    }
}
