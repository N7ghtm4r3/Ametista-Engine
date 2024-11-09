package com.tecknobit.mole

expect class KMole private constructor() {

    companion object {

        val kMole: KMole

    }

    val uniqueIdentifier: String

    val brand: String

    val model: String

    val os: String

    val osVersion: String

}