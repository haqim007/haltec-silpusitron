package com.haltec.silpusitron.shared.formprofile.data.remote

import com.haltec.silpusitron.data.remote.base.KtorService
import com.haltec.silpusitron.shared.formprofile.data.remote.response.ProfileInputOptionsResponse
import com.haltec.silpusitron.shared.formprofile.data.remote.response.ProfileResponse
import com.haltec.silpusitron.shared.formprofile.data.remote.response.SubDistrictsResponse
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get

enum class FormOptionPath(private val path: String){
    GENDER("jenis_kelamin"),
    RELIGION("agama"),
    PROFESSION("pekerjaan"),
    BLOOD_TYPE("golongan_darah"),
    EDUCATION("pendidikan"),
    FAM_RELATION_STATUS("hubungan_keluarga"),
    MARRIAGE_STATUS("status_kawin");

    override fun toString(): String {
        return path
    }
}

internal class FormProfileService(
    override val BASE_URL: String,
    override val API_VERSION: String
) : KtorService() {
    suspend fun getProfile(token: String): ProfileResponse {
        val response = client.get{
            endpoint("users/profile")
            bearerAuth(token)
        }

        checkOrThrowError(response)

        return response.body()
    }

    suspend fun getFormOptions(token: String, optionPath: FormOptionPath): ProfileInputOptionsResponse {
        val response = client.get{
            endpoint("data-umum/key/$optionPath")
            bearerAuth(token)
        }

        checkOrThrowError(response)

        return response.body()
    }

    suspend fun getSubDistricts(token: String, districtId: String?): SubDistrictsResponse {
        val response = client.get{
            if (districtId == null){
                endpoint("desa")
            }else{
                endpoint("desa/kecamatan/$districtId")
            }
            bearerAuth(token)
        }

        checkOrThrowError(response)

        return response.body()
    }
}