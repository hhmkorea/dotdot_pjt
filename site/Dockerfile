FROM adoptopenjdk/openjdk16

COPY ./build/libs/*.jar app.jar

#FROM openjdk:17-jdk-alpine AS builder
#
#WORKDIR /site
#
## COPY만 docker-compose 파일의 위치를 기반으로 작동함
#COPY . .
#
## 개행문자 오류 해결 [unix와 window 시스템 차이] -- 꼭 넣어야함!!!
#RUN sed -i 's/\r$//' gradlew -- gitlab actions 환경은 unbuntu 설치 후 사용.
#
## RUN은 현재 파일의 위치를 기반으로 작동함
#RUN chmod +x ./gradlew
#RUN ./gradlew bootJar
#
## 컨테이너 내부에서 파일을 찾아서 실행 후 파일 명 변경
#ENV JAR_PATH=/site/build/libs
#RUN mv ${JAR_PATH}/*.jar /site/app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
