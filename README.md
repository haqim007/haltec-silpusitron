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
### Layers 
- data: berisi kumpulan sumber data aplikasi
- di: berisi definisi dependency injection
- ui: berisi tampilan
- domain: berisi usecase, bisnis logic dan model 

## Additional Notes
### Files that are not under git control
- app-petugas/google-services.json
  konfigurasi firebase app-petugas
- app/google-services.json
  konfigurasi firebase app
- app/src/main/res/values/google_maps_key.xml
  key google map
- secret.properties
  tempat untuk menyimpan variabel penting untuk dapat diakses semua module
- app/src/release/secret.properties
  tempat untuk menyimpan variable penting untuk hanya dapat diakses app module variant release
- app/src/debug/secret.properties
  tempat untuk menyimpan variable penting untuk hanya dapat diakses app module variant debug
- app-petugas/src/release/secret.properties
  tempat untuk menyimpan variable penting untuk hanya dapat diakses app-petugas module variant release
- app-petugas/src/debug/secret.properties
  tempat untuk menyimpan variable penting untuk hanya dapat diakses app-petugas module variant debug
- app/app-keystore
  keystore module all
- app-petugas/app_petugas-keystore
  keystore module app-petugas

Ask the owner for the files
