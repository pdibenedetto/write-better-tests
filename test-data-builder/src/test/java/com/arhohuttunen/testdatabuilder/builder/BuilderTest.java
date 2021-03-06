package com.arhohuttunen.testdatabuilder.builder;

import com.arhohuttunen.testdatabuilder.model.Order;
import org.junit.jupiter.api.Test;

import static com.arhohuttunen.testdatabuilder.builder.AddressBuilder.anAddress;
import static com.arhohuttunen.testdatabuilder.builder.CustomerBuilder.aCustomer;
import static com.arhohuttunen.testdatabuilder.builder.OrderBuilder.anOrder;
import static com.arhohuttunen.testdatabuilder.builder.OrderItemBuilder.anOrderItem;
import static org.assertj.core.api.Assertions.assertThat;

class BuilderTest {
    @Test
    void buildOrder() {
        Order order = anOrder()
                .with(aCustomer()
                        .withName("Terry Tew")
                        .with(anAddress()
                                .withStreet("1216  Clinton Street")
                                .withCity("Philadelphia")
                                .withPostalCode("19108")
                        )
                )
                .with(anOrderItem().withName("Coffee mug").withQuantity(1))
                .with(anOrderItem().withName("Tea cup").withQuantity(1))
                .build();

        assertThat(order.getCustomer().getName()).isEqualTo("Terry Tew");
    }

    @Test
    void buildSimilarOrders() {
        OrderBuilder coffeeMugAndTeaCup = anOrder()
                .with(anOrderItem().withName("Coffee mug").withQuantity(1))
                .with(anOrderItem().withName("Tea cup").withQuantity(1));

        Order orderWithDiscount = coffeeMugAndTeaCup.but().withDiscountRate(0.1).build();
        Order orderWithCouponCode = coffeeMugAndTeaCup.but().withCouponCode("HALFOFF").build();

        assertThat(orderWithDiscount.getDiscountRate()).isEqualTo(0.1);

        // We have safe default values:
        assertThat(orderWithCouponCode.getCustomer().getAddress().getCountry()).isEqualTo("Some country");
        assertThat(orderWithCouponCode.getCustomer().getName()).isEqualTo("Unimportant");
        assertThat(orderWithCouponCode.getDiscountRate()).isEqualTo(0.0);
    }
}