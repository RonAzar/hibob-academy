package com.hibob.academy.unitests.keren

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ListManagerTest{
    @Test
    fun `Test adding a unique person`(){
        assertEquals(true,ListManager().addPerson(Person("Ron Azar", 24)))
    }

    @Test
    fun `Test adding a duplicate person and ensure it throws the expected exception`(){
        assertThrows(IllegalArgumentException::class.java) {
            val managers = ListManager()
            managers.addPerson(Person("Ron Azar", 24))
            managers.addPerson(Person("Ron Azar", 24))
        }
    }

    @Test
    fun `Test adding multiple people, checking that the list grows appropriately`(){
        val managers = ListManager()
        managers.addPerson(Person("Ron Azar", 24))
        managers.addPerson(Person("Or Azar", 26))
        assertEquals(2,managers.getManagersAmount())
    }

    //* Write tests for removePerson method:
    @Test
    fun `Test removing a person that exists in the list`(){
        val managers = ListManager()
        managers.addPerson(Person("Ron Azar", 24))
        assertEquals(true,managers.removePerson(Person("Ron Azar",24)))
    }

    @Test
    fun `Test trying to remove a person that does not exist, ensuring it returns false`(){
        assertEquals(false,ListManager().removePerson(Person("BlahBlahBlah",120)))
    }

    @Test
    fun `Test the state of the list after multiple add and remove operations`(){
        val ron = Person("Ron Azar", 24)
        val blah = Person("BlahBlah", 120)
        val managers = ListManager()
        managers.addPerson(ron)
        managers.removePerson(ron)
        managers.addPerson(blah)
        assertTrue(managers.containManger(blah))
        assertFalse(managers.containManger(ron))
        managers.removePerson(blah)
        assertEquals(0, managers.getManagersAmount())
    }

    //* Write tests for getPeopleSortedByAgeAndName method:
    @Test
    fun `Test with an empty list`(){
        val managers = ListManager()
        val listAfterSort = managers.getPeopleSortedByAgeAndName()
        assertEquals(listAfterSort.size,managers.getManagersAmount())
    }

    @Test
    fun `Test with one person`(){
        val managers = ListManager()
        managers.addPerson(Person("Ron Azar", 24))
        assertEquals(Person("Ron Azar", 24), managers.getPeopleSortedByAgeAndName().first())
    }

    @Test
    fun `Test with multiple people to ensure they are sorted first by age, then by name`(){
        val person1 = Person("Ron Azar", 24)
        val person2 = Person("BlahBlah", 120)
        val person3 = Person("Gery", 24)
        val managers = ListManager()
        managers.addPerson(person1)
        managers.addPerson(person2)
        managers.addPerson(person3)
        assertEquals(person1, managers.getPeopleSortedByAgeAndName()[1])
        assertEquals(person3, managers.getPeopleSortedByAgeAndName()[0])
        assertEquals(person2, managers.getPeopleSortedByAgeAndName()[2])
    }

    @Test
    fun `Test with edge cases like people with the same name but different ages and vice versa`()
    {
        val person1 = Person("Ron Azar", 24)
        val person2 = Person("Ron Azar", 120)
        val person3 = Person("Gery", 24)
        val managers = ListManager()
        managers.addPerson(person1)
        managers.addPerson(person2)
        managers.addPerson(person3)
        assertEquals(person1, managers.getPeopleSortedByAgeAndName()[1])
        assertEquals(person3, managers.getPeopleSortedByAgeAndName()[0])
        assertEquals(person2, managers.getPeopleSortedByAgeAndName()[2])
    }
}