FROM openjdk:12-jdk-oracle

COPY .m2 /root/.m2
RUN curl -s http://repos.fedorapeople.org/repos/dchen/apache-maven/epel-apache-maven.repo -L -o /etc/yum.repos.d/epel-apache-maven.repo
RUN yum -y install apache-maven
RUN mkdir /app
COPY . /app
WORKDIR /app
RUN mvn clean package
EXPOSE 8080 8083
ENTRYPOINT ["/app/src/main/shell/start.sh"]