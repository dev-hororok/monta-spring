# jdk17 Image Start
FROM openjdk:17

# 컨테이너 8080 포트 사용
EXPOSE 8080

# 인자 설정 - JAR_File
ARG JAR_FILE=build/libs/*.jar

#빌드 파일을 컨테이너로 복사
COPY ${JAR_FILE} app.jar

# jar 파일 실행
ENTRYPOINT ["java", "-jar", "app.jar"]