package com.tecknobit.ametistaengine.deviceinfo

import android.os.Build.VERSION.SDK_INT
import com.tecknobit.apimanager.apis.APIRequest
import com.tecknobit.apimanager.apis.APIRequest.SHA1_ALGORITHM
import com.tecknobit.kinfo.KInfoState

/**
 * Method to provide the current device information
 *
 * @return the device information as [DeviceInfo]
 */
actual fun provideDeviceInfo(): DeviceInfo {
    val androidInfo = KInfoState().androidInfo
    val brand: String = androidInfo.brand
    val model: String = androidInfo.model
    val osVersion: String = androidInfo.version.release
    return DeviceInfo(
        uniqueIdentifier = APIRequest.base64Digest((brand + model + osVersion + SDK_INT).toByteArray(), SHA1_ALGORITHM),
        brand = brand,
        model = model,
        os = androidInfo.androidCodename,
        osVersion = osVersion
    )
}