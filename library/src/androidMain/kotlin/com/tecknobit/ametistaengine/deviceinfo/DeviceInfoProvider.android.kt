package com.tecknobit.ametistaengine.deviceinfo

import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.*
import com.tecknobit.apimanager.apis.APIRequest
import com.tecknobit.apimanager.apis.APIRequest.SHA1_ALGORITHM

/**
 * **BASE** -> "BASE" Android operating system name
 */
private const val BASE = "Base"

/**
 * **CUPCAKE** -> "CUPCAKE" Android operating system name
 */
private const val CUPCAKE = "Cupcake"

/**
 * **DONUT** -> "DONUT" Android operating system name
 */
private const val DONUT = "Donut"

/**
 * **ECLAIR** -> "Eclair" Android operating system name
 */
private const val ECLAIR = "Eclair"

/**
 * **FROYO** -> "Froyo" Android operating system name
 */
private const val FROYO = "Froyo"

/**
 * **GINGERBREAD** -> "Gingerbread" Android operating system name
 */
private const val GINGERBREAD = "Gingerbread"

/**
 * **HONEYCOMB** -> "Honeycomb" Android operating system name
 */
private const val HONEYCOMB = "Honeycomb"

/**
 * **ICE_CREAM_SANDWICH** -> "Ice cream Sandwich" Android operating system name
 */
private const val ICE_CREAM_SANDWICH = "Ice cream Sandwich"

/**
 * **JELLY_BEAN** -> "Jellybean" Android operating system name
 */
private const val JELLY_BEAN = "Jellybean"

/**
 * **KITKAT** -> "Kitkat" Android operating system name
 */
private const val KITKAT = "Kitkat"

/**
 * **LOLLIPOP** -> "Lollipop" Android operating system name
 */
private const val LOLLIPOP = "Lollipop"

/**
 * **MARSHMALLOW** -> "Marshmallow" Android operating system name
 */
private const val MARSHMALLOW = "Marshmallow"

/**
 * **NOUGAT** -> "Nougat" Android operating system name
 */
private const val NOUGAT = "Nougat"

/**
 * **OREO** -> "Oreo" Android operating system name
 */
private const val OREO = "Oreo"

/**
 * **PIE** -> "Pie" Android operating system name
 */
private const val PIE = "Pie"

/**
 * **ANDROID_10** -> "Android 10" Android operating system name
 */
private const val ANDROID_10 = "Android 10"

/**
 * **RED_VELVET_CAKE** -> "Red Velvet Cake" Android operating system name
 */
private const val RED_VELVET_CAKE = "Red Velvet Cake"

/**
 * **SNOW_CONE** -> "Snow Cone" Android operating system name
 */
private const val SNOW_CONE = "Snow Cone"

/**
 * **TIRAMISU** -> "Tiramisu" Android operating system name
 */
private const val TIRAMISU = "Tiramisu"

/**
 * **UPSIDE_DOWN_CAKE** -> "Upside down cake" Android operating system name
 */
private const val UPSIDE_DOWN_CAKE = "Upside down cake"

/**
 * Method to provide the current device information
 *
 * @return the device information as [DeviceInfo]
 */
actual fun provideDeviceInfo(): DeviceInfo {
    val brand: String = Build.BRAND
    val model: String = Build.MODEL
    val os = osNameByVersion()
    val osVersion: String = Build.VERSION.RELEASE
    val uniqueIdentifier = APIRequest.base64Digest((brand + model + osVersion + SDK_INT).toByteArray(), SHA1_ALGORITHM)
    return DeviceInfo(
        uniqueIdentifier = uniqueIdentifier,
        brand = brand,
        model = model,
        os = os,
        osVersion = osVersion
    )
}

/**
 * Method to get the specific version name of the Android operating system
 *
 * @return the specific version name of the Android operating system as [String]
 */
private fun osNameByVersion(): String {
    val osVersion = SDK_INT
    if (osVersion <= 2)
        return BASE
    else if (osVersion == Build.VERSION_CODES.CUPCAKE)
        return CUPCAKE
    else if (osVersion == Build.VERSION_CODES.DONUT)
        return DONUT
    else if (osVersion in Build.VERSION_CODES.ECLAIR..ECLAIR_MR1)
        return ECLAIR
    else if (osVersion == Build.VERSION_CODES.FROYO)
        return FROYO
    else if (osVersion == Build.VERSION_CODES.GINGERBREAD || osVersion == GINGERBREAD_MR1)
        return GINGERBREAD
    else if (osVersion in Build.VERSION_CODES.HONEYCOMB..HONEYCOMB_MR2)
        return HONEYCOMB
    else if (osVersion == Build.VERSION_CODES.ICE_CREAM_SANDWICH || osVersion == ICE_CREAM_SANDWICH_MR1)
        return ICE_CREAM_SANDWICH
    else if (osVersion in Build.VERSION_CODES.JELLY_BEAN..JELLY_BEAN_MR2)
        return JELLY_BEAN
    else if (osVersion == Build.VERSION_CODES.KITKAT || osVersion == KITKAT_WATCH)
        return KITKAT
    else if (osVersion == Build.VERSION_CODES.LOLLIPOP || osVersion == LOLLIPOP_MR1)
        return LOLLIPOP
    else if (osVersion == M)
        return MARSHMALLOW
    else if (osVersion == N || osVersion == N_MR1)
        return NOUGAT
    else if (osVersion == O || osVersion == O_MR1)
        return OREO
    else if (osVersion == P)
        return PIE
    else if (osVersion == Q)
        return ANDROID_10
    else if (osVersion == R)
        return RED_VELVET_CAKE
    else if (osVersion == S)
        return SNOW_CONE
    else if (osVersion == Build.VERSION_CODES.TIRAMISU)
        return TIRAMISU
    else if (osVersion == Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        return UPSIDE_DOWN_CAKE
    return BASE
}