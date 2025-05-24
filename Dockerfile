FROM openjdk:21-jdk-slim 
RUN apt-get update && apt-get install -y curl \ 
  && curl -sL https://deb.nodesource.com/setup_20.x | bash - \ 
  && apt-get install -y nodejs \ 
  && curl -L https://www.npmjs.com/install.sh | npm_install="10.2.3" | sh 
 
ARG MONGO_URI
ARG AUTH0_DOMAIN
ARG OPEN_AI_KEY

 ENV MONGO_URI=${MONGO_URI} \
     AUTH0_DOMAIN=${AUTH0_DOMAIN} \
     OPEN_AI_KEY=${OPEN_AI_KEY}

WORKDIR /usr/src/app 
 
COPY . . 
 
RUN cd frontend && npm install --legacy-peer-deps
RUN cd frontend && npm run build 
RUN rm -r frontend 
 
RUN sed -i 's/\r$//' mvnw 
RUN chmod +x mvnw 
RUN ./mvnw package 
 
 
 
EXPOSE 8080 
CMD ["java", "-jar", "/usr/src/app/target/fundhive-0.0.1-SNAPSHOT.jar"] 