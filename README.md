# SILPUSITRON

## Folder Structures 
### app-petugas
Merupakan module app untuk petugas. ini adalah module utama yang diakses android ketika menjalankan aplikasi.
### app
Merupakan module app untuk penduduk. ini adalah module utama yang diakses android ketika menjalankan aplikasi.
### core
Berisi konfigurasi umum yang digunakan lebih dari 1 module
### feature
Merupakan kumpulan module fitur yang digunakan pada module app dan/atau app-petugas.
### shared
Kumpulan fitur kecil yang digunakan satu module fitur atau lebih.

## Architecture 
Menggunakan clean architecture, MVVM dan jetpack compose.
### Layer 
- data: berisi kumpulan sumber data aplikasi
- di: berisi definisi dependency injection
- ui: berisi tampilan
- domain: berisi usecase, bisnis logic dan model 

## Additional Notes
### File that are not under git control
- app-petugas/google-services.json
- app/google-services.json
- app/src/main/res/values/google_maps_key.xml
- secret.properties
- app/src/release/secret.properties
- app/src/debug/secret.properties
- app-petugas/src/release/secret.properties
- app-petugas/src/debug/secret.properties
- app/app-keystore
- app-petugas/app_petugas-keystore

Ask the owner for the files
