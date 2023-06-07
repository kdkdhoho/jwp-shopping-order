package cart.domain;

import cart.exception.IllegalAccessCartException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CartItemTest {

    private static final Quantity dummyQuantity = new Quantity(1);

    @Test
    void 특정_회원의_소유인지_검증한다_같은_회원이면_예외가_발생하지_않는다() {
        // given
        Member member = new Member(1L, "email", "pw", 10);
        CartItem cartItem = new CartItem(1L, member, new Product("productName", new Price(1000), "productImageUrl", 100), dummyQuantity);

        // expect
        assertThatNoException().isThrownBy(() -> cartItem.validateOwner(member));
    }

    @Test
    void 특정_회원의_소유인지_검증한다_다른_회원이면_예외가_발생한다() {
        // given
        Member member = new Member(1L, "email", "pw", 10);
        CartItem cartItem = new CartItem(1L, member, new Product("productName", new Price(1000), "productImageUrl", 100), dummyQuantity);

        Member otherMember = new Member(2L, "otherEmail", "otherPw", 0);

        // expect
        assertThatThrownBy(() -> cartItem.validateOwner(otherMember))
                .isInstanceOf(IllegalAccessCartException.class);
    }

    @Test
    void 수량을_변경한다() {
        // given
        CartItem cartItem = new CartItem(1L, new Member(1L, "email", "pw", 10), new Product("a", new Price(1000), "b", 10), dummyQuantity);

        // when
        Quantity newQuantity = new Quantity(2);
        CartItem newCartItem = cartItem.changeQuantity(newQuantity);

        // then
        assertThat(newCartItem.getQuantityValue()).isEqualTo(2);
    }

    @Test
    void 모든_필드가_같으면_같은_객체다() {
        // given
        CartItem cartItem = new CartItem(1L, new Member(1L, "email", "pw", 10), new Product("a", new Price(1000), "b", 10), dummyQuantity);
        CartItem sameCartItem = new CartItem(1L, new Member(1L, "email", "pw", 10), new Product("a", new Price(1000), "b", 10), dummyQuantity);

        // expect
        assertThat(cartItem).isEqualTo(sameCartItem);
    }
}
