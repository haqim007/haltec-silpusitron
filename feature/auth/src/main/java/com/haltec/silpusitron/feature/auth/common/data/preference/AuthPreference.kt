package com.haltec.silpusitron.feature.auth.common.data.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.map

// TODO: Save authentication stuff here not in User module

class AuthPreference(
    private val dataStore: DataStore<Preferences>
){
    suspend fun storeAuth(username: String, token: String){
        dataStore.edit {preferences ->
            preferences[USER_NAME] = username
            preferences[TOKEN] = token
        }
    }

    suspend fun storeAuth(username: String, token: String, otpValid: Boolean){
        dataStore.edit {preferences ->
            preferences[USER_NAME] = username
            preferences[TOKEN] = token
            preferences[IS_OTP_VALID] = otpValid
        }
    }

    suspend fun setOTPValid(){
        dataStore.edit {preferences ->
            preferences[IS_OTP_VALID] = true
        }
    }

    suspend fun updateToken(token: String, expiredTime: Long = 0L){
        dataStore.edit {preferences ->
            preferences[TOKEN] = token
        }
    }

    fun getToken() = dataStore.data.map {preferences ->
        preferences[TOKEN] ?: ""
    }

    suspend fun resetAuth() {
        dataStore.edit {preferences ->
            preferences[USER_NAME] = ""
            preferences[TOKEN] = ""
            preferences[IS_OTP_VALID] = false
        }
    }

    fun getUserName() = dataStore.data.map {preferences ->
        preferences[USER_NAME] ?: ""
    }

    fun isSessionValid() = dataStore.data.map { preferences ->
        preferences[TOKEN]?.isNotBlank() == true && preferences[IS_OTP_VALID] == true
    }

    companion object{
        private val USER_NAME = stringPreferencesKey("user_name")
        private val TOKEN = stringPreferencesKey("token")
        private val IS_OTP_VALID = booleanPreferencesKey("is_OTP_valid")
        //private val TOKEN_EXPIRE = longPreferencesKey ("token_expire")
        //// Flag that the session end because of user logout
        //private val HAS_LOGOUT = booleanPreferencesKey("has_logout")
    }
}