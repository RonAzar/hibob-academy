package com.hibob.academy.service

import com.hibob.academy.dao.PetDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class PetService @Autowired constructor(private val petDao: PetDao) {

}