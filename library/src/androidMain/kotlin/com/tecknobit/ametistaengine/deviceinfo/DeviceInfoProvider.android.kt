package com.tecknobit.ametistaengine.deviceinfo

import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.*
import com.tecknobit.apimanager.apis.APIRequest
import com.tecknobit.apimanager.apis.APIRequest.SHA1_ALGORITHM

private const val BASE = "Base"

private const val CUPCAKE = "Cupcake"

private const val DONUT = "Donut"

private const val ECLAIR = "Eclair"

private const val FROYO = "Froyo"

private const val GINGERBREAD = "Gingerbread"

private const val HONEYCOMB = "Honeycomb"

private const val ICE_CREAM_SANDWICH = "Ice cream Sandwich"

private const val JELLY_BEAN = "Jellybean"

private const val KITKAT = "Kitkat"

private const val LOLLIPOP = "Lollipop"

private const val MARSHMALLOW = "Marshmallow"

private const val NOUGAT = "Nougat"

private const val OREO = "Oreo"

private const val PIE = "Pie"

private const val ANDROID_10 = "Android 10"

private const val RED_VELVET_CAKE = "Red Velvet Cake"

private const val SNOW_CONE = "Snow Cone"

private const val TIRAMISU = "Tiramisu"

private const val UPSIDE_DOWN_CAKE = "Upside down cake"

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