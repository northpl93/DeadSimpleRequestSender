FROM node:23-alpine AS build

ARG VITE_DSRS_API
ENV VITE_DSRS_API $VITE_DSRS_API

WORKDIR /app
COPY . /app

RUN npm install
RUN npm run build

FROM nginx:stable-alpine

COPY --from=build /app/dist /usr/share/nginx/html

# Install our custom site configuration
COPY runtime/nginx_site.conf /etc/nginx/conf.d/default.conf
