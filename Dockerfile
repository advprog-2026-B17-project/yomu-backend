# 1. Gunakan pondasi Java 21 yang ringan (Alpine Linux)
FROM eclipse-temurin:21-jdk-alpine

# 2. Buat folder kerja di dalam container
WORKDIR /app

# 3. Copy file JAR hasil build Gradle ke dalam container
# (GitHub Actions sudah melakukan build sebelumnya, jadi kita tinggal ambil hasilnya)
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# 4. Tentukan perintah untuk menyalakan aplikasi
ENTRYPOINT ["java","-jar","/app.jar"]