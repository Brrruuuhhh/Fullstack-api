FROM node:20 AS build
WORKDIR /app
COPY . .
RUN npm install && npm run build --prod

ENV SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/testdb
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=admin

FROM nginx:alpine
COPY --from=build /app/dist/api-front /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
