package com.hibob.Types.Tamir

import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class StoreService {
    // 7. implement pay method
    fun pay(cart: List<Cart>, payment: Payment): Map<String, Check> {
        return cart.associate {
            it.clientId to checkout(it, payment)
        }
    }
}