package com.tecknobit.mole

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
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