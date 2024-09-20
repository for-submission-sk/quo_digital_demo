package com.example.quo_digital_demo.services

data class Page<T>(
    val items: List<T>,
    val itemsTotalNum: Int)
