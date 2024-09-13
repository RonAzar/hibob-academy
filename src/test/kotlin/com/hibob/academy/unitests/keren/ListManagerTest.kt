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
        assertEquals(2,managers.getPeopleCount())
    }

    //* Write tests for removePerson method:
    @Test
    fun `Test removing a person that exists in the list`() {
        val managers = ListManager()
        val person = Person("Ron Azar", 24)

        // Add the person to the list
        managers.addPerson(person)

        // Remove the person and check that the removal was successful
        assertTrue(managers.removePerson(person), "The person should have been removed from the list")

        // Verify that the person is no longer in the list
        assertFalse(managers.getPeopleSortedByAgeAndName().contains(person), "The person should no longer be in the list")
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
        assertEquals(0, managers.getPeopleCount())
    }

    //* Write tests for getPeopleSortedByAgeAndName method:
    @Test
    fun `Test with an empty list`(){
        val managers = ListManager()
        val listAfterSort = managers.getPeopleSortedByAgeAndName()
        assertEquals(listAfterSort.size,managers.getPeopleCount())
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


    //calculateStatistics
    @Test
    fun `Test with an empty list for calculateStatistics`()
    {
        assertEquals(null, ListManager().calculateStatistics())
    }

    @Test
    fun `Test with one person with legal age`(){
        val ron = Person("Ron Azar", 24)
        val managers = ListManager()
        managers.addPerson(ron)
        assertEquals(PeopleStatistics(24.0,ron,ron, mapOf(24 to 1)),managers.calculateStatistics())
    }

    @Test
    fun `Test with few person`(){
        val person1 = Person("Ron Azar", 24)
        val person2 = Person("Ron Azar", 26)
        val person3 = Person("Gery", 23)
        val person4 = Person("Don", 25)
        val managers = ListManager()
        managers.addPerson(person1)
        managers.addPerson(person2)
        managers.addPerson(person3)
        managers.addPerson(person4)
        assertEquals(PeopleStatistics(24.5,person3,person2, mapOf( 24 to 1, 26 to 1, 23 to 1, 25 to 1)),managers.calculateStatistics())
    }

    @Test
    fun `Test with two persons with the same age to see which one will be the younges and oldest`(){
        val person1 = Person("Ron Azar", 24)
        val person2 = Person("BlahBlah", 24)
        val managers = ListManager()
        managers.addPerson(person1)
        managers.addPerson(person2)
        assertEquals(person1, managers.calculateStatistics()?.youngest)
        assertEquals(person1, managers.calculateStatistics()?.oldest)
    }
}