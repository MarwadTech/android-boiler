package com.marwadtech.userapp.utils


object AppConstant

object GoogleLoginKeys{
    const val WebClientKey = "335623827886-gdr0c10lf1gv8n4lc0pq6ftootank9hh.apps.googleusercontent.com"
    const val RequestCode = 264
}

object NextRoute {
    const val verifyOtp = "auth/verify-otp"
}

object OtpType{
    const val verifyPhone = "verify-phone"
    const val verifyEmail = "verify-email"
}

object ToastType {
    const val isError = 1
    const val removed = 2
    const val success = 3
    const val invalid = 4
}

object DateFormat {
    const val MMM_YYYY = "MMM yyyy"
    const val DD_MMM = "dd MMM"
    const val DD_MMM_YYYY = "dd MMM yyyy"
    const val D_MMM_YYYY = "d MMM yyyy"
    const val D_MMMM_YYYY = "d MMMM yyyy"
    const val EEE_DD_MMM_YYYY = "EEE, dd MMM yy"
    const val YYYY_MM_DD = "yyyy-MM-dd"
    const val YYYY_M_D = "yyyy-M-d"
    const val DD_MM_YYYY = "dd-MM-yyyy"
    const val DD_MM_YY = "dd/MM/yy"
    const val DATE_Z = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val DD_MMM_YYYY_HH_MM_SS = "dd MMM yyyy | HH:mm aa"
    const val YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss"
    const val EEE_MMM_DD_HH_MM_SS_ZZZ_YYYY = "EEE MMM dd HH:mm:ss zzz yyyy"
}

object RequestKey {
    const val IMAGE_SELECTION = "ImageSelection"
    const val DELETE_ACCOUNT = "DELETE_ACCOUNT"
    const val DELETE_ADDRESS = "DELETE_ADDRESS"
}
object ImageSelection {
    const val Camera = 1
    const val Gallery = 2
    const val Browser = 3
}

object BottomDialogRequestKey {
    const val LOGOUT = 1
    const val DELETE_ACCOUNT = 2
    const val UPDATE = 3
    const val DELETE_ADDRESS = 4
    const val UPDATE_ADDRESS = 5
}

object ProfileOptionId {
    const val EDIT_PROFILE = 1
    const val ADDRESS = 2
    const val CHANGE_PASSWORD = 3
    const val RATE_US = 4
    const val PRIVACY_POLICY = 5
    const val TERMS_AND_CONDITIONS = 6
    const val HELP_AND_SUPPORT = 7
    const val DELETE_ACCOUNT = 8
    const val LOG_OUT = 9
}
object CommonDataKeys {
    const val TERMS_AND_CONDITION = "TERMS_AND_CONDITION"
    const val PRIVACY_POLICY = "PRIVACY_POLICY"
    const val SHARE_MESSAGE = "SHARE_MESSAGE"
    const val SUPPORT_PHONE_NUMBER = "SUPPORT_PHONE_NUMBER"
    const val SUPPORT_EMAIL_ADDRESS = "SUPPORT_EMAIL_ADDRESS"
    const val SUPPORT_WHATSAPP_NUMBER = "SUPPORT_WHATSAPP_NUMBER"
}