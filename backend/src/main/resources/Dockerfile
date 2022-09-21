FROM maven:latest
RUN useradd -ms /bin/bash user

WORKDIR /home/user/api

COPY ./backend /home/user/api

RUN chown -R user /home/user/api
USER user

RUN mvn -f /home/user/api/pom.xml clean package

ENTRYPOINT [ "java", "-jar", "target/notulus-backend.jar" ]
