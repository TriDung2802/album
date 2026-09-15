# ===== Build stage =====
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests


# ===== Run stage =====
FROM tomcat:10.1-jdk21-temurin

# Xóa ứng dụng mặc định của Tomcat
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# Tắt shutdown port của Tomcat
RUN sed -i 's/<Server port="8005"/<Server port="-1"/' \
    /usr/local/tomcat/conf/server.xml

# Đổi HTTP port 8080 -> 10000
RUN sed -i 's/Connector port="8080"/Connector port="10000"/' \
    /usr/local/tomcat/conf/server.xml

# Deploy project thành ROOT
COPY --from=build /app/target/albums-1.0-SNAPSHOT.war \
    /usr/local/tomcat/webapps/ROOT.war

EXPOSE 10000

CMD ["catalina.sh", "run"]
