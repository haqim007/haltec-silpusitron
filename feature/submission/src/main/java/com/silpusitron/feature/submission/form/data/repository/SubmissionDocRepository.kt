package com.silpusitron.feature.submission.form.data.repository

import com.silpusitron.common.util.DispatcherProvider
import com.silpusitron.common.util.FileHelper
import com.silpusitron.data.mechanism.Resource
import com.silpusitron.feature.submission.form.data.remote.SubmissionDocRemoteDatasource
import com.silpusitron.feature.submission.form.data.remote.request.SubmitSubmissionRequest
import com.silpusitron.feature.submission.form.data.remote.request.SubmitUpdateSubmissionRequest
import com.silpusitron.feature.submission.form.data.remote.response.DraftSubmissionResponse
import com.silpusitron.feature.submission.form.data.remote.response.SubmitResponse
import com.silpusitron.feature.submission.form.data.remote.response.TemplateResponse
import com.silpusitron.feature.submission.form.data.toModel
import com.silpusitron.feature.submission.form.domain.DocId
import com.silpusitron.feature.submission.form.domain.ISubmissionDocRepository
import com.silpusitron.feature.submission.form.domain.SubmissionForms
import com.silpusitron.feature.submission.form.domain.VariableId
import com.silpusitron.shared.auth.mechanism.AuthorizedNetworkBoundResource
import com.silpusitron.shared.auth.preference.AuthPreference
import com.silpusitron.shared.form.domain.model.FileAbsolutePath
import com.silpusitron.shared.form.domain.model.FileValidationType
import com.silpusitron.shared.form.domain.model.InputTextData
import com.silpusitron.shared.form.domain.model.TextValidationType
import com.silpusitron.shared.formprofile.domain.model.FormProfileInputKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import java.io.File

