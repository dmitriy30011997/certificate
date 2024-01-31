Создание пары:
keytool -genkeypair -alias mykey -keyalg RSA -keysize 2048 -keystore keystore.jks -validity 365

Просмотр содержимого хранилища ключей:
keytool -list -v -keystore keystore.jks

Экспорт сертификата из хранилища ключей:
keytool -export -alias mykey -file certificateName.cer -keystore keystore.jks