package com.marwadtech.userapp.retrofit.models.customModels

import android.content.Context
import com.marwadtech.userapp.R
import com.marwadtech.userapp.utils.ProfileOptionId

class ProfileOptionsModel(
    val id:Int,
    val icon:Int? = null,
    val title:String? = null
){
    companion object{
        fun accountOptions(context:Context):ArrayList<ProfileOptionsModel>{
            val accountOptionList = ArrayList<ProfileOptionsModel>()
            accountOptionList.add(ProfileOptionsModel(ProfileOptionId.EDIT_PROFILE, R.drawable.ic_personedit, context.getString(R.string.edit_profile)))
            accountOptionList.add(ProfileOptionsModel(ProfileOptionId.CHANGE_PASSWORD, R.drawable.ic_lock_new, context.getString(R.string.change_password)))
            accountOptionList.add(ProfileOptionsModel(ProfileOptionId.ADDRESS, R.drawable.ic_self_address_new, context.getString(R.string.self_address)))

            return accountOptionList
        }

        fun aboutAppOptions(context: Context):ArrayList<ProfileOptionsModel>{
            val aboutAppOptionList = ArrayList<ProfileOptionsModel>()
            aboutAppOptionList.add(ProfileOptionsModel(ProfileOptionId.RATE_US, R.drawable.ic_starlinehorizontal3,context.getString(R.string.rate_us)))
            aboutAppOptionList.add(ProfileOptionsModel(ProfileOptionId.PRIVACY_POLICY, R.drawable.ic_shieldtask, context.getString(R.string.privacy_policy)))
            aboutAppOptionList.add(ProfileOptionsModel(ProfileOptionId.TERMS_AND_CONDITIONS, R.drawable.ic_documentbulletlist, context.getString(R.string.terms_condition)))

            return aboutAppOptionList
        }

        fun dangerZoneOptions(context: Context): ArrayList<ProfileOptionsModel>{
            val dangerZoneList = ArrayList<ProfileOptionsModel>()
            dangerZoneList.add(ProfileOptionsModel(ProfileOptionId.DELETE_ACCOUNT, R.drawable.ic_persondelete, context.getString(R.string.delete_account)))
            dangerZoneList.add(ProfileOptionsModel(ProfileOptionId.LOG_OUT, R.drawable.ic_logout_new, context.getString(R.string.logout)))

            return dangerZoneList
        }
    }
}