internal class SubmissionDocRepository(
    private val remoteDatasource: SubmissionDocRemoteDatasource,
    private val preferences: AuthPreference,
    private val dispatcher: DispatcherProvider
): ISubmissionDocRepository {
    override fun getTemplate(id: Int): Flow<Resource<SubmissionForms>> {
        return object : AuthorizedNetworkBoundResource<SubmissionForms, TemplateResponse>(preferences) {

            override suspend fun requestFromRemote(): Result<TemplateResponse> {
                return remoteDatasource.getTemplate(id, getToken())
            }

            override fun loadResult(responseData: TemplateResponse): Flow<SubmissionForms> {
                return flowOf(responseData.toModel())
            }

        }
            .asFlow()
            .flowOn(dispatcher.io)
    }

    override fun submit(
        templateId: Int,
        profile: Map<FormProfileInputKey, InputTextData<TextValidationType, String>>,
        docs: Map<DocId, InputTextData<FileValidationType, FileAbsolutePath>>,
        forms: Map<VariableId, InputTextData<TextValidationType, String>>
    ): Flow<Resource<String>> {
        return object : AuthorizedNetworkBoundResource<String, SubmitResponse>(preferences) {

            override suspend fun requestFromRemote(): Result<SubmitResponse> {
                val request = SubmitSubmissionRequest(
                    tempalateSuratId = templateId,
                    subDistrictId = profile[FormProfileInputKey.SUB_DISTRICT]?.value ?: "",
                    rt = profile[FormProfileInputKey.RT]?.value ?: "",
                    phoneNumber = profile[FormProfileInputKey.PHONE_NUMBER]?.value ?: "",
                    educationId = profile[FormProfileInputKey.EDUCATION]?.value ?: "",
                    rw = profile[FormProfileInputKey.RW]?.value ?: "",
                    motherName = profile[FormProfileInputKey.MOTHER_NAME]?.value ?: "",
                    bloodTypeId = profile[FormProfileInputKey.BLOOD_TYPE]?.value ?: "",
                    fullname = profile[FormProfileInputKey.FULL_NAME]?.value ?: "",
                    religionId = profile[FormProfileInputKey.RELIGION]?.value ?: "",
                    districtId = profile[FormProfileInputKey.DISTRICT]?.value ?: "",
                    address = profile[FormProfileInputKey.ADDRESS]?.value ?: "",
                    idNumber = profile[FormProfileInputKey.ID_NUMBER]?.value ?: "",
                    birthPlace = profile[FormProfileInputKey.BIRTH_PLACE]?.value ?: "",
                    profession = profile[FormProfileInputKey.PROFESSION]?.value ?: "",
                    famRelationId = profile[FormProfileInputKey.FAMILY_RELATION]?.value ?: "",
                    fatherName = profile[FormProfileInputKey.FATHER_NAME]?.value ?: "",
                    famCardNumber = profile[FormProfileInputKey.FAM_CARD_NUMBER]?.value ?: "",
                    genderId = profile[FormProfileInputKey.GENDER]?.value ?: "",
                    birthDate = profile[FormProfileInputKey.BIRTH_DATE]?.value ?: "",
                    marriageStatus = profile[FormProfileInputKey.MARRIAGE_STATUS]?.value ?: "",
                    latitude = profile[FormProfileInputKey.LATITUDE]?.value ?: "",
                    longitude = profile[FormProfileInputKey.LONGITUDE]?.value ?: "",
                    docs = docs
                        .filterValues {
                            it.value != null
                        }
                        .map {
                        val file = File(it.value.value!!)
                        val mimeType = FileHelper.getMimeType(file)
                        SubmitSubmissionRequest.BerkasPersyaratanItem(
                            id = it.key,
                            file = SubmitSubmissionRequest.FileRequest(
                                name = file.name,
                                mimeType = mimeType ?: "image/*",
                                binary = file.readBytes()
                            )
                        )
                    },
                    forms = forms.map {
                        SubmitSubmissionRequest.FormulirItem(
                            id = it.key,
                            value = it.value.value ?: ""
                        )
                    }
                )
                return remoteDatasource.submit(
                    getToken(),
                    request
                )
            }

            override fun loadResult(responseData: SubmitResponse): Flow<String> {
                return flowOf(responseData.message)
            }

        }
            .asFlow()
            .flowOn(dispatcher.io)
    }

    override fun getDraftSubmission(submissionId: Int): Flow<Resource<SubmissionForms>> {
        return object : AuthorizedNetworkBoundResource<SubmissionForms, DraftSubmissionResponse>(preferences) {

            override suspend fun requestFromRemote(): Result<DraftSubmissionResponse> {
                return remoteDatasource.getDraftSubmission(getToken(), submissionId)
            }

            override fun loadResult(responseData: DraftSubmissionResponse): Flow<SubmissionForms> {
                return flowOf(responseData.toModel())
            }

        }
            .asFlow()
            .flowOn(dispatcher.io)
    }

    override fun submitUpdate(
        submissionId: Int,
        profile: Map<FormProfileInputKey, InputTextData<TextValidationType, String>>,
        docs: Map<DocId, InputTextData<FileValidationType, FileAbsolutePath>>,
        forms: Map<VariableId, InputTextData<TextValidationType, String>>
    ): Flow<Resource<String>> {
        return object : AuthorizedNetworkBoundResource<String, SubmitResponse>(preferences) {

            override suspend fun requestFromRemote(): Result<SubmitResponse> {
                val request = SubmitUpdateSubmissionRequest(
                    subDistrictId = profile[FormProfileInputKey.SUB_DISTRICT]?.value ?: "",
                    rt = profile[FormProfileInputKey.RT]?.value ?: "",
                    phoneNumber = profile[FormProfileInputKey.PHONE_NUMBER]?.value ?: "",
                    educationId = profile[FormProfileInputKey.EDUCATION]?.value ?: "",
                    rw = profile[FormProfileInputKey.RW]?.value ?: "",
                    motherName = profile[FormProfileInputKey.MOTHER_NAME]?.value ?: "",
                    bloodTypeId = profile[FormProfileInputKey.BLOOD_TYPE]?.value ?: "",
                    fullname = profile[FormProfileInputKey.FULL_NAME]?.value ?: "",
                    religionId = profile[FormProfileInputKey.RELIGION]?.value ?: "",
                    districtId = profile[FormProfileInputKey.DISTRICT]?.value ?: "",
                    address = profile[FormProfileInputKey.ADDRESS]?.value ?: "",
                    idNumber = profile[FormProfileInputKey.ID_NUMBER]?.value ?: "",
                    birthPlace = profile[FormProfileInputKey.BIRTH_PLACE]?.value ?: "",
                    profession = profile[FormProfileInputKey.PROFESSION]?.value ?: "",
                    famRelationId = profile[FormProfileInputKey.FAMILY_RELATION]?.value ?: "",
                    fatherName = profile[FormProfileInputKey.FATHER_NAME]?.value ?: "",
                    famCardNumber = profile[FormProfileInputKey.FAM_CARD_NUMBER]?.value ?: "",
                    genderId = profile[FormProfileInputKey.GENDER]?.value ?: "",
                    birthDate = profile[FormProfileInputKey.BIRTH_DATE]?.value ?: "",
                    marriageStatus = profile[FormProfileInputKey.MARRIAGE_STATUS]?.value ?: "",
                    latitude = profile[FormProfileInputKey.LATITUDE]?.value ?: "",
                    longitude = profile[FormProfileInputKey.LONGITUDE]?.value ?: "",
                    docs = docs
                        .filterValues {
                            it.value != null &&
                            it.value?.contains("http") == false // do not include value contains http because it is default value and is not required
                        }
                        .map {
                            val file = File(it.value.value!!)
                            val mimeType = FileHelper.getMimeType(file)
                            SubmitUpdateSubmissionRequest.BerkasPersyaratanItem(
                                id = it.key,
                                file = SubmitUpdateSubmissionRequest.FileRequest(
                                    name = file.name,
                                    mimeType = mimeType ?: "image/*",
                                    binary = file.readBytes()
                                )
                            )
                        },
                    forms = forms.map {
                        SubmitUpdateSubmissionRequest.FormulirItem(
                            id = it.key,
                            value = it.value.value ?: ""
                        )
                    }
                )
                return remoteDatasource.submitUpdate(
                    getToken(),
                    submissionId,
                    request
                )
            }

            override fun loadResult(responseData: SubmitResponse): Flow<String> {
                return flowOf(responseData.message)
            }

        }
            .asFlow()
            .flowOn(dispatcher.io)
    }
}