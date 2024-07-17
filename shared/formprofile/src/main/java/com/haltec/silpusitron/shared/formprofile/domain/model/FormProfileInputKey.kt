package com.haltec.silpusitron.shared.formprofile.domain.model

enum class FormProfileInputKey(private val key: String){
    GENDER("jenis_kelamin"),
    PHONE_NUMBER("no_hp"),
    DISTRICT("kecamatan_id"),
    SUB_DISTRICT("desa_id"),
    ADDRESS("alamat"),
    RW("rw"),
    RT("rt"),
    BIRTH_PLACE("tempat_lahir"),
    BIRTH_DATE("tanggal_lahir"),
    RELIGION("agama"),
    MARRIAGE_STATUS("status_pernikahan"),
    PROFESSION("pekerjaan"),
    EDUCATION("pendidikan"),
    FAMILY_RELATION("status_hubkel"),
    BLOOD_TYPE("golongan_darah"),
    FATHER_NAME("nama_ayah"),
    MOTHER_NAME("nama_ibu"),
    FULL_NAME("nama_lengkap"),
    LONGITUDE("bujur"),
    LATITUDE("lintang"),
    ID_NUMBER("no_nik"),
    FAM_CARD_NUMBER("no_kk");

    companion object{
        fun fromString(key: String): FormProfileInputKey {
            return entries.find { it.key == key }!!
        }
    }

    override fun toString(): String {
        return key
    }
}