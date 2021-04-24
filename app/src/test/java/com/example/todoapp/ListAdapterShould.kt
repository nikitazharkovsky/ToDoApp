package com.example.todoapp

import com.example.todoapp.list.ListAdapter
import org.junit.Assert.assertEquals
import org.junit.Test

class ListAdapterShould {
    @Test
    fun addItems() {
        val sut = ListAdapter()
        assertEquals(0, sut.itemCount)

        sut.add(arrayListOf("1", "2", "3"))
        assertEquals(3, sut.itemCount)
    }

    @Test
    fun clearItems() {
        val sut = ListAdapter()
        assertEquals(0, sut.itemCount)

        sut.add(arrayListOf("1", "2", "3"))
        sut.clear()

        assertEquals(0, sut.itemCount)
    }
}