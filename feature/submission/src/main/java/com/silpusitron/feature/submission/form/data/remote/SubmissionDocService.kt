package com.silpusitron.feature.submission.form.data.remote

import com.silpusitron.data.remote.base.KtorService
import com.silpusitron.feature.submission.form.data.remote.request.SubmitSubmissionRequest
import com.silpusitron.feature.submission.form.data.remote.request.SubmitUpdateSubmissionRequest
import com.silpusitron.feature.submission.form.data.remote.response.DraftSubmissionResponse
import com.silpusitron.feature.submission.form.data.remote.response.SubmitResponse
import com.silpusitron.feature.submission.form.data.remote.response.TemplateResponse
import com.silpusitron.shared.formprofile.domain.model.FormProfileInputKey
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders

internal class SubmissionDocService(
    override val BASE_URL: String,
    override val API_VERSION: String
) : KtorService() {
    suspend fun getTemplate(
        id: Int,
        token: String
    ): TemplateResponse {
        val response = client.get {
            endpoint("template/$id")
            bearerAuth(token)
        }

        checkOrThrowError(response)

        return response.body<TemplateResponse>()
    }

    suspend fun getDraftSubmission(
        submissionId: Int,
        token: String
    ): DraftSubmissionResponse {
        val response = client.get {
            endpoint("surat/$submissionId/attribute")
            bearerAuth(token)
        }

        checkOrThrowError(response)

        return response.body()
    }

    suspend fun submit(
        token: String,
        request: SubmitSubmissionRequest
    ): SubmitResponse {
        val formData = formData {
            append(FormProfileInputKey.GENDER.toString(), request.genderId)
            append(FormProfileInputKey.PHONE_NUMBER.toString(), request.phoneNumber)
            append(FormProfileInputKey.DISTRICT.toString(), request.districtId)
            append(FormProfileInputKey.SUB_DISTRICT.toString(), request.subDistrictId)
            append(FormProfileInputKey.ADDRESS.toString(), request.address)
            append(FormProfileInputKey.RT.toString(), request.rt)
            append(FormProfileInputKey.RW.toString(), request.rw)
            append(FormProfileInputKey.BIRTH_PLACE.toString(), request.birthPlace)
            append(FormProfileInputKey.RELIGION.toString(), request.religionId)
            append(FormProfileInputKey.MARRIAGE_STATUS.toString(), request.marriageStatus)
            append(FormProfileInputKey.PROFESSION.toString(), request.profession)
            append(FormProfileInputKey.EDUCATION.toString(), request.educationId)
            append(FormProfileInputKey.FAMILY_RELATION.toString(), request.famRelationId)
            append(FormProfileInputKey.BLOOD_TYPE.toString(), request.bloodTypeId)
            append(FormProfileInputKey.FATHER_NAME.toString(), request.fatherName)
            append(FormProfileInputKey.MOTHER_NAME.toString(), request.motherName)
            append("template_surat_id", request.tempalateSuratId)
            request.forms.forEachIndexed { index, it ->
                append("formulir[$index][id]", it.id)
                append("formulir[$index][value]", it.value)
            }
            request.docs.forEachIndexed { index, item ->
                append("berkas_persyaratan[$index][id]", item.id)
                append("berkas_persyaratan[$index][value]", item.file.binary, Headers.build {
                    append(HttpHeaders.ContentType, item.file.mimeType)
                    append(HttpHeaders.ContentDisposition, "filename=\"${item.file.name}\"")
                })
            }
        }

        val response = client.submitFormWithBinaryData(
            formData = formData
        ){
            endpoint("surat")
            bearerAuth(token)
        }

        checkOrThrowError(response)

        return response.body<SubmitResponse>()
    }

    suspend fun submitUpdate(
        token: String,
        id: Int,
        request: SubmitUpdateSubmissionRequest
    ): SubmitResponse {
        val formData = formData {
            append(FormProfileInputKey.GENDER.toString(), request.genderId)
            append(FormProfileInputKey.PHONE_NUMBER.toString(), request.phoneNumber)
            append(FormProfileInputKey.DISTRICT.toString(), request.districtId)
            append(FormProfileInputKey.SUB_DISTRICT.toString(), request.subDistrictId)
            append(FormProfileInputKey.ADDRESS.toString(), request.address)
            append(FormProfileInputKey.RT.toString(), request.rt)
            append(FormProfileInputKey.RW.toString(), request.rw)
            append(FormProfileInputKey.BIRTH_PLACE.toString(), request.birthPlace)
            append(FormProfileInputKey.RELIGION.toString(), request.religionId)
            append(FormProfileInputKey.MARRIAGE_STATUS.toString(), request.marriageStatus)
            append(FormProfileInputKey.PROFESSION.toString(), request.profession)
            append(FormProfileInputKey.EDUCATION.toString(), request.educationId)
            append(FormProfileInputKey.FAMILY_RELATION.toString(), request.famRelationId)
            append(FormProfileInputKey.BLOOD_TYPE.toString(), request.bloodTypeId)
            append(FormProfileInputKey.FATHER_NAME.toString(), request.fatherName)
            append(FormProfileInputKey.MOTHER_NAME.toString(), request.motherName)
            request.forms.forEachIndexed { index, it ->
                append("formulir[$index][key]", it.id)
                append("formulir[$index][value]", it.value)
            }
            request.docs.forEachIndexed { index, item ->
                append("berkas_persyaratan[$index][id]", item.id)
                append("berkas_persyaratan[$index][value]", item.file.binary, Headers.build {
                    append(HttpHeaders.ContentType, item.file.mimeType)
                    append(HttpHeaders.ContentDisposition, "filename=\"${item.file.name}\"")
                })
            }
        }

        val response = client.submitFormWithBinaryData(
            formData = formData
        ){
            endpoint("surat/$id/attribute")
            bearerAuth(token)
        }

        checkOrThrowError(response)

        return response.body()
    }
}